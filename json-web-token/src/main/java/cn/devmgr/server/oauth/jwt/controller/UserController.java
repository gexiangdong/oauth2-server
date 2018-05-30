package cn.devmgr.server.oauth.jwt.controller;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.devmgr.server.oauth.jwt.domain.SecurityUser;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final Log log = LogFactory.getLog(UserController.class);
	
	
    @RequestMapping("/me")
    public Principal user(Principal principal) {
        	if(log.isTraceEnabled()){
        		log.trace("---------------UserController.user()----------------\r\n" + principal + "\r\n--------------------\r\n"); 
        	}
        return principal;
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public SecurityUser getUser(@PathVariable("id")  int id) {
        return null;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public SecurityUser[] listAll() {
        return null;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public SecurityUser createUser(@RequestBody SecurityUser user){
        if(log.isTraceEnabled()) {
            log.trace("create user=" + user + "; " + user.getName());
        }

        return user;
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public SecurityUser modifyUser(@RequestBody SecurityUser user){
        if(log.isTraceEnabled()) {
            log.trace("modifyUser user=" + user + "; " + user.getName());
        }

        return user;
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public SecurityUser deleteUser(@PathVariable("id")  int id){
        if(log.isTraceEnabled()) {
            log.trace("deleteUser user=" + id + "; ");
        }

        return null;
    }
    
}
