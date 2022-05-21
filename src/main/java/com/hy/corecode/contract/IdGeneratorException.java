package com.hy.corecode.contract;

/**
 * @author: 王富贵
 * @description: 自定义异常
 * @createTime: 2022年05月19日 11:19:59
 */
public class IdGeneratorException extends RuntimeException {

    public IdGeneratorException() {
        super();
    }

    public IdGeneratorException(String message) {
        super(message);
    }

    public IdGeneratorException(Throwable cause) {
        super(cause);
    }

    public IdGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdGeneratorException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

}
