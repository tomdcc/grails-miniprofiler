grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.work.dir = 'target'
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

// uncomment (and adjust settings) to fork the JVM to isolate classpaths
//grails.project.fork = [
//   run: [maxMemory:1024, minMemory:64, debug:false, maxPerm:256]
//]

grails.plugin.location.miniprofiler = "../../.."

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

		// here to allow testing against snapshot miniprofiler code
		mavenRepo 'https://oss.sonatype.org/content/repositories/snapshots'
    }

    dependencies {
		test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
		test "org.gebish:geb-spock:0.9.0"
		test "org.seleniumhq.selenium:selenium-support:2.35.0"
		test "org.seleniumhq.selenium:selenium-firefox-driver:2.35.0"
		test "io.jdev.miniprofiler:miniprofiler-test-support:0.3"
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.8.3"
        runtime ":resources:1.1.6"

        build ":tomcat:$grailsVersion"

        runtime ':profiler:0.5'

		test ":geb:0.9.0"
		test(":spock:0.7") {
			exclude "spock-grails-support"
		}

	}
}
