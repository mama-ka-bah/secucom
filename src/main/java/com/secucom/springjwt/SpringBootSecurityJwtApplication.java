package com.secucom.springjwt;

import com.secucom.springjwt.controllers.AuthController;
import com.secucom.springjwt.models.Collaborateurs;
import com.secucom.springjwt.models.ERole;
import com.secucom.springjwt.models.Role;
import com.secucom.springjwt.repository.CollaborateursRepository;
import com.secucom.springjwt.repository.RoleRepository;
import com.secucom.springjwt.security.services.Services;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@SpringBootApplication
public class SpringBootSecurityJwtApplication  implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);


	final private CollaborateursRepository collaborateursRepository;
	final private RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	public static void main(String[] args) {
    SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(roleRepository.findAll().size() == 0){
			roleRepository.save(new Role(ERole.ROLE_USER));
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
		}
		if (collaborateursRepository.findAll().size() == 0){
			Set<Role> roles = new HashSet<>();
			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
			roles.add(adminRole);
			Collaborateurs collaborateurs = new Collaborateurs("keita@123",
					"kmahamadou858@gmail.com",
					encoder.encode("keita@pass"));
			collaborateurs.setRoles(roles);
			collaborateursRepository.save(collaborateurs);
		}
	}
}
