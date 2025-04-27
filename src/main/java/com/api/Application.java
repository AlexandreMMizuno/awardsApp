package com.api;

import com.api.exceptions.EventoException;
import com.api.services.FilmesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;

@SpringBootApplication
public class Application {

	@Autowired
	private FilmesService filmesService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner inicializar(ApplicationContext appContext) throws EventoException {
		return args -> {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classloader.getResourceAsStream("movielist.csv");
			this.filmesService.iniciarDadosCSV(inputStream);
		};
	}

}
