package com.api.repositories;

import com.api.models.FilmesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilmesRepository extends JpaRepository<FilmesModel, UUID> {

}

