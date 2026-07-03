package com.example.demo.common;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * 校验工具类 - 包含自定义校验注解及校验器
 * <p>
 * 用法示例：
 * <pre>{@code
 * public class UserRegisterDTO {
 *     &#64;ValidUtils.Phone
 *     private String phone;
 *
 *     &#64;ValidUtils.Email
 *     private String email;
 * }
 * }</pre>
 */
public final class ValidUtils {

    private ValidUtils() {
    }

    // ============================================================
    //  @Phone - 手机号校验（中国大陆）
    // ============================================================

    /**
     * 中国大陆手机号校验注解
     * <p>支持号段：13x、14x、15x、16x、17x、18x、19x
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PhoneValidator.class)
    @Documented
    public @interface Phone {
        String message() default "手机号格式不正确";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link Phone} 的校验器
     */
    public static class PhoneValidator implements ConstraintValidator<Phone, String> {

        private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            // null 值交给 @NotBlank / @NotNull 处理
            if (value == null || value.isBlank()) {
                return true;
            }
            return PHONE_PATTERN.matcher(value.trim()).matches();
        }
    }

    // ============================================================
    //  @Email - 邮箱格式校验
    // ============================================================

    /**
     * 邮箱格式校验注解
     * <p>支持标准的 Internet 邮箱格式
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = EmailValidator.class)
    @Documented
    public @interface Email {
        String message() default "邮箱格式不正确";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link Email} 的校验器
     */
    public static class EmailValidator implements ConstraintValidator<Email, String> {

        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return true;
            }
            return EMAIL_PATTERN.matcher(value.trim()).matches();
        }
    }

    // ============================================================
    //  @IdCard - 中国大陆身份证号校验（18位）
    // ============================================================

    /**
     * 中国大陆身份证号校验注解（18位，含最后一位校验码验证）
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = IdCardValidator.class)
    @Documented
    public @interface IdCard {
        String message() default "身份证号格式不正确";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link IdCard} 的校验器
     */
    public static class IdCardValidator implements ConstraintValidator<IdCard, String> {

        private static final Pattern ID_CARD_PATTERN = Pattern.compile("^[1-9]\\d{5}(?:18|19|20)\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$");

        // 加权因子
        private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码映射
        private static final char[] CHECK_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return true;
            }
            String id = value.trim().toUpperCase();
            if (!ID_CARD_PATTERN.matcher(id).matches()) {
                return false;
            }
            // 校验最后一位校验码
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += (id.charAt(i) - '0') * WEIGHT[i];
            }
            return id.charAt(17) == CHECK_CODE[sum % 11];
        }
    }

    // ============================================================
    //  @Url - URL 格式校验
    // ============================================================

    /**
     * URL 格式校验注解
     * <p>支持 http / https / ftp 协议
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = UrlValidator.class)
    @Documented
    public @interface Url {
        String message() default "URL格式不正确";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link Url} 的校验器
     */
    public static class UrlValidator implements ConstraintValidator<Url, String> {

        private static final Pattern URL_PATTERN =
                Pattern.compile("^https?://[\\w.-]+(:\\d+)?(/[\\w./%-]*)?(\\?[\\w&=.-]*)?(#\\w*)?$",
                        Pattern.CASE_INSENSITIVE);

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return true;
            }
            return URL_PATTERN.matcher(value.trim()).matches();
        }
    }

    // ============================================================
    //  @Chinese - 只包含中文字符
    // ============================================================

    /**
     * 纯中文校验注解
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = ChineseValidator.class)
    @Documented
    public @interface Chinese {
        String message() default "只能包含中文字符";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link Chinese} 的校验器
     */
    public static class ChineseValidator implements ConstraintValidator<Chinese, String> {

        private static final Pattern CHINESE_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5]+$");

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return true;
            }
            return CHINESE_PATTERN.matcher(value.trim()).matches();
        }
    }

    // ============================================================
    //  @Numeric - 纯数字字符串
    // ============================================================

    /**
     * 纯数字字符串校验注解
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = NumericValidator.class)
    @Documented
    public @interface Numeric {
        String message() default "只能包含数字";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link Numeric} 的校验器
     */
    public static class NumericValidator implements ConstraintValidator<Numeric, String> {

        private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+$");

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null || value.isBlank()) {
                return true;
            }
            return NUMERIC_PATTERN.matcher(value.trim()).matches();
        }
    }

    // ============================================================
    //  @EnumValue - 枚举值校验（限制字段值在指定范围内）
    // ============================================================

    /**
     * 枚举值范围校验注解
     * <p>校验字段值是否在指定的字符串数组中
     * <pre>{@code
     * &#64;ValidUtils.EnumValue({"draft", "published", "archived"})
     * private String status;
     * }</pre>
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = EnumValueValidator.class)
    @Documented
    public @interface EnumValue {
        String[] value();

        boolean caseSensitive() default true;

        String message() default "值不在允许范围内";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * {@link EnumValue} 的校验器
     */
    public static class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {

        private String[] allowedValues;
        private boolean caseSensitive;

        @Override
        public void initialize(EnumValue annotation) {
            this.allowedValues = annotation.value();
            this.caseSensitive = annotation.caseSensitive();
        }

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
            if (value == null || value.toString().isBlank()) {
                return true;
            }
            String input = value.toString().trim();
            for (String allowed : allowedValues) {
                if (caseSensitive ? allowed.equals(input) : allowed.equalsIgnoreCase(input)) {
                    return true;
                }
            }
            return false;
        }
    }
}
