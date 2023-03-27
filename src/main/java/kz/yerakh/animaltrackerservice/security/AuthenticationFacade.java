package kz.yerakh.animaltrackerservice.security;

import java.util.Optional;

public interface AuthenticationFacade {

    Optional<AppUserDetails> getUser();
}
