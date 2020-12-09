package com.samcancode.security.providers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.samcancode.security.authtokens.OtpAuthenticationToken;
import com.samcancode.security.entities.Otp;
import com.samcancode.security.repositories.OtpRepository;

@Component
public class OtpAuthProvider implements AuthenticationProvider {
	
	@Autowired
	private OtpRepository otpRepo;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("\t... authenticate using "+ this.getClass().getName());
		
		String username = authentication.getName();
		String rawOtp = String.valueOf(authentication.getCredentials());
		
		Optional<Otp> optionalOtp = otpRepo.findOtpByUsername(username);
		Otp otp = optionalOtp.orElseThrow(() -> new BadCredentialsException("Bad OTP"));
		
		if (otp.getOtp().equals(rawOtp)) {
			// all is good, so issue authorization code
			
			String uuid = UUID.randomUUID().toString();
			otp.setOtp(null); // we reset the otp to null as it is a one time use
			otp.setAuthorization(uuid);
			otpRepo.save(otp);

			//set the principal to uuid, for use in response header in filter.
			return new OtpAuthenticationToken(uuid, uuid, List.of( () -> "read"));
		}
		
		throw new BadCredentialsException("Fail to authenticate!");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		/*
		 * Authentication Manager will call this method to check for authentication token type is supported.
		 * If yes, then it will call the above authenticate method. 
		 * Here we check for support for OtpAuthenticationToken type since this is the type 
		 * returned by authenticate method above if authenticate is successful.
		 */
		return OtpAuthenticationToken.class.equals(authentication);
	}

}
