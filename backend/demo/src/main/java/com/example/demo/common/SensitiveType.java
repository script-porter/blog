package com.example.demo.common;

/**
 * 脱敏类型枚举
 */
public enum SensitiveType {

    /** 手机号：185****1554 */
    PHONE,
    /** 邮箱：x***@example.com */
    EMAIL,
    /** 身份证号：110***********1234 */
    ID_CARD,
    /** 银行卡号：6222*******1234 */
    BANK_CARD,
    /** 姓名：张* */
    NAME,
    /** 地址：北京市*** */
    ADDRESS,
    /** 密码：****** */
    PASSWORD,
}
