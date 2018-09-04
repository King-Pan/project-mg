package com.asiainfo.projectmg.excel;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/4
 * Time: 下午3:23
 * Description: No Description
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    public String value() default "";
}
