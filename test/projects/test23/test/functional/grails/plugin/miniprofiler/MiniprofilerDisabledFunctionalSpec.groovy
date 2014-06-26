package grails.plugin.miniprofiler

import geb.spock.GebReportingSpec
import io.jdev.miniprofiler.test.pages.MiniProfilerGapModule
import io.jdev.miniprofiler.test.pages.MiniProfilerQueryModule
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import spock.lang.IgnoreIf

@IgnoreIf({ !(ConfigurationHolder.getConfig()?.grails?.profiler?.disable as boolean) })
class MiniprofilerDisabledFunctionalSpec extends GebReportingSpec {

	def "cannot see miniprofiler when plugin disabled"() {
		when:
			to BookListPage

		then: 'mini profiler visible with single timing info'
			!miniProfiler

	}
}
