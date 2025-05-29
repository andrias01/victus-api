package co.edu.uco.easy.victusresidencias.victus_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"co.edu.uco.easy.victusresidencias.victus_api", "co.edu.uco.easy.victusresidencias.victus_api.controller", "co.edu.uco.easy.victusresidencias.victus_api.config"})
public class VictusRApplication {

	public static void main(String[] args) {
		SpringApplication.run(VictusRApplication.class, args);
	}

}
