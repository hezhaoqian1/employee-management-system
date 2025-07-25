package com.company.ems.exception;

/**
 * 业务异常类
 */
public class BusinessException extends RuntimeException {
    
    private String code;
    
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
    }
    
    public String getCode() {
        return code;
    }
}