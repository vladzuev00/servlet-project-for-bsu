package logic.useridentifierfromdatabase.exception;

public final class UserIdentifyingFromDataBaseException extends Exception
{
    public UserIdentifyingFromDataBaseException()
    {
        super();
    }

    public UserIdentifyingFromDataBaseException(final String description)
    {
        super(description);
    }

    public UserIdentifyingFromDataBaseException(final Exception cause)
    {
        super(cause);
    }

    public UserIdentifyingFromDataBaseException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
