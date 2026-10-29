package com.brenon.validador_sped;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Validador SPED.
 *
 * <p>O {@code @SpringBootApplication} habilita automaticamente:
 * <ul>
 *   <li>{@code @Configuration} — define a classe como fonte de beans.</li>
 *   <li>{@code @EnableAutoConfiguration} — configura Spring Data JPA, H2, Web MVC etc.</li>
 *   <li>{@code @ComponentScan} — escaneia todos os subpacotes (controller, service, repository, model).</li>
 * </ul>
 *
 * @author Brenon
 */
@SpringBootApplication
public class ValidadorSpedApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValidadorSpedApplication.class, args);
    }
}