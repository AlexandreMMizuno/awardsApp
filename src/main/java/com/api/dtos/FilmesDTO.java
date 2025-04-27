package com.api.dtos;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class FilmesDTO {

    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;

}
