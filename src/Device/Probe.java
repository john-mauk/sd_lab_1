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

@WebServlet("/Device.Probe")
public class Probe extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        toggleProbe();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().print(checkProbe());
    }

    private static void toggleProbe(){
        try{
            Connection con = ExDatabase.open();
            Controls.toggleFor(con,Controls.ControlType.PROBE);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Toggle Prove : "+ex.getMessage());
        }
    }

    private static int checkProbe(){
        int probe = 0;
        try{
            Connection con = ExDatabase.open();
            probe = Controls.checkValue(con,Controls.ControlType.PROBE);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Check Probe : " + ex.getMessage());
        }
        return probe;
    }

}
