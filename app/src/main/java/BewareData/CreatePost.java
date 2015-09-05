package BewareData;

/**
 * Created by Prasanna on 05-09-2015.
 */
public class CreatePost {

    private String UserId,Category,Subject,PostText;
    private int anonymous;

    public CreatePost() {
    }


    public CreatePost(String UserId, String Category, String Subject, String PostText,int anonymous)
    {
        super();
        this.UserId=UserId;
        this.Category=Category;
        this.Subject=Subject;
        this.PostText=PostText;
        this.anonymous=anonymous;
    }


    public String getUserId() {return UserId;}

    public void setUserId(String UserId) {this.UserId = UserId;}

    public String getCategory() {return Category;}

    public void setCategory(String Category) {this.Category = Category;}

    public String getSubject() {return Subject;}

    public void setSubject(String Subject) {this.Subject = Subject;}

    public String getPostText() {return PostText;}

    public void setPostText(String PostText) {this.PostText = PostText;}

    public int getAnonymous() {return anonymous;}

    public void setAnonymous(int anonymous) {this.anonymous = anonymous;}



}
