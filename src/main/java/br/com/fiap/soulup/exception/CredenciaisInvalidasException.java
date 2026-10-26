package br.com.fiap.soulup.exception;

/** CPF ou senha incorretos no login - responde 401. */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException() {
        super("CPF ou senha inválidos.");
    }
}
