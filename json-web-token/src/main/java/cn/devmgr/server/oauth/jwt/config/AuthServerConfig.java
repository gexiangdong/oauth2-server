package cn.devmgr.server.oauth.jwt.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import cn.devmgr.server.oauth.jwt.service.SecurityUserDetailService;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final Log log = LogFactory.getLog(AuthServerConfig.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityUserDetailService userDetailsService;

    @Autowired
    private DataSource dataSource;

    private transient BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Bean
    public TokenStore tokenStore() {
        if(log.isTraceEnabled()){
            log.trace("..tokenStore()..");
        }
        return new JwtTokenStore(accessTokenConverter());
    }
    
    
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
         return new JdbcAuthorizationCodeServices(dataSource);
    }



    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.passwordEncoder(passwordEncoder).allowFormAuthenticationForClients()
            .checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // use default token.        
//        endpoints.authenticationManager(authenticationManager)
//               .tokenStore(tokenStore())
//               .accessTokenConverter(accessTokenConverter());

    	TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore())
		        .tokenEnhancer(tokenEnhancerChain)
		        .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

    }

   
 

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        if(log.isTraceEnabled()){
            log.trace("..accessTokenConverter()..");
        }
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory = 
                new KeyStoreKeyFactory(new ClassPathResource("oauthserver.jks"), "hellojwttoken".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("thejwttokenkey"));
//        converter.setSigningKey("userId");
        return converter;
    }
    
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new AuthoritiesTokenEnhancer();
    }
}
