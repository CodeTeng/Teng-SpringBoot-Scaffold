package com.lt.boot.controller;

import com.lt.boot.annotation.AuthCheck;
import com.lt.boot.common.BaseResponse;
import com.lt.boot.common.ErrorCode;
import com.lt.boot.common.ResultUtils;
import com.lt.boot.common.page.PageVO;
import com.lt.boot.constant.UserConstant;
import com.lt.boot.exception.ThrowUtils;
import com.lt.boot.model.dto.log.SysLogQuery;
import com.lt.boot.model.entity.SysLog;
import com.lt.boot.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/19 20:18
 */
@Api(tags = "日志模块")
@RestController
@RequestMapping("/logs")
@Slf4j
public class SysLogController {
    @Resource
    private SysLogService sysLogService;

    @PostMapping("/list/page")
    @ApiOperation("分页查询日志(后台管理)默认按照创建时间降序，还可以根据响应时间，用户id排序")
    @ApiImplicitParam(name = "sysLogQuery", value = "后台日志分页查询", required = true, dataType = "SysLogQuery", paramType = "body")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PageVO<SysLog>> listSysLogByPage(@RequestBody @Validated SysLogQuery sysLogQuery) {
        ThrowUtils.throwIf(sysLogQuery == null, ErrorCode.PARAMS_ERROR);
        PageVO<SysLog> page = sysLogService.listSysLogByPage(sysLogQuery);
        return ResultUtils.success(page);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据id获取日志(后台管理)")
    @ApiImplicitParam(name = "id", value = "日志id", required = true, dataType = "Long", paramType = "path")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<SysLog> getSysLogById(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        SysLog sysLog = sysLogService.getById(id);
        ThrowUtils.throwIf(sysLog == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(sysLog);
    }
}
