package Temp;

import Databases.Controls;
import Databases.ExDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/Temp.Low")
public class Low extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String low = request.getParameter("temp");
        if(low !=null){
            ChangeLow(low);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().print(getLow());
    }
    private static void ChangeLow(String low)
    {
        try{
           Connection con = ExDatabase.open();
            Controls.updateValue(con,Controls.ControlType.LOW, Integer.valueOf(low));
            ExDatabase.close(con);
        }
        catch(Exception ex)
        {
            System.out.println("Change Low: " + ex.getMessage());
        }

    }
    private static int getLow()
    {
        int tempLow = 0;
        try{
            Connection con = ExDatabase.open();
            tempLow = Controls.checkValue(con,Controls.ControlType.LOW);
            ExDatabase.close(con);
        }
        catch(Exception ex)
        {
            System.out.println("Get Low: " + ex.getMessage());
        }
        return tempLow;
    }
}
