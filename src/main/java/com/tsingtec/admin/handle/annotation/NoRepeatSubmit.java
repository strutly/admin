package com.tsingtec.admin.handle.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface NoRepeatSubmit {
    /**
     * 默认时间  默认5秒钟
     * @return
     */
    int lockTime() default 5000;
}