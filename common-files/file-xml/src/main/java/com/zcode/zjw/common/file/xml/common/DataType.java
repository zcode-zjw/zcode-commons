package com.zcode.zjw.common.file.xml.common;


import cn.hutool.core.date.DateUtil;
import com.sun.org.apache.xpath.internal.operations.Number;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 描述
 *
 * @author zhangjiwei
 * @date 2023/6/6
 */
public class DataType {

    private static final Logger log = LoggerFactory.getLogger(DataType.class);

    public static final String DATATYPE_STRING = "java.lang.String";
    public static final String DATATYPE_SHORT = "java.lang.Short";
    public static final String DATATYPE_INTEGER = "java.lang.Integer";
    public static final String DATATYPE_LONG = "java.lang.Long";
    public static final String DATATYPE_DOUBLE = "java.lang.Double";
    public static final String DATATYPE_FLOAT = "java.lang.Float";
    public static final String DATATYPE_BYTE = "java.lang.Byte";
    public static final String DATATYPE_CHAR = "java.lang.Character";
    public static final String DATATYPE_BOOLEAN = "java.lang.Boolean";
    public static final String DATATYPE_DATE = "java.util.Date";
    public static final String DATATYPE_TIME = "java.sql.Time";
    public static final String DATATYPE_TIMESTAMP = "java.sql.Timestamp";
    public static final String DATATYPE_OBJECT = "java.lang.Object";
    /** 8λ MIN_VALUE??-128 MAX_VALUE??127 */
    public static final String DATATYPE_byte = "byte";
    /** 16λ MIN_VALUE??0 MAX_VALUE??65535 */
    public static final String DATATYPE_char = "char";
    /** 16λ MIN_VALUE??-32768 MAX_VALUE??32767 */
    public static final String DATATYPE_short = "short";
    /** 32λ MIN_VALUE??-2147483648 MAX_VALUE??2147483647 */
    public static final String DATATYPE_int = "int";
    /** 32λ MIN_VALUE??1.4E-45 MAX_VALUE??3.4028235E38 */
    public static final String DATATYPE_float = "float";
    /** 64λ MIN_VALUE??-9223372036854775808 MAX_VALUE??9223372036854775807 */
    public static final String DATATYPE_long = "long";
    /** 64λ MIN_VALUE??4.9E-324 MAX_VALUE??1.7976931348623157E308 */
    public static final String DATATYPE_double = "double";
    public static final String DATATYPE_boolean = "boolean";
    public static final String NULLSTR = "";


    public static <T> T transfer(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        if (DATATYPE_STRING.equals(type.getName())) {
            if (DATATYPE_TIMESTAMP.equals(value.getClass().getName())) {
                return (T) String.valueOf(((Timestamp) value).getTime());
            }
            if (value instanceof Date) {
                return (T) DateUtil.formatDate((Date) value);
            }
            return (T) value.toString();
        }
        try {
            if (DATATYPE_SHORT.equals(type.getName()) || DATATYPE_short.equals(type.getName())) {
                return (T) Short.valueOf(value.toString());
            }
            if (DATATYPE_INTEGER.equals(type.getName()) || DATATYPE_int.equals(type.getName())) {
                return (T) Integer.valueOf(value.toString());
            }
            if (DATATYPE_LONG.equals(type.getName()) || DATATYPE_long.equals(type.getName())) {
                if (value instanceof Date) {
                    return (T) Long.valueOf(((Date) value).getTime());
                }
                return (T) Long.valueOf(value.toString());
            }
            if (DATATYPE_DOUBLE.equals(type.getName()) || DATATYPE_double.equals(type.getName())) {
                if (value instanceof Date) {
                    return (T) Double.valueOf(((Date) value).getTime());
                }
                return (T) Double.valueOf(value.toString());
            }
            if (DATATYPE_FLOAT.equals(type.getName()) || DATATYPE_float.equals(type.getName())) {
                if (value instanceof Date) {
                    return (T) Float.valueOf(((Date) value).getTime());
                }
                return (T) Float.valueOf(value.toString());
            }
            if (DATATYPE_BYTE.equals(type.getName()) || DATATYPE_byte.equals(type.getName())) {
                return (T) Byte.valueOf(value.toString());
            }
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {}", e.getMessage());
        }
        if (DATATYPE_CHAR.equals(type.getName()) || DATATYPE_char.equals(type.getName())) {
            if (value.toString().length() == 1) {
                return (T) Character.valueOf(value.toString().charAt(0));
            }
        }
        if (DATATYPE_BOOLEAN.equals(type.getName()) || DATATYPE_boolean.equals(type.getName())) {
            if (value instanceof Boolean) {
                return (T) value;
            }
            if (Boolean.TRUE.toString().equalsIgnoreCase(value.toString())) {
                return (T) Boolean.TRUE;
            }
            if (Boolean.FALSE.toString().equalsIgnoreCase(value.toString())) {
                return (T) Boolean.FALSE;
            }
        }
        if (DATATYPE_DATE.equals(type.getName())) {
            return (T) getDate(value);
        }
        if (DATATYPE_TIME.equals(type.getName())) {
            Date date = getDate(value);
            if (date != null) {
                return (T) new Time(date.getTime());
            }
        }
        if (DATATYPE_TIMESTAMP.equals(type.getName())) {
            Date date = getDate(value);
            if (date != null) {
                return (T) new Timestamp(date.getTime());
            }
        }
        return null;
    }

    private static Date getDate(Object value) {
        Date date = null;
        if (value instanceof Date) {
            date = (Date) value;
        } else if (DATATYPE_LONG.equals(value.getClass().getTypeName())) {
            date = (Date) new Date((Long) value);
        } else if (DATATYPE_STRING.equals(value.getClass().getTypeName())) {
            date = (Date) DateUtil.parseDate(value.toString());
            if (date == null) {
                date = (Date) new Date(Long.parseLong(value.toString()));
            }
        }
        return date;
    }

    public static String getAsString(Object obj) {
        return transfer(obj, String.class);
    }

    public static String getAsStringNotNull(Object obj) {
        if (null == obj) {
            return NULLSTR;
        }
        return transfer(obj, String.class);
    }

    public static short getAsShort(Object obj) {
        Short s = transfer(obj, Short.class);
        return s == null ? 0 : s;
    }


    public static int getAsInt(Object obj) {
        Integer i = transfer(obj, Integer.class);
        return i == null ? 0 : i;
    }

    public static long getAsLong(Object obj) {
        Long l = transfer(obj, Long.class);
        return l == null ? 0 : l;
    }

    public static double getAsDouble(Object obj) {
        Double d = transfer(obj, Double.class);
        return d == null ? 0 : d;
    }

    public static float getAsFloat(Object obj) {
        Float f = transfer(obj, Float.class);
        return f == null ? 0 : f;
    }

    public static byte getAsByte(Object obj) {
        Byte b = transfer(obj, Byte.class);
        return b == null ? 0 : b;
    }

    public static char getAsChar(Object obj) {
        Character c = transfer(obj, Character.class);
        return c == null ? '\u0000' : c;
    }

    public static boolean getAsBoolean(Object obj) {
        Boolean b = transfer(obj, Boolean.class);
        return b == null ? Boolean.FALSE : b;
    }

    public static Date getAsDate(Object obj) {
        return transfer(obj, Date.class);
    }

    public static Time getAsTime(Object obj) {
        return transfer(obj, Time.class);
    }

    public static Timestamp getAsTimestamp(Object obj) {
        return transfer(obj, Timestamp.class);
    }

    public static boolean isStringType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_STRING);
    }

    public static boolean isShortType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_SHORT);
    }

    public static boolean isIntType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_INTEGER);
    }

    public static boolean isLongType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_LONG);
    }

    public static boolean isFloatType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_FLOAT);
    }

    public static boolean isByteType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_BYTE);
    }

    public static boolean isBooleanType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_BOOLEAN);
    }

    public static boolean isDateType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_DATE);
    }

    public static boolean isTimeType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_TIME);
    }

    public static boolean isTimeStampType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_TIMESTAMP);
    }

    public static boolean isObjectType(Object obj) {
        return obj != null && obj.getClass().getName().equals(DATATYPE_OBJECT);
    }

    public static boolean isNumberType(Object obj) {
        try {
            Number number = (Number) obj;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDate(Object obj) {
        return obj != null && obj instanceof Date;
    }

    public static boolean isDate(String typeName) {
        return DATATYPE_DATE.equals(typeName) || DATATYPE_TIME.equals(typeName)
                || DATATYPE_TIMESTAMP.equals(typeName);
    }

    /**
     * 返回object对象的实际类型
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T get(Object obj) {
        return (T) obj;
    }

}

