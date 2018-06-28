package com.example.provaH2;

import com.example.provaH2.entity.Account;
import com.example.provaH2.guess.db.Item;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Voto;
import com.example.provaH2.filter.LoginFilter;
import com.example.provaH2.prova.ProvaEntitaRepository;
import com.example.provaH2.repository.AccountRepository;
import com.example.provaH2.guess.db.ItemRepository;
import com.example.provaH2.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ProvaH2Application {

	@Autowired
	private ProvaEntitaRepository repository;
	@Autowired
	private AccountRepository repositoryA;
	@Autowired
	private PartitaRepository repositoryP;
	/*	@Autowired
	private ItemRepository repositoryI;
*/

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
		/*	repository.save(new provaEntita("SaraM","Zia"));
			repository.save(new provaEntita("Sara","Zia"));
			repository.save(new provaEntita("Cinzia", "Mamma"));
			repository.findAll().forEach(System.out::println);
			System.out.println("ok");
			System.out.println(repository.findOneByCognome("Mamma"));
			System.out.println(repository.findByCognome("Zia"));

			//System.out.println(repository.findOneByFirstName("Sara"));
		*/
			Account cinzia=new Account("Cinzia", "cinzia@gmail.com", "cinzia");
			Account luigi=new Account("Luigi", "caio@gmail.com", "luigi");
			repositoryA.save(new Account("Catello", "tizio@gmail.com", "catello"));
			repositoryA.save(luigi);
			repositoryA.save(cinzia);
			repositoryA.findAll().forEach(System.out::println);

		/*	Item item= new Item("Sara");
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
*/
			/*CustomSpringEvent customSpringEvent = new CustomSpringEvent(this, "sono un messaggio",4);
			applicationEventPublisher.publishEvent(customSpringEvent);
			*/

			Partita partita= new Partita(new Timestamp(new Date().getTime()));
			partita.addVoto(new Voto(cinzia,10));
			repositoryP.save(partita);
			repositoryP.findAll().forEach(System.out::println);

			repositoryA.updatePassword("cinzia@gmail.com", "nuovaPassword");
			repositoryA.findAll().forEach(System.out::println);
			repositoryP.findAll().forEach(System.out::println);

			Partita partita1= new Partita(new Timestamp(new Date().getTime()));
			partita1.addVoto(new Voto(cinzia,120));
			repositoryP.save(partita1);

			Partita partita2= new Partita(new Timestamp(new Date().getTime()));
			partita2.addVoto(new Voto(luigi,10));
			repositoryP.save(partita2);

			Partita speriamo= repositoryP.lastPartita(cinzia);
			repositoryP.findAll().forEach(System.out::println);
			System.out.println("");
			System.out.println(speriamo);

			List<Partita> partite=repositoryP.cercaPartite(luigi);
			System.out.println(partite);

			//cinzia.setImage();

		/*	ClassResource resource= new ClassResource("/profilo.jpg");
			Embedded embedded= new Embedded("a", resource);
			ByteArrayOutputStream strem= new ByteArrayOutputStream();
			luigi.setImage(strem.toByteArray());
			*/

			//FileResource resourcefile = new FileResource(new File("/profilo.jpg"));
			/*String currentPath = new File("./src/main/resources/profilo.jpg").getAbsolutePath();
			System.out.println(currentPath);*/

			/*	String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
			byte[] array = Files.readAllBytes(new File(basePath+"/profilo.jpg").toPath());
			luigi.setImage(array);
			*/
			/*Path path = Paths.get("src/main/resources/profilo.jpg");
			byte[] data = Files.readAllBytes(path);
			luigi.setImage(data);
			System.out.println("_"+ data.length);*/
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
