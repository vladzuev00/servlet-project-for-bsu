package servlet.exception;

public final class ServletDestroyingException extends RuntimeException
{
    public ServletDestroyingException()
    {
        super();
    }

    public ServletDestroyingException(final String description)
    {
        super(description);
    }

    public ServletDestroyingException(final Exception cause)
    {
        super(cause);
    }

    public ServletDestroyingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
