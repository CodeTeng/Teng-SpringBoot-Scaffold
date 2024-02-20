package com.lt.boot.exception;

import com.lt.boot.common.BaseResponse;
import com.lt.boot.common.ErrorCode;
import com.lt.boot.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 处理 json 请求体调用接口校验失败抛出的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> MethodArgumentNotValidException, {}", msg);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, msg);
    }

    // 处理 form data 方式调用接口校验失败抛出的异常
    @ExceptionHandler(BindException.class)
    public BaseResponse<?> handleBindException(BindException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数绑定异常 ->BindException, {}", msg);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, msg);
    }

    @ExceptionHandler(NestedServletException.class)
    public BaseResponse<?> handleNestedServletException(NestedServletException e) {
        log.error("参数异常 -> NestedServletException, {}", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR);
    }

    // 处理单个参数校验失败抛出的异常
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<?> handleViolationException(ConstraintViolationException e) {
        log.error("请求参数异常 -> ConstraintViolationException, {}", e.getMessage());
        return ResultUtils.error(ErrorCode.PARAMS_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("自定义异常 -> BusinessException, {}", e.getMessage());
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    public BaseResponse<?> fileUploadExceptionHandler(MultipartException e) {
        log.error("文件上传异常 -> MultipartException, {}", e.getMessage());
        return ResultUtils.error(ErrorCode.UPLOAD_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> runtimeExceptionHandler(Exception e) {
        log.error("其它异常 -> Exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "服务器内部异常");
    }
}
