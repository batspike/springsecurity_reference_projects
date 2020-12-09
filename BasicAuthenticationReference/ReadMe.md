# springboot-security

## Reference Project to demo Spring Boot Security
This project consists of a bare bone Spring Boot MVC Web application with a web endpoint to help demonstrate Spring Security. The primary focus of the project is to explore how Spring Security works and ways to configure it. This project primary focus is on Basic Authentication where a request with a user name and password is submitted for authentication. A database (MySQL) has been setup to demonstrate user persistence. 

The following Rest endpoint has been created for testing:
- http://localhost:8080/hello

Login with bill/12345

#### Components of Spring Security Architecture

Spring Security consists of the following components:
1. Authentication Filter - SecurityContext
2. Authentication Manager
3. Authentication Provider(s)
4. UserDetailsService / UserDetailsManager
5. PasswordEncoder

#### Process flow:
1. When a http request with Basic Authentication is sent from the client, there will be a header attribute called Authorization. The value of
this attribute is a base64 encoded of the username/password pair. This intercepted by the Authentication Filter at the server.
2. The Authentication Filter will convert the username/password into UsernamePasswordAuthenticationToken object. This is used by the
Authentication Manager to authenticate the user.
3. The Authentication Manager will look for a Authentication Provider that supports authenticating UsernamePasswordAunthicationToken type object.
4. The Authentication Provider will called the UserDetailsService to lookup user name from the database. This service returns a UserDetails type object.
5. Using the PasswordEncoder to verify the password from the request against UserDetails credential, the UserDetailsService will return authenticated Authentication token all the way back to the Authentication Filter.
6. If the authentication is successful the filter will set the session SecurityContext via the SecurityContextHolder.
7. The request is then pass onto downstream filter chain.
8. If authentication failed, the response is returned to client with status code 401.