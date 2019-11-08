package com.sl.service.impl;

import com.sl.dao.UserDao;
import com.sl.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;
    @Override
    public String getEmailByUserName(String userName) {
        return userDao.getEmailByUserName(userName);
    }
}
