package frp.utn.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "frp.utn.edu")
public class TpProgApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpProgApplication.class, args);
	}

}
