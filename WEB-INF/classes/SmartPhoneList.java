
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SmartPhoneList")

public class SmartPhoneList extends HttpServlet {

    /* SmartPhone Page Displays all the SmartPhones and their Information in Best Deals */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String name = null;
        String CategoryName = request.getParameter("maker");


        /* Checks the SmartPhone type whether it is OnePlus, Apple, Samsung */
        HashMap<String, SmartPhone> hm = new HashMap<>();
        if (CategoryName == null) {
            hm.putAll(SaxParserDataStore.smartphones);
            name = "";
        } else if (CategoryName.equals("oneplus")) {
            for (Map.Entry<String, SmartPhone> entry : SaxParserDataStore.smartphones.entrySet()) {
                if (entry.getValue().getRetailer().equals("OnePlus")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "OnePlus";
        } else if (CategoryName.equals("apple")) {
            for (Map.Entry<String, SmartPhone> entry : SaxParserDataStore.smartphones.entrySet()) {
                if (entry.getValue().getRetailer().equals("Apple")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Apple";
        } else if (CategoryName.equals("samsung")) {
            for (Map.Entry<String, SmartPhone> entry : SaxParserDataStore.smartphones.entrySet()) {
                if (entry.getValue().getRetailer().equals("Samsung")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Samsung";
        }


        Utilities utility = new Utilities(request, pw);
        /*Print header section*/
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class='col-md-8'>");
        pw.print("<div class='row'>");
        int i = 1;
        int size = hm.size();//get number of items in respective category and print all out
        for (Map.Entry<String, SmartPhone> entry : hm.entrySet()) {
            SmartPhone smartphone = entry.getValue();
            pw.print("<div class='col-sm-6 col-md-4'>");
            pw.print("<div class='thumbnail'>");
            pw.print("<img src='images/smartphones/" + smartphone.getImage() + "' alt='...'>");
            pw.print("<div class='caption'>");
            pw.print("<p id='product_name'>" + smartphone.getName() + "</p>");
            pw.print("<p>$" + smartphone.getPrice() + "<span style='color:red;'>   | -$" + smartphone.getDiscount() + "</span></p>");
            pw.print("<div class='button-container'>");
            pw.print("<form action='Cart' method='post class='form_style'>");
            pw.print("<input type='hidden' name='name' value='" + entry.getKey() + "'>");
            pw.print("<input type='hidden' name='type' value='smartphones'>");
            pw.print("<input type='hidden' name='maker' value='" + CategoryName + "'>");
            pw.print("<input type='hidden' name='access' value=''>");
            pw.print("<div class='btn_fancy'>");
            pw.print("<button type='submit' class='btn btn-primary btn'>Buy Now</button>");
            pw.print("</div>");
            pw.print("</form>");
            pw.print("<form action='ViewReview' method='post' class='form_style'>");
            pw.print("<input type='hidden' name='productName' value='" + smartphone.getName() + "'>");
            pw.print("<input type='hidden' name='type' value='smartphones'>");
            pw.print("<input type='hidden' name='retailer' value='" + smartphone.getRetailer() + "'>");
            pw.print("<input type='hidden' name='price' value='" + smartphone.getPrice() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + smartphone.getDiscount() + "'>");
            pw.print("<input type='hidden' name='access' value=''>");
            pw.print("<div class='btn_fancy'>");
            pw.print("<button type='submit' class='btn btn-success'>Reviews</button>");
            pw.print("</div>");
            pw.print("</form>");
            pw.print("</div>");
            pw.print("</div>");
            pw.print("</div>");
            pw.print("</div>");
            i++;
        }

        pw.print("</div>");
        pw.print("</div>");
    }
}
