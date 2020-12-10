package com.samcancode.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// note if specified, this encoder applies to Authorization Server (clientId/secret), 
		// 					  user management side has its own encoder;
		// 		if not specified, then Auth.Server will use the same encoder as specified in user management side.
		security.passwordEncoder(NoOpPasswordEncoder.getInstance());
	
		// this enable the check token endpoint - default is disabled;
		// possible setting are:
		// 	permitAll() - no need authentication when checking token
		// 	isAuthenticated() - need clientid/secret to authenticate when checking token.
		security.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Grant Types:
		// 1. authorization_code / pkce - User is redirected by client app to a page in Auth.Server for authentication.
		// 2. password ( to be deprecated ) - User's id/password is used by the client app to authenticate with Auth. Server.
		// 3. client_credentials
		// 4. refresh_token
		// 5. implicit ( deprecated )
		
		clients.jdbc(dataSource) // .inMemory()

		/* IMPORTANT NOTE: The following codes should only be run once to create the oauth_client_details records per specified below.
		 * Attempting to recreate them when they already exist in the table will result in status 401 Unauthorized.
		 * 
			// Grant Type = password
			// User submit username/password to client app which then authenticate with Auth.Server together with its clientId/Secret to get token.
			// The client app submit the username/password to Auth.Server together with its own client1/secret1 to obtain the access code.
			.withClient("client1").secret("secret1").scopes("read")
			.accessTokenValiditySeconds(5000)
			.authorizedGrantTypes("password","refresh_token")
			.and()
			.withClient("resourceserver").secret("12345").scopes("read")
			.and()
				
			// Grant Type = authorization_code
			// User initiate login via client app which then redirect user to Auth.Server login page for username/password authentication.
			// Auth.Server then return to client app if user login successful, and client submit its clientId/Secret to Auth.Server.
			// Auth.Server return authorization code if successful.
			.withClient("client2").secret("secret2").scopes("read").authorizedGrantTypes("authorization_code", "refresh_token")
			.redirectUris("http://localhost:9090") // redirect link to the Resource Server after successfully authenticated with Auth.Server.
			.and()
				
			// Grant Type = client_credentials
			// Client App send clientid/secret to Auth.Server
			// Auth.Server return Access Token and Refresh Token to Client App.
			.withClient("client3").secret("secret3").scopes("read").authorizedGrantTypes("client_credentials")
		
		*/
		
		;
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore()); // use JDBC database access for token persistence.
		endpoints.authenticationManager(authenticationManager); // this link this Authorization Server Config to the UserManagementConfig.
		endpoints.userDetailsService(userDetailsService); // needed for refresh token.
	}

}
