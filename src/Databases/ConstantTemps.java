package Databases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ConstantTemps implements Runnable {

    private boolean keepRunning = true;

    public static void main(String[] args){
        //Connection connection = null;
        try {
            //connection = ExDatabase.open();
            //removeOldData(connection);
            ConstantTemps test = new ConstantTemps(true);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConstantTemps(boolean krun){
        keepRunning = krun;
    }

    public void run(){
        Connection con = ExDatabase.open();
        long current = System.currentTimeMillis();
        Date start = new Date(current);
        Random random = new Random();
        int count=0;

        System.out.println(removeOldData(con));
        removeOldData(con);

        while(keepRunning){
            boolean needVal = true;
            while(new Date(current+5*3600000+count*1000).compareTo(new Date(System.currentTimeMillis()+5*3600000+10*1000))<0 && needVal){
                int rand = random.nextInt(83)-20;
                if(rand<-10 || rand>62){
                    constantTempUpload(con,current,"null",count);
                }else{
                    constantTempUpload(con,current,Integer.toString(rand),count);
                }
                if(new Date(current+5*3600000+count*1000).compareTo(new Date(System.currentTimeMillis() + 5*3600000+10*1000))>0){
                    needVal=false;
                }else{
                    count++;
                }
            }
            if(new Date(current+5*3600000+(count-300)*1000).compareTo(new Date(System.currentTimeMillis()+5*3600000)) < 0){
                constantRemoveOld(con,current,count-3000);
            }

            try {
                Thread.sleep(500);
            }catch(Exception ex){
                System.err.println("While sleeping: " + ex.getClass().getName() + ": " + ex.getMessage());
                System.exit(0);
            }
            keepRunning = (Controls.checkValue(con,Controls.ControlType.TEMPSIM)==1)? TRUE:FALSE;
        }
        ExDatabase.close(con);
    }

    private static void constantTempUpload(Connection con, long current, String value, int count){
        String date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date(current+5*3600000+count*1000));

        Statement stmt = null;

        try{
            con.setAutoCommit(false);
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO temps(temp_datetime, temp_c_val,temp_series_name)" +
                    "VALUES('"+date+"',"+value+",'Temps');");
            stmt.close();
            con.commit();
        }catch(Exception ex){
            System.err.println("Constant Temp Upload: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    private static void constantRemoveOld(Connection con, long current, int count){
        Statement stmt = null;
        String date = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date(current+5*3600000+count*1000));

        try{
            con.setAutoCommit(false);
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM temps WHERE temp_datetime<'"+date+"';");
            stmt.close();
            con.commit();
        }catch(Exception ex){
            System.err.println("Constant Removal: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
    }

    private static boolean removeOldData(Connection con){
        boolean removed = false;
        Statement stmt = null;

        try{
            con.setAutoCommit(false);
            stmt = con.createStatement();
            Date currentTime = new Date(System.currentTimeMillis());
            String sql2 = "Select count(*) from temps;";
            ResultSet num = stmt.executeQuery(sql2);

            int count = 0;
            String sqlRemove="";
            num.next();
            if(num.getInt("Count(*)")>1){
                String sql = "Select * FROM temps WHERE temp_datetime<'"+currentTime+"';";
                ResultSet rs = stmt.executeQuery(sql);
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
