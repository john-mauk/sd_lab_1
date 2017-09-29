package Contact;

import Databases.Controls;
import Databases.ExDatabase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/Contact.TextContent")
public class TextContent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String field = request.getParameter("field");
        String text = request.getParameter("text");
        if(field!=null && text!=null){
            updateTextField(field, text);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String field = request.getParameter("field");
        String text = "";
        if(field!=null){
           text = getTextField(field);
        }
        response.setContentType("text/plain");
        response.getWriter().print(text);
    }

    private static void updateTextField(String field, String text){
        try{
            Connection con = ExDatabase.open();
            Controls.updateText(con, Controls.ControlType.getControlType(field),text);
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Update Text Field : " + ex.getMessage());
        }
    }

    private static String getTextField(String field){
        String text = "";
        try{
            Connection con = ExDatabase.open();
            text = Controls.checkText(con, Controls.ControlType.getControlType(field));
            ExDatabase.close(con);
        }catch(Exception ex){
            System.out.println("Get Text Field : " + ex.getMessage());
        }
        return text;
    }


}
