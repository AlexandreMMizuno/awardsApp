package com.api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class FilmesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String ano;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String estudio;
    @Column(nullable = false)
    private String produtor;
    @Column(nullable = false)
    private String ganhador;

}
