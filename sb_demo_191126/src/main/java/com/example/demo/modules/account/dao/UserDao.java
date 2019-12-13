package com.example.demo.modules.account.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.account.entity.User;

@Repository
@Mapper
public interface UserDao {

	@Insert("insert into m_user (user_name,password,create_date) values(#{userName},#{password},#{createDate})")
	@Options(useGeneratedKeys = true,keyColumn = "user_id",keyProperty = "userId")
	public void addUserByUser(User user);

	@Delete("delete from m_user where user_id = #{userId}")
	public void deleteUserByUserId(int userId);

	@Update("update m_user set password = #{password} where user_id = #{userId}")
	public void updateUserByUser(User user);

	@Select("select * from m_user where user_id = #{userId}")
	public User selectUserByUserId(int userId);
	
	@Select("select *from m_user")
	public List<User> selectUsers();

}
