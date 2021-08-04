package com.haptik.ftl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticResourceHandler implements WebMvcConfigurer {

    private final String jsFolderPath;
    private final String cssFolderPath;

    @Autowired
    public StaticResourceHandler(@Value("${static.folder.js}") String jsFolderPath, @Value("${static.folder.css}") String cssFolderPath) {
        boolean isWindows = System.getProperty("os.name").startsWith("Windows");
        if (jsFolderPath == null || jsFolderPath.isEmpty()) {
            if (isWindows) {
                this.jsFolderPath = String.format("file:///%s/templates/static/js/", System.getProperty("user.dir"));
            } else {
                this.jsFolderPath = String.format("file:%s/templates/static/js/", System.getProperty("user.dir"));
            }
        } else {
            this.jsFolderPath = jsFolderPath;
        }
        if (cssFolderPath == null || cssFolderPath.isEmpty()) {
            if (isWindows) {
                this.cssFolderPath = String.format("file:///%s/templates/static/css/", System.getProperty("user.dir"));
            } else {
                this.cssFolderPath = String.format("file:%s/templates/static/css/", System.getProperty("user.dir"));
            }
        } else {
            this.cssFolderPath = cssFolderPath;
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/ftl/static/js/**")
                .addResourceLocations(jsFolderPath);
        registry.addResourceHandler("/ftl/static/css/**")
                .addResourceLocations(cssFolderPath);
    }
}
