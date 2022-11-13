package com.kitaplik.libraryservice;

import com.kitaplik.libraryservice.config.VaultConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bootstrap.BootstrapConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.vault.annotation.VaultPropertySource;

@SpringBootApplication
@EnableFeignClients
public class LibraryServiceApplication implements CommandLineRunner {

	private final VaultConfig vaultConfig;

	public LibraryServiceApplication(VaultConfig vaultConfig) {
		this.vaultConfig = vaultConfig;
	}

	public static void main(String[] args) {
		SpringApplication.run(LibraryServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Book Count Config" + vaultConfig.getCount());
	}
/*
	//Feign Client Error Handling
	@Bean
	public ErrorDecoder errorDecoder() {
		return new RetreiveMessageErrorDecoder();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}
*/
}
