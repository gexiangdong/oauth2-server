package cn.devmgr.server.oauth.jwt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cn.devmgr.server.oauth.jwt.dao.UserDao;
import cn.devmgr.server.oauth.jwt.domain.SecurityUser;
import cn.devmgr.server.oauth.jwt.domain.User;


@Service
public class SecurityUserDetailService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);
		if(user == null){
			throw new UsernameNotFoundException(username + " not found.");
		}
		
		List<String> authorities = userDao.findUserAuthorities(user.getId());
		SecurityUser su = new SecurityUser(user, authorities);
		
		return su;
	}

}
