package Databases;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Date;
public class Temps {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = ExDatabase.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Temps() {
    }

    //temp data generator
    private static String[][] tempDataGenerator(){
        String tempData [][] = new String [300][2];
        Random random = new Random();

        for(int ii = 0; ii < 300; ii++){
            tempData[ii][0] = new SimpleDateFormat("yyyy-MM-dd  HH.mm.ss").format(new Date(System.currentTimeMillis() + 1000*ii));
            tempData[ii][1] = Integer.toString(random.nextInt(73)-10);
        }
        return tempData;
    }//end tempDataGenerator

    private static boolean upLoadData(Connection con, String[][] tempData){
        boolean flag = false;
        Statement stat = null;
        try{
            con.setAutoCommit(false);
            stat = con.createStatement();
            for(int ii = 0; ii < 300; ii++){
                String sql = "INSERT into temps(temp_datetime, temp_c_val, temp_series_name) VALUES" +
                    "('" + tempData[ii][0]+ "'," + tempData[ii][1] + "', testing);";
                stat.executeUpdate(sql);
            }//end for
            stat.close();
            con.commit();
            flag = true;
        }
        catch(Exception ex){
            System.err.println("UploadData: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);

        }
        return flag;

    }//end upLoadData

}

