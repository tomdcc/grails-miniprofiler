package com.energizedwork.miniprofiler;

import groovy.text.Template;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.grails.web.pages.GroovyPagesServlet;
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine;
import org.codehaus.groovy.grails.web.pages.discovery.GroovyPageScriptSource;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;

public class ProfilingGroovyPagesServlet extends GroovyPagesServlet {

    protected void renderPageWithEngine(GroovyPagesTemplateEngine engine, HttpServletRequest request,
                                        HttpServletResponse response, Template template) throws IOException {

        String pageName = determinePageName(request);
//        System.out.println(pageName);
//        super.renderPageWithEngine(engine, request, response, template);
    }

    protected void renderPageWithEngine(GroovyPagesTemplateEngine engine, HttpServletRequest request,
                                        HttpServletResponse response, GroovyPageScriptSource scriptSource) throws Exception {

        String pageName = determinePageName(request);
//        System.out.println(pageName);
        super.renderPageWithEngine(engine, request, response, scriptSource);
    }

    protected String determinePageName(HttpServletRequest request) {

        String pageName = (String)request.getAttribute(GrailsApplicationAttributes.GSP_TO_RENDER);
        if (StringUtils.isBlank(pageName)) {
            Object includePath = request.getAttribute("javax.servlet.include.servlet_path");
            if (includePath != null) {
                pageName = (String) includePath;
            } else {
                pageName = request.getServletPath();
            }
        }

        return pageName;
    }
}
