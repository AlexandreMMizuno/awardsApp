package com.api.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class ListaFilmesDTO {

    private List<FilmesDTO> min;
    private List<FilmesDTO> max;

}
