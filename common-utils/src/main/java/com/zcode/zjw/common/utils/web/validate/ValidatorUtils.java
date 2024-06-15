package com.zcode.zjw.common.utils.web.validate;


import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhangjiwei
 */
public final class ValidatorUtils {
    private ValidatorUtils() {
    }

    private static final String IS_EMPTY = "%s不能为空";
    private static final String LESS_THAN_ZERO = "%s不能小于0";
    private static final String LENGTH_OUT_OF_RANGE = "%s长度要在%d~%d之间";
    private static final String LENGTH_LESS_THAN = "%s长度不能小于%d";
    private static final String LENGTH_GREATER_THAN = "%s长度不能大于%d";
    private static final String ILLEGAL_PARAM = "%s不符合规则";
    // 手机号码正则，可以根据需要自行调整
    public static final String MOBILE = "1\\d{10}";

    /**
     * 是否为true
     *
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ValidatorException(message);
        }
    }

    /**
     * 校验参数是否为null
     *
     * @param param
     * @param fieldName
     */
    public static void checkNull(Object param, String fieldName) {
        if (param == null) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }
    }

    /**
     * 校验参数是否为null或empty
     *
     * @param param
     * @param fieldName
     */
    public static void checkNullOrEmpty(Object param, String fieldName) {
        if (param == null) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }

        if (param instanceof CharSequence) {
            if (param instanceof String && "null".equals(((String) param).toLowerCase())) {
                throw new ValidatorException(String.format(IS_EMPTY, fieldName));
            }
            if (isBlank((CharSequence) param)) {
                throw new ValidatorException(String.format(IS_EMPTY, fieldName));
            }
        }

        if (isCollectionsSupportType(param) && sizeIsEmpty(param)) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }
    }

    /**
     * 校验id是否合法
     *
     * @param id
     * @param fieldName
     */
    public static void checkId(Long id, String fieldName) {
        if (id == null) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }
        if (id < 0) {
            throw new ValidatorException(String.format(LESS_THAN_ZERO, fieldName));
        }
    }

    /**
     * 校验id是否合法
     *
     * @param id
     * @param fieldName
     */
    public static void checkId(Integer id, String fieldName) {
        if (id == null) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }
        if (id < 0) {
            throw new ValidatorException(String.format(LESS_THAN_ZERO, fieldName));
        }
    }

    /**
     * 校验参数字符串
     *
     * @param param 字符串参数
     * @param min   最小长度
     * @param max   最大长度
     */
    public static void checkLength(String param, int min, int max, String fieldName) {
        if (param == null || "".equals(param)) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }

        int length = param.length();
        if (length < min || length > max) {
            throw new ValidatorException(String.format(LENGTH_OUT_OF_RANGE, fieldName, min, max));
        }
    }

    /**
     * 校验参数字符串
     *
     * @param param 字符串参数
     * @param min   最小长度
     */
    public static void checkMinLength(String param, int min, String fieldName) {
        if (param == null || "".equals(param)) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }

        if (param.length() < min) {
            throw new ValidatorException(String.format(LENGTH_LESS_THAN, fieldName, min));
        }
    }

    /**
     * 校验参数字符串
     *
     * @param param 字符串参数
     * @param max   最大长度
     */
    public static void checkMaxLength(String param, int max, String fieldName) {
        if (param == null || "".equals(param)) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }

        if (param.length() > max) {
            throw new ValidatorException(String.format(LENGTH_GREATER_THAN, fieldName, max));
        }
    }

    /**
     * 校验手机号是否合法
     *
     * @param phone 手机号
     */
    public static void checkPhone(String phone, String fieldName) {
        if (phone == null || "".equals(phone)) {
            throw new ValidatorException(String.format(IS_EMPTY, fieldName));
        }
        boolean matches = Pattern.matches(MOBILE, phone);
        if (!matches) {
            throw new ValidatorException(String.format(ILLEGAL_PARAM, fieldName));
        }
    }

    // --------- private method ----------

    private static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    private static boolean sizeIsEmpty(final Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof Collection<?>) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map<?, ?>) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        } else {
            try {
                return Array.getLength(object) == 0;
            } catch (final IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }
    }

    private static boolean isCollectionsSupportType(Object value) {
        boolean isCollectionOrMap = value instanceof Collection || value instanceof Map;
        return isCollectionOrMap || value.getClass().isArray();
    }
}