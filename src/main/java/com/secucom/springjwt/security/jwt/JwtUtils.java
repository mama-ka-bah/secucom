package com.secucom.springjwt.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.secucom.springjwt.security.services.CollaborateurDetailsImpl;
import io.jsonwebtoken.*;

@Component
public class JwtUtils {

  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${bezkoder.app.jwtSecret}")
  private String jwtSecret;

  @Value("${bezkoder.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  public String generateJwtToken(Authentication authentication) {

    CollaborateurDetailsImpl userPrincipal = (CollaborateurDetailsImpl) authentication.getPrincipal();

    //generation du token
    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();//HS256
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Signature JWT non valide: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Jeton JWT non valide: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("Le jeton JWT a expiré: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("Le jeton JWT n'est pas pris en charge: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("La chaîne de revendications JWT est vide: {}", e.getMessage());
    }

    return false;
  }
}
