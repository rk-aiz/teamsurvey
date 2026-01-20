package com.github.rk_aiz.teamsurvey;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Configuration
public class ProxyConfig {

    @Value("${app.proxy.prefix:}")
    private String proxyPrefix;

    // 1. 不足している X-Forwarded-Prefix ヘッダーを強制的に注入するフィルタ
    @Bean
    public FilterRegistrationBean<Filter> addProxyPrefixFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletRequest req = (HttpServletRequest) request;
                chain.doFilter(new HttpServletRequestWrapper(req) {
                    @Override
                    public String getHeader(String name) {
                        // code-serverが送ってこないPrefixをここで補完
                        if ("X-Forwarded-Prefix".equalsIgnoreCase(name) && proxyPrefix != null && !proxyPrefix.isEmpty()) {
                            return proxyPrefix;
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        List<String> names = Collections.list(super.getHeaderNames());
                        if (!names.contains("X-Forwarded-Prefix") && proxyPrefix != null && !proxyPrefix.isEmpty()) {
                            names.add("X-Forwarded-Prefix");
                        }
                        return Collections.enumeration(names);
                    }
                }, response);
            }
        });
        // SpringのForwardedHeaderFilterより先に実行させる
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    // 2. Spring標準のForwardedHeaderFilterを手動登録（注入したヘッダーを読み取らせるため）
    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        // 上記のフィルタの直後に実行
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return bean;
    }
}