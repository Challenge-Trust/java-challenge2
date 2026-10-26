package br.com.fiap.soulup.exception;

/** Registro duplicado (ex.: CPF já cadastrado) - responde 409. */
public class ConflitoException extends RuntimeException {

    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
