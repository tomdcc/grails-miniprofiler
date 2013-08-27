grails.project.work.dir = 'target'
grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.project.source.level = 1.6

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
        mavenCentral()

		if(System.getenv("MINIPROFILER_CI") == 'true' && appVersion.endsWith("-SNAPSHOT")) {
			println "Using Sonatype OSS snapshot repository. If you are reading this anywhere other than the miniprofiler plugin travis CI build, something probably went wrong"
			// here to allow testing against snapshot miniprofiler code
			mavenRepo 'https://oss.sonatype.org/content/repositories/snapshots'
		}
    }

    dependencies {
        compile "io.jdev.miniprofiler:miniprofiler-core:0.3"
    }

    plugins {
        build(':release:2.2.1', ':rest-client-builder:1.0.3') {
            export = false
        }

        compile(':profiler:0.5') {
			export = false
		}
    }
}

grails.project.repos.grailsCentral.username = System.getenv("GRAILS_CENTRAL_USERNAME")
grails.project.repos.grailsCentral.password = System.getenv("GRAILS_CENTRAL_PASSWORD")
