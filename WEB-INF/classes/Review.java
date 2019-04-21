import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author Raymond
 */
//@WebServlet(urlPatterns = {"/Review"})
@WebServlet("/Review")
public class Review extends HttpServlet {

    //auto increment review id in database
    private String productName;
    private String productType;
    private String userName;
    private String reviewTitle;
    private String reviewDesc;
    private int reviewRating;
    private String reviewDate;

    public Review(String productName, String productType, String userName, String reviewTitle, String reviewDesc,
            int reviewRating, String reviewDate) {
        this.productName = productName;
        this.productType = productType;
        this.userName = userName;
        this.reviewTitle = reviewTitle;
        this.reviewDesc = reviewDesc;
        this.reviewRating = reviewRating;
        this.reviewDate = reviewDate;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the reviewTitle
     */
    public String getReviewTitle() {
        return reviewTitle;
    }

    /**
     * @param reviewTitle the reviewTitle to set
     */
    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    /**
     * @return the reviewDesc
     */
    public String getReviewDesc() {
        return reviewDesc;
    }

    /**
     * @param reviewDesc the reviewDesc to set
     */
    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }

    /**
     * @return the reviewRating
     */
    public int getReviewRating() {
        return reviewRating;
    }

    /**
     * @param reviewRating the reviewRating to set
     */
    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    /**
     * @return the reviewDate
     */
    public String getReviewDate() {
        return reviewDate;
    }

    /**
     * @param reviewDate the reviewDate to set
     */
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

   

}
