package cn.devmgr.server.oauth;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class Application {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}



//	@Autowired
//	public void init(AuthenticationManagerBuilder auth) throws Exception {
//		// @formatter:off
//			auth.jdbcAuthentication().dataSource(dataSource).withUser("dave")
//					.password("secret").roles("USER", "ACTUATOR");
//			// @formatter:on
//	}
}
