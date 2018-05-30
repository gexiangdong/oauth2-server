package cn.devmgr.server.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cn.devmgr.server.oauth.service.SecurityUserDetailService;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private SecurityUserDetailService userDetailService;
    

     @Override
     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
         auth.userDetailsService(userDetailService);
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
    
    /**
     * 实际中请勿使用明文密码, 如果仅仅是为了测试和学习，可以替换成NoOpPasswordEncoder，即明文密码
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return passwordEncoder;
        //return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
    
    public static void main(String[] argv) throws Exception{
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("admpwd=" + passwordEncoder.encode("admpwd"));
        System.out.println("secret=" + passwordEncoder.encode("secret"));
        System.out.println("thepwd=" + passwordEncoder.encode("thepwd"));
    }
}
