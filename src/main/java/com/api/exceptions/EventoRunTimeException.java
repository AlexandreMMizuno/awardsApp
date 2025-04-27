package com.api.exceptions;

public class EventoRunTimeException extends RuntimeException {
    public EventoRunTimeException() {
        super("Arquivo CSV não recebido.");
    }

    public EventoRunTimeException(String mensagem) {
        super(mensagem);
    }
}
