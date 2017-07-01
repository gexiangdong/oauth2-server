package cn.devmgr.server.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.devmgr.server.oauth.service.SecurityUserDetailService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
    private SecurityUserDetailService userDetailService;
	
	
	@Autowired
	public void globalUserDetails(final AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userDetailService);

		// @formatter:off
//		auth.jdbcAuthentication().dataSource(dataSource);
		// .withUser("username")
		// .password("password").roles("USER");
		// auth.inMemoryAuthentication()
		// .withUser("john").password("123").roles("USER").and()
		// .withUser("tom").password("111").roles("ADMIN").and()
		// .withUser("user1").password("pass").roles("USER").and()
		// .withUser("admin").password("nimda").roles("ADMIN");
	}// @formatter:on

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// @formatter:off
		http.csrf().disable().authorizeRequests().anyRequest().permitAll()
				.and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
		// @formatter:on

	}


}