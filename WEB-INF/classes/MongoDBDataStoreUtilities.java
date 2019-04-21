
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/MongoDBDataStoreUtilities"})
public class MongoDBDataStoreUtilities extends HttpServlet {

    static DBCollection myReviews;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    //change later for wide range use
    public static void getConnection() {
        MongoClient mongo;
        try {
            mongo = new MongoClient("localhost", 27017);
            DB db = mongo.getDB("CustomerReviews");
            //connects to myReviewsCollection
            myReviews = db.getCollection("myReviews");
        } catch (Exception e) {
            System.out.print("error insdie getConnection: " + e);
        }
    }//end getConnection - mongodb

    //method to store reviews using - insert and select review function
    public static void storeReview(String productName, String productType, String userName, String reviewTitle,
            String reviewDesc, int reviewRating, String reviewDate, String zipCode) {

        try {
            MongoDBDataStoreUtilities.insertReview(productName, productType, userName, reviewTitle, reviewDesc, reviewRating, reviewDate, zipCode);
        } catch (Exception e) {
            System.out.println("Error inside storeReview | " + e);
        }

    }

    //select review and return as HM
    public static HashMap<String, ArrayList<Review>> selectReview(String productName) {
        getConnection();
        BasicDBObject query = new BasicDBObject();
        query.put("productName", productName);
        DBCursor cursor = null;
        try {

            System.out.println("Inside select review | special check:" + productName);
            cursor = myReviews.find(query);

        } catch (Exception e) {
            System.out.println("Inside select review | check 1 | " + e);
        }

        HashMap<String, ArrayList<Review>> reviewHM = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();

                if (!reviewHM.containsKey(obj.getString("productName"))) { //check this - changed from !db. to !reviewHM

                    ArrayList<Review> arr = new ArrayList<>();
                    reviewHM.put(obj.getString("productName"), arr);
                }

                ArrayList<Review> listReview = reviewHM.get(obj.getString("productName"));
                Review review = new Review(obj.getString("productName"), obj.getString("productType"), obj.getString("userName"),
                        obj.getString("reviewTitle"), obj.getString("reviewDesc"), obj.getInt("reviewRating"), obj.getString("reviewDate"));

                listReview.add(review);

            }

        } catch (Exception e) {
            System.out.println("SelectReview | Check 2: " + e);
        } finally {
            cursor.close();
        }
        return reviewHM;
    }//end selectReview

    //insert new items into review
    public static void insertReview(String productName, String productType, String userName, String reviewTitle,
            String reviewDesc, int reviewRating, String reviewDate, String zipCode) {
        getConnection();
        BasicDBObject doc = new BasicDBObject("title", "myReviews").
                append("productName", productName).
                append("productType", productType).
                append("userName", userName).
                append("reviewTitle", reviewTitle).
                append("reviewDesc", reviewDesc).
                append("reviewRating", reviewRating).
                append("reviewDate", reviewDate).
                append("zipCode", zipCode);
        myReviews.insert(doc);
    }//end insertReview

    //select review and return as HM
    public static long reviewCount(String productName) {
        getConnection();
        long count = 0;
        BasicDBObject query = new BasicDBObject();
        query.put("productName", productName);

        try {
            count = myReviews.count(query);
        } catch (Exception e) {
            System.out.println("Error inside reviewCount: " + e);
        }
        return count;
    }

    public static HashMap<String, ArrayList<Review>> topLikedProducts() {
        getConnection();
        BasicDBObject query = new BasicDBObject();
        query.put("reviewRating", 5);
        DBCursor cursor = null;
        try {
            cursor = myReviews.find(query);
        } catch (Exception e) {
        }

        HashMap<String, ArrayList<Review>> reviewHM = new HashMap<>();
        try {
            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();

                if (!reviewHM.containsKey(obj.getString("productName"))) { //check this - changed from !db. to !reviewHM

                    ArrayList<Review> arr = new ArrayList<>();
                    reviewHM.put(obj.getString("productName"), arr);
                }

                ArrayList<Review> listReview = reviewHM.get(obj.getString("productName"));
                Review review = new Review(obj.getString("productName"), obj.getString("productType"), obj.getString("userName"),
                        obj.getString("reviewTitle"), obj.getString("reviewDesc"), obj.getInt("reviewRating"), obj.getString("reviewDate"));

                listReview.add(review);
            }
        } catch (Exception e) {
            System.out.println("SelectReview | Check 2: " + e);
        } finally {
            cursor.close();
        }
        return reviewHM;
    }

    public static AggregationOutput topReviewedProducts() {
        getConnection();
        DBObject groupFields = new BasicDBObject("_id", "$productName");
        groupFields.put("count", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);
        // count descending
        DBObject sortFields = new BasicDBObject("count", -1);
        DBObject sort = new BasicDBObject("$sort", sortFields);

        AggregationOutput output = myReviews.aggregate(group, sort);

        /*
        TreeMap<String, String> sortedTM = new TreeMap<>();
        for (DBObject dbObject : output.results()) {
            sortedTM.put(dbObject.get("_id").toString(), dbObject.get("count").toString());
        }
         */
        return output;
    }

    public static AggregationOutput topReZipCodeBasedProducts() {
        getConnection();
        DBObject groupFields = new BasicDBObject("_id", 0);
        groupFields.put("count", new BasicDBObject("$sum", 1));
        groupFields.put("_id", "$zipCode");
        
        DBObject group = new BasicDBObject("$group", groupFields);
        DBObject sort = new BasicDBObject();
        
        HashMap<String, String> projectFields = new HashMap<>();
        projectFields.put("zipCode", "$_id");
        projectFields.put("reviewRating", "$count");
        
        DBObject project = new BasicDBObject("$project", projectFields);
        sort.put("reviewRating", -1);
        
        DBObject orderby = new BasicDBObject("$sort", sort);
        DBObject limit = new BasicDBObject("$limit", 5);
        AggregationOutput output = myReviews.aggregate(group, project, orderby, limit);
        
        System.out.println("Results from top zip: " + output.results());
        return output;
    }
}
