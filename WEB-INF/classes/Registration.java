
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;

@WebServlet("/Registration")

public class Registration extends HttpServlet {

    private String error_msg;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        displayRegistration(request, response, pw, false);
    }

    /*   Username,Password,Usertype information are Obtained from HttpServletRequest variable and validates whether
		 the User Credential Already Exists or else User Details are Added to the Users HashMap */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lasttname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        String usertype = request.getParameter("usertype");
        int success = 0;

        if (!utility.isLoggedin()) {
            usertype = request.getParameter("usertype");
        }

        HashMap<String, ArrayList<User>> hm = new HashMap<>();

        //if password and repassword does not match show error message
        if (!password.equals(repassword)) {
            error_msg = "Passwords doesn't match!";
        } else {
            try {
                //check if user already exists in DB
                boolean isEmpty;
                isEmpty = MyDataStoreUtilities.selectUser(username);

                //empty = true
                if (isEmpty) {
                    //create new user
                    success = MyDataStoreUtilities.insertUser(username, password, firstname, lastname, email, usertype);
                    //add code to redirect user to login page on successful registration
                    if (success > 0) {
                        response.sendRedirect("Login");
                        pw.print("<h4 style='color:red'>" + success + " rows affected</h4>");
                    } else {
                        //
                        pw.print("<h4 style='color:red'>something went wrong</h4>");
                    }
                } else {
                    //print user already exits
                    System.out.print(isEmpty);
                    String message = "Username already exists";
                    pw.print("<h4 style='color:red'>" + message + "</h4>");
                }
            } catch (Exception e) {
                System.out.print(e);
            }

        }//end if-else

        //display the error message
        if (utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", error_msg);
            response.sendRedirect("Account");
            return;
        }
        displayRegistration(request, response, pw, true);

    }

    /*  displayRegistration function displays the Registration page of New User */
    protected void displayRegistration(HttpServletRequest request,
            HttpServletResponse response, PrintWriter pw, boolean error)
            throws ServletException, IOException {
        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        if (error) {
            pw.print("<h4 style='color:red'>" + error_msg + "</h4>");
        }

        utility.printHtml("Registration.html");
        utility.printHtml("Footer.html");
    }
}
