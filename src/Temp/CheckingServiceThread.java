package Temp;

import Databases.Contacts;
import Databases.Controls;
import Databases.ExDatabase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CheckingServiceThread implements Runnable {

    private static boolean runStatus;
    private static int low;
    private static int high;
    private static boolean outOfRange;
    private static int lastAlert;
    private static String highRange;
    private static String lowRange;
    private static String increase;
    private static String decrease;
    private static String stable;
    private static String subject;

    public static void main(String[] args) {
        try {
            CheckingServiceThread test = new CheckingServiceThread();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(test);
        } catch (Exception ex) {
            System.out.println("Not again");
        }
    }


    public CheckingServiceThread() {
    }

    @Override
    public void run() {
        long current = System.currentTimeMillis() + 5 * 3600000;
        int count = 0;
        runStatus = true;
        outOfRange = false;

        Connection con = ExDatabase.open();
        System.out.println("Run Run Run!");
        while (runStatus) {
            if(count%10==0) {
                updateStatus(con);
            }
            if(count>1000000000){
                System.out.println("Counter reset");
                current = System.currentTimeMillis() + 5 * 3600000;
                count = 0;
            }
            System.out.println("next check at count: " + count);
            checkTemps(con, current, count);
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("While check service is sleeping");
            }
            count++;

        }

        ExDatabase.close(con);
    }

    private void checkTemps(Connection con, long current, int count) {
        Statement stmt = null;

        try {
            con.setAutoCommit(false);
            stmt = con.createStatement();
            String highDate = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date(current + count * 1000));
            String lowDate = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date(current + (count - 2) * 1000));
            String sql = "SELECT * FROM temps WHERE temp_datetime>'" + lowDate + "' AND temp_datetime<'" + highDate + "'";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (rs.getString("temp_c_val") != null) {
                    int temp = rs.getInt("temp_c_val");
                    if (temp < low || temp > high) {
                        if(!outOfRange){
                            outOfRange = true;
                            lastAlert = temp;
                            if(temp<low){
                                Contacts.sendTextMsg(con, textBuilder(lowRange,temp,lastAlert), "Temp too low" );
                                System.out.println("Temp just became too low at "+temp);
                            }else{
                                Contacts.sendTextMsg(con, textBuilder(highRange,temp,lastAlert), "Temp too high");
                                System.out.println("Temp just became too high at "+temp);
                            }
                        }else{
                            if (temp < low && (lastAlert - temp) > 5) {
                                Contacts.sendTextMsg(con, textBuilder(decrease,temp,lastAlert), "Temp decreased" );
                                System.out.println("Temp is decreased from " + lastAlert + " to " + temp);
                            } else if (temp > high && (temp - lastAlert) > 5) {
                                Contacts.sendTextMsg(con, textBuilder(increase,temp,lastAlert), "Temp increased");
                                System.out.println("Temp increased from " + lastAlert + " to " + temp);
                            }
                        }
                        lastAlert = temp;
                    } else {
                        if (outOfRange) {
                            Contacts.sendTextMsg(con, textBuilder(stable,temp,lastAlert), "Temp stabilized");
                            System.out.println("Temp is good at " + temp);
                            outOfRange = false;
                        }
                    }
                }
            }
            stmt.close();
            con.commit();
        } catch (Exception ex) {
            System.out.println("CheckTemps Error : " + ex.getMessage());
        }
    }

    private void updateStatus(Connection con){
        high = Controls.checkValue(con,Controls.ControlType.HIGH);
        low = Controls.checkValue(con,Controls.ControlType.LOW);
        highRange = Controls.checkText(con,Controls.ControlType.HIGHRANGE);
        lowRange = Controls.checkText(con, Controls.ControlType.LOWRANGE);
        increase = Controls.checkText(con,Controls.ControlType.INCREASE);
        decrease = Controls.checkText(con,Controls.ControlType.DECREASE);
        stable = Controls.checkText(con,Controls.ControlType.STABLE);
        runStatus = Controls.checkValue(con, Controls.ControlType.CHECKINGSERVICE)==0?FALSE:TRUE;
    }

    private String textBuilder(String sentence, int current, int last){
        String[][] values = new String[4][4];
        values[0][0] = "[current]";
        values[1][0] = "[low]";
        values[2][0] = "[high]";
        values[3][0] = "[last]";
        values[0][1] = Integer.toString(current);
        values[1][1] = Integer.toString(low);
        values[2][1] = Integer.toString(high);
        values[3][1] = Integer.toString(lastAlert);
        for (String[] value : values) {
            CharSequence cs = value[0];
            sentence = sentence.replace(cs, value[1]);
        }
        return sentence;
    }

}
