package com.example.provaH2;

import com.example.provaH2.entity.Account;
import com.example.provaH2.entity.Item;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Voto;
import com.example.provaH2.filter.LoginFilter;
import com.example.provaH2.prova.ProvaEntitaRepository;
import com.example.provaH2.prova.provaEntita;
import com.example.provaH2.repository.AccountRepository;
import com.example.provaH2.repository.ItemRepository;
import com.example.provaH2.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.util.List;

@SpringBootApplication
public class ProvaH2Application {

	@Autowired
	private ProvaEntitaRepository repository;
	@Autowired
	private ItemRepository repositoryI;
	@Autowired
	private AccountRepository repositoryA;
	@Autowired
	private PartitaRepository repositoryP;

/*	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
*/

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

			Account cinzia=new Account("Cinzia", "cinzia@gmail.com", "cinzia");
			Account luigi=new Account("Luigi", "caio@gmail.com", "luigi");
			repositoryA.save(new Account("Catello", "tizio@gmail.com", "catello"));
			repositoryA.save(luigi);
			repositoryA.save(cinzia);
			repositoryA.findAll().forEach(System.out::println);

			Item item= new Item("Sara");
			item.addIndizio(0,"come");
			item.addIndizio(1,"dove");
			item.addIndizio(2,"quando");
			item.addIndizio(3,"perchè");
			repositoryI.save(item);

			Item item2= new Item("Sara2");
			item2.addIndizio(0,"come2");
			item2.addIndizio(1,"dove2");
			item2.addIndizio(2,"quando2");
			item2.addIndizio(3,"perchè2");
			repositoryI.save(item2);
			repositoryI.findAll().forEach(System.out::println);

			long i= repositoryI.numeroRighe();
			//List<Item> i= repositoryI.dammiACaso();
			System.out.println(i);

			/*CustomSpringEvent customSpringEvent = new CustomSpringEvent(this, "sono un messaggio",4);
			applicationEventPublisher.publishEvent(customSpringEvent);
			*/

			Partita partita= new Partita("prima Partita");
			partita.add(new Voto(cinzia,10));
			repositoryP.save(partita);
			repositoryP.findAll().forEach(System.out::println);

			repositoryA.updatePassword("cinzia@gmail.com", "nuovaPassword");
			repositoryA.findAll().forEach(System.out::println);
			repositoryP.findAll().forEach(System.out::println);

			List<Partita> partite=repositoryP.cercaPartite(luigi);
			System.out.println(partite);
		};

	}



	@Bean
	public FilterRegistrationBean someFilterRegistration(){
		FilterRegistrationBean filterRegistrationBean= new FilterRegistrationBean();
		filterRegistrationBean.setFilter(someFilter());
		filterRegistrationBean.addUrlPatterns("/private/*");
		filterRegistrationBean.setName("someFilter");
		filterRegistrationBean.setOrder(1);
		return  filterRegistrationBean;
	}

	public Filter someFilter() {
		return new LoginFilter();
	}

}
