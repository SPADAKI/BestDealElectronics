
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;

@WebServlet("/Utilities")

/* 
	Utilities class contains class variables of type HttpServletRequest, PrintWriter,String and HttpSession.

	Utilities class has a constructor with  HttpServletRequest, PrintWriter variables.
	  
 */
public class Utilities extends HttpServlet {

    HttpServletRequest req;
    PrintWriter pw;
    String url;
    HttpSession session;

    public Utilities(HttpServletRequest req, PrintWriter pw) {
        this.req = req;
        this.pw = pw;
        this.url = this.getFullURL();
        this.session = req.getSession(true);
    }

    /*  Printhtml Function gets the html file name as function Argument, 
		If the html file name is Header.html then It gets Username from session variables.
		Account ,Cart Information ang Logout Options are Displayed*/
    public void printHtml(String file) {
        String result = HtmlToString(file);
        //to print the right navigation in header of username cart and logout etc

        if (file == "Header.html") {
            result = result + "<nav class='navbar navbar-default'>"
                    + "<div class='container-fluid'>"
                    + "<ul class='nav navbar-nav'>";
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
                result = result + "<li><a href='ViewOrder'>ViewOrder</a></li>"
                        + "<li><a>Hello," + username + "</a></li>"
                        + "<li><a href='Account'>Account</a></li>"
                        + "<li><a href='Logout'>Logout</a></li>";
            } else {
                result = result + "<li><a href='ViewOrder'>View Order</a></li>" + "<li><a href='Login'>Login</a></li>";
            }
            result = result + "<li><a href='Cart'>Cart(" + CartCount() + ")</a></li></ul></div></div><div id='page'></ul></div>";
            pw.print(result);
        } else {
            pw.print(result);
        }

    }


    /*  getFullURL Function - Reconstructs the URL user request  */
    public String getFullURL() {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        url.append("/");
        return url.toString();
    }

    /*  HtmlToString - Gets the Html file and Converts into String and returns the String.*/
    public String HtmlToString(String file) {
        String result = null;
        try {
            String webPage = url + file;
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();
        } catch (Exception e) {
        }
        return result;
    }

    /*  logout Function removes the username , usertype attributes from the session variable*/
    public void logout() {
        session.removeAttribute("username");
        session.removeAttribute("usertype");
    }

    /*  logout Function checks whether the user is loggedIn or Not*/
    public boolean isLoggedin() {
        if (session.getAttribute("username") == null) {
            return false;
        }
        return true;
    }

    /*  username Function returns the username from the session variable.*/
    public String username() {
        if (session.getAttribute("username") != null) {
            return session.getAttribute("username").toString();
        }
        return null;
    }

    /*  usertype Function returns the usertype from the session variable.*/
    public String usertype() {
        if (session.getAttribute("usertype") != null) {
            return session.getAttribute("usertype").toString();
        }
        return null;
    }

    /*  getUser Function checks the user is a customer or retailer or manager and returns the user class variable.*/
    public User getUser() {
        String usertype = usertype();
        HashMap<String, User> hm = new HashMap<String, User>();
        String TOMCAT_HOME = System.getProperty("catalina.home");
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "\\webapps\\BestDeals\\UserDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            hm = (HashMap) objectInputStream.readObject();
        } catch (Exception e) {
        }
        User user = hm.get(username());
        return user;
    }

    /*  getCustomerOrders Function gets  the Orders for the user*/
    public ArrayList<OrderItem> getCustomerOrders() {
        ArrayList<OrderItem> order = new ArrayList<OrderItem>();
        if (OrdersHashMap.orders.containsKey(username())) {
            order = OrdersHashMap.orders.get(username());
        }
        return order;
    }

    /*  getReviews for a PARTICULAR product - login not required at this stage */
    public ArrayList<Review> getReviews() {
        ArrayList<Review> prodreviews = new ArrayList<Review>();
        //get the product Id to which reviews are requests from request
        String prodName = req.getParameter("name");
        prodreviews = ReviewHashMap.reviews.get(prodName);

        return prodreviews;
    }

    /* test function - please ignnore*/
    public String testName() {
        String prodName = req.getParameter("name");
        return prodName;
    }

    /* getProductReviewCount return the number of reviews for the selected product*/
    public int getProductReviewCount() {
        int size = getReviews().size();
        //System.out.println(size);
        return size;
    }

    //store user reviews for products
    public void storeReview(String name, String title, String desc) {
        //save the username, product name, title and commnent as desc
        //reviews are stored based on product name (K) - needs further improvement
        System.out.println(title); //output works
        System.out.println(desc);
        System.out.println(name);

        String user = username();
        if (!ReviewHashMap.reviews.containsKey(name)) {
            ArrayList<Review> arr = new ArrayList<Review>();
            ReviewHashMap.reviews.put(name, arr);
        }

        ArrayList<Review> userreviews = ReviewHashMap.reviews.get(name);
        //Review reviews = new Review(title, desc, user);
        //userreviews.add(reviews);

    }

    /*  getOrdersPaymentSize Function gets  the size of OrderPayment */
    public int getOrderPaymentSize() {
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
        String TOMCAT_HOME = System.getProperty("catalina.home");
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "\\webapps\\BestDeals\\PaymentDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            orderPayments = (HashMap) objectInputStream.readObject();
        } catch (Exception e) {

        }
        int size = 0;
        for (Map.Entry<Integer, ArrayList<OrderPayment>> entry : orderPayments.entrySet()) {
            size = entry.getKey();
        }
        return size;
    }

    /*  CartCount Function gets  the size of User Orders*/
    public int CartCount() {
        if (isLoggedin()) {
            return getCustomerOrders().size();
        }
        return 0;
    }

    /* reviewCount*/

 /* StoreProduct Function stores the Purchased product in Orders HashMap according to the User Names.*/
    public void storeProduct(String name, String type, String maker, String acc) {
        if (!OrdersHashMap.orders.containsKey(username())) {
            ArrayList<OrderItem> arr = new ArrayList<>();
            OrdersHashMap.orders.put(username(), arr);
        }
        ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
        if (type.equals("smartphones")) {
            SmartPhone phone;
            phone = SaxParserDataStore.smartphones.get(name);
            OrderItem orderitem = new OrderItem(phone.getName(), phone.getPrice(), phone.getImage(), phone.getRetailer());
            orderItems.add(orderitem);
        }
        if (type.equals("laptops")) {
            Laptop laptop = null;
            laptop = SaxParserDataStore.laptops.get(name);
            OrderItem orderitem = new OrderItem(laptop.getName(), laptop.getPrice(), laptop.getImage(), laptop.getRetailer());
            orderItems.add(orderitem);
        }
        if (type.equals("tablets")) {
            Tablet tablet = null;
            tablet = SaxParserDataStore.tablets.get(name);
            OrderItem orderitem = new OrderItem(tablet.getName(), tablet.getPrice(), tablet.getImage(), tablet.getRetailer());
            orderItems.add(orderitem);
        }
        if (type.equals("tvs")) {
            TV tv = null;
            tv = SaxParserDataStore.tvs.get(name);
            OrderItem orderitem = new OrderItem(tv.getName(), tv.getPrice(), tv.getImage(), tv.getRetailer());
            orderItems.add(orderitem);
        }
        if (type.equals("accessories")) {
            Accessory accessory = SaxParserDataStore.accessories.get(name);
            OrderItem orderitem = new OrderItem(accessory.getName(), accessory.getPrice(), accessory.getImage(), accessory.getRetailer());
            orderItems.add(orderitem);
        }

    }
    // store the payment details for orders

    public void storePayment(int orderId,
            String orderName, double orderPrice, String userAddress, String creditCardNo) {
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
        String TOMCAT_HOME = System.getProperty("catalina.home");
        // get the payment details file 
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "\\webapps\\BestDeals\\PaymentDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            orderPayments = (HashMap) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {

        }
        if (orderPayments == null) {
            orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
        }
        // if there exist order id already add it into same list for order id or create a new record with order id

        if (!orderPayments.containsKey(orderId)) {
            ArrayList<OrderPayment> arr = new ArrayList<OrderPayment>();
            orderPayments.put(orderId, arr);
        }
        ArrayList<OrderPayment> listOrderPayment = orderPayments.get(orderId);
        OrderPayment orderpayment = new OrderPayment(orderId, username(), orderName, orderPrice, userAddress, creditCardNo);
        listOrderPayment.add(orderpayment);

        // add order details into file
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(TOMCAT_HOME + "\\webapps\\BestDeals\\PaymentDetails.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(orderPayments);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("inside exception file not written properly");
        }
    }

    public void deleteProduct(String name, String type) {
        ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
        if (type.equals("smartphones")) {
            SmartPhone phone;
            phone = SaxParserDataStore.smartphones.get(name);
            OrderItem orderitem = new OrderItem(phone.getName(), phone.getPrice(), phone.getImage(), phone.getRetailer());
            orderItems.remove(orderitem);
        }
        if (type.equals("laptops")) {
            Laptop laptop = null;
            laptop = SaxParserDataStore.laptops.get(name);
            OrderItem orderitem = new OrderItem(laptop.getName(), laptop.getPrice(), laptop.getImage(), laptop.getRetailer());
            orderItems.remove(orderitem);
        }
        if (type.equals("tablets")) {
            Tablet tablet = null;
            tablet = SaxParserDataStore.tablets.get(name);
            OrderItem orderitem = new OrderItem(tablet.getName(), tablet.getPrice(), tablet.getImage(), tablet.getRetailer());
            orderItems.remove(orderitem);
        }
        if (type.equals("tvs")) {
            TV tv = null;
            tv = SaxParserDataStore.tvs.get(name);
            OrderItem orderitem = new OrderItem(tv.getName(), tv.getPrice(), tv.getImage(), tv.getRetailer());
            orderItems.remove(orderitem);
        }
        if (type.equals("accessories")) {
            Accessory accessory = SaxParserDataStore.accessories.get(name);
            OrderItem orderitem = new OrderItem(accessory.getName(), accessory.getPrice(), accessory.getImage(), accessory.getRetailer());
            orderItems.remove(orderitem);
        }
    }

    /* getSmartphne Functions returns the Hashmap with all consoles in the store.*/
    public HashMap<String, SmartPhone> getSmartPhones() {
        HashMap<String, SmartPhone> hm = new HashMap<String, SmartPhone>();
        hm.putAll(SaxParserDataStore.smartphones);
        return hm;
    }

    /* getLaptop Functions returns the  Hashmap with all laptops in the store.*/
    public HashMap<String, Laptop> getLaptops() {
        HashMap<String, Laptop> hm = new HashMap<String, Laptop>();
        hm.putAll(SaxParserDataStore.laptops);
        return hm;
    }

    /* getTablet Functions returns the Hashmap with all Tablet in the store.*/
    public HashMap<String, Tablet> getTablets() {
        HashMap<String, Tablet> hm = new HashMap<String, Tablet>();
        hm.putAll(SaxParserDataStore.tablets);
        return hm;
    }

    /* getTV Functions returns the Hashmap with all TVs in the store.*/
    public HashMap<String, TV> getTVs() {
        HashMap<String, TV> hm = new HashMap<String, TV>();
        hm.putAll(SaxParserDataStore.tvs);
        return hm;
    }

    /* getProducts Functions returns the Arraylist of smartphones in the store.*/
    public ArrayList<String> getProductSmartPhones() {
        ArrayList<String> ar = new ArrayList<String>();
        for (Map.Entry<String, SmartPhone> entry : getSmartPhones().entrySet()) {
            ar.add(entry.getValue().getName());
        }
        return ar;
    }

    /* getProducts Functions returns the Arraylist of laptops in the store.*/
    public ArrayList<String> getProductLaptops() {
        ArrayList<String> ar = new ArrayList<String>();
        for (Map.Entry<String, Laptop> entry : getLaptops().entrySet()) {
            ar.add(entry.getValue().getName());
        }
        return ar;
    }

    /* getProducts Functions returns the Arraylist of Tablets in the store.*/
    public ArrayList<String> getProductTablets() {
        ArrayList<String> ar = new ArrayList<String>();
        for (Map.Entry<String, Tablet> entry : getTablets().entrySet()) {
            ar.add(entry.getValue().getName());
        }
        return ar;
    }

    /* getProducts Functions returns the Arraylist of tvs in the store.*/
    public ArrayList<String> getProductTVs() {
        ArrayList<String> ar = new ArrayList<String>();
        for (Map.Entry<String, TV> entry : getTVs().entrySet()) {
            ar.add(entry.getValue().getName());
        }
        return ar;
    }

    public void printStars(int stars) {
        for (int i = 0; i < stars; i++) {
            pw.print("<span class='glyphicon glyphicon-star' aria-hidden='true'></span>");
        }
    }
}
