package com.samcancode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated();
		
		http.oauth2Login();
	}
	
	@Bean
	public ClientRegistrationRepository crr() {
		return new InMemoryClientRegistrationRepository(gitHubClient());
	}

	private ClientRegistration gitHubClient() {
		return CommonOAuth2Provider.GITHUB.getBuilder("github")
				.clientId("503a800d9cc4128b3716")
				.clientSecret("d1acc6ac4ff959f221320dca0d508ade1c4b198e")
				.build();
	}
}
