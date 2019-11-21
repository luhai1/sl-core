package com.sl.entity;

import lombok.Data;

import java.util.List;

@Data
public class DictionaryEntity extends BaseEntity{
    /**
     * 字典编码
     */
    private String dictCode;
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 排序
     */
    private String sortBy;

    /**
     * 字典值集合
     */
    private List<DictionaryItem> dictionaryItemList;
}
