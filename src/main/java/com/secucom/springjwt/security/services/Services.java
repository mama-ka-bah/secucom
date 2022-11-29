package com.secucom.springjwt.security.services;

import com.secucom.springjwt.models.Collaborateurs;
import com.secucom.springjwt.models.Role;

import java.util.List;

public interface Services {
    String modifierCollaborateur(Collaborateurs collaborateurs);
    List<Role> afficherRoles();
    String modifierRole(Role roles);
    String supprimerRole(Long id);
    String supprimerCollaborateur(Long id);
}
