package com.hiynn.fl.jingwuyun.util;

import java.lang.annotation.*;

/**
 * <p>Title: SystemControllerLog </p>
 * <p>Description: TODO </p>
 * Date: 16/7/26 上午10:08.
 *
 * @author zhangdepeng@hiynn.com
 * @version 1.0 </p>
 *          Significant Modify：
 *          Date               Author           Content
 *          ==========================================================
 *          16/7/26         zhangdepeng         创建文件,实现基本功能
 *          <p/>
 *          ==========================================================
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {
    String description()  default "";
}
