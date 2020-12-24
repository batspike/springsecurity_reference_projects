###Spring Security OAuth2 Single Sign-On using GitHub as Auth.Server
Dependency used in this Resource Server Project:
1. Spring Web
2. Spring Security
3. Spring Security - OAuth2 Client

####Note on Setup at GitHub:
1. Go to GitHub Settings -> Developer settings
2. Under 'OAuth Apps' tab section, create a new OAuth App
3. Follow instructions to create Client-Id and Client Secret, note them down.
4. Note the following Settings:
    <p>Homepage URL - this can be anything
    <p>Authorization callback URL - this must be the client web domain URL i.e. homepage
    