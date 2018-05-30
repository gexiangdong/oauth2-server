package cn.devmgr.server.oauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.devmgr.server.oauth.domain.User;

@Mapper
public interface UserDao {

	 @Select("SELECT id, username, name, password, head_image, email, phone, position, department, enabled, expireDate FROM users WHERE username = #{username}")
	 User findByUsername(@Param("username") String username);
	 
	 @Select("select ga.authority from groups g join group_authorities ga on g.id=ga.group_id join group_members gm on g.id=gm.group_id where gm.user_id = #{userId}")
	 List<String> findUserAuthorities(@Param("userId") int userId);
}
