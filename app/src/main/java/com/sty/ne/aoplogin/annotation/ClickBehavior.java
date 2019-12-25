package com.sty.ne.aoplogin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tian on 2019/12/25.
 */

//用户点击痕迹（行为统计）
@Target(ElementType.METHOD) //目标作用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickBehavior {
    String value();
}
