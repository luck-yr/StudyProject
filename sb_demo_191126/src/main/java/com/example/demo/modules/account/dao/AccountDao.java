package com.example.demo.modules.account.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.modules.account.entity.Resource;
import com.example.demo.modules.account.entity.Role;
import com.example.demo.modules.account.entity.User;

@Repository
@Mapper
public interface AccountDao {

	@Select("select * from m_user")
	public List<User> selectAllUsers() ;

	@Select("select * from m_role")
	public List<Role> selectAllRolls();

	@Select("select * from m_resource")
	public List<Resource> selectAllResources();

	@Delete("delete from m_user where user_id = #{userId}")
	public void deleteUserByUserId(int userId);

	@Delete("delete from m_user_role where user_id = #{userId}")
	public void deleteUserRoleByUserId(int userId);

	@Insert("insert into m_user_role (user_id,role_id) values(#{userId},#{roleId})")
	public void addUserRoleByUser(@Param("userId") int userId,@Param("roleId") int roleId);

	@Select("SELECT\r\n" + 
			"m_role.role_id,\r\n" + 
			"m_role.role_name\r\n" + 
			"FROM\r\n" + 
			"m_user_role\r\n" + 
			"INNER JOIN m_role ON m_user_role.role_id = m_role.role_id\r\n" + 
			"WHERE\r\n" + 
			"m_user_role.user_id = #{userId}")
	public List<Role> selectRolesByUserId(int userId);

	@Insert("insert into m_role (role_name) values(#{roleName})")
	public void insertRoleByRole(String roleName);

	@Update("update m_role set role_name = #{roleName} where role_id = #{roleId}")
	public void updateRoleByRole(String roleName, int roleId);

	@Delete("delete from m_role where role_id = #{roleId}")
	public void deleteRoleByRoleId(int roleId);

	@Delete("delete from m_resource where resource_id = #{resourceId}")
	public void deleteResourceByResourceId(int resourceId);

	@Select("SELECT\r\n" + 
			"m_role.role_id,\r\n" + 
			"m_role.role_name\r\n" + 
			"FROM\r\n" + 
			"m_role_resource\r\n" + 
			"INNER JOIN m_role ON m_role_resource.role_id = m_role.role_id\r\n" + 
			"WHERE\r\n" + 
			"m_role_resource.resource_id = #{resourceId}" )
	public List<Role> selectRolesByResourceId(int resourceId);

	@Insert("insert into m_resource (resource_name,resource_uri,permission) values(#{resourceName},#{resourceUri},#{permission}) ")
	@Options(useGeneratedKeys = true,keyColumn = "resource_id",keyProperty = "resourceId")
	public void addResourceByResource(Resource resource);

	@Update("update m_resource set resource_name = #{resourceName}, resource_uri = #{resourceUri}, permission = #{permission} where resource_id = #{resourceId}")
	public void updateResourceByResource(Resource resource);

	@Insert("insert into m_role_resource (resource_id,role_id) values(#{resourceId},#{roleId})")
	public void addRoleResourceBy(@Param("resourceId") int resourceId,@Param("roleId") int roleId);

	@Delete("delete from m_role_resource where resource_id = #{resourceId}")
	public void deleteRoleResourceByResourceId(int resourceId);

	@Select("select * from m_user where user_name = #{userName}")
	public User selectUserByUserName(String userName);

	@Insert("insert into m_user (user_name,password,create_date) values(#{userName},#{password},#{createDate})")
	public void insertUserByUser(User user);

	@Select("SELECT\r\n" + 
			"m_resource.resource_id,\r\n" + 
			"m_resource.permission,\r\n" + 
			"m_resource.resource_name,\r\n" + 
			"m_resource.resource_uri\r\n" + 
			"FROM\r\n" + 
			"m_role_resource\r\n" + 
			"INNER JOIN m_resource ON m_role_resource.resource_id = m_resource.resource_id\r\n" + 
			"WHERE\r\n" + 
			"m_role_resource.role_id = #{roleId}")
	public List<Resource> selectResoucesByRoleId(int roleId);

}
