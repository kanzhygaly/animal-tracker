package kz.yerakh.animaltrackerservice.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public Optional<AppUserDetails> getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.of((AppUserDetails) authentication.getPrincipal());
        }
        return Optional.empty();
    }
}
