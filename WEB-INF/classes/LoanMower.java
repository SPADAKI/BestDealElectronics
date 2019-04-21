/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raymond
 */
@WebServlet(urlPatterns = {"/LoanMower"})
public class LoanMower extends HttpServlet {

    private String id;
    private String name;
    private double price;
    private double rent;
    private String image;
    private String retailer;
    private String condition;
    private double discount;
    HashMap<String, String> accessories;

    public LoanMower(String name, double price, String image, String retailer, String condition, double discount, double rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.image = image;
        this.retailer = retailer;
        this.condition = condition;
        this.discount = discount;
        this.accessories = new HashMap<String, String>();
    }

    HashMap<String, String> getAccessories() {
        return accessories;
    }

    public LoanMower() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public void setAccessories(HashMap<String, String> accessories) {
        this.accessories = accessories;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

}
