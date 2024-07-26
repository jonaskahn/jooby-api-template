package io.github.jonaskahn.services.authen;

import com.google.inject.ImplementedBy;

@ImplementedBy(BcryptPasswordEncoderImpl.class)
public interface PasswordEncoder {

    String encode(String raw);

    Boolean matches(String raw, String encoded);
}
