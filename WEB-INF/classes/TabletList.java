
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TabletList")

public class TabletList extends HttpServlet {

    /* Trending Page Displays all the Tablets and their Information in Best Deals */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        /* Checks the Tablets type whether it is microsft or apple or samsung */
        String name = null;
        String CategoryName = request.getParameter("maker");
        HashMap<String, Tablet> hm = new HashMap<>();

        if (CategoryName == null) {
            hm.putAll(SaxParserDataStore.tablets);
            name = "";
        } else if (CategoryName.equals("apple")) {
            for (Map.Entry<String, Tablet> entry : SaxParserDataStore.tablets.entrySet()) {
                if (entry.getValue().getRetailer().equals("Apple")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Apple";
        } else if (CategoryName.equals("microsoft")) {
            for (Map.Entry<String, Tablet> entry : SaxParserDataStore.tablets.entrySet()) {
                if (entry.getValue().getRetailer().equals("Microsoft")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Microsoft";
        } else if (CategoryName.equals("samsung")) {
            for (Map.Entry<String, Tablet> entry : SaxParserDataStore.tablets.entrySet()) {
                if (entry.getValue().getRetailer().equals("Samsung")) {
                    hm.put(entry.getValue().getId(), entry.getValue());
                }
            }
            name = "Samsung";
        }

        /* Header, Left Navigation Bar are Printed.

		All the tablets and tablet information are dispalyed in the Content Section

		and then Footer is Printed*/
        Utilities utility = new Utilities(request, pw);
        /*Print header section*/
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class='col-md-8'>");
        pw.print("<div class='row'>");
        int i = 1;
        int size = hm.size();//get number of items in respective category and print all out
        for (Map.Entry<String, Tablet> entry : hm.entrySet()) {
            Tablet tablet = entry.getValue();
            pw.print("<div class='col-sm-6 col-md-4'>");
            pw.print("<div class='thumbnail'>");
            pw.print("<img src='images/tablets/" + tablet.getImage() + "' alt='...'>");
            pw.print("<div class='caption'>");
            pw.print("<p id='product_name'>" + tablet.getName() + "</p>");
            pw.print("<p>$" + tablet.getPrice() + "<span style='color:red;'>   | -$" + tablet.getDiscount() + "</span></p>");
            pw.print("<form action='Cart' method='post'>");
            pw.print("<input type='hidden' name='name' value='" + entry.getKey() + "'>");
            pw.print("<input type='hidden' name='type' value='tablets'>");
            pw.print("<input type='hidden' name='maker' value='" + CategoryName + "'>");
            pw.print("<input type='hidden' name='access' value=''>");
            pw.print("<button type='submit' class='btn btn-primary'>Buy Now</button>");
            pw.print("</form></br>");
            pw.print("<form action='ViewReview' method='post'>");
            pw.print("<input type='hidden' name='productName' value='" + tablet.getName() + "'>");
            pw.print("<input type='hidden' name='type' value='smartphones'>");
            pw.print("<input type='hidden' name='retailer' value='" + tablet.getRetailer() + "'>");
            pw.print("<input type='hidden' name='price' value='" + tablet.getPrice() + "'>");
            pw.print("<input type='hidden' name='discount' value='" + tablet.getDiscount() + "'>");
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
