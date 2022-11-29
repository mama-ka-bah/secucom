package com.secucom.springjwt.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.secucom.springjwt.models.Collaborateurs;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CollaborateurDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;


  private Long id;

  private String username;

  private String email;

  @JsonIgnore
  private String password;

  //GrantedAuthority est le privilège individuel . Exemples:  READ_AUTHORITY , WRITE_PRIVILEGE ou même CAN_EXECUTE_AS_ROOT

  private Collection<? extends GrantedAuthority> authorities;

  public CollaborateurDetailsImpl(Long id, String username, String email, String password,
                                  Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  //return l'user avec tous ces droits et toutes ces informations
  public static CollaborateurDetailsImpl build(Collaborateurs user) {
    List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
        .collect(Collectors.toList());

    return new CollaborateurDetailsImpl(
        user.getId(), 
        user.getUsername(), 
        user.getEmail(),
        user.getPassword(), 
        authorities
    );
  }

  //recupere les information l'user de l'utilisateur
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CollaborateurDetailsImpl user = (CollaborateurDetailsImpl) o;
    return Objects.equals(id, user.id);
  }
}
