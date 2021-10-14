package logic.databaseconnectionpool.exception;

public final class DataBaseConnectionPoolFullingException extends DataBaseConnectionPoolException
{
    public DataBaseConnectionPoolFullingException()
    {
        super();
    }

    public DataBaseConnectionPoolFullingException(final String description)
    {
        super(description);
    }

    public DataBaseConnectionPoolFullingException(final Exception cause)
    {
        super(cause);
    }

    public DataBaseConnectionPoolFullingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
