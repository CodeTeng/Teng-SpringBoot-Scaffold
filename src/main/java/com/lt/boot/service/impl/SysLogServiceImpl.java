package com.lt.boot.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.boot.common.page.PageVO;
import com.lt.boot.mapper.SysLogMapper;
import com.lt.boot.model.dto.log.SysLogQuery;
import com.lt.boot.model.entity.SysLog;
import com.lt.boot.service.SysLogService;
import com.lt.boot.utils.CollUtils;
import com.lt.boot.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author teng
 * @description 针对表【sys_log(系统日志表)】的数据库操作Service实现
 * @createDate 2024-02-18 20:45:00
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog>
        implements SysLogService {
    @Override
    public PageVO<SysLog> listSysLogByPage(SysLogQuery sysLogQuery) {
        Long id = sysLogQuery.getId();
        Long userId = sysLogQuery.getUserId();
        String username = sysLogQuery.getUsername();
        String value = sysLogQuery.getValue();
        String operation = sysLogQuery.getOperation();
        String url = sysLogQuery.getUrl();
        String methodName = sysLogQuery.getMethodName();
        String ip = sysLogQuery.getIp();
        Boolean isAsc = sysLogQuery.getIsAsc();
        String sortBy = sysLogQuery.getSortBy();
        Page<SysLog> page = lambdaQuery()
                .eq(id != null, SysLog::getId, id)
                .eq(userId != null, SysLog::getUserId, userId)
                .eq(StringUtils.isNotBlank(operation), SysLog::getOperation, operation)
                .like(StringUtils.isNotBlank(username), SysLog::getUsername, username)
                .like(StringUtils.isNotBlank(value), SysLog::getValue, value)
                .like(StringUtils.isNotBlank(url), SysLog::getUrl, url)
                .like(StringUtils.isNotBlank(methodName), SysLog::getMethodName, methodName)
                .like(StringUtils.isNotBlank(ip), SysLog::getIp, ip)
                .page(sysLogQuery.toMpPageDefaultSortByCreateTimeDesc());
        if (SqlUtils.validSortField(sortBy)) {
            page.addOrder(new OrderItem().setColumn(sortBy).setAsc(isAsc));
        }
        List<SysLog> sysLogList = page.getRecords();
        if (CollUtils.isEmpty(sysLogList)) {
            return PageVO.empty(page);
        }
        return PageVO.of(page, sysLogList);
    }
}




