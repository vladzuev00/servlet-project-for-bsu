package logic.idgenerator.exception;

public class IdGeneratorException extends Exception
{
    public IdGeneratorException()
    {
        super();
    }

    public IdGeneratorException(final String description)
    {
        super(description);
    }

    public IdGeneratorException(final Exception cause)
    {
        super(cause);
    }

    public IdGeneratorException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
