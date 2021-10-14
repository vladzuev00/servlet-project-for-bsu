package logic.databaseconnectionpool;

import logic.databaseconnectionpool.exception.DataBaseConnectionPoolAccessConnectionException;
import logic.databaseconnectionpool.exception.DataBaseConnectionPoolFullingException;
import logic.databaseconnectionpool.exception.DataBasePoolConnectionCreatingException;
import logic.databaseconnectionpool.validator.DataBaseConnectionPoolValidator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.*;

public final class DataBaseConnectionPool implements AutoCloseable
{
    private final Future<BlockingQueue<Connection>> holderOfAvailableConnections;

    private DataBaseConnectionPool(final Future<BlockingQueue<Connection>> holderOfAvailableConnections)
    {
        super();
        this.holderOfAvailableConnections = holderOfAvailableConnections;
    }

    public static DataBaseConnectionPool createDataBaseConnectionPool(final String nameOfClassOfDriver,
                                                                      final String urlOfDataBase, final String user,
                                                                      final String password)
    {
        return DataBaseConnectionPool.createDataBaseConnectionPool(nameOfClassOfDriver, urlOfDataBase, user, password,
                DataBaseConnectionPool.DEFAULT_AMOUNT_OF_INVOLVED_CONNECTIONS);
    }

    private static final int DEFAULT_AMOUNT_OF_INVOLVED_CONNECTIONS = 20;

    public static DataBaseConnectionPool createDataBaseConnectionPool(final String nameOfClassOfDriver,
                                                                      final String urlOfDataBase, final String user,
                                                                      final String password,
                                                                      final int amountOfInvolvedConnections)
    {
        if(!DataBaseConnectionPool.DATA_BASE_CONNECTION_POOL_VALIDATOR
                .isValidAmountOfInvolvedConnections(amountOfInvolvedConnections))
        {
            throw new DataBasePoolConnectionCreatingException("Impossible to create pool of connections to data base by "
                    + "given not valid amount of involved connections: " + amountOfInvolvedConnections + ".");
        }
        if(!DataBaseConnectionPool.driverIsLoaded)
        {
            DataBaseConnectionPool.loadDriver(nameOfClassOfDriver);
        }
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final PoolFuller poolFuller = new PoolFuller(urlOfDataBase, user, password, amountOfInvolvedConnections);
        final Future<BlockingQueue<Connection>> holderOfAvailableConnections = executorService.submit(poolFuller);
        return new DataBaseConnectionPool(holderOfAvailableConnections);
    }

    private static final DataBaseConnectionPoolValidator DATA_BASE_CONNECTION_POOL_VALIDATOR
            = new DataBaseConnectionPoolValidator();
    private static boolean driverIsLoaded = false;

    private static void loadDriver(final String nameOfClassOfDriver)
            throws DataBasePoolConnectionCreatingException
    {
        try
        {
            Class.forName(nameOfClassOfDriver);
            DataBaseConnectionPool.driverIsLoaded = true;
        }
        catch(final ClassNotFoundException cause)
        {
            throw new DataBasePoolConnectionCreatingException(cause);
        }
    }

    private static final class PoolFuller implements Callable<BlockingQueue<Connection>>
    {
        private final String urlOfDataBase;
        private final String user;
        private final String password;
        private final int amountOfFulledConnections;

        public PoolFuller(final String urlOfDataBase, final String user,
                          final String password, final int amountOfFulledConnections)
        {
            this.urlOfDataBase = urlOfDataBase;
            this.user = user;
            this.password = password;
            this.amountOfFulledConnections = amountOfFulledConnections;
        }

        @Override
        public final BlockingQueue<Connection> call()
        {
            final BlockingQueue<Connection> connections = new ArrayBlockingQueue<Connection>(
                    this.amountOfFulledConnections);
            try
            {
                Connection currentFulledConnection;
                for(int i = 0; i < this.amountOfFulledConnections; i++)
                {
                    currentFulledConnection = DriverManager.getConnection(this.urlOfDataBase, this.user, this.password);
                    connections.add(currentFulledConnection);
                }
                return connections;
            }
            catch(final SQLException cause)
            {
                final DataBaseConnectionPoolFullingException mainException
                        = new DataBaseConnectionPoolFullingException(cause);
                if(!connections.isEmpty())
                {
                    try
                    {
                        for(Connection connection : connections)
                        {
                            connection.close();
                        }
                    }
                    catch(final SQLException exceptionOfCloseConnection)
                    {
                        mainException.addSuppressed(exceptionOfCloseConnection);
                    }
                }
                throw mainException;
            }
        }
    }

    public final int findAmountOfAvailableConnections()
    {
        try
        {
            return this.holderOfAvailableConnections.get().size();
        }
        catch(final ExecutionException | InterruptedException cause)
        {
            throw new DataBaseConnectionPoolFullingException(cause);
        }
    }

    public final Connection findAvailableConnection()
            throws DataBaseConnectionPoolAccessConnectionException
    {
        try
        {
            final BlockingQueue<Connection> availableConnection = this.holderOfAvailableConnections.get();
            final Connection foundConnection = availableConnection.poll(
                    DataBaseConnectionPool.AMOUNT_OF_UNITS_OF_WAITING_OF_CONNECTION,
                    DataBaseConnectionPool.TIME_UNIT_OF_WAITING_OF_CONNECTION);
            if(foundConnection == null)
            {
                throw new DataBaseConnectionPoolAccessConnectionException("Trying of getting connection from connection"
                        + " pool is very long.");
            }
            return foundConnection;
        }
        catch(final ExecutionException cause)
        {
            throw new DataBaseConnectionPoolFullingException(cause);
        }
        catch(final InterruptedException cause)
        {
            throw new DataBaseConnectionPoolAccessConnectionException(cause);
        }
    }

    private static final long AMOUNT_OF_UNITS_OF_WAITING_OF_CONNECTION = 5;
    private static final TimeUnit TIME_UNIT_OF_WAITING_OF_CONNECTION = TimeUnit.SECONDS;

    public final void returnConnectionToPool(final Connection returnedConnection)
    {
        try
        {
            final BlockingQueue<Connection> availableConnections = this.holderOfAvailableConnections.get();
            availableConnections.add(returnedConnection);
        }
        catch(final InterruptedException | ExecutionException cause)
        {
            throw new DataBaseConnectionPoolFullingException(cause);
        }
    }

    @Override
    public final void close() throws IOException
    {
        try
        {
            final BlockingQueue<Connection> availableConnections = this.holderOfAvailableConnections.get();
            for(final Connection closedConnection : availableConnections)
            {
                closedConnection.close();
            }
        }
        catch(final ExecutionException | InterruptedException cause)
        {
            throw new DataBaseConnectionPoolFullingException(cause);
        }
        catch(final SQLException cause)
        {
            throw new IOException(cause);
        }
    }
}


