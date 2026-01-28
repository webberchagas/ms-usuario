package br.com.webberchagas.ms_usuario.infrastructure.exception;

public class ConflitoException extends RuntimeException {
    public ConflitoException(String message) {
        super(message);
    }

    public ConflitoException(String message, Throwable cause) {
        super(message, cause);
    }
}
