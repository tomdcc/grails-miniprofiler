package com.energizedwork.miniprofiler;

import groovy.text.Template;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.grails.web.pages.GroovyPagesServlet;
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProfilingGroovyPagesServlet extends GroovyPagesServlet {

    @Override
    protected void renderPageWithEngine(GroovyPagesTemplateEngine engine, HttpServletRequest request,
                                        HttpServletResponse response, Template template) throws IOException {

        String pageName = (String)request.getAttribute(GrailsApplicationAttributes.GSP_TO_RENDER);
        if (StringUtils.isBlank(pageName)) {
            Object includePath = request.getAttribute("javax.servlet.include.servlet_path");
            if (includePath != null) {
                pageName = (String) includePath;
            } else {
                pageName = request.getServletPath();
            }
        }
        System.out.println(pageName);
        super.renderPageWithEngine(engine, request, response, template);

    }

}