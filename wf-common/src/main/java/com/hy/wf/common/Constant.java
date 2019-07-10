package com.hy.wf.common;

import java.util.Date;

/**
 * @program: hy-wf
 * @description: 常量类
 * @author: jt
 * @create: 2019-01-08 15:25
 **/
public class Constant {
//    public static final int FREE_TIME = 5;
//    public static final int FREE_COUNT = 3;

    public static final String ANDROID = "android";
    public static final String IOS = "ios";
    /** 分隔符 */
    public static final String SEPARATOR = ",";

    public static final String[] DATE_PATTERNS = new String[] {"yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss"};

    /**初始化收款日期*/
    public static final Date date = new Date(1L);

    public static final String REFRESH_TOKEN = "refresh";
    public static final String ACCESS_TOKEN = "access";

    public static final String  UPLOAD_PATH= "/upload/";

    public static final int BALANCE= 100;

    public static final int LIMIT= 50;

}
