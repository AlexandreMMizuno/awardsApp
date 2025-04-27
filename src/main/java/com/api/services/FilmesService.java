package com.api.services;

import com.api.dtos.MensagemDTO;
import com.api.exceptions.EventoException;
import com.api.models.FilmesModel;
import com.api.repositories.FilmesRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@AllArgsConstructor
@Service
public class FilmesService {

    private FilmesRepository filmesRepository;

    public void iniciarDadosCSV(InputStream file) throws EventoException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {
            Iterable<CSVRecord> csvRecords = CSVFormat.Builder.create().setDelimiter(';').build().withFirstRecordAsHeader().parse(reader);
            this.salvarDadosBanco(csvRecords);
        } catch (IOException ex) {
            throw new EventoException();
        }
    }

    public MensagemDTO carregarDadosCSV(MultipartFile file) throws EventoException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> csvRecords = CSVFormat.Builder.create().setDelimiter(';').build().withFirstRecordAsHeader().parse(reader);
            filmesRepository.deleteAll();
            return this.salvarDadosBanco(csvRecords);
        } catch (IOException ex) {
            throw new EventoException();
        }
    }

    private MensagemDTO salvarDadosBanco(Iterable<CSVRecord> csvRecords) {

        FilmesModel data = null;
        MensagemDTO mensagem;
        for (CSVRecord csvRecord : csvRecords) {
            data = new FilmesModel();

            data.setAno(csvRecord.get(0) != null ? csvRecord.get(0) : "");
            data.setTitulo(csvRecord.get(1) != null ? csvRecord.get(1) : "");
            data.setEstudio(csvRecord.get(2) != null ? csvRecord.get(2) : "");
            data.setProdutor(csvRecord.get(3) != null ? csvRecord.get(3) : "");
            data.setGanhador(csvRecord.get(4) != null ? csvRecord.get(4) : "");

            filmesRepository.save(data);
        }
        if (data == null) {
            mensagem = new MensagemDTO(HttpStatus.NO_CONTENT.value(),"Arquivo CSV não possui dados.");
            return mensagem;
        } else {
            mensagem = new MensagemDTO(HttpStatus.CREATED.value(),"Arquivo CSV importado com sucesso.");
            return mensagem;
        }
    }
}
