package logic.databaseconnectionpool.exception;

public final class DataBasePoolConnectionCreatingException extends DataBaseConnectionPoolException
{
    public DataBasePoolConnectionCreatingException()
    {
        super();
    }

    public DataBasePoolConnectionCreatingException(final String description)
    {
        super(description);
    }

    public DataBasePoolConnectionCreatingException(final Exception cause)
    {
        super(cause);
    }

    public DataBasePoolConnectionCreatingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
