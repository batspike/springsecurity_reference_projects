package com.samcancode.security.filters;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.samcancode.security.authtokens.OtpAuthenticationToken;

/*
 * This filter provides dual authentication on UsernamePasswordAuthenticationToken and OtpAuthenticationToken.
 * It only support request for end-point "/login".
 */
public class CustomUsernamePasswordAuthFilter extends OncePerRequestFilter {
	
	private BasicAuthenticationConverter authenticationConverter = new BasicAuthenticationConverter();
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("Filtering process by "+ this.getClass().getName());
		
		String otpUsername = request.getHeader("username");
		String requestOTP = request.getHeader("otp");
		
		try {
			if (requestOTP == null) {
				// perform UsernamePassword token authentication
				System.out.println("\t... authenticating username/password");
				
				UsernamePasswordAuthenticationToken authRequestToken = this.authenticationConverter.convert(request);
				if (authRequestToken == null) { // no token to process continue with next filter chain
					filterChain.doFilter(request, response);
					return;
				}
				// otherwise token found, start authentication process
				this.authenticationManager.authenticate(authRequestToken);
				
				// at this point, authentication is successful and OTP has been set
				return;
				
			}
			else {
				// perform OTP authentication
				System.out.println("\t... authenticating OTP");
				
				Authentication a = new OtpAuthenticationToken(otpUsername, requestOTP );
				Authentication authResult = this.authenticationManager.authenticate(a);
				
				// at this point otp is authenticated successfully, and authResult credentials contain the Authorization code.
				// We set the Authorization attribute in the response header for subsequent request. 
				// a separate filter will be used to process this attribute when used for other end-points not equal "/login".
				response.setHeader("authorization", String.valueOf(authResult.getPrincipal())); // principal has been set to the authorization code uuid.
				
				return;
			}
		}
		catch (AuthenticationException ex) { // in the event of any authentication exception
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/login"); // this filter is only applicable to end-point "/login"
	}

	
}
