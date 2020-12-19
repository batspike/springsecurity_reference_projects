# springboot-security

## Reference Project to demo Spring Boot Security with JWT - Authorization Server
This is a Springboot project to demo how to configure the Authorization Server using Springboot depandancy on Spring Security, and Cloud OAuth2 Security.
There are two config classes in the setup. First, there is a end user authentication configuration, UserManagementConfig which inherits the 
WebSecurityConfigurerAdapter class. This config file setup the end user login username/password and authorities. For simplicity, we use the in memory for this setup. The password encoder is also defined, and the Authentication Manager is also exposed in the context for the second config. The second config class is related to the Authorization Server, and inherits from AuthorizationServerConfigurerAdapter. Key configurations are:
1. TokenStore - This defines the source of the token.
2. JwtAccessTokenConverter - this defines how the token certificate and its private and public keys. This converter is used by TokenStore.
3. Configurations inherited from AuthorizationServerConfigurerAdapter - As mentioned there are three methods that are used to further configure OAuth2:
a. AuthorizationServerSecurityConfigurer - this specify if authentication is required when querying the public key using client-id/secret.
b. ClientDetailsServiceConfigurer - this specify the list of client's application client-id/secret, scopes, and grant types to support.
c. AuthorizationServerEndpointsConfigurer - this specify the endpoints configuration. At a minimum, the token store, token converter, and authentication manager must be setup.


### Generating JWT private and public keys
The certificate file ssia.jks along with its key pair is generated via the following command with password ssia123. The ssia.jks should be placed at the src/main/resources location since the code is reading from there.
1. private key: keytool -genkeypair -alias ssia -keyalg RSA -keypass ssia123 -keystore ssia.jks -storepass ssia123
2. public- key: keytool -list -rfc --keystore ssia.jks | openssl x509 -inform pem -pubkey

<p>