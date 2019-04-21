
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Raymond
 */
public class MyDataStoreUtilities extends HttpServlet {

    HttpServletRequest req;
    PrintWriter pw;
    HttpSession session;

    public MyDataStoreUtilities(HttpServletRequest req, PrintWriter pw) {
        this.req = req;
        this.pw = pw;
        this.session = req.getSession(true);
    }

    //method to setup database connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bestdeals?autoReconnect=true&useSSL=false", "root", "admin");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
            System.out.print("error insdie getConnection: " + e);
        }
        return conn;
    }

    //method to select user from database - registration table
    //returns false if username found.
    public static boolean selectUser(String userName) {
        Connection conn;
        ResultSet rs;
        //Selection Query
        // String getUserFromLoginQuery = ;

        //if empty = true, no user found, else user already exists with username
        boolean empty = true;
        //HM to store values retreived 
        HashMap<String, ArrayList<User>> selectedUserHM = new HashMap<>();
        try {
            conn = getConnection();
            System.out.println("Connection is closed: " + conn.isClosed());
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM registration WHERE username =?");
            pst.setString(1, userName);
            rs = pst.executeQuery();

            //store values into selectedUser hashMap and return HashMap selectedUser
            while (rs.next()) {
                //user already exists because username was retreived
                System.out.println(rs.getString("username"));
                empty = false;
            }
        }//end of try
        catch (Exception e) {
            System.out.print("Error inside selectUser: " + e);
        }
        return empty;
    }//end method selectUser

    //if not working, remove getConnection, change to static function, hardcode getconnection
    public static int insertUser(String userName, String password, String firstname, String lastname, String email, String userType) {
        Connection conn;
        //String insertInforCustomerRegisterQuery = ;

        int insertSuccess = 0;
        try {
            conn = getConnection();
            PreparedStatement pst = conn.prepareStatement("INSERT INTO REGISTRATION"
                    + "(username, password, firstName, lastName, email, usertype)"
                    + "VALUES (?,?,?,?,?,?)");
            pst.setString(1, userName);
            pst.setString(2, password);
            pst.setString(3, firstname);
            pst.setString(4, lastname);
            pst.setString(5, email);
            pst.setString(6, userType);
            insertSuccess = pst.executeUpdate();
        } catch (Exception e) {
            System.out.print("Error inside insertUser: " + e);
        }
        return insertSuccess;
    }//end method insertUser

    //method to select user from database - registration table
    public static HashMap<String, User> loginUser(String userName, String password, String userType) {
        Connection conn;
        ResultSet rs;
        //Selection Query
        //String getUserFromLoginQuery = ;

        //HM to store values retreived 
        HashMap<String, User> loginUserHM = new HashMap<>();
        try {
            conn = getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM registration WHERE username =? AND password =?");
            pst.setString(1, userName);
            pst.setString(2, password);

            rs = pst.executeQuery();
            //store values into selectedUser hashMap and return HashMap selectedUser
            while (rs.next()) {
                //user exists
                User currentUser = new User(rs.getString("username"), rs.getString("firstname"), rs.getString("lastname"),
                        rs.getString("email"), rs.getString("password"), rs.getString("usertype"));
                loginUserHM.put(rs.getString("username"), currentUser);
            }
        }//end of try
        catch (Exception e) {
            System.out.print("Error inside loginUser: " + e);
        }
        return loginUserHM;
    }//end method loginUser

    //iserts data into customer order table
    public static int insertOrder(String username, String orderName, double price, String userAddress, String creditCardNumber) {
        Connection conn;
        int insertSuccessFlag = 0;
        try {
            //get connection
            conn = getConnection();
            System.out.println("Connection is closed: " + conn.isClosed());
            PreparedStatement pst = conn.prepareStatement("INSERT INTO customerOrders (userName, orderName, price, userAddress, creditCardNumber)"
                    + " values (?,?,?,?,?)");
            pst.setString(1, username);
            pst.setString(2, orderName);
            pst.setDouble(3, price);
            pst.setString(4, userAddress);
            pst.setString(5, creditCardNumber);
            insertSuccessFlag = pst.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error in |insertOrder| : " + e);
        }
        return insertSuccessFlag;
    }//end storeProductOrders

    //function selects user order based on username from customerorders table
    public HashMap<Integer, ArrayList<OrderPayment>> selectOrder(String userName) {
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<>();
        Connection conn;
        ResultSet rs;
        try {
            conn = getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM customerorders WHERE username =?");
            pst.setString(1, userName);
            rs = pst.executeQuery();

            ArrayList<OrderPayment> orderList = new ArrayList<>();
            //store values into selectedUser hashMap and return HashMap selectedUser
            while (rs.next()) {
                System.out.println("Inside selectOrer|rs.next()|");
                if (!orderPayments.containsKey(rs.getInt("OrderId"))) {
                    ArrayList<OrderPayment> arr = new ArrayList<>();
                    orderPayments.put(rs.getInt("orderId"), arr);
                }
                ArrayList<OrderPayment> listOrderPayment = orderPayments.get(rs.getInt("OrderId"));
                OrderPayment order = new OrderPayment(rs.getInt("orderId"), rs.getString("userName"), rs.getString("orderName"),
                        rs.getDouble("price"), rs.getString("userAddress"), rs.getString("creditCardNumber"));
                listOrderPayment.add(order);
            }
        } catch (Exception e) {
            System.out.println("error in select order: " + e);
        }
        System.out.println("Inside selectOrder | OrderPayments is empty: " + orderPayments.isEmpty());
        return orderPayments;
    }

    //function to call insertOrder & selectOrder
    public void storePayment(int orderId, String orderName, double price, String userAddress, String creditCardNumber, String userName) {
        System.out.println("storePayment|: " + orderId);
        System.out.println("storePayment|: " + orderName);
        System.out.println("storePayment|: " + price);
        System.out.println("storePayment|: " + userAddress);
        System.out.println("storePayment|: " + creditCardNumber);
        System.out.println("storePayment|: " + userName);
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<>();
        try {
            orderPayments = selectOrder(userName);
            if (!orderPayments.containsKey(orderId)) {
                ArrayList<OrderPayment> arr = new ArrayList<>();
                orderPayments.put(orderId, arr);
            }
        } catch (Exception e) {
            System.out.println("Error in |storePayment|selectOrder: " + e);
        }

        try {
            ArrayList<OrderPayment> listOrderPayment = orderPayments.get(orderId);
            OrderPayment orderpayment = new OrderPayment(orderId, userName, orderName, price, userAddress, creditCardNumber);
            listOrderPayment.add(orderpayment);
        } catch (Exception e) {
            System.out.println("Error in |StorePayment|OrderPayment|" + e);
        }

        try {
            insertOrder(userName, orderName, price, userAddress, creditCardNumber);
        } catch (Exception e) {
            System.out.println("Error trying insert in storePayments: " + e);
        }
    }

    public User getUser(String username) {
        User user = new User();
        Connection conn;
        ResultSet rs;
        try {
            conn = getConnection();
            System.out.println("Connection is closed: " + conn.isClosed());
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM registration WHERE username =?");
            pst.setString(1, username);
            rs = pst.executeQuery();

            while (rs.next()) {
                user.setUserName(rs.getString("username"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastame(rs.getString("lastName"));
                user.setUserEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setUsertype(rs.getString("userType"));
            }
        } catch (Exception e) {
            System.out.println("Error in getUser| :" + e);
        }
        System.out.println("Inside getUser userType: " + user.getUsertype());
        return user;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String password, String userName) {
        Connection conn;
        ResultSet rs;
        try {
            conn = getConnection();
            System.out.println("Connection is closed: " + conn.isClosed());
            PreparedStatement pst = conn.prepareStatement("UPDATE registration SET "
                    + "password = ?, firstName = ?, lastName = ?, email = ? WHERE username =?");
            pst.setString(1, password);
            pst.setString(2, firstName);
            pst.setString(3, lastName);
            pst.setString(4, email);
            pst.setString(5, userName);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in updateUserInformation|: " + e);
        }
    }

    public HashMap<Integer, ArrayList<OrderPayment>> selectAllOrders() {
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<>();
        Connection conn;
        ResultSet rs;
        try {
            conn = getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM customerorders");
            rs = pst.executeQuery();

            ArrayList<OrderPayment> orderList = new ArrayList<>();
            //store values into selectedUser hashMap and return HashMap selectedUser
            while (rs.next()) {
                if (!orderPayments.containsKey(rs.getInt("OrderId"))) {
                    ArrayList<OrderPayment> arr = new ArrayList<>();
                    orderPayments.put(rs.getInt("orderId"), arr);
                }
                ArrayList<OrderPayment> listOrderPayment = orderPayments.get(rs.getInt("OrderId"));
                OrderPayment order = new OrderPayment(rs.getInt("OrderId"), rs.getString("userName"), rs.getString("orderName"),
                        rs.getDouble("price"), rs.getString("userAddress"), rs.getString("creditCardNumber"));
                listOrderPayment.add(order);
            }
        } catch (Exception e) {
            System.out.println("error in select order: " + e);
        }

        return orderPayments;
    }

    public static void handleOrderUpdate(Integer orderId, String userName, String orderName, Double orderPrice,
            String userAddress, String creditCardNo) {
        Connection conn;
        try {
            conn = getConnection();
            PreparedStatement pst = conn.prepareStatement("UPDATE customerorders SET "
                    + "userName = ?, orderName = ?, price = ?, userAddress = ?, creditCardNumber = ? WHERE orderID =?");
            pst.setString(1, userName);
            pst.setString(2, orderName);
            pst.setDouble(3, orderPrice);
            pst.setString(4, userAddress);
            pst.setString(5, creditCardNo);
            pst.setInt(6, orderId);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in handleOrderUpdate|: " + e);
        }
        System.out.println("Successful handleOrderUpdate");
    }

    public static void handleOrderDelete(Integer orderId, String userName, String orderName, Double orderPrice,
            String userAddress, String creditCardNo) {
        Connection conn;
        try {
            conn = getConnection();
            PreparedStatement pst = conn.prepareStatement("DELETE FROM customerorders WHERE orderID =?");
            pst.setInt(1, orderId);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error in handleOrderDelete|: " + e);
        }
        System.out.println("Successful handleOrderDelete");
    }
}
