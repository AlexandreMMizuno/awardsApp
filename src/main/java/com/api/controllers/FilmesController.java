package com.api.controllers;

import com.api.dtos.MensagemDTO;
import com.api.exceptions.EventoException;
import com.api.services.FilmesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class FilmesController {

    private FilmesService filmesService;

    @PostMapping("/arquivo")
    public ResponseEntity<MensagemDTO> arquivo(@RequestParam("file") MultipartFile file) throws EventoException {

        MensagemDTO retorno = filmesService.carregarDadosCSV(file);
        return retorno.getCodigo() == HttpStatus.NO_CONTENT.value() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(retorno) :
                ResponseEntity.status(HttpStatus.CREATED).body(retorno);
    }
}
