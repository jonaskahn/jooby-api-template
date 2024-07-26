package io.github.jonaskahn.services.authen;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BcryptPasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String encode(String raw) {
        return BCrypt.withDefaults().hashToString(12, raw.toCharArray());
    }

    @Override
    public Boolean matches(String raw, String encoded) {
        return BCrypt.verifyer().verify(raw.toCharArray(), encoded.toCharArray()).verified;
    }
}
