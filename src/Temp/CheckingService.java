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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebServlet("/Temp.CheckingService")
public class CheckingService extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int service = Integer.valueOf(request.getParameter("service"));
        if(service!=getCheckingServiceValue()){
            toggleCheckingService();
            if(service==1){
                startChecking();
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().print(getCheckingServiceValue());
    }

    private static int getCheckingServiceValue(){
        int value = 0;
        try{
            Connection con = ExDatabase.open();
            value = Controls.checkValue(con, Controls.ControlType.CHECKINGSERVICE);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Get Checking Service Val : " + ex.getMessage());
        }
        return value;
    }

    private static void toggleCheckingService(){
        try{
            Connection con = ExDatabase.open();
            Controls.toggleFor(con,Controls.ControlType.CHECKINGSERVICE);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Toggle Checking Service : "+ ex.getMessage());
        }
    }

    private static void startChecking(){
        try{
            System.out.println("Starting Thread");
            Connection con = ExDatabase.open();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new CheckingServiceThread());
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Start Checking : "+ex.getMessage());
        }
    }





}
