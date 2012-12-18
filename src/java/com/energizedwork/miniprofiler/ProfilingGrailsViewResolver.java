package com.energizedwork.miniprofiler;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class ProfilingGrailsViewResolver implements ViewResolver {

    private ViewResolver wrapped;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        return wrapped.resolveViewName(viewName, locale);
    }

    public void setWrapped(ViewResolver wrapped) {
        this.wrapped = wrapped;
    }
}
