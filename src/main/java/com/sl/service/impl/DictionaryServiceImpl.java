package com.sl.service.impl;

import com.sl.dao.DictionaryDao;
import com.sl.entity.DictionaryEntity;
import com.sl.service.DictionaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Resource
    DictionaryDao dictionaryDao;


    @Override
    public List<DictionaryEntity> getAllDict() {
        return dictionaryDao.getAllDict();
    }


}
