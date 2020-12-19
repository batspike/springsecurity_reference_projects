# springboot-security

## Reference Project to demo Spring Boot Security with JWT - Resource Server
This is a Springboot project to demo how to configure the Resource Server using Springboot depandancy on Spring Security, and Cloud OAuth2 Security.
The config class ResourceServerConfig is annotated with @EnableResourceServer in addition to the regualar @Configuration. This class needs to extends the ResourceServerConfigurerAdapter class in order to inherit functionalities pre-built in Spring Security. However, there is no need to override any methods as the parameters to specify the uri for the public key is in the application.properties file along with the client-id/secret information.

The following Rest endpoint has been created for testing:<br>
 http://localhost:8080/hello

Authenticate with username/password bill/12345


<p>