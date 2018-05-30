package cn.devmgr.test;

import cn.devmgr.server.oauth.jwt.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppTest {

    @Test
    public void contextLoads() {
        //if loaded then pass
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        System.out.println("admpwd=" + bpe.encode("admpwd"));
        System.out.println("thepwd=" + bpe.encode("thepwd"));
    }
}
