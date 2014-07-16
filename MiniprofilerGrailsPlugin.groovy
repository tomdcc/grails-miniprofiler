import grails.util.Environment
import groovy.util.slurpersupport.GPathResult
import io.jdev.miniprofiler.DefaultProfilerProvider
import io.jdev.miniprofiler.MiniProfiler
import io.jdev.miniprofiler.servlet.ProfilingFilter
import io.jdev.miniprofiler.sql.ProfilingDataSource
import io.jdev.miniprofiler.storage.EhcacheStorage
import net.sf.ehcache.store.MemoryStoreEvictionPolicy
import org.codehaus.groovy.grails.commons.spring.BeanConfiguration

import grails.plugin.miniprofiler.profilerplugin.MiniProfilerAppender
import grails.plugin.miniprofiler.profilerplugin.MiniprofilerCondition
import org.springframework.cache.ehcache.EhCacheFactoryBean

class MiniprofilerGrailsPlugin {
    def version = "0.4.1-SNAPSHOT"
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
                'filter-class'(ProfilingFilter.name)
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
		try {
			// this class exists in grails 2.0-2.3, and replacing it with our profiling one
			// is how we measure how long a layout takes.
			// no solution for grails 2.4 yet
			Class.forName('org.codehaus.groovy.grails.web.sitemesh.GrailsPageFilter')
			def pageFilterMapping = webXml.'filter'.find { it.'filter-name'.text() == 'sitemesh' }
			if(pageFilterMapping) { // only do this if we can find sitemesh - someone may have removed it
				def filterClassNode = pageFilterMapping.'filter-class'[0]
				def newFilterClass = 'grails.plugin.miniprofiler.sitemesh.grails20.ProfilingGrailsPageFilter'
				if (filterClassNode instanceof GPathResult) {
					filterClassNode.replaceBody(newFilterClass)
				} else {
					filterClassNode.setTextContent(newFilterClass)
				}
			}
		} catch(ClassNotFoundException e) {
			// not grails 2.0-2.3, so don't try to replace it - we just won't have layout timing
		}
    }

    private boolean isProfilingDisabled(application) {
        application.config.grails?.profiler?.disable as boolean
    }

    def doWithSpring = {
        if (isProfilingDisabled(application)) return

		// use an in-memory ehcache by default
		profilingCache(EhCacheFactoryBean) {
			cacheName = 'miniprofilerCache'
			maxElementsInMemory = 1000
			maxElementsOnDisk = 0
			memoryStoreEvictionPolicy = 'LRU'
			overflowToDisk = false
			eternal = false
			timeToLive = 3600
		}
		profilerStorage(EhcacheStorage, profilingCache)

		// main profiler provider
		profilerProvider(DefaultProfilerProvider) {
			storage = profilerStorage
		}

		// replace data source with our proxying one, if it's there
        BeanConfiguration dataSourceConfig = springConfig.getBeanConfig('dataSource')
		if(dataSourceConfig) {
			springConfig.addBeanConfiguration("dataSourceOriginal", dataSourceConfig)
			dataSource(ProfilingDataSource, ref('dataSourceOriginal'), profilerProvider)
		}

		// profiler plugin related stuff below

		// just switch profiling on for all requests by default
		profilerCondition(MiniprofilerCondition)

		// set up our profiler appender to capture output from the profiler
		// plugin and pipe it through a mini profiler session instead
		miniProfilerAppender(MiniProfilerAppender) {
            profilerProvider = ref('profilerProvider')
        }

    }

    def doWithApplicationContext = { applicationContext ->
        if (isProfilingDisabled(application)) return

		def profilerProvider = applicationContext.profilerProvider

		// make available in static contexts, just in case someone needs it
		MiniProfiler.profilerProvider = profilerProvider

		// set name to have grails env name as well
		profilerProvider.machineName = "$profilerProvider.machineName($Environment.current.name)"

        // only send profiler data to miniprofiler, don't log to file etc
        applicationContext.profilerLog.appenderNames = ['miniProfilerAppender']
    }
}
