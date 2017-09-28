package Device;

import Databases.Controls;
import Databases.ExDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/Device.Display")
public class Display extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     String displayOption = request.getParameter("displayFlag");
     toggleForDisplay();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().print(getDisplay());
    }
    private static void toggleForDisplay()
    {
        try{
            Connection con = ExDatabase.open();
            Controls.toggleFor(con, Controls.ControlType.DISPLAY);
            ExDatabase.close(con);
        }
        catch(Exception ex) {
            System.out.println("updateDisplayValue = " + ex.getMessage());
        }
    }
    private static int getDisplay()
    {
        int displayValue = 0;
        try{
            Connection con = ExDatabase.open();
            displayValue = Controls.checkValue(con,Controls.ControlType.DISPLAY);
            ExDatabase.close(con);
        }
        catch(Exception ex)
        {
            System.out.println("Get Display: " + ex.getMessage());
        }
        return displayValue;
    }


}
