package entity;

import logic.cryptographer.Cryptographer;
import logic.cryptographer.stringcryptographer.StringCryptographer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

public final class User extends Entity
{
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String password;
    private User.Role role;

    public static enum Role
    {
        NOT_DEFINED, GENERAL_USER, ADMIN_USER
    }

    public User()
    {
        super();
        this.name = User.VALUE_OF_NOT_DEFINED_NAME;
        this.surname = User.VALUE_OF_NOT_DEFINED_SURNAME;
        this.patronymic = User.VALUE_OF_NOT_DEFINED_PATRONYMIC;
        this.email = User.VALUE_OF_NOT_DEFINED_EMAIL;
        this.password = User.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.role = User.Role.NOT_DEFINED;
    }

    private static final String VALUE_OF_NOT_DEFINED_NAME = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_SURNAME = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PATRONYMIC = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_EMAIL = "not defined";
    private static final String VALUE_OF_NOT_DEFINED_PASSWORD = "not defined";

    public User(final long id)
    {
        super(id);
        this.name = User.VALUE_OF_NOT_DEFINED_NAME;
        this.surname = User.VALUE_OF_NOT_DEFINED_SURNAME;
        this.patronymic = User.VALUE_OF_NOT_DEFINED_PATRONYMIC;
        this.email = User.VALUE_OF_NOT_DEFINED_EMAIL;
        this.password = User.VALUE_OF_NOT_DEFINED_PASSWORD;
        this.role = User.Role.NOT_DEFINED;
    }

    public User(final String name, final String surname, final String patronymic,
                final String email, final String password, final User.Role role)
    {
        super();
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(final long id, final String name, final String surname, final String patronymic,
                final String email, final String password, final User.Role role)
    {
        super(id);
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public final void setName(final String name)
    {
        this.name = name;
    }

    public final String getName()
    {
        return this.name;
    }

    public final void setSurname(final String surname)
    {
        this.surname = surname;
    }

    public final String getSurname()
    {
        return this.surname;
    }

    public final void setPatronymic(final String patronymic)
    {
        this.patronymic = patronymic;
    }

    public final String getPatronymic()
    {
        return this.patronymic;
    }

    public final void setEmail(final String email)
    {
        this.email = email;
    }

    public final String getEmail()
    {
        return email;
    }

    public final void setPassword(final String password)
    {
        this.password = password;
    }

    public final String getPassword()
    {
        return this.password;
    }

    public final void setRole(final User.Role role)
    {
        this.role = role;
    }

    public final User.Role getRole()
    {
        return this.role;
    }

    @Override
    public final boolean equals(final Object otherObject)
    {
        if(!super.equals(otherObject))
        {
            return false;
        }
        final User other = (User)otherObject;
        return     Objects.equals(this.name, other.name) && Objects.equals(this.surname, other.name)
                && Objects.equals(this.patronymic, other.patronymic) && Objects.equals(this.email, other.email)
                && Objects.equals(this.password, other.password) && this.role == other.role;
    }

    @Override
    public final int hashCode()
    {
        return super.hashCode() + Objects.hash(this.name, this.surname, this.patronymic,
                this.email, this.password, this.role);
    }

    @Override
    public final String toString()
    {
        return super.toString() + "[name = " + this.name + ", surname = " + surname + ", patronymic = " + patronymic
                + ", email = " + this.email + ", password = " + password + ", role = " + this.role + "]";
    }

    @Override
    public final void writeExternal(final ObjectOutput objectOutput)
            throws IOException
    {
        super.writeExternal(objectOutput);

        objectOutput.writeObject(this.name);
        objectOutput.writeObject(this.surname);
        objectOutput.writeObject(this.patronymic);
        objectOutput.writeObject(this.email);

        final String encryptedPassword = User.CRYPTOGRAPHER.encrypt(this.password);
        objectOutput.writeObject(encryptedPassword);

        objectOutput.writeObject(this.role);
    }

    private static final Cryptographer<String, String> CRYPTOGRAPHER = new StringCryptographer();

    @Override
    public final void readExternal(final ObjectInput objectInput)
            throws IOException, ClassNotFoundException
    {
        super.readExternal(objectInput);

        this.name = (String)objectInput.readObject();
        this.surname = (String)objectInput.readObject();
        this.patronymic = (String)objectInput.readObject();
        this.email = (String)objectInput.readObject();

        final String encryptedPassword = (String)objectInput.readObject();
        this.password = User.CRYPTOGRAPHER.decrypt(encryptedPassword);

        this.role = (User.Role)objectInput.readObject();
    }
}
