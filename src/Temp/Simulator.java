package Temp;

import Databases.ConstantTemps;
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

@WebServlet(value="/Temp.Simulator")
public class Simulator extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        toggleSimulation();
        if(getSimulation()==1){
            startSimulation();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private static void toggleSimulation(){
        try{
            Connection con = ExDatabase.open();
            Controls.toggleFor(con,Controls.ControlType.TEMPSIM);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Toggle Simulation : " + ex.getMessage());
        }
    }

    private static int getSimulation(){
        int value = 0;
        try{
            Connection con = ExDatabase.open();
            value = Controls.checkValue(con, Controls.ControlType.TEMPSIM);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Check Simulation : " + ex.getMessage());
        }
        return value;
    }

    private static void startSimulation(){
        try{
            Connection con = ExDatabase.open();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new ConstantTemps(true));
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Start Simulation : "+ex.getMessage());
        }
    }
}
