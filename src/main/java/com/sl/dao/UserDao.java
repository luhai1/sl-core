package com.sl.dao;

import com.sl.entity.LoginUser;

public interface UserDao {
    LoginUser getUserByUserName(String userName);
    LoginUser getRoleResourcesByUserName(String userName);
}
