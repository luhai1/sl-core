package com.sl.service;

import com.sl.entity.LoginUser;

import java.util.List;

public interface UserService {
     List<LoginUser> selectAll();
     LoginUser getUserByUserName(String userName);
     LoginUser getRoleResourcesByUserName(String userName);
     Integer addUser(LoginUser user);
     Integer updateUser(LoginUser user);
     Integer deleteUserByUserName(String userName);

}
