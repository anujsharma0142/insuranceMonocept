package com.insurance.monocept.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().antMatcher("/**").addFilterBefore(getCustomFilter(),
				BasicAuthenticationFilter.class);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CustomTokenAuthenticationFilter getCustomFilter() {
		return new CustomTokenAuthenticationFilter();
	}

	/**
	 * To stop registering {@link}CustomTokenAuthenticationFilter filter
	 * automatically.
	 * 
	 * @param filter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<CustomTokenAuthenticationFilter> registration(CustomTokenAuthenticationFilter filter) {
		FilterRegistrationBean<CustomTokenAuthenticationFilter> registration = new FilterRegistrationBean<CustomTokenAuthenticationFilter>(filter);
		registration.setEnabled(false);
		return registration;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api/v1/user/signup", "/api/v1/user/login", "/swagger-ui/**", "/api/vi/admin/getImage/{fileName}",
				"/rpg-socket/**", "/topic/greetings", "/app/hello", "/user/signup","/user/login", "/verify/**",
				"/v2/api-docs", "/v3/api-docs", "/configuration/ui", "/swagger-resources/**","/api/vi/admin/signupAdmin");
	}

}