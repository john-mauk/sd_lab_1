package Device;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Device.Display")
public class Display extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     String displayOption = request.getParameter("displayFlag");
     if(displayOption!=null)
     {

     }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private static void updateDisplayValue(String option)
    {
        try{

        }
        catch(Exception ex) {
            System.out.println("updateDisplayValue = " + ex.getMessage());
        }
    }


}
