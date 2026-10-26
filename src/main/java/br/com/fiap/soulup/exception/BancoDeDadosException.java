package br.com.fiap.soulup.exception;

/** Falha de acesso ao banco (envolve a SQLException original) - responde 500. */
public class BancoDeDadosException extends RuntimeException {

    public BancoDeDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
