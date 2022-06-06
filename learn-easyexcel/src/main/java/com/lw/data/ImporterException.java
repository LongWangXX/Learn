package com.lw.data;

/**
 * 导出异常
 */
public class ImporterException extends RuntimeException {
    public ImporterException(String message) {
        super(message);
    }

    public ImporterException(Throwable cause) {
        super(cause);
    }
}
