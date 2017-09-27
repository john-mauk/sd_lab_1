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

@WebServlet("/Temp.High")
public class High extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String high = request.getParameter("temp");
        if(high!=null){
            changeHigh(high);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().print(getHigh());
    }

    private static void changeHigh(String high){
        try{
            Connection con = ExDatabase.open();
            Controls.updateValue(con, Controls.ControlType.HIGH,Integer.valueOf(high));
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Change High : " + ex.getMessage());
        }
    }

    private static int getHigh(){
        int temp = 0;
        try{
            Connection con = ExDatabase.open();
            temp = Controls.checkValue(con, Controls.ControlType.HIGH);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Get High : " + ex.getMessage());
        }
        return temp;
    }
}
