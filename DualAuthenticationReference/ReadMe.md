# springboot-security

## Reference Project to demo Dual Authentication in Spring Boot Security
This project consists of a bare bone Spring Boot MVC Web application with a web endpoint to help demonstrate dual authentication in Spring Security. The primary focus of the project is to explore how Spring Security for dual authentication. This project primary focus is on Basic Authentication where a request with a user name and password is submitted for authentication. A database (MySQL) has been setup to demonstrate user persistence. Upon successful Basic Authentication (first level), a random generated One-Time-Password code will be issue to the login user. The OTP code will be stored in the database and delivered to user, typically via mobile phone messaging (not demonstrated in this project). The user can then submit the OTP code with its user name for the second level authentication. Once the second level authentication is successful, an authorization code is return in the response header for use in the session. With the authorization code user can access resource end-point during the session.

The following Rest endpoint has been created for testing:
- http://localhost:8080/hello

Login with bill/12345
- OTP code is available from the database.

#### Components of Spring Security Architecture

Spring Security consists of the following components:
1. Authentication Filter - SecurityContext
2. Authentication Manager
3. Authentication Provider(s)
4. UserDetailsService / UserDetailsManager
5. PasswordEncoder
6. Authentication Tokens

#### Process flow:
1. When a http request with Basic Authentication is sent from the client, there will be a header attribute called Authorization. The value of
this attribute is a base64 encoded of the username/password pair. This is intercepted by the CustomUsernamePasswordAuthFilterr at the server.
2. For the purpose of dual authentication, the filter will check if there is a OTP attribute filter in the request header. If there is no OTP attribute, then the filter
will proceed to perform username/password token authentication. On the other hand, if there is a OTP attribute, then proceed to perform OTP authentication.
3. For username/password token authentication,
3.i. The Authentication Filter will convert the username/password into UsernamePasswordAuthenticationToken object. This is used by the
Authentication Manager in selecting appropriate Authentication Provider to authenticate the user.
3.ii. The Authentication Manager will look for a Authentication Provider that supports authenticating UsernamePasswordAunthicationToken type object - first level authentication.
3.iii. The Authentication Provider will called the UserDetailsService to lookup user name from the database. This service returns a UserDetails type object.
3.iv. Using the PasswordEncoder to verify the password from the request against UserDetails credential, the UserDetailsService will return authenticated Authentication token all the way back to the Authentication Filter.
3.v. If the authentication is successful the filter will generate a OTP code and save into the database where it will be send to the user via mobile messaging (not implemented in this project), and be ready for OTP authentication.
4. For OTP token authentication,
4.ii. A OtpAuthenticationToken will be created with the request username and OTP code. This OtpAuthenticationToken is used by the Authentication Manager to call the appropriate authentication provider. In this case
the OtpAuthProvider will be called.
4.iii. The OtpAuthProvider will lookup the database and verify the OTP code is valid. Invalid code will result in exception thrown. A valid code will result in a authenticated OtpAuthenticationToken, and a authorization code UUID will be generated and saved in the database.
4.iv. The UUID will be return in the header of the response object so that subsequent user requests will use it for access to other end-points.
5. The request is then pass onto downstream filter chain.
6. If authentication failed, the response is returned to client with status code 401.
7. Note that the CustomUsernamePasswordAuthFilter is applicable for incoming request to "/login" end-point.
8. For access to other end-point resources, the valid authorization code and username is sent as part of the request header. The request will be intercepted by the AuthorizationTokenAuthFilter.
9. The AuthorizationTokenAuthFilter will create a AuthorizationCodeAuthenticationToken for the the authentication manager to to call the appropriate provider, in this case the AuthorizationCodeAuthProvider.
10. Successful authentication by the provider will result in creation of SecurityContext for the request session, and continue processing with subsequent filter chain.

# 
