package com.txm.teamprojects.service;

import com.txm.teamprojects.pojo.Admin;

public interface AdminService {
    /**
     * 登录数据校验
     * */
    Admin login(int aId, String aPassword);
}
