package com.secucom.springjwt.controllers;

import com.secucom.springjwt.models.Collaborateurs;
import com.secucom.springjwt.repository.CollaborateursRepository;
import com.secucom.springjwt.security.services.Services;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborateur")
@AllArgsConstructor
public class CollaborateurControlleur {

    private static final Logger log = LoggerFactory.getLogger(CollaborateurControlleur.class);


    final private CollaborateursRepository collaborateursRepository;

    final private Services services;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/afficherCollaborateur")
    public List<Collaborateurs> afficherUtilisateur(){
        return collaborateursRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modifierCollaborateur")
    public String modifierCollaborateur(Collaborateurs collaborateurs){
        return services.modifierCollaborateur(collaborateurs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/supprimerCollaborateur/{id}")
    public String supprimerCollaborateur(@PathVariable Long id){
        return services.supprimerCollaborateur(id);
    }

}
