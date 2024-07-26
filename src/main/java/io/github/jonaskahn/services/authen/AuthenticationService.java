package io.github.jonaskahn.services.authen;

import com.google.inject.ImplementedBy;

@ImplementedBy(AuthenticationServiceImpl.class)
public interface AuthenticationService {
    String generateToken(String username, String password, Boolean increaseExpiration);

    void logout();
}
