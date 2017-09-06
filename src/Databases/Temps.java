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
            System.out.println("Removed Old Date: " + removeOldData(connection));
            System.out.println("Uploaded Data: " + upLoadData(connection,tempDataGenerator()));
            ExDatabase.close(connection);
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
        long currentTime = System.currentTimeMillis();

        for(int ii = 0; ii < 100; ii++){
            tempData[ii][0] = new SimpleDateFormat("yyyy-MM-dd  HH.mm.ss").format(new Date(currentTime + 1000*ii+ 5*3600000));
            tempData[ii][1] = Integer.toString(random.nextInt(73)-10);
        }
        for(int ii = 100; ii < 200; ii++){
            tempData[ii][0] = new SimpleDateFormat("yyyy-MM-dd  HH.mm.ss").format(new Date(currentTime + 1000*ii+ 5*3600000));
            tempData[ii][1] = "null";
        }
        for(int ii = 200; ii < 300; ii++){
            tempData[ii][0] = new SimpleDateFormat("yyyy-MM-dd  HH.mm.ss").format(new Date(currentTime + 1000*ii+ 5*3600000));
            tempData[ii][1] = Integer.toString(random.nextInt(73)-10);
        }
        System.out.println("Date: " + tempData[0][0]);
        System.out.println("Date: " + tempData[10][0]);
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
                    "('" + tempData[ii][0]+ "'," + tempData[ii][1] + ", 'Temps');";
                stat.executeUpdate(sql);
            }//end for
            stat.close();
            con.commit();
            flag = true;
        }
        catch(Exception ex){
            System.err.println("UploadData: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            flag = false;

        }
        return flag;

    }//end upLoadData

    private static boolean removeOldData(Connection con){
        boolean removed = false;
        Statement stmt = null;

        try{
            con.setAutoCommit(false);
            stmt = con.createStatement();
            Date currentTime = new Date(System.currentTimeMillis());
            String sql = "Select * FROM temps WHERE temp_datetime<'"+currentTime+"';";
            ResultSet rs = stmt.executeQuery(sql);

            int count = 0;

            String sqlRemove="";
            while(rs.next()){
                if(count==0){
                    sqlRemove ="DELETE FROM temps WHERE temp_id IN (";
                    count++;
                }else {
                    sqlRemove = sqlRemove + rs.getString("temp_id") + ",";
                }
            }
            if(count==1) {
                sqlRemove = sqlRemove.substring(0, sqlRemove.length() - 1) + ");";
                stmt.executeUpdate(sqlRemove);
                stmt.close();
            }
            con.commit();
            removed = true;
        }catch(Exception ex){
            System.err.println("Remove Old Data: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return removed;
    }

}

