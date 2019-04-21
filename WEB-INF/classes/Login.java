
import java.io.*;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")

public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        /* User Information(username,password,usertype) is obtained from HttpServletRequest,
		Based on the Type of user(customer,retailer,manager) respective hashmap is called and the username and 
		password are validated and added to session variable and display Login Function is called */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String usertype = request.getParameter("usertype");
        HashMap<String, User> hm;

        //get user from database - registration table
        hm = MyDataStoreUtilities.loginUser(username, password, usertype);
        System.out.println("hm is empty: " + hm.isEmpty());
        System.out.println("hm username: " + hm.containsKey(username));
        if (hm.containsKey(username)) {
            //create session
            HttpSession session = request.getSession(true);
            User currentUser = hm.get(username);
            String fname = currentUser.getFirstName();
            session.setAttribute("firstname", fname);
            session.setAttribute("username", username);
            response.sendRedirect("Home");
        } else {
            displayLogin(request, response, pw, true);
        }
    }//end doPost

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        displayLogin(request, response, pw, false);
    }


    /*  Login Screen is Displayed, Registered User specifies credentials and logins into the Application. */
    protected void displayLogin(HttpServletRequest request,
            HttpServletResponse response, PrintWriter pw, boolean error)
            throws ServletException, IOException {

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        if (error) {
            pw.print("<h4 style='color:red'>Please check your username, password and user type!</h4>");
        }
        HttpSession session = request.getSession(true);
        if (session.getAttribute("login_msg") != null) {
            pw.print("<h4 style='color:red'>" + session.getAttribute("login_msg") + "</h4>");
            session.removeAttribute("login_msg");
        }
        utility.printHtml("Login.html");
        utility.printHtml("Footer.html");
    }

}
