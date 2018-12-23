package com.pengchant.utils;

import com.pengchant.model.ArgumentInvalidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
        logger.error(ex.toString());
        return errorObject;
    }
}
