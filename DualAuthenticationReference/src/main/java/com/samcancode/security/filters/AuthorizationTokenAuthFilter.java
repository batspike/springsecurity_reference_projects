package com.samcancode.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.samcancode.security.authtokens.AuthorizationCodeAuthenticationToken;

/*
 * This filter intercepts request for all end-points other than "/login".
 * It will verify the AuthorizationCodeAuthenticationToken via AuthorizationCodeAuthProvider.
 */
public class AuthorizationTokenAuthFilter extends OncePerRequestFilter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("Filtering process by "+ this.getClass().getName());
		
		try {
			String authorizationCode = request.getHeader("authorization");
			String username = request.getHeader("username");
			
			Authentication a = new AuthorizationCodeAuthenticationToken(username, authorizationCode);
			Authentication authResult = this.authenticationManager.authenticate(a);
			
			// At this point, authentication is successful, so we set the SecurityContext and continue with the filter chain.
			SecurityContextHolder.getContext().setAuthentication(authResult);
			filterChain.doFilter(request, response);
		}
		catch(AuthenticationException e) {
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals(String.valueOf("/login")); //this filter supports all end-points other than "/login"
	}
	
	

}
