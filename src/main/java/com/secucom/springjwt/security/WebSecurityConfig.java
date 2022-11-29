package com.secucom.springjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.secucom.springjwt.security.jwt.AuthEntryPointJwt;
import com.secucom.springjwt.security.jwt.AuthTokenFilter;
import com.secucom.springjwt.security.services.CollaborateurDetailsServiceImpl;


/*
* HttpSecurity configurations pour configurer cors, csrf, la gestion de session, les règles
*  pour les ressources protégées.
* */

/*
* @EnableGlobalMethodSecurityassure la sécurité AOP sur les méthodes. Il permet @PreAuthorize,
*  @PostAuthorize, il prend également en charge JSR-250 .
* */

/*
@EnableWebSecurity permet à Spring de trouver et d'appliquer automatiquement
la sécurité à la classe Web globale.
 */

/*
Nous redéfinissons la configure(HttpSecurity http)méthode de l' WebSecurityConfigurerAdapterinterface.
 Il indique à Spring Security comment nous configurons CORS et CSRF, quand nous voulons exiger que
 tous les utilisateurs soient authentifiés ou non, quel filtre ( AuthTokenFilter) et quand nous
 voulons que cela fonctionne (filtre avant UsernamePasswordAuthenticationFilter), quel
  gestionnaire d'exception est choisi ( AuthEntryPointJwt).
 */

/*
- UserDetailsService L'interface a une méthode pour charger l'utilisateur par nom d' utilisateur
et renvoie un UserDetailsobjet que Spring Security peut utiliser pour l'authentification et la
validation.

– UserDetailscontient les informations nécessaires (telles que : nom d'utilisateur, mot de passe,
 autorités) pour construire un objet d'authentification.
 */


/*
L'implémentation de UserDetailsService sera utilisée pour la configuration DaoAuthenticationProviderpar
 AuthenticationManagerBuilder.userDetailsService()méthode.

– Nous avons également besoin d'un PasswordEncoderpour le DaoAuthenticationProvider. Si nous ne le
spécifions pas, il utilisera du texte brut.
 */

@Configuration
@EnableGlobalMethodSecurity(
    prePostEnabled = true)
public class WebSecurityConfig {



  @Autowired
  CollaborateurDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {


      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

      //
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  /*
  Pour activer la sécurité HTTP dans Spring, nous devons créer un bean SecurityFilterChain

  La configuration ci-dessus garantit que toute demande adressée à l'application est authentifiée
  avec une connexion basée sur un formulaire ou une authentification de base HTTP.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        //.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
        .antMatchers("/api/bienvenue/**").permitAll()
            .antMatchers("/api/roles/**").permitAll()
            .antMatchers("/api/collaborateur/**").permitAll()
        .anyRequest().authenticated()
            .and()
            .oauth2Login();
    http.formLogin();

    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }

}
