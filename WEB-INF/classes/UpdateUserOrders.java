/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Raymond
 */
@WebServlet(urlPatterns = {"/UpdateUserOrders"})
public class UpdateUserOrders extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //2 function in mydatastoreutilities
        //1. handleOrderDelete - delete data based on orderID
        //2. handleOrderUpdate - update data based on orderID
        
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        
        String userName = request.getParameter("userName");
        String orderName = request.getParameter("orderName");
        Double orderPrice = Double.parseDouble(request.getParameter("orderPrice"));
        String userAddress = request.getParameter("userAddress");
        String creditCardNo = request.getParameter("creditCardNo");
        Integer orderId = Integer.parseInt(request.getParameter("orderId"));
        
        String delete = request.getParameter("delete");
        String update = request.getParameter("update");
        
        System.out.println("delete value: " + delete);
        System.out.println("update value: " + update);
        
        if(update != null){
            //do update function
            MyDataStoreUtilities.handleOrderUpdate(orderId, userName, orderName, orderPrice, userAddress, creditCardNo);
            pw.print("<h2>'"+ orderId +"' successfully updated.</h2>");
        }else{
            //do delete function
            MyDataStoreUtilities.handleOrderDelete(orderId, userName, orderName, orderPrice, userAddress, creditCardNo);
            pw.print("<h2>'"+ orderId +"' successfully deleted.</h2>");
        }
    }

   

}
