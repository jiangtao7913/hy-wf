package com.hy.wf.api.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @program: hy-wf
 * @description: web配置信息
 * @author: jt
 * @create: 2019-01-04 15:14
 **/
@Configuration
public class HttpConvertConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorHandler interceptorHandler;

    /**
     * @Description: 添加fastjson支持
     * @Param: [converters]
     * @return: void
     * @Author: jt
     * @Date: 2018/12/25
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1.定义一个converters转换消息的对象
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        // 2.添加fastjson的配置信息，比如: 是否需要格式化返回的json数据
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.PrettyFormat);
        // 3.在converter中添加配置信息
        converter.setFastJsonConfig(config);
//        // 4.将converter赋值给HttpMessageConverter
//        HttpMessageConverter<?> httpMessageConverter = converter;
        // 5.返回HttpMessageConverters对象
        converters.add(converter);
    }

    /**
     * @Description:  添加跨域支持
     * @Param: [registry]
     * @return: void
     * @Author: jt
     * @Date: 2018/12/25
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                //配置可以被跨域的路径，可以任意配置，可以具体到直接请求路径
                .addMapping("/**")
                //允许所有的请求域名访问我们的跨域资源
                .allowedOrigins("*")
                //允许所有的请求方法访问该跨域资源服务器
                .allowedMethods("*")
                //允许所有的请求header访问
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * @Description:  添加拦截器
     * @Param: [registry]
     * @return: void
     * @Author: jt
     * @Date: 2018/12/25
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(interceptorHandler).addPathPatterns("/**").excludePathPatterns("/error")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }
}
