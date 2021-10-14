package servlet.exception;

public class ServletInitializationException extends RuntimeException
{
    public ServletInitializationException()
    {
        super();
    }

    public ServletInitializationException(final String description)
    {
        super(description);
    }

    public ServletInitializationException(final Exception cause)
    {
        super(cause);
    }

    public ServletInitializationException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
