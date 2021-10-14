package logic.idgenerator.exception;

public final class IdGeneratorGenerationException extends IdGeneratorException
{
    public IdGeneratorGenerationException()
    {
        super();
    }

    public IdGeneratorGenerationException(final String description)
    {
        super(description);
    }

    public IdGeneratorGenerationException(final Exception cause)
    {
        super(cause);
    }

    public IdGeneratorGenerationException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
