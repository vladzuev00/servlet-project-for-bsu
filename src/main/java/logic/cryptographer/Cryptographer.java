package logic.cryptographer;

public interface Cryptographer<TypeOfEncryptedData, TypeOfResultData>
{
    public abstract TypeOfResultData encrypt(final TypeOfEncryptedData encryptedData);
    public abstract TypeOfEncryptedData decrypt(final TypeOfResultData decryptedData);
}
