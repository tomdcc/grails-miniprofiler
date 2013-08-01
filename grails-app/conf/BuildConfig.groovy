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
    }

    plugins {
        build(':release:2.2.1', ':rest-client-builder:1.0.3') {
            export = false
        }

        runtime ":hibernate:$grailsVersion"
        compile ':profiler:0.4'
    }
}

println "build config: " + System.getenv("GRAILS_CENTRAL_USERNAME")
grails.project.repos.grailsCentral.username = System.getenv("GRAILS_CENTRAL_USERNAME")
grails.project.repos.grailsCentral.password = System.getenv("GRAILS_CENTRAL_PASSWORD")

println "build config: set variable = $grails.project.repos.grailsCentral.username"
println "build config: set variable = $grails.project.repos.grailsCentral.password"