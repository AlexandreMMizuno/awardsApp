package com.api.repositories;

import com.api.models.FilmesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FilmesRepository extends JpaRepository<FilmesModel, UUID> {

    String query = "SELECT FUNCTION(GROUP_CONCAT, mt2.produtor), "
            + "FUNCTION(GROUP_CONCAT,mt2.ano) "
            + "FROM FilmesModel mt1 JOIN FilmesModel mt2 ON mt1.produtor LIKE CONCAT('%', mt2.produtor, '%') "
            + "WHERE mt2.produtor IS NOT NULL AND mt2.produtor <> '' AND mt2.ganhador = 'yes' "
            + "GROUP BY mt1.id, mt1.produtor, mt1.ano "
            + "HAVING COUNT(*) > 1";
    @Query(query)
    List<String[]> listarGanhadoresCanditatos();
}

