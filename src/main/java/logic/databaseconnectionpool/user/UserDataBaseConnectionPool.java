package logic.databaseconnectionpool.user;

import logic.databaseconnectionpool.DataBaseConnectionPool;
import logic.databaseconnectionpool.exception.DataBaseConnectionPoolAccessConnectionException;
import logic.databaseconnectionpool.exception.DataBasePoolConnectionCreatingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Properties;

public final class UserDataBaseConnectionPool implements AutoCloseable        //TODO: find way to close singleton
{
    private final DataBaseConnectionPool dataBaseConnectionPool;

    private UserDataBaseConnectionPool(final DataBaseConnectionPool dataBaseConnectionPool)
    {
        this.dataBaseConnectionPool = dataBaseConnectionPool;
    }

    public static UserDataBaseConnectionPool getUserDataBaseConnectionPool()
    {
        if(UserDataBaseConnectionPool.userDataBaseConnectionPool == null)
        {
            synchronized(UserDataBaseConnectionPool.class)
            {
                if(UserDataBaseConnectionPool.userDataBaseConnectionPool == null)
                {
                    UserDataBaseConnectionPool.userDataBaseConnectionPool = UserDataBaseConnectionPool
                            .createUserDataBaseConnectionPool();
                }
            }
        }
        return UserDataBaseConnectionPool.userDataBaseConnectionPool;
    }

    private static UserDataBaseConnectionPool userDataBaseConnectionPool = null;

    private static UserDataBaseConnectionPool createUserDataBaseConnectionPool()
    {
        try
        {
            final Properties dataBaseProperties = new Properties();
            try(final InputStream inputStream = Files.newInputStream(Paths.get(
                    UserDataBaseConnectionPool.PATH_OF_FILE_OF_DATA_BASE_PROPERTIES)))
            {
                dataBaseProperties.load(inputStream);
            }

            final String urlOfDataBase = dataBaseProperties.getProperty(
                    UserDataBaseConnectionPool.NAME_OF_PROPERTY_OF_URL_OF_DATA_BASE);
            final String userOfDataBase = dataBaseProperties.getProperty(
                    UserDataBaseConnectionPool.NAME_OF_PROPERTY_OF_USER_OF_DATA_BASE);
            final String passwordOfDataBase = dataBaseProperties.getProperty(
                    UserDataBaseConnectionPool.NAME_OF_PROPERTY_OF_PASSWORD_OF_DATA_BASE);

            final DataBaseConnectionPool dataBaseConnectionPool = DataBaseConnectionPool.createDataBaseConnectionPool(
                    UserDataBaseConnectionPool.NAME_OF_CLASS_OF_DRIVER, urlOfDataBase, userOfDataBase, passwordOfDataBase);
            return new UserDataBaseConnectionPool(dataBaseConnectionPool);
        }
        catch(final IOException cause)
        {
            throw new DataBasePoolConnectionCreatingException(cause);
        }
    }
    //TODO: remake path
    private static final String PATH_OF_FILE_OF_DATA_BASE_PROPERTIES = "C:\\Users\\Ania\\Desktop\\learning\\java\\универ\\2 курс 4 семестр\\labs\\решения\\seventh_lab_servlet\\src\\main\\resources\\user_database\\user_database.properties";
    private static final String NAME_OF_PROPERTY_OF_URL_OF_DATA_BASE = "user_database.url";
    private static final String NAME_OF_PROPERTY_OF_USER_OF_DATA_BASE = "user_database.user";
    private static final String NAME_OF_PROPERTY_OF_PASSWORD_OF_DATA_BASE = "user_database.password";
    private static final String NAME_OF_CLASS_OF_DRIVER = "org.postgresql.Driver";

    public final int findAmountOfAvailableConnections()
    {
        return this.dataBaseConnectionPool.findAmountOfAvailableConnections();
    }

    public final Connection findAvailableConnection()
            throws DataBaseConnectionPoolAccessConnectionException
    {
        return this.dataBaseConnectionPool.findAvailableConnection();
    }

    public final void returnConnectionToPool(final Connection returnedConnection)
    {
        this.dataBaseConnectionPool.returnConnectionToPool(returnedConnection);
    }

    @Override
    public final void close() throws IOException
    {
        this.dataBaseConnectionPool.close();
    }
}
