package cn.devmgr.server.oauth.controller;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	private static final Log log = LogFactory.getLog(UserController.class);
	
	
    @RequestMapping("/user/me")
    public Principal user(Principal principal) {
    	if(log.isTraceEnabled()){
    		log.trace("---------------UserController.user()----------------\r\n" + principal + "\r\n--------------------\r\n"); 
    	}
        return principal;
    }
}
