package com.sl.service.impl;

import com.sl.dao.db2.DictionaryDao;
import com.sl.service.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Resource
    DictionaryDao dictionaryDao;

    @Override
    public String getNameByCode(String dictCode) {
        return dictionaryDao.getNameByCode(dictCode);
    }
}
