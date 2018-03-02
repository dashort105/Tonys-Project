package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(ProductsRepository repository) {
		return (args) -> {
			
			
			// fetch all products
			log.info("Products found with findAll():");
			log.info("-------------------------------");
			for (Products products : repository.findAll()) {
				log.info(products.toString());
			}
			log.info("");

			/* fetch an individual products by ID
			Products products = repository.findOne(new String());  //(new Integer(1))
			log.info("Products found with findOne(1L):");
			log.info("--------------------------------");
			log.info(products.toString());
			log.info("");
			*/

			// fetch products by prodId
			log.info("Products found with findByProdIdStartsWithIgnoreCase('BR02'):");
			log.info("--------------------------------------------");
			for (Products br02 : repository
					.findByProdIdStartsWithIgnoreCase("BR02")) {
				log.info(br02.toString());
			}
			log.info("");
			
		};
	}

}
