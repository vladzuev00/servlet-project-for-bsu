package logic.cryptographer.stringcryptographer;

import logic.cryptographer.Cryptographer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class StringCryptographer implements Cryptographer<String, String>
{
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    public StringCryptographer()
    {
        super();
        this.encoder = Base64.getEncoder();
        this.decoder = Base64.getDecoder();
    }

    @Override
    public final String encrypt(final String encryptedString)
    {
        return this.encoder.encodeToString(encryptedString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public final String decrypt(final String decryptedData)
    {
        return new String(this.decoder.decode(decryptedData));
    }
}
