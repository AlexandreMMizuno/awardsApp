package com.api.services;

import com.api.dtos.FilmesDTO;
import com.api.dtos.ListaFilmesDTO;
import com.api.dtos.MensagemDTO;
import com.api.exceptions.EventoException;
import com.api.models.FilmesModel;
import com.api.repositories.FilmesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmesService {

    @Autowired
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

    public MensagemDTO salvarDadosBanco(Iterable<CSVRecord> csvRecords) {
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

    public ListaFilmesDTO procurarCandidatoMaxMin() {
        List<String[]> lista = filmesRepository.listarGanhadoresCanditatos();
        Set<String> data = new HashSet<>();
        for (String[] entry : lista) {
            entry[0] = encontrarProdutor(entry[0]);
            entry[1] = String.valueOf(calcularIntevalo(entry[1]));
            String combined = String.join(",", entry);
            data.add(combined);
        }

        return procurarMaxMin(data);
    }

    private static String encontrarProdutor(String input) {
        String[] produtores = input.replaceAll("[,]", " ").split("\\s+");
        Map<String, Long> produtorQuant = new LinkedHashMap<>();

        for (String palavra : produtores) {
            produtorQuant.put(palavra, produtorQuant.getOrDefault(palavra, 0L) + 1);
        }

        long maxQuant= produtorQuant.values().stream()
                .max(Long::compareTo)
                .orElse(0L);
        String produtorEncontrado = produtorQuant.entrySet().stream()
                .filter(entry -> entry.getValue() == maxQuant)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(" "));

        return produtorEncontrado;
    }

    private static String calcularIntevalo(String input) {
        String intervalo="";
        List<Integer> anoIntervalo= new ArrayList<>();

        int[] anos = Arrays.stream(Arrays.stream(input.split(","))
                .mapToInt(Integer::parseInt)
                .toArray()).sorted().toArray();
        int ano = Integer.MIN_VALUE;

        for (int i = 0; i < anos.length; i++) {
            if(i < anos.length-1) {
                int result = anos[i + 1] - anos[i];
                if (result > ano) {
                    intervalo = String.valueOf(result)+","+String.valueOf(anos[i])+","+String.valueOf(anos[i+1]);
                }
            }
        }
        return intervalo;
    }

    private static ListaFilmesDTO procurarMaxMin(Set<String> input) {
        ListaFilmesDTO listaMinMax = new ListaFilmesDTO();
        FilmesDTO filmeMin = new FilmesDTO();
        FilmesDTO filmeMax = new FilmesDTO();
        List<FilmesDTO> listaMin = new ArrayList<>();
        List<FilmesDTO> listaMax = new ArrayList<>();
        int maxValue = Integer.MIN_VALUE;
        int minValue = Integer.MAX_VALUE;

        for (String entry : input) {
            String[] parts = entry.split(",");
            String name = parts[0].trim();
            int value = Integer.parseInt(parts[1].trim());

            if (value > maxValue) {
                maxValue = value;
                filmeMax.setProducer(name);
                filmeMax.setInterval(value);
                filmeMax.setPreviousWin(Integer.parseInt(parts[2]));
                filmeMax.setFollowingWin(Integer.parseInt(parts[3]));
            }
            if (value < minValue) {
                minValue = value;
                filmeMin.setProducer(name);
                filmeMin.setInterval(value);
                filmeMin.setPreviousWin(Integer.parseInt(parts[2]));
                filmeMin.setFollowingWin(Integer.parseInt(parts[3]));
            }
        }
        listaMin.add(filmeMin);
        listaMax.add(filmeMax);
        listaMinMax.setMin(listaMin);
        listaMinMax.setMax(listaMax);

        return listaMinMax;
    }
}