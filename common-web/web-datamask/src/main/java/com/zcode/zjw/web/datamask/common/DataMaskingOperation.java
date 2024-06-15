package com.zcode.zjw.web.datamask.common;

public interface DataMaskingOperation {

    String MASK_CHAR = "*";

    String mask(String content, String maskChar);

}