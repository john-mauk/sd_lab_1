package Contact;


import Databases.Contacts;
import Databases.ExDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/Contact.AddRemoveTest")
public class AddRemoveTest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phone = request.getParameter("p");
        String carrier = request.getParameter("c");
        String btn = request.getParameter("btn");
        String status = "";

        if (btn == null) {
            status = "error";
        } else if (btn.equals("add")) {
            status = addContact(phone, carrier)?"added":"notadded";
        } else if (btn.equals("remove")) {
            status = removeContact(phone)?"removed":"notremoved";
        } else if (btn.equals("test")){
            status = testContact(phone,carrier)?"tested":"nottested";
        }

        System.out.println(status);

        response.setContentType("text/plain");
        response.getWriter().print(status);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private static boolean addContact(String phone, String carrier) {
        Connection con = ExDatabase.open();
        boolean added = Contacts.addContact(con,phone,carrier);
        ExDatabase.close(con);
        return added;
    }

    private static boolean removeContact(String phone) {
        Connection con = ExDatabase.open();
        boolean removed = Contacts.removeContact(con,phone);
        ExDatabase.close(con);
        return removed;
    }

    private static boolean testContact(String phone, String carrier){
        Connection con = ExDatabase.open();
        boolean test = Contacts.testContact(con,phone,carrier);
        ExDatabase.close(con);
        return test;
    }
}
