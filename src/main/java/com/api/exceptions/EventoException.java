package com.api.exceptions;

import java.io.IOException;

public class EventoException extends IOException {
    public EventoException() {
        super("Dados CSV não importados.");
    }

    public EventoException(String mensagem) {
        super(mensagem);
    }
}
