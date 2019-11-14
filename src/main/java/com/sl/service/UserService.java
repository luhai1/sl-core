package com.sl.service;

import com.sl.entity.LoginUser;

public interface UserService {
     LoginUser getUserByUserName(String userName);
     LoginUser getRoleResourcesByUserName(String userName);
}
