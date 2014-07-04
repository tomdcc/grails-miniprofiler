package grails.plugin.miniprofiler

import geb.spock.GebReportingSpec
import io.jdev.miniprofiler.test.pages.MiniProfilerGapModule
import io.jdev.miniprofiler.test.pages.MiniProfilerQueryModule
import spock.lang.IgnoreIf

@IgnoreIf({ ! new Boolean(System.getProperty('grails.profiler.disable')) })
class MiniprofilerDisabledFunctionalSpec extends GebReportingSpec {

	def "cannot see miniprofiler when plugin disabled"() {
		when:
			to BookListPage

		then: 'mini profiler visible with single timing info'
			!miniProfiler

	}
}
