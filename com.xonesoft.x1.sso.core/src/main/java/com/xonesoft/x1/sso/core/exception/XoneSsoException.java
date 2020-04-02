package com.xonesoft.x1.sso.core.exception;

public class XoneSsoException extends RuntimeException {

	private static final long serialVersionUID = 7452338259244322175L;

	public XoneSsoException(String msg) {
        super(msg);
    }

    public XoneSsoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public XoneSsoException(Throwable cause) {
        super(cause);
    }
}
