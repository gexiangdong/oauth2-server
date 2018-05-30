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
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final Log log = LogFactory.getLog(AuthServerConfig.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    private transient BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public JdbcTokenStore tokenStore() {
        if(log.isTraceEnabled()){
            log.trace("..tokenStore()..");
        }
        return new JdbcTokenStore(dataSource);
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
        // 注意authorizedGrantTypes 方法，指定授权类型，如果没有password，则必须在此模块提供的页面登录,
        // 通过跳转回去带有authorization_code，然后获取token
        // 如果rs1需要密码保护(password模式下密码保护意义不大，authorization_code模式可添加密码保护)，可在
        // withClient("rs1")后增加.secret(passwordEncoder.encode("secret"))
//        clients.inMemory()
//            .withClient("rs1")
//            .authorizedGrantTypes("password","authorization_code", "refresh_token")
//            .scopes("user_info")
//            .autoApprove(true);
        

        clients.jdbc(dataSource);

    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // use default token.
        endpoints.authenticationManager(authenticationManager);

        // use custom token.
        // endpoints.authorizationCodeServices(authorizationCodeServices())
        //        .authenticationManager(authenticationManager).tokenStore(tokenStore())
        //        .tokenEnhancer(tokenEnhancer()).accessTokenConverter(accessTokenConverter())
        //        .approvalStoreDisabled();
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
