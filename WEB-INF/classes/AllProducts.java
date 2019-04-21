/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raymond
 */
@WebServlet(urlPatterns = {"/AllProducts"})
public class AllProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //list all products from ProductCatalog.xml here
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String CategoryName = request.getParameter("maker");

        HashMap<String, SnowBlower> sb = new HashMap<String, SnowBlower>();
        sb.putAll(SaxParserDataStore.snowblowers);

        HashMap<String, LoanMower> lm = new HashMap<String, LoanMower>();
        lm.putAll(SaxParserDataStore.loanmowers);

        pw.print("<table>");
        int i = 1;
        int sbsize = sb.size();
        for (Map.Entry<String, SnowBlower> entry : sb.entrySet()) {
            SnowBlower snowblower = entry.getValue();
            if (i % 3 == 1) {
                pw.print("<tr>");
            }
            pw.print("<td><div id='shop_item'>");
            pw.print("<h3>" + snowblower.getName() + "</h3>");
            pw.print("<strong>$" + snowblower.getPrice() + "</strong><ul>");
            pw.print("<li id='item'><img src='images/consoles/" + snowblower.getImage() + "' alt='' /></li>");
            pw.print("<li><form method='post' action='Cart'>"
                    + "<input type='hidden' name='name' value='" + entry.getKey() + "'>"
                    + "<input type='hidden' name='type' value='consoles'>"
                    + "<input type='hidden' name='maker' value='" + CategoryName + "'>"
                    + "<input type='hidden' name='access' value=''>"
                    + "<input type='hidden' name='rentOrBuy' value='buy'>"
                    + "<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
            pw.print("<li><form method='post' action='Cart'>"
                    + "<input type='hidden' name='name' value='" + entry.getKey() + "'>"
                    + "<input type='hidden' name='type' value='consoles'>"
                    + "<input type='hidden' name='maker' value='" + CategoryName + "'>"
                    + "<input type='hidden' name='access' value=''>"
                    + "<input type='hidden' name='rentOrBuy' value='rent'>"
                    + "<input type='submit' class='btnbuy' value='Rent'></form></li>");
            pw.print("</ul></div></td>");
            if (i % 3 == 0 || i == sbsize) {
                pw.print("</tr>");
            }
            i++;
        }
        int j = 1;
        int lmsize = sb.size();
        for (Map.Entry<String, LoanMower> entry : lm.entrySet()) {
            LoanMower loanmower = entry.getValue();
            if (j % 3 == 1) {
                pw.print("<tr>");
            }
            pw.print("<td><div id='shop_item'>");
            pw.print("<h3>" + loanmower.getName() + "</h3>");
            pw.print("<strong>$" + loanmower.getPrice() + "</strong><ul>");
            pw.print("<li id='item'><img src='images/consoles/" + loanmower.getImage() + "' alt='' /></li>");
            pw.print("<li><form method='post' action='Cart'>"
                    + "<input type='hidden' name='name' value='" + entry.getKey() + "'>"
                    + "<input type='hidden' name='type' value='consoles'>"
                    + "<input type='hidden' name='maker' value='" + CategoryName + "'>"
                    + "<input type='hidden' name='access' value=''>"
                    + "<input type='hidden' name='rentOrBuy' value='buy'>"
                    + "<input type='submit' class='btnbuy' value='Buy Now'></form></li>");
            pw.print("<li><form method='post' action='Cart'>"
                    + "<input type='hidden' name='name' value='" + entry.getKey() + "'>"
                    + "<input type='hidden' name='type' value='consoles'>"
                    + "<input type='hidden' name='maker' value='" + CategoryName + "'>"
                    + "<input type='hidden' name='access' value=''>"
                    + "<input type='hidden' name='rentOrBuy' value='rent'>"
                    + "<input type='submit' class='btnbuy' value='Rent'></form></li>");
            pw.print("</ul></div></td>");
            if (j % 3 == 0 || j == lmsize) {
                pw.print("</tr>");
            }
            j++;
        }
        pw.print("</table>");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
