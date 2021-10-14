package logic.useridentifierfromdatabase;

import entity.User;
import logic.cryptographer.Cryptographer;
import logic.cryptographer.stringcryptographer.StringCryptographer;
import logic.useridentifierfromdatabase.exception.UserIdentifyingFromDataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class UserIdentifierFromDataBase
{
    private final Cryptographer<String, String> passwordCryptographer;

    public UserIdentifierFromDataBase()
    {
        super();
        this.passwordCryptographer = new StringCryptographer();
    }

    public final User identifyUser(final Connection connection, final String email, final String password)
            throws UserIdentifyingFromDataBaseException
    {
        try
        {
            final PreparedStatement preparedStatement = connection.prepareStatement(
                    UserIdentifierFromDataBase.DESCRIPTION_OF_PREPARED_STATEMENT_TO_IDENTIFYING_USER);
            preparedStatement.setString(
                    UserIdentifierFromDataBase.INDEX_OF_PARAMETER_OF_USER_EMAIL_IN_PREPARED_STATEMENT, email);
            final ResultSet resultSet = preparedStatement.executeQuery();       //result set contains of only one user, because email of user is unique

            if(!resultSet.next())
            {
                throw new UserIdentifyingFromDataBaseException(
                        "User with email '" + email + "' hasn't been registered.");
            }

            final String encryptedPasswordOfIdentifiedUser = resultSet.getString(
                    UserIdentifierFromDataBase.NAME_OF_COLUMN_OF_USER_ENCRYPTED_PASSWORD);
            final String decryptedPasswordOfIdentifiedUser = this.passwordCryptographer
                    .decrypt(encryptedPasswordOfIdentifiedUser);
            if(!password.equals(decryptedPasswordOfIdentifiedUser))
            {
                throw new UserIdentifyingFromDataBaseException("Password '" + password + "' is wrong");
            }

            final long idOfIdentifiedUser = resultSet.getLong(UserIdentifierFromDataBase.NAME_OF_COLUMN_OF_USER_ID);
            final String nameOfIdentifiedUser = resultSet.getString(
                    UserIdentifierFromDataBase.NAME_OF_COLUMN_OF_USER_NAME);
            final String surnameOfIdentifiedUser = resultSet.getString(
                    UserIdentifierFromDataBase.NAME_OF_COLUMN_OF_USER_SURNAME);
            final String patronymicOfIdentifiedUser = resultSet.getString(
                    UserIdentifierFromDataBase.NAME_OF_COLUMN_OF_USER_PATRONYMIC);

            final String nameOfRoleOfIdentifiedUser = resultSet.getString(
                    UserIdentifierFromDataBase.NAME_OF_COLUMN_OF_USER_ROLE_NAME);
            final User.Role roleOfIdentifiedUser = UserIdentifierFromDataBase
                    .identifyUserRole(nameOfRoleOfIdentifiedUser);

            return new User(idOfIdentifiedUser, nameOfIdentifiedUser, surnameOfIdentifiedUser,
                    patronymicOfIdentifiedUser, email, password, roleOfIdentifiedUser);
        }
        catch(final SQLException cause)
        {
            throw new UserIdentifyingFromDataBaseException(cause);
        }
    }

    private static final String DESCRIPTION_OF_PREPARED_STATEMENT_TO_IDENTIFYING_USER
            = "SELECT users.user_id, users.name, users.surname, users.patronymic, users.email, "
            + "users.encrypted_password, user_role.user_role_name "
            + "FROM users INNER JOIN user_role "
            + "ON users.role_id = user_role.user_role_id "
            + "WHERE users.email = ?;";
    private static final int INDEX_OF_PARAMETER_OF_USER_EMAIL_IN_PREPARED_STATEMENT = 1;

    private static final String NAME_OF_COLUMN_OF_USER_ENCRYPTED_PASSWORD = "encrypted_password";
    private static final String NAME_OF_COLUMN_OF_USER_ID = "user_id";
    private static final String NAME_OF_COLUMN_OF_USER_NAME = "name";
    private static final String NAME_OF_COLUMN_OF_USER_SURNAME = "surname";
    private static final String NAME_OF_COLUMN_OF_USER_PATRONYMIC = "patronymic";
    private static final String NAME_OF_COLUMN_OF_USER_ROLE_NAME = "user_role_name";

    private static User.Role identifyUserRole(final String nameOfUserRoleInDataBase)
    {
        return UserIdentifierFromDataBase.MAP_OF_ROLE_NAMES_IN_DATA_BASE_TO_ROLES.get(nameOfUserRoleInDataBase);
    }

    private static final Map<String, User.Role> MAP_OF_ROLE_NAMES_IN_DATA_BASE_TO_ROLES = new HashMap<String, User.Role>()
    {
        {
            this.put(UserIdentifierFromDataBase.NAME_OF_GENERAL_ROLE_IN_DATA_BASE, User.Role.GENERAL_USER);
            this.put(UserIdentifierFromDataBase.NAME_OF_ADMIN_ROLE_IN_DATA_BASE, User.Role.ADMIN_USER);
        }
    };

    private static final String NAME_OF_GENERAL_ROLE_IN_DATA_BASE = "general_user";
    private static final String NAME_OF_ADMIN_ROLE_IN_DATA_BASE = "admin_user";
}
