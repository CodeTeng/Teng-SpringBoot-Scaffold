package com.lt.boot.constant;

import cn.hutool.core.util.RandomUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 用户常量
 */
public interface UserConstant {
    String SALT = "muziteng";

    List<String> USER_ROLE_LIST = Arrays.asList("user", "admin", "ban");

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    /**
     * 被封号
     */
    String BAN_ROLE = "ban";

    String DEFAULT_USER_NAME = "用户" + RandomUtil.randomNumbers(4);

    String DEFAULT_USER_AVATAR = "https://img.zcool.cn/community/01cfd95d145660a8012051cdb52093.png@2o.png";

    String DEFAULT_USER_PASSWORD = "123456";
}
