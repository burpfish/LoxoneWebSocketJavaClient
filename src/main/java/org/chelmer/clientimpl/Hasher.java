package org.chelmer.clientimpl;

import org.chelmer.exceptions.ParsingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class Hasher {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public String hash(String data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            Formatter formatter = new Formatter();
            for (byte b : mac.doFinal(data.getBytes())) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException e) {
            throw new ParsingException("Cannot hash key " + e);
        }
    }
}
