
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;

@WebServlet("/CheckOut")

//once the user clicks buy now button page is redirected to checkout page where user has to give checkout information
public class CheckOut extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities Utility = new Utilities(request, pw);
        storeOrders(request, response);
        double total = Double.parseDouble(request.getParameter("amountDue"));
        String name = request.getParameter("creditCardNo");
        String address = request.getParameter("userAddress");

        Utility.storePayment(0, name, total, address, name);
    }

    protected void storeOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html");
            PrintWriter pw = response.getWriter();
            Utilities utility = new Utilities(request, pw);
            if (!utility.isLoggedin()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("login_msg", "Please Login to add items to cart");
                response.sendRedirect("Login");
                return;
            }

            HttpSession session = request.getSession();

            //get the order product details on clicking submit the form will be passed to submitorder page	
            String userName = session.getAttribute("username").toString();
            String orderTotal = request.getParameter("orderTotal");
            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");
            pw.print("<form name ='CheckOut' action='Payment' method='post'>");
            pw.print("<div id='content'><div class='post'><h2 class='title meta'>");
            pw.print("<a style='font-size: 24px;'>Order</a>");
            pw.print("</h2><div class='entry'>");
            pw.print("<table  class='gridtable'><tr><td>Customer Name:</td><td>");
            pw.print(userName);
            pw.print("</td></tr>");
            // for each order iterate and display the order name price
            for (OrderItem oi : utility.getCustomerOrders()) {
                pw.print("<tr><td> Product Purchased:</td><td>");
                pw.print(oi.getName() + "</td></tr><tr><td>");
                pw.print("<input type='hidden' name='orderPrice' value='" + oi.getPrice() + "'>");
                pw.print("<input type='hidden' name='orderName' value='" + oi.getName() + "'>");
                pw.print("Product Price:</td><td>" + oi.getPrice());
                pw.print("</td></tr>");
            }

            pw.print("<tr><td>");
            //when buying new items
            double with_no_replacement = 0.00;
            double with_1_year_replacement = 50.00;
            double with_lifetime_replacement = 65.00;

            //when renting items
            double limited_duration_rental = 15.00;
            double single_season_rental = 100.00;

            //store final total
            double amountDue = 0.0;
            String userRequest = request.getParameter("equipment_rentOrBuy");
            if (null != userRequest) {
                switch (userRequest) {
                    case "with_no_replacement":
                        amountDue = Integer.parseInt(orderTotal) + with_no_replacement;
                        pw.print("With no replacement</td><td>" + with_no_replacement);
                        break;
                    case "with_1_year_replacement":
                        amountDue = Integer.parseInt(orderTotal) + with_1_year_replacement;
                        pw.print("with 1 year replacement</td><td>" + with_1_year_replacement);
                        break;
                    case "with_lifetime_replacement":
                        amountDue = Integer.parseInt(orderTotal) + with_lifetime_replacement;
                        pw.print("with lifetime replacement</td><td>" + with_lifetime_replacement);
                        break;
                    case "limited_duration_rental":
                        amountDue = Integer.parseInt(orderTotal) + limited_duration_rental;
                        pw.print("Limited duration rental</td><td>" + limited_duration_rental);
                        break;
                    case "single_season_rental":
                        amountDue = Integer.parseInt(orderTotal) + single_season_rental;
                        pw.print("Single season rental</td><td>" + single_season_rental);
                        break;
                    default:
                        break;
                }
            }
            pw.print("<td><tr>");
            pw.print("Total Order Cost</td><td>" + amountDue);
            pw.print("<input type='hidden' name='orderTotal' value='" + amountDue + "'>");
            pw.print("</td></tr></table><table><tr></tr><tr></tr>");
            pw.print("<tr><td>");
            pw.print("Name on Card</td>");
            pw.print("<td><input type='text' name='nameOnCard'>");
            pw.print("</td></tr>");
            pw.print("<tr><td>");
            pw.print("Credit/accountNo</td>");
            pw.print("<td><input type='text' name='creditCardNo'>");
            pw.print("</td></tr>");
            pw.print("<tr><td>");
            pw.print("Customer Address</td>");
            pw.print("<td><input type='text' name='userAddress'>");
            pw.print("</td></tr>");
            pw.print("<tr><td colspan='2'>");
            pw.print("<input type='submit' name='submit' class='btnbuy'>");
            pw.print("</td></tr></table></form>");
            pw.print("</div></div></div>");
            utility.printHtml("Footer.html");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
    }
}
