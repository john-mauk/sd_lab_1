package Databases;

import java.sql.*;

public class ExDatabase {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            //connection = open();
            close(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExDatabase() {
    }

    public static boolean open() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://ex-design.cfilhwe2swqf.us-west-2.rds.amazonaws.com:3306/ex_design_database", "johnny", "[Jamochame13]");
        } catch (Exception ex) {
            System.err.println("Open: " + ex.getClass().getName() + ": " + ex.getMessage());
            return false;
            //System.exit(0);
        }
        System.out.println("Database opened!");
        return true;
    }

    public static void close(Connection con) {
        try {
            con.close();
        } catch (Exception ex) {
            System.err.println("Close: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        System.out.println("Database closed!");
    }
}

