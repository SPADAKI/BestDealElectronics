
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/LaptopList")

public class LaptopList extends HttpServlet {

    /* SmartPhone Page Displays all the SmartPhones and their Information in Best Deals */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String name = null;
        String CategoryName = request.getParameter("maker");


        /* Checks the SmartPhone type whether it is OnePlus, Apple, Samsung */
        HashMap<String, Laptop> hm = new HashMap<String, Laptop>();
        if (CategoryName == null) {
            hm.putAll(SaxParserDataStore.laptops);
            name = "";
        } else if (CategoryName.equals("apple")) {
            for (Map.Entry<String, Laptop> entry : SaxParserDataStore.laptops.entrySet()) {
                if (entry.getValue().getRetailer().equals("Apple")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Apple";
        } else if (CategoryName.equals("hp")) {
            for (Map.Entry<String, Laptop> entry : SaxParserDataStore.laptops.entrySet()) {
                if (entry.getValue().getRetailer().equals("HP")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "HP";
        } else if (CategoryName.equals("dell")) {
            for (Map.Entry<String, Laptop> entry : SaxParserDataStore.laptops.entrySet()) {
                if (entry.getValue().getRetailer().equals("Dell")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Dell";
        }

        /* Header, Left Navigation Bar are Printed.

		All the Console and Console information are dispalyed in the Content Section

		and then Footer is Printed*/
        Utilities utility = new Utilities(request, pw);
        /*Print header section*/
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class='col-md-8'>");
        pw.print("<div class='row'>");
        int i = 1;
        int size = hm.size();//get number of items in respective category and print all out
        for (Map.Entry<String, Laptop> entry : hm.entrySet()) {
            Laptop laptop = entry.getValue();
            pw.print("<div class='col-sm-6 col-md-4'>");
            pw.print("<div class='thumbnail'>");
            pw.print("<img src='images/laptops/" + laptop.getImage() + "' alt='...'>");
            pw.print("<div class='caption'>");
            pw.print("<p id='product_name'>" + laptop.getName() + "</p>");
            pw.print("<p>$" + laptop.getPrice() + "<span style='color:red;'>   | -$" + laptop.getDiscount() + "</span></p>");
            pw.print("<form action='Cart' method='post'>");
            pw.print("<input type='hidden' name='name' value='" + entry.getKey() + "'>");
            pw.print("<input type='hidden' name='type' value='laptops'>");
            pw.print("<input type='hidden' name='maker' value='" + CategoryName + "'>");
            pw.print("<input type='hidden' name='access' value=''>");
            pw.print("<button type='submit' class='btn btn-primary'>Buy Now</button>");
            pw.print("</form></br>");
            pw.print("<form action='ViewReview' method='post'>");
            pw.print("<input type='hidden' name='productName' value='" + laptop.getName() + "'>");
            pw.print("<input type='hidden' name='type' value='smartphones'>");
            pw.print("<input type='hidden' name='retailer' value='" + laptop.getRetailer() + "'>");
            pw.print("<input type='hidden' name='price' value='" + laptop.getPrice() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + laptop.getDiscount() + "'>");
            pw.print("<input type='hidden' name='access' value=''>");
            pw.print("<button type='submit' class='btn btn-success'>Reviews</button>");
            pw.print("</form>");
            pw.print("</div>");
            pw.print("</div>");
            pw.print("</div>");
            i++;
        }

        pw.print("</div>");
        pw.print("</div>");
    }
}
