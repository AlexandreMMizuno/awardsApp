package com.api.exceptions;

import com.api.dtos.MensagemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EventoException.class)
    private ResponseEntity<MensagemDTO> eventoException (EventoException ex) {
        MensagemDTO mensagem = new MensagemDTO(HttpStatus.NOT_FOUND.value(),"Erro ao carregar arquivo CSV.");
        return ResponseEntity. status(HttpStatus.NOT_FOUND).body(mensagem);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<MensagemDTO> runtimeException (RuntimeException ex) {
        MensagemDTO mensagem = new MensagemDTO(HttpStatus.NOT_FOUND.value(),"Arquivo CSV não encontrado.");
        return ResponseEntity. status(HttpStatus.NOT_FOUND).body(mensagem);
    }
}
