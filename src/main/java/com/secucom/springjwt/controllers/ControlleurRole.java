package com.secucom.springjwt.controllers;

import com.secucom.springjwt.models.Role;
import com.secucom.springjwt.repository.RoleRepository;
import com.secucom.springjwt.security.services.Services;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class ControlleurRole {

    private static final Logger log = LoggerFactory.getLogger(ControlleurRole.class);


    final private Services services;
    final private RoleRepository roleRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ajouterRoles")
    public Role ajouterRoles(@RequestBody Role roles){

        return roleRepository.save(roles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/AfficherRoles")
    public List<Role> afficherRoles(){

        return services.afficherRoles();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modifierRoles")
    public String modifierRoles(Role roles){
        return services.modifierRole(roles);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/supprimerRole/{id}")
    public String supprimerRole(@PathVariable Long id){
        return services.supprimerRole(id);
    }

}
