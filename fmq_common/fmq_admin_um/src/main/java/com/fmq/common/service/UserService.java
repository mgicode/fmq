package com.fmq.common.service;

import com.fmq.common.dto.UserDTO;

/**
 * 业务逻辑接口类
 *
 */
public interface UserService {

    /**
     */
    UserDTO findUerByName(String userName);
}