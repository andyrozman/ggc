package ggc.web.servlets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Andy
 *
 */
public class GGCNetworkServlet extends HttpServlet 
{
    /**
     * 
     */
    private static final long serialVersionUID = -7296357160678571419L;

    /**
     * doGet
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        
        DataInputStream in = 
            new DataInputStream((InputStream)request.getInputStream());
    
    String text = in.readUTF();
    
    System.out.println("Data Read: \n" + text);
    
    String message;
    try {
        message = "100 ok";
    } catch (Throwable t) {
        message = "200 " + t.toString();
    }
    response.setContentType("text/plain");
    response.setContentLength(message.length());
    PrintWriter out = response.getWriter();
    out.println(message);
    in.close();
    out.close();
    out.flush();        
        /*
        response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String title = "Reading Three Request Parameters";
    out.println(ServletUtilities.headWithTitle(title) +
                "<BODY>\n" +
                "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
                "<UL>\n" +
                "  <LI>param1: "
                + request.getParameter("param1") + "\n" +
                "  <LI>param2: "
                + request.getParameter("param2") + "\n" +
                "  <LI>param3: "
                + request.getParameter("param3") + "\n" +
                "</UL>\n" +
                "</BODY></HTML>");
                */
  }

    /** 
     * doPost
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        doGet(request, response);
    }
}
