package com.zwd.springboot.form.aop.annotion;

/**
 * @author zwd
 * @date 2018/12/17 17:21
 * @Email stephen.zwd@gmail.com
 */
/**
 * 重复提交异常
 */
public class DuplicateSubmitException extends RuntimeException {
    public DuplicateSubmitException(String msg) {
        super(msg);
    }

    public DuplicateSubmitException(String msg, Throwable cause){
        super(msg,cause);
    }
}