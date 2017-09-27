package Databases;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controls {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = ExDatabase.open();
            //System.out.println("Removed Old Date: " + removeOldData(connection));
            //System.out.println("Uploaded Data: " + upLoadData(connection,tempDataGenerator()));
            ExDatabase.close(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Controls() {}

    public enum ControlType{
        DISPLAY("Display"),
        PROBE("Probe"),
        CHECKINGSERVICE("CheckingService"),
        HIGH("High"),
        LOW("Low");

        String name;
        ControlType(String name){
            this.name = name;
        }

        public String getString(){
            return name;
        }
    }

    public static void toggleFor(Connection con, ControlType type){
        try{
            con.setAutoCommit(false);
            updateValue(con,type,checkValue(con,type)==0 ? 1: 0);
            con.commit();
        }catch(Exception ex){
            System.out.println("Toggle Display: "+ex.getMessage());
        }
    }

    public static int checkValue(Connection con, ControlType type){
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select control_val from controls where control_type="+type.getString());
            rs.next();
            if(rs.next()){
                return rs.getInt("control_val");
            }
            con.commit();
            stmt.close();
        }catch(Exception ex){
            System.out.println("Check Value for "+type+": "+ex.getMessage());
        }
        return 0;
    }

    public static void updateValue(Connection con, ControlType type, int value){
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Update control set control_val="+value+" WHERE control_type='"+type.getString()+"'");
            con.commit();
            stmt.close();
        }catch(Exception ex) {
            System.out.println("Update Value for " + type + ": " + ex.getMessage());
        }
    }

}