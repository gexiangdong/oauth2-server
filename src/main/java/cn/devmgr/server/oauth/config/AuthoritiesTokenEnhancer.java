package cn.devmgr.server.oauth.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import cn.devmgr.server.oauth.domain.SecurityUser;

/**
 * 修改此类，可能测试时发现获得的AccessToken还是老的样子，没有改变。这是因为没改的AccessToken是修改前
 * 生成的，被存在数据库内，还没过期。可以删除oauth_access_token表内数据测试，或者换个新用户。
 *
 */
public class AuthoritiesTokenEnhancer implements TokenEnhancer {
	private static final Log log = LogFactory.getLog(AuthoritiesTokenEnhancer.class);
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		if(log.isTraceEnabled()){
			log.trace("user in authentication " + authentication.getPrincipal().getClass().getName());
		}
		SecurityUser su = (SecurityUser)  authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("userId", su.getId());
        additionalInfo.put("name", su.getName());
        additionalInfo.put("email", su.getEmail());
        additionalInfo.put("phone", su.getPhone());
        additionalInfo.put("headImage", su.getHeadImage());
        additionalInfo.put("authorities", authentication.getAuthorities());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
	}

}
