package com.api;

import com.api.controllers.FilmesController;
import com.api.dtos.ListaFilmesDTO;
import com.api.dtos.MensagemDTO;
import com.api.exceptions.EventoException;
import com.api.models.FilmesModel;
import com.api.repositories.FilmesRepository;
import com.api.services.FilmesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class ApplicationTests {

	@InjectMocks
	private FilmesController filmesController;

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private FilmesService filmesService;

	@Autowired
	private FilmesRepository filmesRepository;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	public void testArquivo_CSV_importado() throws EventoException {
		MockMultipartFile file = new MockMultipartFile("arquivo", "testeMovielist.csv", "text/csv", "data".getBytes());
		MensagemDTO mensagemDTO = new MensagemDTO(HttpStatus.CREATED.value(), "Arquivo CSV importado com sucesso.");
		when(filmesService.carregarDadosCSV(file)).thenReturn(mensagemDTO);

		ResponseEntity<MensagemDTO> response = filmesController.arquivo(file);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals("Arquivo CSV importado com sucesso.", response.getBody().getMensagem());
		verify(filmesService, times(1)).carregarDadosCSV(file);
	}

	@Test
	public void testArquivo_CSV_nao_importado() throws EventoException {
		MockMultipartFile file = new MockMultipartFile("arquivo", "testeMovielist.csv", "text/csv", "data".getBytes());
		MensagemDTO mensagemDTO = new MensagemDTO(HttpStatus.NO_CONTENT.value(), "Arquivo CSV não possui dados.");
		when(filmesService.carregarDadosCSV(file)).thenReturn(mensagemDTO);

		ResponseEntity<MensagemDTO> response = filmesController.arquivo(file);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertEquals("Arquivo CSV não possui dados.", response.getBody().getMensagem());
		verify(filmesService, times(1)).carregarDadosCSV(file);
	}

	@Test
	public void testArquivo_CSV_importado2() throws Exception {
		String csvData = "Ano;Titulo;Estudio;Produtor;Ganhador\n2025;Filme teste;Estudio teste;Produtor teste;yes";
		MockMultipartFile file = new MockMultipartFile("arquivo", "filmes.csv", "text/csv", csvData.getBytes());

		mockMvc.perform(multipart("/api/arquivo").file(file))
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"codigo\":201,\"mensagem\":\"Arquivo CSV importado com sucesso.\"}"));

		assertEquals(1, filmesRepository.count());
		FilmesModel filme = filmesRepository.findAll().get(0);
		assertEquals("Filme teste", filme.getTitulo());
	}

	@Test
	public void testArquivo_CSV_nao_importado2() throws Exception {
		MockMultipartFile file = new MockMultipartFile("arquivo", "vazio.csv", "text/csv", "".getBytes());

		mockMvc.perform(multipart("/api/arquivo").file(file))
				.andExpect(status().isNoContent())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("{\"codigo\":204,\"mensagem\":\"Arquivo CSV não possui dados.\"}"));

		assertEquals(0, filmesRepository.count());
	}

	@Test
	public void testIntervalo() throws Exception {
		String csvData = "Ano;Titulo;Estudio;Produtor;Ganhador\n2022;Filme teste2;Estudio teste2;Produtor teste2;yes\n2021;Filme teste3;Estudio teste3;Produtor teste3;yes";
		MockMultipartFile file = new MockMultipartFile("arquivo", "filmes.csv", "text/csv", csvData.getBytes());

		// Carregar os dados primeiro
		mockMvc.perform(multipart("/api/arquivo").file(file)).andExpect(status().isCreated());

		// Agora, teste o endpoint de intervalo
		mockMvc.perform(get("/api/intervalo"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testIntervalo2() {

		ListaFilmesDTO listaFilmesDTO = new ListaFilmesDTO();
		when(filmesService.procurarCandidatoMaxMin()).thenReturn(listaFilmesDTO);

		ResponseEntity<ListaFilmesDTO> response = filmesController.intervalo();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(listaFilmesDTO, response.getBody());
		verify(filmesService, times(1)).procurarCandidatoMaxMin();
	}

	@Test
	@Transactional
	public void test_salvar_recuperar_dado() {

		FilmesModel filme = new FilmesModel();
		filme.setAno("1979");
		filme.setTitulo("Filme teste4");
		filme.setEstudio("Estúdio teste4");
		filme.setProdutor("Produtor teste4");
		filme.setGanhador("yes");

		filmesRepository.deleteAll();
		filmesRepository.save(filme);

		List<FilmesModel> filmes = filmesRepository.findAll();
		assertEquals(1, filmes.size());
		assertEquals("Filme teste4", filmes.get(0).getTitulo());
	}
}
