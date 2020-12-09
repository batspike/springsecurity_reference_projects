package com.samcancode.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.filter.OncePerRequestFilter;

public class CustomAuthenticationFilter extends OncePerRequestFilter {
	
	private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
	
	private AuthenticationManager authenticationManager;
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		try {
			System.out.println("Authenticating in CustomAuthenticationFilter...");
			
			UsernamePasswordAuthenticationToken authRequestToken = this.authenticationConverter.convert(request);
			if (authRequestToken == null) { // no token to process continue with next filter chain
				chain.doFilter(request, response);
				return;
			}
			// otherwise token found, start authentication process

			Authentication authResult = this.authenticationManager.authenticate(authRequestToken);
			SecurityContextHolder.getContext().setAuthentication(authResult);
				
		}
		catch (AuthenticationException ex) { // in the event of any authentication exception
			
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;

		}

		chain.doFilter(request, response);
		
	}

}
