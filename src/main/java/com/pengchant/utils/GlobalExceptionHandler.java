package com.pengchant.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.JSONReaderScanner;
import com.pengchant.model.ArgumentInvalidResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


/**
 * Controller增强器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(Exception ex) {
        Object errorObject = JSONResult.errorMsg("请求异常，请稍后重试!");
        // 处理绑定异常
        if (ex instanceof MethodArgumentNotValidException) {
            List<ArgumentInvalidResult> invalidResultList = new ArrayList<>();
            for (FieldError error : ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getFieldErrors()) {
                ArgumentInvalidResult invalidResult = new ArgumentInvalidResult();
                invalidResult.setDefaultMessage(error.getDefaultMessage());
                invalidResult.setField(error.getField());
                invalidResult.setRejectedValue(error.getRejectedValue());
                invalidResultList.add(invalidResult);
            }
            errorObject = JSONResult.errorMsgData("字段验证不通过", invalidResultList);
        }
        return errorObject;
    }
}
