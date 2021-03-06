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
        LOW("Low"),
        HIGHRANGE("HighRange"),
        LOWRANGE("LowRange"),
        INCREASE("Increase"),
        DECREASE("Decrease"),
        STABLE("Stable"),
        TEMPSIM("TempSim"),
        ERROR("error");

        String name;
        ControlType(String name){
            this.name = name;
        }
        public String getString(){
            return name;
        }

        public static ControlType getControlType(String name){
            for(ControlType type : ControlType.values()){
                if(type.name.equals(name)){
                    return type;
                }
            }
            return ERROR;
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
        System.out.println("Toggled Basics");
    }

    public static int checkValue(Connection con, ControlType type){
        int value = 0;
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from controls where control_type='"+type.getString()+"'");
            if(rs.next()){
                value =  rs.getInt("control_val");
            }
            con.commit();
            stmt.close();
        }catch(Exception ex){
            System.out.println("Check Value for "+type+": "+ex.getMessage());
        }
        System.out.println(value);
        return value;
    }

    public static String checkText(Connection con, ControlType type){
        String text = "";
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from controls where control_type='"+type.getString()+"'");
            if(rs.next()){
                text =  rs.getString("control_text");
            }
            con.commit();
            stmt.close();
        }catch(Exception ex){
            System.out.println("Check Value for "+type+": "+ex.getMessage());
        }
        System.out.println(text);
        return text;
    }

    public static void updateValue(Connection con, ControlType type, int value){
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Update controls set control_val="+value+" WHERE control_type='"+type.getString()+"'");
            con.commit();
            stmt.close();
        }catch(Exception ex) {
            System.out.println("Update Value for " + type + ": " + ex.getMessage());
        }
    }

    public static void updateText(Connection con, ControlType type, String text){
        try{
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("Update controls set control_text='"+text+"' WHERE control_type='"+type.getString()+"'");
            con.commit();
            stmt.close();
        }catch(Exception ex){
            System.out.println("Update Text for " + type + ": " + ex.getMessage());
        }
    }

}