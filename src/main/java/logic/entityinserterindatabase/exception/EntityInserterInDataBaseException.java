package logic.entityinserterindatabase.exception;

public class EntityInserterInDataBaseException extends Exception
{
    public EntityInserterInDataBaseException()
    {
        super();
    }

    public EntityInserterInDataBaseException(final String description)
    {
        super(description);
    }

    public EntityInserterInDataBaseException(final Exception cause)
    {
        super(cause);
    }

    public EntityInserterInDataBaseException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
