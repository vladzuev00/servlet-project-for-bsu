package servlet;

import entity.User;
import logic.databaseconnectionpool.exception.DataBaseConnectionPoolAccessConnectionException;
import logic.databaseconnectionpool.user.UserDataBaseConnectionPool;
import logic.entityinserterindatabase.EntityInserterInDataBase;
import logic.entityinserterindatabase.exception.EntityInsertionInDataBaseException;
import logic.entityinserterindatabase.user.UserInserterInDataBase;
import logic.idgenerator.IdGenerator;
import logic.idgenerator.exception.IdGeneratorCreatingException;
import logic.idgenerator.exception.IdGeneratorGenerationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

public final class ServletOfRegistration extends HttpServlet
{
    private IdGenerator idGenerator;
    private final UserDataBaseConnectionPool userDataBaseConnectionPool;
    private final EntityInserterInDataBase<User> userInserterInDataBase;

    public ServletOfRegistration()
    {
        super();
        this.userDataBaseConnectionPool = UserDataBaseConnectionPool.getUserDataBaseConnectionPool();
        this.userInserterInDataBase = new UserInserterInDataBase();
    }

    @Override
    public final void init()
            throws ServletException
    {
        try
        {
            this.idGenerator = IdGenerator.createIdGenerator();
        }
        catch(final IdGeneratorCreatingException cause)
        {
            throw new ServletException(cause);
        }
    }

    @Override
    protected final void doGet(final HttpServletRequest httpServletRequest,
                               final HttpServletResponse httpServletResponse)
            throws IOException, ServletException
    {
        httpServletResponse.setContentType(ServletOfRegistration.CONTENT_TYPE_OF_RESPONSE);
        httpServletResponse.setCharacterEncoding(ServletOfRegistration.CHARACTER_ENCODING_OF_RESPONSE);

        final ServletContext servletContext = this.getServletContext();
        final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(
                ServletOfRegistration.PATH_OF_FILE_OF_REGISTRATION_PAGE);
        requestDispatcher.forward(httpServletRequest, httpServletResponse);
    }

    private static final String CONTENT_TYPE_OF_RESPONSE = "text/html";
    private static final String CHARACTER_ENCODING_OF_RESPONSE = "UTF-8";
    private static final String PATH_OF_FILE_OF_REGISTRATION_PAGE = "/registration_page/registration_page.html";

    @Override
    protected final void doPost(final HttpServletRequest httpServletRequest,
                                final HttpServletResponse httpServletResponse)
            throws ServletException, IOException
    {
        try
        {
            httpServletResponse.setContentType(ServletOfRegistration.CONTENT_TYPE_OF_RESPONSE);
            httpServletResponse.setCharacterEncoding(ServletOfRegistration.CHARACTER_ENCODING_OF_RESPONSE);

            final User currentUser = this.createCurrentUser(httpServletRequest);

            final HttpSession httpSession = httpServletRequest.getSession();
            httpSession.setAttribute(ServletOfRegistration.SESSION_ATTRIBUTE_NAME_OF_CURRENT_USER, currentUser);

            this.insertCurrentUserInDataBase(currentUser);

            final ServletContext servletContext = this.getServletContext();
            final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(
                    ServletOfRegistration.PATH_OF_FILE_OF_COMPLETED_REGISTRATION);
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }
        catch(final IdGeneratorGenerationException cause)
        {
            throw new ServletException(cause);
        }
    }

    private static final String PATH_OF_FILE_OF_COMPLETED_REGISTRATION
            = "/registration_page/completed_registration_page.html";

    private User createCurrentUser(final HttpServletRequest httpServletRequest)
            throws IdGeneratorGenerationException
    {
        final long idOfCurrentUser = this.idGenerator.generateId();
        final String nameOfCurrentUser = httpServletRequest.getParameter(
                ServletOfRegistration.PARAMETER_NAME_OF_USER_NAME_OF_HTTP_REQUEST).trim();
        final String surnameOfCurrentUser = httpServletRequest.getParameter(
                ServletOfRegistration.PARAMETER_NAME_OF_USER_SURNAME_OF_HTTP_REQUEST).trim();
        final String patronymicOfCurrentUser = httpServletRequest.getParameter(
                ServletOfRegistration.PARAMETER_NAME_OF_USER_PATRONYMIC_OF_HTTP_REQUEST).trim();
        final String emailOfCurrentUser = httpServletRequest.getParameter(
                ServletOfRegistration.PARAMETER_NAME_OF_USER_EMAIL_OF_HTTP_REQUEST).trim();
        final String passwordOfCurrentUser = httpServletRequest.getParameter(
                ServletOfRegistration.PARAMETER_NAME_OF_USER_PASSWORD_OF_HTTP_REQUEST).trim();
        final User.Role roleOfCurrentUser = User.Role.GENERAL_USER;
        return new User(idOfCurrentUser, nameOfCurrentUser, surnameOfCurrentUser, patronymicOfCurrentUser,
                emailOfCurrentUser, passwordOfCurrentUser, roleOfCurrentUser);
    }

    private static final String PARAMETER_NAME_OF_USER_NAME_OF_HTTP_REQUEST = "user_name";
    private static final String PARAMETER_NAME_OF_USER_SURNAME_OF_HTTP_REQUEST = "user_surname";
    private static final String PARAMETER_NAME_OF_USER_PATRONYMIC_OF_HTTP_REQUEST = "user_patronymic";
    private static final String PARAMETER_NAME_OF_USER_EMAIL_OF_HTTP_REQUEST = "user_email";
    private static final String PARAMETER_NAME_OF_USER_PASSWORD_OF_HTTP_REQUEST = "user_password";

    private static final String SESSION_ATTRIBUTE_NAME_OF_CURRENT_USER = "current_user";

    private void insertCurrentUserInDataBase(final User currentUser)
            throws ServletException
    {
        Connection connection = null;
        try
        {
            connection = this.userDataBaseConnectionPool.findAvailableConnection();
            this.userInserterInDataBase.insertEntity(currentUser, connection);
        }
        catch(final DataBaseConnectionPoolAccessConnectionException | EntityInsertionInDataBaseException cause)
        {
            throw new ServletException(cause);
        }
        finally
        {
            if(connection != null)
            {
                this.userDataBaseConnectionPool.returnConnectionToPool(connection);
            }
        }
    }
}
