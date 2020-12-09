package com.samcancode.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.samcancode.security.filters.CustomAuthenticationFilter;
import com.samcancode.security.providers.CustomAuthenticationProvider;
import com.samcancode.security.services.JpaUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomAuthenticationProvider authenticationProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public AuthenticationManager manager() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new JpaUserDetailsService();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// we manually create the filter instead of declaring it as a bean to avoid circular
		// instantiation between CustomAuthenticationFilter(AuthenticationManager) and AuthenticationManager bean.
		// Alternatively, we could autowired AuthenticationManager in CustomAuthenticationFilter instead of wiring
		// through its constructor. See CustomAuthenticationFilter constructor.
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter(manager());
		http.addFilterAt(filter, BasicAuthenticationFilter.class);
	}
	
	
	
	
}
