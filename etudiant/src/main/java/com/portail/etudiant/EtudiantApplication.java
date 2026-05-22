package com.portail.etudiant;

import com.portail.etudiant.model.Etudiant;
import com.portail.etudiant.repository.EtudiantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class EtudiantApplication {

	public static void main(String[] args) {
		SpringApplication.run(EtudiantApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(EtudiantRepository etudiantRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			Optional<Etudiant> adminOpt = etudiantRepository.findFirstByEmail("admin@supnum.mr");
			if (adminOpt.isEmpty()) {
				Etudiant admin = new Etudiant();
				admin.setNom("Admin");
				admin.setPrenom("SupNum");
				admin.setEmail("admin@supnum.mr");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN");
				etudiantRepository.save(admin);
			} else {
				Etudiant admin = adminOpt.get();
				admin.setPassword(passwordEncoder.encode("admin123"));
				etudiantRepository.save(admin);
			}
		};
	}
}
