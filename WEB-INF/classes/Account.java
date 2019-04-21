
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Map.Entry;
import static jdk.nashorn.internal.objects.NativeArray.map;

@WebServlet("/Account")

public class Account extends HttpServlet {

    private String error_msg;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        MyDataStoreUtilities myDataUtility = new MyDataStoreUtilities(request, pw);

        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String maker = request.getParameter("maker");
        String access = request.getParameter("access");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        displayAccount(request, response);
    }

    /* Display Account Details of the Customer (Name and Usertype) */
    protected void displayAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        MyDataStoreUtilities myDataUtility = new MyDataStoreUtilities(request, pw);
        String username;

        try {
            response.setContentType("text/html");
            if (!utility.isLoggedin()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("login_msg", "Please Login to add items to cart");
                response.sendRedirect("Login");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error in Account1: " + e);
        }
        HttpSession session = request.getSession();
        username = (String) session.getAttribute("username");
        System.out.println("Username in account: " + username);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        User user = myDataUtility.getUser(username);
        System.out.println("The user type is: " + user.getUsertype());
        pw.print("<div class='col-md-8'>");
        pw.print("<div class='panel panel-default'>");
        pw.print("<div class='panel-heading'>");
        pw.print("<p>" + user.getUserName() + "-" + user.getUsertype() + "</p>");
        pw.print("</div>");
        pw.print("</div></br>");

        //print the title
        pw.print("</br>");
        pw.print("<hr>");
        pw.print("<h2>Update personal information</h2>");
        pw.print("</br></br>");

        //display form to edit user information
        pw.print("<form method='post' action='UpdateUserInfo'>");
        pw.print("<p>First name</p>");
        pw.print("<input type ='text' name='firstName'/></br>");
        pw.print("<p>Last Name</p>");
        pw.print("<input type ='text' name='lastName'/></br>");
        pw.print("<p>Email</p>");
        pw.print("<input type ='text' name='email'/></br>");
        pw.print("<p>Password</p>");
        pw.print("<input type ='password' name='password'/></br>");
        pw.print("<button id='update' name='update'>Update info</button>");
        pw.print("<input type= 'hidden' name= 'username' value ='" + user.getUserName() + "' />");
        pw.print("</form>");
        //end user information

        //print the title
        pw.print("</br></br>");
        pw.print("</hr>");
        pw.print("<h2>Update user orders</h2>");
        pw.print("<p>P.S - You cannot change the username</p>");
        pw.print("</br></br>");

        try {
            if ("retailer".equals(user.getUsertype())) {
                //add function, delete, update function 
                HashMap<Integer, ArrayList<OrderPayment>> userOrdersHM = new HashMap<>();
                userOrdersHM = myDataUtility.selectAllOrders();
                System.out.println("userOrdersHM is empty: " + userOrdersHM.isEmpty());

                for (Entry<Integer, ArrayList<OrderPayment>> ee : userOrdersHM.entrySet()) {
                    Integer key = ee.getKey();
                    ArrayList<OrderPayment> values = ee.getValue();

                    for (OrderPayment payment : values) {
                        pw.print("<form method='post' action='UpdateUserOrders'>");
                        pw.print("<table class='gridtable'>");
                        pw.print("<tr>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<input type= 'text' name= 'userName' value ='" + payment.getUserName() + "' readonly/>");
                        pw.print("</td>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<input type= 'text' name= 'orderName' value ='" + payment.getOrderName() + "' />");
                        pw.print("</td>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<input type= 'text' name= 'orderPrice' value ='" + payment.getOrderPrice() + "' />");
                        pw.print("</td>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<input type= 'text' name= 'userAddress' value ='" + payment.getUserAddress() + "' />");
                        pw.print("</td>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<input type= 'text' name= 'creditCardNo' value ='" + payment.getCreditCardNo() + "' />");
                        pw.print("</td>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<button id='update' name='update' value='update'>Update</button>");
                        pw.print("<input type= 'hidden' name= 'orderId' value ='" + payment.getOrderId() + "' />");
                        pw.print("</td>");
                        pw.print("<td contenteditable='true' style=padding:10px;>");
                        pw.print("<button id='delete' name='delete' value='delete'>Delete</button>");
                        pw.print("<input type= 'hidden' name= 'orderId' value ='" + payment.getOrderId() + "' />");
                        pw.print("</td>");
                        pw.print("</tr>");
                        pw.print("</table>");
                        pw.print("</form>");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error in Account2: " + e);
        }

    }
}
