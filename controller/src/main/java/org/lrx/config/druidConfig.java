package org.lrx.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class druidConfig {
    //配置druid数据源的信息，使其能够正常使用
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }
    //配置druid的监控
    //1.配置一个管理就太的servlet
    @Bean
    public ServletRegistrationBean servletRegistration(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,Object> initparams = new HashMap<>();
        initparams.put("loginUsername","lrx");
        initparams.put("loginPassword","240034");
        initparams.put("allow","");   //默认就是允许所有访问
        initparams.put("deny","192.168.15.21");
        bean.setInitParameters(initparams);
        return bean;
    }
    //2.配置一个web监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        Map<String,Object> initparams = new HashMap<>();
        //忽略过滤格式
        initparams.put("exclusions","*.js,*.gif,*.jpg,*.png,*.ico,*.ja,*.css,/druid/*");
        bean.setInitParameters(initparams);
        //添加过滤股则
        bean.setUrlPatterns(Arrays.asList("/*"));
        return  bean;
    }
}
