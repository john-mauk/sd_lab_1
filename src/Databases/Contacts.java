package Databases;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.Properties;

public class Contacts {

    public static void main(String[] args) {
        //String phone = "6512109890";
        //String carrier = "AT&T";
        //Connection con = ExDatabase.open();
        //System.out.println("Contact was added: "+addContact(con,phone,carrier));
        //System.out.println("Contact was removed: "+removeContact(con,phone));
        //System.out.println("Text Sent: "+SendTextMsg(con));
        //ExDatabase.close(con);
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
                String sql = "INSERT INTO contacts (contact_phone,contact_carrier,contact_domain)" +
                        "VALUES ('" + phone + "','" + carrier + "','" + Carriers.lookup(carrier).domain + "');";
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

    public static boolean SendTextMsg(Connection con) {
        final String adminUsername = "ex Design";
        final String adminEmail = "ex.design.solutions@gmail.com";
        final String adminPassword = "[Jamochame13]";

        try {
            Properties properties = new Properties();

            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port", "465");
            properties.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");

            Authenticator authenticator = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(adminEmail, adminPassword);
                }
            };

            Session session = Session.getDefaultInstance(properties, authenticator);
            Message msg = new MimeMessage(session);

            Statement stmt = null;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM contacts;");

            while (rs.next()) {
                msg.setFrom(new InternetAddress(adminUsername + "<" + adminEmail + ">"));
                msg.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(rs.getString("contact_phone") + "@" + rs.getString("contact_domain")));
                msg.setSubject("Lab One");
                msg.setText("Temperature has changed by more than 10 Celsius!");
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

    private enum Carriers {
        ATT("AT&T", "mms.att.net"),
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

