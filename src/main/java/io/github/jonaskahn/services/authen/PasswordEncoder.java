package io.github.jonaskahn.services.authen;

public interface PasswordEncoder {

    String encode(String raw);

    Boolean matches(String raw, String encoded);
}
