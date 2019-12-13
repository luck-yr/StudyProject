package com.example.demo.config.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.modules.account.entity.Resource;
import com.example.demo.modules.account.entity.Role;
import com.example.demo.modules.account.entity.User;
import com.example.demo.modules.account.service.AccountService;

@Component
public class MyRealm extends AuthorizingRealm{

	@Autowired
	private AccountService accountService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		String userName  = (String) principals.getPrimaryPrincipal();
		User user = accountService.selectUserByUserName(userName);
		if(user == null) {
			return null;
		}
		List<Role> roles = accountService.selectRolesByUserId(user.getUserId());
		for(Role  role :roles) {
			authorizationInfo.addRole(role.getRoleName());
			List<Resource> resources = accountService.selectResoucesByRoleId(role.getRoleId());
			for(Resource resource :resources) {
				authorizationInfo.addStringPermission(resource.getPermission());
			}
		}
		
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName = (String) token.getPrincipal();
		User user = accountService.selectUserByUserName(userName);
		if(user == null) {
			throw new UnknownAccountException("account do not exist");
		}
		return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
	}

}
