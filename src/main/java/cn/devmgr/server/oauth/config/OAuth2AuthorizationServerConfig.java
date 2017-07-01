package cn.devmgr.server.oauth.config;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	private static final Log log = LogFactory.getLog(OAuth2AuthorizationServerConfig.class);

	@Autowired
	private AuthenticationManager auth;

	@Autowired
	private DataSource dataSource;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Bean
	public JdbcTokenStore tokenStore() {
		if(log.isTraceEnabled()){
			log.trace("..tokenStore()..");
		}
		return new JdbcTokenStore(dataSource);
	}

//	@Bean
//	public JwtTokenStore jwtTokenStore(){
//		return new JwtTokenStore( );
//	}
//	
	@Bean
	protected AuthorizationCodeServices authorizationCodeServices() {
		if(log.isTraceEnabled()){
			log.trace("..authorizationCodeServices()..");
		}
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security)
			throws Exception {
		if(log.isTraceEnabled()){
			log.trace("..configure(AuthorizationServerSecurityConfigurer security)..");
		}
		security.passwordEncoder(passwordEncoder).allowFormAuthenticationForClients()
				.checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		if(log.isTraceEnabled()){
			log.trace("..configure(AuthorizationServerEndpointsConfigurer endpoints)..");
		}
		endpoints.authorizationCodeServices(authorizationCodeServices())
				.authenticationManager(auth).tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancer()).accessTokenConverter(accessTokenConverter())
				.approvalStoreDisabled();
		
//		endpoints
//        .tokenStore(tokenStore)
//        .tokenEnhancer(tokenEnhancer())
//        .accessTokenConverter(accessTokenConverter())
//        .authorizationCodeServices(codeServices)
//        .authenticationManager(authenticationManager)
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		if(log.isTraceEnabled()){
			log.trace("..configure(ClientDetailsServiceConfigurer clients)..");
		}
		// @formatter:off
		clients.jdbc(dataSource)
				.passwordEncoder(passwordEncoder);
		
		//autoApprove (skip the  “OAuth Approval” page at oauth/authorize)


//				.withClient("a-micro-service")
//				.authorizedGrantTypes("authorization_code")
//				.authorities("ROLE_CLIENT").scopes("read", "trust")
//				.resourceIds("microservice").autoApprove(true)
//				.redirectUris("http://127.0.0.1?k=ms");
		
//			.withClient("my-trusted-client")
//				.authorizedGrantTypes("password", "authorization_code",
//						"refresh_token", "implicit")
//				.authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//				.scopes("read", "write", "trust")
//				.resourceIds("oauth2-resource")
//				.accessTokenValiditySeconds(60).and()
//			.withClient("my-client-with-registered-redirect")
//				.authorizedGrantTypes("authorization_code")
//				.authorities("ROLE_CLIENT").scopes("read", "trust")
//				.resourceIds("oauth2-resource")
//				.redirectUris("http://127.0.0.1?key=value").and()
//			.withClient("my-client-with-secret")
//				.authorizedGrantTypes("client_credentials", "password")
//				.authorities("ROLE_CLIENT").scopes("read")
//				.resourceIds("oauth2-resource").secret("secret");
		// @formatter:on
	}

	@Bean
    public TokenEnhancer tokenEnhancer() {
		if(log.isTraceEnabled()){
			log.trace("..tokenEnhancer()..");
		}
        return new AuthoritiesTokenEnhancer();
    }
	
	@Bean
	public DefaultAccessTokenConverter accessTokenConverter() {
		if(log.isTraceEnabled()){
			log.trace("..accessTokenConverter()..");
		}
	    return new DefaultAccessTokenConverter();
	}
}

		