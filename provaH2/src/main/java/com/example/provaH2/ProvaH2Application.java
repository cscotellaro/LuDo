package com.example.provaH2;

import com.example.provaH2.entity.Account;
import com.example.provaH2.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProvaH2Application {

	@Autowired
	private ProvaEntitaRepository repository;

	@Autowired
	private AccountRepository repositoryA;

	public static void main(String[] args) {
		SpringApplication.run(ProvaH2Application.class, args);
	}

	@Bean
	public CommandLineRunner initializeData(){
		return args -> {
			//repository.deleteAll();
			repository.save(new provaEntita("SaraM","Zia"));
			repository.save(new provaEntita("Sara","Zia"));
			repository.save(new provaEntita("Cinzia", "Mamma"));
			repository.findAll().forEach(System.out::println);
			System.out.println("ok");
			System.out.println(repository.findOneByCognome("Mamma"));
			System.out.println(repository.findByCognome("Zia"));

			//System.out.println(repository.findOneByFirstName("Sara"));

			repositoryA.save(new Account("Catello", "tizio@gmail.com", "catello"));
			repositoryA.save(new Account("Luigi", "caio@gmail.com", "luigi"));
			repositoryA.findAll().forEach(System.out::println);

		};

	}

}
