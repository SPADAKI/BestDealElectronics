/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raymond
 */
@WebServlet(urlPatterns = {"/ViewReview"})

public class ViewReview extends HttpServlet {

    /*handles the post functions*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        /* StoreProduct Function stores the Purchased product in Orders HashMap.*/
        //utility.storeProduct(name, type, maker, access);
        displayReviews(request, response);
    }

    /* displayReviews Function shows all the reviews for the particular product*/
    protected void displayReviews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        String productName = request.getParameter("productName");
        System.out.println("Inside ViewReview Servlet | productName: " + productName);
        String type = request.getParameter("type");
        String retailer = request.getParameter("retailer");
        String price = request.getParameter("price");
        String discount = request.getParameter("discount");

        if (productName == null) {
            response.sendRedirect("Home");
        }

        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");
        pw.print("<div class=\"col-md-8\">");
        //print the product and let the user write a review (login check done in - WriteReview.java)
        //write a review option
        pw.print("<div class=\"panel panel-default\">");
        pw.print("<div class=\"panel-heading\">");
        pw.print("<p>Write a review here</p>");
        pw.print("</div>");
        pw.print("<div class=\"panel-body\">");
        pw.print("<form action='WriteReview' method='get'>");
        pw.print("<input type='hidden' name='productName' value='" + productName + "'>");
        pw.print("<input type='hidden' name='type' value='" + type + "'>");
        pw.print("<input type='hidden' name='maker' value='" + retailer + "'>");
        pw.print("<input type='hidden' name='price' value='" + price + "'>");
        pw.print("<input type='hidden' name='discount' value='" + discount + "'>");
        pw.print("<button type='submit' class='btn btn-primary'>Write a review</button>");
        pw.print("</form>");
        pw.print("</div>");
        pw.print("</div>");
        pw.print("</br>");

        int i = 1;
        //print all reviews for the product
        System.out.println("Total rows from mongoDB | viewReview : " + MongoDBDataStoreUtilities.reviewCount(productName));
        if (MongoDBDataStoreUtilities.reviewCount(productName) > 0) {
            HashMap<String, ArrayList<Review>> reviewHM = MongoDBDataStoreUtilities.selectReview(productName);
            ArrayList<Review> listReview = reviewHM.get(productName);

            for (Review r : listReview) {
                int stars = r.getReviewRating();
                utility.printStars(stars);
                pw.print("<h4>" + r.getReviewTitle() + "</h4>");
                pw.print("<p>" + r.getReviewDesc() + "</p>");
                pw.print("<p>-by " + r.getUserName() + "</p>");
                i++;
            }
        } else {
            pw.print("<p>No reviews for this product yet! :|</p>");
        }
        pw.print("</div>");
    }

    /*requied if someone type BestDeals/ViewReview into the address bar directly*/
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        String name = request.getParameter("name");
        if (name == null) {
            response.sendRedirect("Home");
        }
        displayReviews(request, response);
    }

}
