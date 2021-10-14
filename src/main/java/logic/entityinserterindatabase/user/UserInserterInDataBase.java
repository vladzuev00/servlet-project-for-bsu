package logic.entityinserterindatabase.user;

import entity.User;
import logic.cryptographer.Cryptographer;
import logic.cryptographer.stringcryptographer.StringCryptographer;
import logic.entityinserterindatabase.EntityInserterInDataBase;
import logic.entityinserterindatabase.exception.EntityInsertionInDataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class UserInserterInDataBase implements EntityInserterInDataBase<User>
{
    private final Cryptographer<String, String> passwordCryptographer;

    public UserInserterInDataBase()
    {
        super();
        this.passwordCryptographer = new StringCryptographer();
    }

    @Override
    public final void insertEntity(final User insertedUser, final Connection connection)
            throws EntityInsertionInDataBaseException
    {
        try
        {
            final PreparedStatement preparedStatement = connection.prepareStatement(
                    UserInserterInDataBase.DESCRIPTION_OF_PREPARED_STATEMENT_OF_INSERTING_USER_IN_DATA_BASE);

            preparedStatement.setLong(UserInserterInDataBase.INDEX_OF_PARAMETER_OF_USER_ID_IN_PREPARED_STATEMENT,
                    insertedUser.getId());
            preparedStatement.setString(UserInserterInDataBase.INDEX_OF_PARAMETER_OF_USER_NAME_IN_PREPARED_STATEMENT,
                    insertedUser.getName());
            preparedStatement.setString(UserInserterInDataBase.INDEX_OF_PARAMETER_OF_USER_SURNAME_IN_PREPARED_STATEMENT,
                    insertedUser.getSurname());
            preparedStatement.setString(UserInserterInDataBase.INDEX_OF_PARAMETER_OF_USER_PATRONYMIC_IN_PREPARED_STATEMENT,
                    insertedUser.getPatronymic());
            preparedStatement.setString(UserInserterInDataBase.INDEX_OF_PARAMETER_OF_USER_EMAIL_IN_PREPARED_STATEMENT,
                    insertedUser.getEmail());
            final String encryptedPasswordOfCurrentUser = this.passwordCryptographer.encrypt(insertedUser.getPassword());
            preparedStatement.setString(
                    UserInserterInDataBase.INDEX_OF_PARAMETER_OF_USER_ENCRYPTED_PASSWORD_IN_PREPARED_STATEMENT,
                    encryptedPasswordOfCurrentUser);

            preparedStatement.executeUpdate();
        }
        catch(final SQLException cause)
        {
            throw new EntityInsertionInDataBaseException(cause);
        }
    }

    private static final String DESCRIPTION_OF_PREPARED_STATEMENT_OF_INSERTING_USER_IN_DATA_BASE
            = "INSERT INTO users (user_id, name, surname, patronymic, email, encrypted_password, role_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, (SELECT user_role_id FROM user_role WHERE user_role_name = 'general_user'));";
    private static final int INDEX_OF_PARAMETER_OF_USER_ID_IN_PREPARED_STATEMENT = 1;
    private static final int INDEX_OF_PARAMETER_OF_USER_NAME_IN_PREPARED_STATEMENT = 2;
    private static final int INDEX_OF_PARAMETER_OF_USER_SURNAME_IN_PREPARED_STATEMENT = 3;
    private static final int INDEX_OF_PARAMETER_OF_USER_PATRONYMIC_IN_PREPARED_STATEMENT = 4;
    private static final int INDEX_OF_PARAMETER_OF_USER_EMAIL_IN_PREPARED_STATEMENT = 5;
    private static final int INDEX_OF_PARAMETER_OF_USER_ENCRYPTED_PASSWORD_IN_PREPARED_STATEMENT = 6;
}
