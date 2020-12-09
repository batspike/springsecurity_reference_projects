package com.samcancode.security.providers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.samcancode.security.authtokens.AuthorizationCodeAuthenticationToken;
import com.samcancode.security.entities.Otp;
import com.samcancode.security.repositories.OtpRepository;

@Component
public class AuthorizationCodeAuthProvider implements AuthenticationProvider {
	
	@Autowired
	private OtpRepository otpRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("\t... authenticate using "+ this.getClass().getName());
		
		String username = authentication.getName();
		String rawAuthorizationCode = String.valueOf(authentication.getCredentials());
		
		Optional<Otp> optionalOtp = otpRepo.findOtpByUsername(username);
		Otp otp = optionalOtp.orElseThrow(() -> new BadCredentialsException(username));
		
		if (otp.getAuthorization().equals(rawAuthorizationCode)) {
			return new AuthorizationCodeAuthenticationToken(username, rawAuthorizationCode, List.of( () -> "read"));
		}
		
		throw new BadCredentialsException("Fail to authenticate!");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		/*
		 * Authentication Manager will call this method to check for authentication token type is supported.
		 * If yes, then it will call the above authenticate method. 
		 * Here we check for support for AuthorizationCodeAuthenticationToken type since this is the type 
		 * returned by authenticate method above if authenticate is successful.
		 */
		return AuthorizationCodeAuthenticationToken.class.equals(authentication);
	}

}
