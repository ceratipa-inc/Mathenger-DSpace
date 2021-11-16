package com.example.mathengerapi.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

@RequiredArgsConstructor
public class CustomSpringLiquibase implements InitializingBean, BeanNameAware, ResourceLoaderAware {
    private final SpringLiquibase springLiquibase;

    @Override
    public void setBeanName(String name) {
        springLiquibase.setBeanName(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        springLiquibase.afterPropertiesSet();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        springLiquibase.setResourceLoader(resourceLoader);
    }
}
