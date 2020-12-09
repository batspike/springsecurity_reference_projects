package com.samcancode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.samcancode.security.filters.AuthorizationTokenAuthFilter;
import com.samcancode.security.filters.CustomUsernamePasswordAuthFilter;
import com.samcancode.security.providers.AuthorizationCodeAuthProvider;
import com.samcancode.security.providers.CustomUsernamePasswordAuthProvider;
import com.samcancode.security.providers.OtpAuthProvider;

@SuppressWarnings("deprecation")
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomUsernamePasswordAuthProvider customUsernamePasswordProvider;
	
	@Autowired
	private OtpAuthProvider otpAuthProvider;
	
	@Autowired
	private AuthorizationCodeAuthProvider authorizationCodeAuthProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// using clear password encoder for simplicity.
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	// making our custom filter available via ApplicationContext for use by AuthenticationManager.
	public CustomUsernamePasswordAuthFilter customUsernamePasswordAuthFilter() {
		return new CustomUsernamePasswordAuthFilter();
	}
	
	@Bean
	// making our custom filter available via ApplicationContext for use by AuthenticationManager.
	public AuthorizationTokenAuthFilter authorizationTokenAuthFilter() {
		return new AuthorizationTokenAuthFilter();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// this is how we add custom authentication providers to Spring Security.
		auth.authenticationProvider(customUsernamePasswordProvider)
			.authenticationProvider(otpAuthProvider)
			.authenticationProvider(authorizationCodeAuthProvider);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// This is how we add our custom filters to the filter chain.
		// We called the custom filter methods directly to avoid circular dependency.
		http.addFilterAt(customUsernamePasswordAuthFilter(), BasicAuthenticationFilter.class);
		http.addFilterAt(authorizationTokenAuthFilter(), BasicAuthenticationFilter.class);
	}
	
	
	
}
