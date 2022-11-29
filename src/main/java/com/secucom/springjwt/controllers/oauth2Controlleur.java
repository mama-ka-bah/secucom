package com.secucom.springjwt.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class oauth2Controlleur {

    private static final Logger log = LoggerFactory.getLogger(oauth2Controlleur.class);


    @Autowired
    OAuth2AuthorizedClientService  authorizedClientService;

    /*public oauth2Controlleur(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }*/


    @RequestMapping("/**")
   // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
        public StringBuffer getOauth2LoginInfo(Principal user){



            StringBuffer protectedInfo = new StringBuffer();

            OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
            OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
            if(authToken.isAuthenticated()){

                Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

                String userToken = authClient.getAccessToken().getTokenValue();
                protectedInfo.append("Bienvenu, " + userAttributes.get("name")+"<br><br>");
                protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
                protectedInfo.append("Access Token: " + userToken+"<br><br>");
            }
            else{
                protectedInfo.append("NA");
            }
            return protectedInfo;
        }
    }

