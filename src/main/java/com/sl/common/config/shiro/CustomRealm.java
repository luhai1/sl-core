package com.sl.common.config.shiro;

import com.sl.common.util.JedisUtil;
import com.sl.common.util.JsonUtil;
import com.sl.common.util.SerializeUtil;
import com.sl.common.util.TokenTools;
import com.sl.entity.LoginUser;
import com.sl.entity.SysResources;
import com.sl.entity.SysRole;
import com.sl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@Configurable
public class CustomRealm extends AuthorizingRealm {
    @Value("${shiro.redis.expire}")
    private Integer expire;
    private static String REQUEST_TIMESTAMP_PARAM="timeStamp";
    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principalCollection.getPrimaryPrincipal();

        LoginUser user = userService.getRoleResourcesByUserName(username);

        for (SysRole role : user.getRoleList()) {
            authorizationInfo.addRole(role.getRoleCode());
            for (SysResources sysResources : role.getResourcesList()) {
                authorizationInfo.addStringPermission(sysResources.getResourceCode());
            }
        }
        return authorizationInfo;
    }

    /**
     * 这里可以注入userService,为了方便演示，我就写死了帐号了密码
     * private UserService userService;
     * <p>
     * 获取即将需要认证的信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("-------身份认证方法--------");
        String userName = (String) authenticationToken.getPrincipal();
        String userPwd = new String((char[]) authenticationToken.getCredentials());
        //根据用户名从数据库获取密码
        LoginUser loginUser = userService.getRoleResourcesByUserName(userName);
        if( null == loginUser){
            throw new AccountException("用户名不正确");
        } else if (!userPwd.equals(loginUser.getPassword())) {
            throw new AccountException("密码不正确");
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = TokenTools.makeToken(Long.valueOf(request.getParameter(REQUEST_TIMESTAMP_PARAM)) ,userName);
        System.out.println(token);
     //  JedisUtil.set(token, JsonUtil.toJson(loginUser), expire);
        return new SimpleAuthenticationInfo(userName, loginUser.getPassword(),getName());
    }
}
