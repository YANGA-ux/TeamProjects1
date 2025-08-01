package com.txm.teamprojects.service;

import java.util.HashMap;

import com.txm.teamprojects.pojo.Drug;

public interface DrugService {
    /**
     * 分页模糊查询所有药物信息
     */
    HashMap<String, Object> findAllDrugs(int pageNumber, int size, String query);
    /**
     * 根据id查找药物
     */
    Drug findDrug(int drId);
    /**
     * 根据id删除药物数量
     */
    Boolean reduceDrugNumber(int drId,int usedNumber);
    /**
     * 增加药物信息
     */
    Boolean addDrug(Drug drug);
    /**
     * 删除药物信息
     */
    Boolean deleteDrug(int drId);
    /**
     * 修改药物信息
     */
    Boolean modifyDrug(Drug drug);
}
