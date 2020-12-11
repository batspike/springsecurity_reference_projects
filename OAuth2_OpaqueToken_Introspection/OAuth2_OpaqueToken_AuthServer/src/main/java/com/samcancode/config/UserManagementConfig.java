package com.samcancode.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@SuppressWarnings("deprecation")
@Configuration
public class UserManagementConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		var udm = new JdbcUserDetailsManager(dataSource); 

		/* IMPORTANT NOTE: The following codes should only be run once to create the users and authorities records per specified below.
		 * Attempting to recreate them when they already exist in the table will result in start up errors. All that is needed
		 * is to start and stop the application to create the records in the database.
		 * 
		var u = User.withUsername("bill").password("12345").authorities("read").build();
		udm.createUser(u);
		 * 
		 */
		
		return udm;
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean(); // needed by AuthServerConfig as a link to user login account.
	}

}
