package com.energizedwork.miniprofiler;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ProfilingView implements View {

    public String getContentType() {
        return null;
    }

    public void render(Map<String, ?> stringMap, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

    }
}
