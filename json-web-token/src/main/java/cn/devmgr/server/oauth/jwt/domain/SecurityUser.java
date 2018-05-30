package cn.devmgr.server.oauth.jwt.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityUser extends User implements UserDetails, Serializable {
	private static final Log log = LogFactory.getLog(SecurityUser.class);
	
	private static final long serialVersionUID = 6387651194240732484L;

	private Collection<GrantedAuthority> authorities;
	
	public SecurityUser(User user, List<String> userAuthorities){
		this.setId(user.getId());
		this.setName(user.getName());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		this.setDepartment(user.getDepartment());
		this.setPosition(user.getPosition());
		this.setPhone(user.getPhone());
		this.setEmail(user.getEmail());
		this.setEnabled(user.isEnabled());
		this.setExpireDate(user.getExpireDate());
		this.setHeadImage(user.getHeadImage());
		
		this.authorities = new ArrayList<GrantedAuthority>();
		for(String ua : userAuthorities){
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ua);
            this.authorities.add(authority);
		}
	    
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return (getExpireDate() == null ? true : (new Date()).before(getExpireDate()));
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		if(log.isTraceEnabled()){
			log.trace("SecurityUser isCredentialsNonExpired() called on " + this.getUsername());
		}
		return true;
	}


}
