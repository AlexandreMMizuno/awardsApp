package com.api.controllers;

import com.api.dtos.ListaFilmesDTO;
import com.api.dtos.MensagemDTO;
import com.api.exceptions.EventoException;
import com.api.services.FilmesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FilmesController {

    @Autowired
    private FilmesService filmesService;

    @PostMapping("/arquivo")
    public ResponseEntity<MensagemDTO> arquivo(@RequestParam("arquivo") MultipartFile arquivo) throws EventoException {

        MensagemDTO retorno = filmesService.carregarDadosCSV(arquivo);
        return retorno.getCodigo() == HttpStatus.NO_CONTENT.value() ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(retorno) :
                ResponseEntity.status(HttpStatus.CREATED).body(retorno);
    }

    @GetMapping("/intervalo")
    public ResponseEntity<ListaFilmesDTO> intervalo() {

        return ResponseEntity.status(HttpStatus.OK).body(this.filmesService.procurarCandidatoMaxMin());
    }
}
