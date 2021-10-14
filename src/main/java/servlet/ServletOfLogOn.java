package servlet;

import entity.User;
import logic.databaseconnectionpool.exception.DataBaseConnectionPoolAccessConnectionException;
import logic.databaseconnectionpool.user.UserDataBaseConnectionPool;
import logic.useridentifierfromdatabase.UserIdentifierFromDataBase;
import logic.useridentifierfromdatabase.exception.UserIdentifyingFromDataBaseException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;

public final class ServletOfLogOn extends HttpServlet
{
    private final UserDataBaseConnectionPool userDataBaseConnectionPool;
    private final UserIdentifierFromDataBase userIdentifierFromDataBase;

    public ServletOfLogOn()
    {
        super();
        this.userDataBaseConnectionPool = UserDataBaseConnectionPool.getUserDataBaseConnectionPool();
        this.userIdentifierFromDataBase = new UserIdentifierFromDataBase();
    }

    @Override
    protected final void doGet(final HttpServletRequest httpServletRequest,
                               final HttpServletResponse httpServletResponse)
            throws ServletException, IOException
    {
        httpServletResponse.setContentType(ServletOfLogOn.CONTENT_TYPE_OF_RESPONSE);
        httpServletResponse.setCharacterEncoding(ServletOfLogOn.CHARACTER_ENCODING_OF_RESPONSE);

        final ServletContext servletContext = this.getServletContext();
        final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(
                ServletOfLogOn.PATH_OF_FILE_OF_LOG_ON_PAGE);
        requestDispatcher.forward(httpServletRequest, httpServletResponse);
    }

    private static final String CONTENT_TYPE_OF_RESPONSE = "text/html";
    private static final String CHARACTER_ENCODING_OF_RESPONSE = "UTF-8";
    private static final String PATH_OF_FILE_OF_LOG_ON_PAGE = "/log_on_page/log_on_page.html";

    @Override
    protected final void doPost(final HttpServletRequest httpServletRequest,
                                final HttpServletResponse httpServletResponse)
            throws ServletException, IOException
    {
        final String inputtedEmail = httpServletRequest.getParameter(
                ServletOfLogOn.PARAMETER_NAME_OF_USER_EMAIL_OF_HTTP_REQUEST).trim();
        final String inputtedPassword = httpServletRequest.getParameter(
                ServletOfLogOn.PARAMETER_NAME_OF_USER_PASSWORD_OF_HTTP_REQUEST).trim();

        Connection connection = null;
        try
        {
            connection = this.userDataBaseConnectionPool.findAvailableConnection();
            final User currentUser = this.userIdentifierFromDataBase.identifyUser(
                    connection, inputtedEmail, inputtedPassword);
            final HttpSession httpSession = httpServletRequest.getSession();
            httpSession.setAttribute(ServletOfLogOn.SESSION_ATTRIBUTE_NAME_OF_CURRENT_USER, currentUser);

            final ServletContext servletContext = this.getServletContext();
            final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(
                    ServletOfLogOn.PATH_OF_FILE_OF_USER_INFORMATION_PAGE);
            requestDispatcher.forward(httpServletRequest, httpServletResponse);
        }
        catch(final DataBaseConnectionPoolAccessConnectionException | UserIdentifyingFromDataBaseException cause)
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

    private static final String PARAMETER_NAME_OF_USER_EMAIL_OF_HTTP_REQUEST = "user_email";
    private static final String PARAMETER_NAME_OF_USER_PASSWORD_OF_HTTP_REQUEST = "user_password";
    private static final String SESSION_ATTRIBUTE_NAME_OF_CURRENT_USER = "current_user";
    private static final String PATH_OF_FILE_OF_USER_INFORMATION_PAGE
            = "/user_information_page/user_information_page.jsp";
}
