grails.project.work.dir = 'target'
grails.project.source.level = 1.6

//plugin.location.profiler = "../grails-profiler-clean"

grails.project.dependency.resolution = {

    inherits 'global'
    log 'warn'

    repositories {
        grailsCentral()
    }

    plugins {
        compile ':profiler:0.4'
    }
}
