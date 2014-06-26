grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.plugin.location.miniprofiler = "../../.."

grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
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
        mavenLocal()
        grailsCentral()
        mavenCentral()

		// here to allow testing against snapshot miniprofiler code
		mavenRepo 'https://oss.sonatype.org/content/repositories/snapshots'
    }

    dependencies {
		test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
		test "org.gebish:geb-spock:0.9.3"
		test "org.seleniumhq.selenium:selenium-support:2.41.0"
		test "org.seleniumhq.selenium:selenium-firefox-driver:2.41.0"
		test "io.jdev.miniprofiler:miniprofiler-test-support:0.4"
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.53"

        // plugins for the compile step
        compile ":scaffolding:2.0.3"
        compile ':cache:1.1.2'

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.16" // or ":hibernate4:4.3.5.4"
        runtime ":database-migration:1.4.0"
        runtime ":jquery:1.11.1"
        runtime ":resources:1.2.7"

		runtime ':profiler:0.5'

		test ":geb:0.9.3"
		test(":spock:0.7") {
			exclude "spock-grails-support"
		}

	}
}
