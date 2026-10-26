package br.com.fiap.soulup.exception;

/** Dados inválidos ou regra de negócio violada - responde 400. */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
