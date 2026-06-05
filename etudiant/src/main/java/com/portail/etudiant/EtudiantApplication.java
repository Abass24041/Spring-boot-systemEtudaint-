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
			}

			Optional<Etudiant> demoOpt = etudiantRepository.findFirstByEmail("etudiant@supnum.mr");
			if (demoOpt.isEmpty()) {
				Etudiant demo = new Etudiant();
				demo.setNom("Diallo");
				demo.setPrenom("Aminata");
				demo.setEmail("etudiant@supnum.mr");
				demo.setMatricule("SN2026001");
				demo.setNumeroNational("1234567890");
				demo.setPassword(passwordEncoder.encode("1234567890"));
				demo.setRole("USER");
				demo.setSpecialite("Développement Web et Mobile");
				demo.setNiveau("L3");
				etudiantRepository.save(demo);
			}
		};
	}
}
