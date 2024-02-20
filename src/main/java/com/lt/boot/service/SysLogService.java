package com.lt.boot.service;

import com.lt.boot.common.page.PageVO;
import com.lt.boot.model.dto.log.SysLogQuery;
import com.lt.boot.model.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author teng
 * @description 针对表【sys_log(系统日志表)】的数据库操作Service
 * @createDate 2024-02-18 20:45:00
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询日志(后台管理)
     *
     * @param sysLogQuery 后台日志分页查询请求体
     * @return 分页结果
     */
    PageVO<SysLog> listSysLogByPage(SysLogQuery sysLogQuery);
}
