package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Attendee;
import beans.RegistrationManager;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @EJB
    RegistrationManager registrationManager;

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        
        
        try
        {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegistrationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            Attendee attendee = registrationManager.register("  Bill Schroder  ", "  Manager", "  Acme Software");
            out.println("<h3>" + attendee.getName() + " has been registered</h3>");
            out.println("</body>");
            out.println("</html>");
            
        }
        finally
        {
            out.close();
        }
    }

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationServlet()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        processRequest(request, response);
    }

}
