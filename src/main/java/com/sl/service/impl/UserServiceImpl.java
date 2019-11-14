package com.sl.service.impl;

import com.sl.dao.UserDao;
import com.sl.entity.LoginUser;
import com.sl.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;
    @Override
    public LoginUser getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public LoginUser getRoleResourcesByUserName(String userName) {
        return userDao.getRoleResourcesByUserName(userName);
    }
}
