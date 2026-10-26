package br.com.fiap.soulup.exception;

/** Registro inexistente - responde 404. */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
