package com.huifenqi.study.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)//注解使用的目标是类级别
@Retention(RetentionPolicy.SOURCE)//注解保留范围为源代码
//@Retention(RetentionPolicy.CLASS)//注解保留范围为字节码
//@Retention(RetentionPolicy.RUNTIME)//注解保留范围为运行时
public @interface GenerateSQLMapper {
    /**
     * 表前缀
     *
     * @return
     */
    String prefix() default "t_";

    /**
     * 缓存设置
     *
     * @return
     */
    String cache() default "";


    /**
     * 生成的mapper的位置
     *
     * @return
     */
    String mapperLocation() default "";

}
