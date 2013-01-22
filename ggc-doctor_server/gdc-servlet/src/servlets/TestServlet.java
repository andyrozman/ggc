package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Attendee;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        ArrayList<Attendee> lst = new ArrayList<Attendee>();
        lst.add(new Attendee("Johnaes Schrooder", "", ""));
        lst.add(new Attendee("Alex Rozmanr", "", ""));
        lst.add(new Attendee("Diana Ross", "", ""));
        lst.add(new Attendee("Michael Jackson", "", ""));
        
        
        Collections.sort(lst);
        
        try
        {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegistrationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            for(Attendee a : lst)
            {
                
                out.println("<h3>" + a.getName() + " </h3>");
                
            }
            
            
            
            ///Attendee attendee = registrationManager.register("  Bill Schroder  ", "  Manager", "  Acme Software");
            //out.println("<h3>" + attendee.getName() + " has been registered</h3>");
            out.println("</body>");
            out.println("</html>");

        }
        finally
        {
            out.close();
        }
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
