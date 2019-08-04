package com.tyf.mqas.config.webMvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    private final static Logger logger = LoggerFactory.getLogger(MvcConfig.class);



    public MvcConfig(){
        logger.info("********************加载WebConfig************************* ");
    }

    /*资源处理器*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(!registry.hasMappingForPattern("/**")){
            registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        }
    }

    /**
     * 配置访问路径
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/treetable").setViewName("system/treetable");
        registry.addViewController("/accessDenied").setViewName("exception/403");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {




    }
}
