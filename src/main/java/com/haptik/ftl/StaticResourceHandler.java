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

    private final String staticAssetsPath;

    @Autowired
    public StaticResourceHandler(@Value("${static.folder}") String staticAssetsPath) {
        boolean isWindows = System.getProperty("os.name").startsWith("Windows");
        if (staticAssetsPath == null || staticAssetsPath.isEmpty()) {
            if (isWindows) {
                this.staticAssetsPath = String.format("file:///%s/templates/static/", System.getProperty("user.dir"));
            } else {
                this.staticAssetsPath = String.format("file:%s/templates/static/", System.getProperty("user.dir"));
            }
        } else {
            this.staticAssetsPath = staticAssetsPath;
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/ftl/static/**")
                .addResourceLocations(staticAssetsPath);
    }
}
