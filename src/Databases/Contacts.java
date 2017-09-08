package Databases;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Properties;

/**
 *
 */
public class Contacts {

    /**
     * Runs the methods throughout the class
     * @param args
     */
    public static void main(String[] args) {
        //String phone = "6512109890";
        //String carrier = "AT&T";
        Connection con = ExDatabase.open();
        //System.out.println("Contact was added: "+addContact(con,phone,carrier));
        //System.out.println("Contact was removed: "+removeContact(con,phone));
        //System.out.println("Text Sent: "+sendTextMsg(con));
        ExDatabase.close(con);
    }

    public Contacts() {
    }

    public static boolean addContact(Connection con, String phone, String carrier) {
        Statement stmt = null;
        boolean added = false;

        try {
            con.setAutoCommit(false);
            stmt = con.createStatement();
            if (!checkContact(con, phone)) {
                String carrierName = Carriers.lookup(carrier).getDomain();
                String sql = "INSERT INTO contacts (contact_phone,contact_carrier,contact_domain)" +
                        "VALUES ('" + phone + "','" + carrier + "','" + Carriers.lookup(carrier).getDomain() + "');";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            con.commit();
        } catch (Exception ex) {
            System.err.println("Add Contact: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        added = checkContact(con, phone);
        return added;
    }

    public static boolean checkContact(Connection con, String phone) {
        boolean checked = false;
        try {
            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(contact_phone) FROM contacts WHERE contact_phone ='" + phone + "';");
            rs.next();
            checked = (rs.getString("count(contact_phone)").equals("1"));
            stmt.close();
        } catch (Exception ex) {
            System.err.println("CheckContact: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return checked;
    }

    public static boolean removeContact(Connection con, String phone) {
        boolean removed = false;
        try {
            con.setAutoCommit(false);
            Statement stmt = null;
            stmt = con.createStatement();
            if (checkContact(con, phone)) {
                String sql = "DELETE FROM contacts WHERE contact_phone = " + phone + ";";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            con.commit();
            removed = !checkContact(con, phone);
        } catch (Exception ex) {
            System.err.println("RemoveContact: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return removed;
    }

    public static boolean sendTextMsg(Connection con, String contents, String subject) {
        final String adminUsername = "ex Design";
        final String adminEmail = "ex.design.solutions@gmail.com";
        final String adminPassword = "[Jamochame13]";

        try {
            Properties properties = setUpProperties();
            Message msg = setUpMessage(properties,adminEmail,adminPassword);

            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contacts;");

            while (rs.next()) {
                msg = setUpMessageContents(msg,rs,adminUsername,adminEmail,contents, subject);
                Transport.send(msg);
                System.out.println("Sent text to: " + rs.getString("contact_phone"));
            }

        } catch (Exception ex) {
            System.err.println("SendTextMsg: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            return false;
        }
        return true;
    }

    public static boolean testContact(Connection con,String phone, String carrier){
        final String adminUsername = "ex Design";
        final String adminEmail = "ex.design.solutions@gmail.com";
        final String adminPassword = "[Jamochame13]";
        final String contents = "This is a test message";
        final String subject = "Test";
        try {
            Properties properties = setUpProperties();
            Message msg = setUpMessage(properties,adminEmail,adminPassword);

            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contacts WHERE contact_phone='"+phone+"';");

            while (rs.next()) {
                msg = setUpMessageContents(msg,rs,adminUsername,adminEmail,contents, "Test");
                Transport.send(msg);
                System.out.println("Sent text to: " + rs.getString("contact_phone"));
            }

        } catch (Exception ex) {
            System.err.println("SendTextMsg: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
            return false;
        }
        return true;
    }

    private static MimeMessage setUpMessage(Properties properties, String adminEmail, String adminPassword){
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, adminPassword);
            }
        };

        Session session = Session.getDefaultInstance(properties, authenticator);
        return new MimeMessage(session);
    }

    private static Properties setUpProperties(){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");
        return prop;
    }

    private static Message setUpMessageContents(Message msg,ResultSet rs,String adminUsername, String adminEmail, String contents, String subject){
        try {
            msg.setFrom(new InternetAddress(adminUsername + "<" + adminEmail + ">"));
            msg.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(rs.getString("contact_phone") + "@" + rs.getString("contact_domain")));
            msg.setSubject(subject);
            msg.setText(contents);
        }catch(Exception ex){
            System.err.println("Set Up Message Contents: " + ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return msg;
    }

    private enum Carriers {
        ATT("ATT", "mms.att.net"),
        SPRINT("Sprint", "pm.sprint.com"),
        TMOBILE("T-Mobile", "tmomail.net"),
        USCELLULAR("U.S. Cellular", "mms.uscc.net"),
        VERIZON("Verizon", "vzwpix.com");

        private final String domain;
        private final String name;

        Carriers(String name, String domain) {
            this.name = name;
            this.domain = domain;
        }

        public String getDomain() {
            return domain;
        }

        public String getName() {
            return name;
        }

        public static Carriers lookup(String name) {
            for (Carriers carrier : Carriers.values()) {
                if (name.equals(carrier.name)) {
                    return carrier;
                }
            }
            return null;
        }
    }

}

