package com.tyf.mqas.config.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
/**
* @Description: 视图配置
* @Author: Mr.Tan
* @Date: 2019/10/8 9:19
*/
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    private final static Logger logger = LoggerFactory.getLogger(MvcConfig.class);



    public MvcConfig(){
        logger.info("********************加载WebConfig************************* ");
    }

    /**
    * @Description:  资源处理器
    * @Param:
    * @return:
    * @Author: Mr.Tan
    * @Date: 2019/10/8 9:19
    */
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
        registry.addViewController("/accessDenied").setViewName("exception/403");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {




    }
}
