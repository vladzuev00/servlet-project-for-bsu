package logic.entityinserterindatabase.exception;

public final class EntityInsertionInDataBaseException extends EntityInserterInDataBaseException
{
    public EntityInsertionInDataBaseException()
    {
        super();
    }

    public EntityInsertionInDataBaseException(final String description)
    {
        super(description);
    }

    public EntityInsertionInDataBaseException(final Exception cause)
    {
        super(cause);
    }

    public EntityInsertionInDataBaseException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}

