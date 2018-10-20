package com.example.provaH2;

import com.example.provaH2.entity.Account;
import com.example.provaH2.guess.db.Item;
import com.example.provaH2.entity.Partita;
import com.example.provaH2.entity.Punteggio;
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
			//vedi che a cinzia ho fatto il cambio password
			Account cinzia=new Account("Cinzia", "cinzia@gmail.com", "cinzia");
			Account luigi=new Account("Luigi", "caio@gmail.com", "luigi");
			Account francesca=new Account("Francesca", "francesca@gmail.com", "francesca");
			Account antonio=new Account("Antonio", "antonio@gmail.com", "antonio");
			Account gianluca=new Account("Gianluca", "gianluca@gmail.com", "gianluca");
			Account sara=new Account("Sara", "sara@gmail.com", "sara");
			repositoryA.save(new Account("Catello", "tizio@gmail.com", "catello"));
			repositoryA.save(luigi);
			repositoryA.save(cinzia);
			repositoryA.save(francesca);
			repositoryA.save(antonio);
			repositoryA.save(gianluca);
			repositoryA.save(sara);
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

			Partita partita= new Partita(new Timestamp(new Date().getTime()), "unGioco");
			partita.addPunteggio(new Punteggio(cinzia,10));
			partita.addPunteggio(new Punteggio(luigi,18));
			repositoryP.save(partita);
			//repositoryP.findAll().forEach(System.out::println);

			repositoryA.updatePassword("cinzia@gmail.com", "nuovaPassword");
			repositoryA.findAll().forEach(System.out::println);
			//repositoryP.findAll().forEach(System.out::println);

			Partita partita1= new Partita(new Timestamp(new Date().getTime()), "unGioco");
			partita1.addPunteggio(new Punteggio(cinzia,120));
			partita1.addPunteggio(new Punteggio(luigi,40));
			repositoryP.save(partita1);

			Partita partita2= new Partita(Timestamp.valueOf("2018-05-03 11:20:09"),"Guess");
			partita2.addPunteggio(new Punteggio(luigi,10));
			partita2.addPunteggio(new Punteggio(cinzia,100));
			repositoryP.save(partita2);
			Partita partita4= new Partita(Timestamp.valueOf("2018-03-21 16:20:09"),"Guess");
			partita4.addPunteggio(new Punteggio(luigi,40));
			partita4.addPunteggio(new Punteggio(cinzia,10));
			repositoryP.save(partita4);
			Partita partita5= new Partita(Timestamp.valueOf("2018-01-04 22:17:02"),"Guess");
			partita5.addPunteggio(new Punteggio(luigi,20));
			partita5.addPunteggio(new Punteggio(cinzia,70));
			repositoryP.save(partita5);
			Partita partita6= new Partita(Timestamp.valueOf("2018-05-04 11:20:09"),"Guess");
			partita6.addPunteggio(new Punteggio(luigi,10));
			partita6.addPunteggio(new Punteggio(cinzia,100));
			repositoryP.save(partita6);
			Partita partita7= new Partita(Timestamp.valueOf("2018-02-21 16:20:09"),"Guess");
			partita7.addPunteggio(new Punteggio(luigi,40));
			partita7.addPunteggio(new Punteggio(cinzia,10));
			repositoryP.save(partita7);
			Partita partita8= new Partita(Timestamp.valueOf("2018-07-04 12:17:02"),"Guess");
			partita8.addPunteggio(new Punteggio(luigi,20));
			partita8.addPunteggio(new Punteggio(cinzia,70));
			repositoryP.save(partita8);


			Partita partita3= new Partita(new Timestamp(new Date().getTime()), "unGioco");
			partita3.addPunteggio(new Punteggio(cinzia,20));
			partita3.addPunteggio(new Punteggio(luigi,20));
			repositoryP.save(partita3);

			Partita speriamo= repositoryP.lastPartita(cinzia);
			//repositoryP.findAll().forEach(System.out::println);
			System.out.println("");
//			System.out.println(speriamo);


			System.out.println("");
			List<Partita> partite=repositoryP.cercaPartite(cinzia);
			//partite.forEach(System.out::println);

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
