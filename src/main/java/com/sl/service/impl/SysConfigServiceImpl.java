package com.sl.service.impl;

import com.sl.dao.SysConfigDao;
import com.sl.entity.SysConfig;
import com.sl.service.SysConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Resource
    SysConfigDao sysConfigDao;
    @Override
    public List<SysConfig> getAllSysConfig() {
        return sysConfigDao.getAllSysConfig();
    }
}
