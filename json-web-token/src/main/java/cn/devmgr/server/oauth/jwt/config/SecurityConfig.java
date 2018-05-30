package cn.devmgr.server.oauth.jwt.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cn.devmgr.server.oauth.jwt.service.SecurityUserDetailService;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log log = LogFactory.getLog(SecurityConfig.class);


    @Autowired
    private SecurityUserDetailService userDetailService;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        if(log.isTraceEnabled()) {
            log.trace("creating passwordEncoder...");
        }
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
     @Override
     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.authenticationProvider(authProvider());
         //auth.userDetailsService(userDetailService); //如果不需要passwordEncoder可以这样写
    }
     
    @Override
    protected void configure(HttpSecurity http) throws Exception { // @formatter:off
        http.requestMatchers()
            .antMatchers("/login", "/oauth/authorize")
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin().loginPage("/login")
            .permitAll();
        
    } // @formatter:on


    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    

    public static void main(String[] argv) throws Exception{
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("admpwd=" + passwordEncoder.encode("admpwd"));
        System.out.println("secret=" + passwordEncoder.encode("secret"));
        System.out.println("thepwd=" + passwordEncoder.encode("thepwd"));
    }
}
