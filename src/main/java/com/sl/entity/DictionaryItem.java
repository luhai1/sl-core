package com.sl.entity;

import lombok.Data;

import java.util.List;

@Data
public class DictionaryItem extends BaseEntity {
    /**
     * 字典值编码
     */
    private String  itemCode;
    /**
     * 字典值名称
     */
    private String  itemName;
    /**
     * 字典值父级编码
     */
    private String  parentItemCode;
    /**
     * 描述
     */
    private String  description;
    /**
     * 排序
     */
    private String  sortBy;
    /**
     * 层级
     */
    private String  lever;
    /**
     * 字级典值集合
     */
    private List<DictionaryItem> dictionaryItemList;
}
