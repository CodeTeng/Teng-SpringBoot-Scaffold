package com.lt.boot.config.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lt.boot.model.vo.user.UserVO;
import com.lt.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/17 22:49
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Resource
    private UserService userService;

    @Override
    public void insertFill(MetaObject metaObject) {
        UserVO currentUser = userService.getCurrentUser();
        this.strictInsertFill(metaObject, "creater", () -> currentUser == null || currentUser.getId() == null ? 0L : currentUser.getId(), Long.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserVO currentUser = userService.getCurrentUser();
        this.strictInsertFill(metaObject, "updater", () -> currentUser == null || currentUser.getId() == null ? 0L : currentUser.getId(), Long.class);
    }
}
