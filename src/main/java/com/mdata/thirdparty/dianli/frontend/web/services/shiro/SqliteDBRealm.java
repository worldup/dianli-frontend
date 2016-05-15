package com.mdata.thirdparty.dianli.frontend.web.services.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Created by administrator on 16/5/15.
 */
@Component

public class SqliteDBRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            final AuthenticationToken token)
            throws AuthenticationException {
        final UsernamePasswordToken credentials = (UsernamePasswordToken) token;
        final String loginName = credentials.getUsername();
        if (loginName == null) {
            throw new UnknownAccountException("user not exists");
        }
        final User user = userService.findByLoginName(loginName);
        if (user == null) {
            throw new UnknownAccountException("Account does not exist");
        }
        return new SimpleAuthenticationInfo(loginName, user.getPassword().toCharArray(),
                ByteSource.Util.bytes(loginName), getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            final PrincipalCollection principals) {
        // retrieve role names and permission names
        final String loginName = (String) principals.getPrimaryPrincipal();
        final User user = userService.findByLoginName(loginName);
        if (user == null) {
            throw new UnknownAccountException("Account does not exist");
        }
        final int totalRoles = user.getRoles().size();
        final Set<String> roleNames = new LinkedHashSet<String>(totalRoles);
        final Set<String> permissionNames = new LinkedHashSet<String>();
        if (totalRoles > 0) {
            for (Role role : user.getRoles()) {
                roleNames.add(role.getName());
                for (Permission permission : role.getPermissions()) {
                    permissionNames.add(permission.getName());
                }
            }
        }
        final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        info.setStringPermissions(permissionNames);
        return info;
    }

}