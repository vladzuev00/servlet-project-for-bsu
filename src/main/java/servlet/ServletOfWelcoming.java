package servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class ServletOfWelcoming extends HttpServlet
{
    public ServletOfWelcoming()
    {
        super();
    }

    @Override
    protected final void doGet(final HttpServletRequest httpServletRequest,
                               final HttpServletResponse httpServletResponse)
            throws IOException, ServletException
    {
        httpServletResponse.setContentType(ServletOfWelcoming.CONTENT_TYPE_OF_RESPONSE);
        httpServletResponse.setCharacterEncoding(ServletOfWelcoming.CHARACTER_ENCODING_OF_RESPONSE);

        final ServletContext servletContext = this.getServletContext();
        final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(
                ServletOfWelcoming.PATH_OF_FILE_OF_WELCOMING_PAGE);
        requestDispatcher.forward(httpServletRequest, httpServletResponse);
    }

    private static final String CONTENT_TYPE_OF_RESPONSE = "text/html";
    private static final String CHARACTER_ENCODING_OF_RESPONSE = "UTF-8";
    private static final String PATH_OF_FILE_OF_WELCOMING_PAGE = "/welcome_page/welcome_page.html";
}
