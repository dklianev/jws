package org.informatics.winter_olympics.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class NavigationModelAdvice {

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        return hasAuthority(authentication, "ADMIN");
    }

    @ModelAttribute("isAthlete")
    public boolean isAthlete(Authentication authentication) {
        return hasAuthority(authentication, "ATHLETE");
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return isLoggedIn(authentication) && authentication.getAuthorities().stream()
                .anyMatch(item -> item.getAuthority().equals(authority));
    }
}
