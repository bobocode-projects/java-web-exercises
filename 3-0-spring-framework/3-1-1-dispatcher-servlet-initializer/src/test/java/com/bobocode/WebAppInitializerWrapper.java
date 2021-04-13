package com.bobocode;

import com.bobocode.config.WebAppInitializer;

class WebAppInitializerWrapper extends WebAppInitializer {
    public String[] getServletMappings() {
        return super.getServletMappings();
    }

    public Class[] getRootConfigClasses() {
        return super.getRootConfigClasses();
    }

    public Class<?>[] getServletConfigClasses() {
        return super.getServletConfigClasses();
    }
}