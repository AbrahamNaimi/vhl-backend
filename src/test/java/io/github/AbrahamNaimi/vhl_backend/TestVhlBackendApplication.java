package io.github.AbrahamNaimi.vhl_backend;

import org.springframework.boot.SpringApplication;

public class TestVhlBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(VhlBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
