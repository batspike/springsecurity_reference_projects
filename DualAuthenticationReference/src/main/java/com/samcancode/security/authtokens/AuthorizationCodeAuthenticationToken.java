package com.samcancode.security.authtokens;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
/*
 * The purpose of creating this class even though it appears to be doing the same thing as
 * the inbuilt UsernamePasswordAuthenticationToken type, is so that the Authentication Manager
 * will know to use our custom AuthorizationCodeAuthProvider during authentication.
 * Authentication Manager select the appropriate provider based on the authentication type,
 * which in this case will use the AuthorizationCodeAuthProvider for AuthorizationCodeAuthenticationToken type.
 */
public class AuthorizationCodeAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	public AuthorizationCodeAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
		
	}

	public AuthorizationCodeAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		
	}

}
