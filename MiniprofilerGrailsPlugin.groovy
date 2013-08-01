import groovy.util.slurpersupport.GPathResult

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import net.sf.ehcache.store.MemoryStoreEvictionPolicy
import net.sf.log4jdbc.SpyLogFactory

import org.codehaus.groovy.grails.commons.spring.BeanConfiguration
import org.springframework.cache.ehcache.EhCacheFactoryBean

import com.energizedwork.miniprofiler.MiniProfilerAppender
import com.energizedwork.miniprofiler.MiniProfilerFilter
import com.energizedwork.miniprofiler.MiniprofilerCondition
import com.energizedwork.miniprofiler.NullSpyLogDelegator
import com.energizedwork.miniprofiler.ProfilerProvider
import com.energizedwork.miniprofiler.ProfilingDataSource
import com.energizedwork.miniprofiler.ProfilingSpyLogDelegator
import com.energizedwork.miniprofiler.ServletRequestProfilerProvider

class MiniprofilerGrailsPlugin {
    def version = "0.2-SNAPSHOT"
    def grailsVersion = "1.3 > *"

    def loadAfter = ['profiler'] // as we want to modify a couple of things that it creates

    def author = "Tom Dunstan"
    def authorEmail = "grails@tomd.cc"
    def title = "Mini Profiler plugin"
    def description = '''\
Shows timing and sql query information in a head-up display in a web page, useful for debugging
database and other performance problems.
'''
    def documentation = "http://grails.org/plugin/miniprofiler"

    def license = 'APACHE'
    def scm = [url: 'https://github.com/tomdcc/grails-miniprofiler']
    def issueManagement = [system: 'Github', url: 'https://github.com/tomdcc/grails-miniprofiler/issues']

    // place right on the outside
    def getWebXmlFilterOrder() {
        def FilterManager = getClass().getClassLoader().loadClass('grails.plugin.webxml.FilterManager')
        [miniProfilerFilter: FilterManager.GRAILS_WEB_REQUEST_POSITION + 1]
    }

    def doWithWebDescriptor = { webXml ->
        if (isProfilingDisabled(application)) return

        // Add the profiler filter to the web app.
        def filterDef = webXml.'filter'
        filterDef[filterDef.size() - 1] + {
            'filter' {
                'filter-name'('miniProfilerFilter')
                'filter-class'(MiniProfilerFilter.name)
            }
        }

        // This filter should come before the standard profiler filter, otherwise we'll miss some events
        def filterMapping = webXml.'filter-mapping'.find { it.'filter-name'.text() == "charEncodingFilter" }
        filterMapping + {
            'filter-mapping' {
                'filter-name'("miniProfilerFilter")
                'url-pattern'("/*")
            }
        }

        // now the fun part - override the grails page filter with one which profiles layout rendering
        def pageFilterMapping = webXml.'filter'.find { it.'filter-name'.text() == 'sitemesh' }
        def filterClassNode = pageFilterMapping.'filter-class'[0]
        def newFilterClass = 'com.energizedwork.miniprofiler.ProfilingGrailsPageFilter'
        if (filterClassNode instanceof GPathResult) {
            filterClassNode.replaceBody(newFilterClass)
        } else {
            filterClassNode.setTextContent(newFilterClass)
        }

    }

    private boolean isProfilingDisabled(application) {
        application.config.grails?.profiler?.disable as boolean
    }

    def doWithSpring = {
        if (isProfilingDisabled(application)) return

        // make log4jdbc not spam the logs while we're starting up and don't have the real log catcher ready yet
        setuplLog4JdbcLogger(null)

        // just switch profiling on for all requests by default
        profilerCondition(MiniprofilerCondition)

        // replace data source with our proxying one
        BeanConfiguration dataSourceConfig = springConfig.getBeanConfig('dataSource')
        springConfig.addBeanConfiguration("dataSourceOriginal", dataSourceConfig)

        dataSource(ProfilingDataSource) {
            targetDataSource = ref('dataSourceOriginal')
        }

        profilingCache(EhCacheFactoryBean) {
            cacheName = 'miniprofilerCache'
            maxElementsInMemory = 1000
            maxElementsOnDisk = 0
            memoryStoreEvictionPolicy = MemoryStoreEvictionPolicy.LRU
            overflowToDisk = false
            eternal = false
            timeToLive = 3600
        }

        profilerProvider(ServletRequestProfilerProvider) {
            cache = ref('profilingCache')
        }

        miniProfilerAppender(MiniProfilerAppender) {
            profilerProvider = ref('profilerProvider')
        }

        // commented out for now since this isn't working reliably yet
        // just trying to get the gsp names

//        BeanConfiguration viewResolverConfig = springConfig.getBeanConfig('jspViewResolver')
//        springConfig.addBeanConfiguration("jspViewResolverOriginal", viewResolverConfig)
//
//        jspViewResolver(ProfilingGrailsViewResolver) {
//            wrapped = ref('jspViewResolverOriginal')
//        }
    }

    private void setuplLog4JdbcLogger(ProfilerProvider profilerProvider) {
        // work around hardcoded delegator in log4jdbc, this probably won't work if you're running under
        // a SecurityManager
        Field field = SpyLogFactory.getDeclaredField('logger')
        field.setAccessible(true)
        Field modifiersField = Field.getDeclaredField("modifiers")
        modifiersField.setAccessible(true)
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL)
        field.set(null, profilerProvider ? new ProfilingSpyLogDelegator(profilerProvider) : new NullSpyLogDelegator())
    }

    def doWithApplicationContext = { applicationContext ->
        if (isProfilingDisabled(application)) return
        setuplLog4JdbcLogger(applicationContext.profilerProvider)
        // only send profiler data to miniprofiler, don't log to file etc
        applicationContext.profilerLog.appenderNames = ['miniProfilerAppender']
    }
}
