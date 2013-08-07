Grails Gson plugin
------------------

[![Build Status](https://travis-ci.org/tomdcc/grails-miniprofiler.png)][1]

This plugin provides the functionality of the StackExchange [.NET MiniProfiler][2] for Grails applications.

It uses the functionality of the [Grails Profiler plugin][3] along with SQL intrumentation using [log4jdbc][4] to shows timing information and sql query information in a heads-up display in a web page, useful for debugging database and other performance problems.

Installation
============

This plugin also requires the Profiler Plugin to be installed:

    plugins {
        // etc
        runtime ':profiler:0.4'
        runtime ':miniprofiler:0.1'
    }

You should then add the following to the bottom of any layouts that you would like to see the profiling information on, just inside the bottom of the html body:

        <!-- rest of layout above -->
        <miniprofiler:javascript/>
        </body>
    </html

Usage
=====

This plugin adds a clickable overlay onto your website as per the Stack Exchange MiniProfiler. Your controller methods, views and service methods are all visible, and any SQL queries executed during each of those calls can be seen.
See the talk from the Groovy & Grails Exchange 2012 which introduced the plugin [here][5].


Known Issues
============
 - Currently, the plugin depends on the current version of the profiler plugin from github, the last version 0.4 does not work well with Grails 2.2+
 - At time of writing, the profiler plugin does not correctly wrap controller methods which are not closures, so controller methods may or may not appear correctly in the output.

This is very early code, all bug reports and suggestions very welcome!

[1]:https://travis-ci.org/tomdcc/grails-miniprofiler
[2]:http://miniprofiler.com/
[3]:http://grails.org/plugin/profiler
[4]:https://code.google.com/p/log4jdbc/
[5]:http://skillsmatter.com/podcast/groovy-grails/debugging-grails-database-performance/te-6299
