
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Cart")

public class Cart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        
        /* From the HttpServletRequest variable name,type,maker and acessories information are obtained.*/
        Utilities utility = new Utilities(request, pw);
        String productname = request.getParameter("name");
        String type = request.getParameter("type");
        String maker = request.getParameter("maker");
        String access = request.getParameter("access");
        int insertSuccessFlag = 0;
        /* StoreProduct Function stores the Purchased product in Orders HashMap.*/
        String username = "";
        username = utility.username();

        //temp store products (of cart) in HM
        utility.storeProduct(productname, type, maker, access);
        displayCart(request, response);
    }


    /* displayCart Function shows the products that users has bought, these products will be displayed with Total Amount.*/
    protected void displayCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);
        if (!utility.isLoggedin()) {
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to add items to cart");
            response.sendRedirect("Login");
            return;
        }

        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<form name ='Cart' action='CheckOut' method='post'>");
        pw.print("<h2>Cart(" + utility.CartCount() + ")</h2>");
        pw.print("</h2>");
        pw.print("<div class='entry'>");
        if (utility.CartCount() > 0) {
            pw.print("<table  class='gridtable'>");
            int i = 1;
            double total = 0;
            for (OrderItem oi : utility.getCustomerOrders()) {
                pw.print("<tr>");
                pw.print("<td>" + i + ".</td><td>" + oi.getName() + "</td><td>: " + oi.getPrice() + "</td>");
                pw.print("<td>");
                pw.print("<form action='DeleteFromCart' method='post'>");
                pw.print("<input type='hidden' name='orderName' value='" + oi.getName() + "'>");
                pw.print("<button type='submit' class='btn btn-primary'>Delete</button>");
                pw.print("</form></br>");
                pw.print("</td>");
                pw.print("<input type='hidden' name='orderName' value='" + oi.getName() + "'>");
                pw.print("<input type='hidden' name='orderPrice' value='" + oi.getPrice() + "'>");
                pw.print("</tr>");
                total = total + oi.getPrice();
                i++;
            }
            pw.print("<input type='hidden' name='orderTotal' value='" + total + "'>");
            pw.print("<tr><th></th><th>Total</th><th>" + total + "</th>");
            pw.print("<tr><td></td><td></td><td><input type='submit' name='CheckOut' value='CheckOut' class='btnbuy'></td>");
            pw.print("</table>");
        } else {
            pw.print("<h4 style='color:red'>Your Cart is empty</h4>");
        }
        pw.print("</form>");
        utility.printHtml("Footer.html");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        displayCart(request, response);
    }
}
