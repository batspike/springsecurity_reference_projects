# springboot-security

## Reference Project to demo OAuth2 in Spring Boot Security
This project consists of a bare bone Spring Boot setup to demonstrate Spring Security implementation of OAuth2. OAuth2 provide a set of specification for authorizing an application to access another application via an authorization server. 

The following are the actors needed to understand OAuth2:
1. End user - this is the user with username/password who wish to access a resource server via another client application.
2. Client Application - the application in which the end user wish to use but requires services/resources from another application, the Resource Server.
3. Resource Server/Application - the application/server where end user has access to various resources/services.
4. Authorization Server - the application/server whose primary function is to authenticate end user (username/password) as well as the client application (clientId/secret).
5. OAuth2 not just authenticate, but also limit authorization access to Resource Server/Application accordingly.
6. Grant-Type - grant types dictates the behavior of the overall authentication process.

User Login with john/12345.
Client Application authentication: client/secret.

#### Spring Security Architecture per Grant Type
##### 1. Password Grant Type
<img src="GrantType_password.jpg"/><p>
a. User wants to use the Client App which need some resources/services from the Resource Server.<br>
b. User submit username/password (john/12345) to Client App.<br>
c. Client App send the username/password (john/12345) along with its clientid/secret (client1/secret1) to Authorization Server for authentication. The clientid/secret is for authentication of the Client App.<br>
d. Auth.Server returns a Access Token back to the Client App if authentication is successful.<br>
e. Client App uses the Access Token to request for resources/services from the Resource Server.<br>
f. If the token is valid, Resource Server will provide the requested resources/services to the Client App according to given authorization.<br>

##### 2. authorization_code Grant Type
<img src="GrantType_authorization_code.jpg"/><p>
a. User wants to use the Client App (browser - http://localhost:8080/oauth/authorize?response_type=code&client_id=client2&scope=read) which need some resources/services from the Resource Server.<br>
b. Client App (browser) is redirected to a username/password authorization page in the Authorization Server.<br>
c. User submit username/password (john/12345) to Auth.Server login page.<br>
d. Auth.Server will request for Approval to access resources/services (first time only).<br>
e. Auth.Server returns a Authorization Code to Client App upon succesful user authentication and approval.<br>
f. Client App (postman) send the Authorization Code along with its clientid/secret (client2/secret2) to Authorization Server for authentication. The clientid/secret is for authentication of the Client App. Note that the Authorization Code can only be used only once to obtain the access token.<br>
g. Auth.Server returns a Access Token back to the Client App if authentication is successful, along with a configured "redirectUris" (http://localhost:9090/?code=0xmYdo) to the resources/services.<br>
h. Client App uses the Access Token and the "redirectUris" to request for resources/services from the Resource Server.<br>
i. If the token is valid, Resource Server will provide the requested resources/services to the Client App according to given authorization.<br>

##### 3. client_credentials Grant Type - does not involve end user
<img src="GrantType_client_credentials.jpg"/><p>
a. There is no end-user involvement as this setup is generally meant for one app uses resources/services from another app.<br>
b. Client App send clientid/secret to the Authorization Server.<br>
c. Auth.Server returns a Access Token back to the Client App if authentication is successful.<br>
h. Client App uses the Access Token to request for resources/services from the Resource Server.<br>
i. If the token is valid, Resource Server will provide the requested resources/services to the Client App according to given authorization.<br>

##### 4. refresh_token Grant Type
<img src="GrantType_client_credentials.jpg"/><p>
a. This usually works together with another grant type, so they are usually configured together.<br>
b. Assuming we configure this grant type together with password grant type, then when performing authentication as explain in password grant type process, a additional "refresh_token" will be returned by Auth.Server at the end of the process.<br>
c. The refresh token allow us to get a new access token without having to perform end-user authentication again.<br>
d. The Client App will use the refresh token to request a new access token if the old one expired.<br>


#### Opaque Token vs JWT (Non-Opaque Token)
Opaque Token as described above is just a string of code which does not carry any meta-information. JWT on the other hand are tokens with embeded meta-information. Thus it is possible for JWT to self verified if the token is valid, but for Opaque Token we must verified with Auth.Server.<br>

#### 1. Opaque Token
How does the Resource Server knows if the access token is valid? It can verify with the Auth.Server via end-point /oauth/check_token, also known as Introspection Approach. This can be configured as shown in the AuthServerConfig. We override the configure(AuthorizationServerSecurityConfigurer security) method and set the checkTokenAccess().<br>
<img src="AccessToken_Verification.jpg"/><p>

Another way to verified access token is to have a shared database for Auth.Server and Resource Server. In this case, access token is save in the database when Auth.Server generates it. Resource Server can then read and verify from the database. This approach is also known as Black Boarding.

<p>A third less seen option is where the Auth.Server resides in the Resource Server, i.e. same application. In this case, we need to setup a ResourceServerConfig that is annotated with @EnableResourceServer. To access any resources (from controller) we must first get the access token, and then call the resource end-point with a header attribute Authorization containing the access token.<br>
<img src="AccessToken_Verification3.jpg"/><p>

# 
