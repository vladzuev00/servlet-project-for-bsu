package logic.databaseconnectionpool.validator;

public final class DataBaseConnectionPoolValidator
{
    public DataBaseConnectionPoolValidator()
    {
        super();
    }

    public final boolean isValidAmountOfInvolvedConnections(final int researchAmountOfInvolvedConnections)
    {
        return     DataBaseConnectionPoolValidator.MINIMAL_ALLOWABLE_AMOUNT_OF_INVOLVED_CONNECTIONS <= researchAmountOfInvolvedConnections
                && researchAmountOfInvolvedConnections <= DataBaseConnectionPoolValidator.MAXIMAL_ALLOWABLE_AMOUNT_OF_INVOLVED_CONNECTIONS;
    }

    private static final int MINIMAL_ALLOWABLE_AMOUNT_OF_INVOLVED_CONNECTIONS = 1;
    private static final int MAXIMAL_ALLOWABLE_AMOUNT_OF_INVOLVED_CONNECTIONS = 100;
}
