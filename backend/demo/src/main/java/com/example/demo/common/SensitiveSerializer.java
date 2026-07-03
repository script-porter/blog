package com.example.demo.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * Jackson 脱敏序列化器 —— 根据 @Sensitive 注解对字段值进行脱敏
 */
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private SensitiveType type;
    private char maskChar;

    // 无参构造器（Jackson 反序列化时需要）
    public SensitiveSerializer() {
    }

    public SensitiveSerializer(SensitiveType type, char maskChar) {
        this.type = type;
        this.maskChar = maskChar;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(mask(value));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {
        if (property == null) {
            return this;
        }
        Sensitive ann = property.getAnnotation(Sensitive.class);
        if (ann == null) {
            return this;
        }
        return new SensitiveSerializer(ann.value(), ann.maskChar());
    }

    /**
     * 根据脱敏类型执行脱敏
     */
    private String mask(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        switch (type) {
            case PHONE:
                return maskPhone(value);
            case EMAIL:
                return maskEmail(value);
            case ID_CARD:
                return maskIdCard(value);
            case BANK_CARD:
                return maskBankCard(value);
            case NAME:
                return maskName(value);
            case ADDRESS:
                return maskAddress(value);
            case PASSWORD:
                return maskPassword(value);
            default:
                return value;
        }
    }

    private String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /** 手机号：185****1554 */
    private String maskPhone(String phone) {
        if (phone.length() < 7) return phone;
        return phone.substring(0, 3) + repeat(maskChar, 4) + phone.substring(7);
    }

    /** 邮箱：x***@example.com */
    private String maskEmail(String email) {
        int atIdx = email.indexOf('@');
        if (atIdx <= 1) return email;
        return email.charAt(0) + repeat(maskChar, 3) + email.substring(atIdx);
    }

    /** 身份证号：110***********1234 */
    private String maskIdCard(String idCard) {
        if (idCard.length() < 6) return idCard;
        int maskLen = idCard.length() - 6;
        return idCard.substring(0, 3) + repeat(maskChar, maskLen) + idCard.substring(idCard.length() - 4);
    }

    /** 银行卡号：6222*******1234 */
    private String maskBankCard(String card) {
        if (card.length() < 8) return card;
        return card.substring(0, 4) + repeat(maskChar, card.length() - 8) + card.substring(card.length() - 4);
    }

    /** 姓名：张* */
    private String maskName(String name) {
        if (name.length() <= 1) return name;
        return name.charAt(0) + repeat(maskChar, name.length() - 1);
    }

    /** 地址：北京市*** */
    private String maskAddress(String address) {
        int len = address.length();
        if (len <= 4) return address;
        return address.substring(0, len / 3) + repeat(maskChar, len - len / 3);
    }

    /** 密码：****** */
    private String maskPassword(String pwd) {
        return repeat(maskChar, Math.min(pwd.length(), 6));
    }
}
