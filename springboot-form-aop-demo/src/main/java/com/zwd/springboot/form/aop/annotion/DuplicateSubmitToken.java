package com.zwd.springboot.form.aop.annotion;

/**
 * @author zwd
 * @date 2018/12/17 17:20
 * @Email stephen.zwd@gmail.com
 */

import java.lang.annotation.*;

/**
 * @description 防止表单重复提交注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DuplicateSubmitToken {

    //保存重复提交标记 默认为需要保存
    boolean save() default true;
}