package BewareData;

/**
 * Created by Prasanna on 03-09-2015.
 */
public class Post {

    private int PostId,HelpFull,NotHelpFull;
    private String UserId,UserName,Category,Subject,PostText,TopComment,TopCommentUserName,TimeStamp;


    public Post() {
    }


    public Post(int PostId,String UserId, int HelpFull,int NotHelpFull,String UserName,String Category,String Subject,String PostText,String TopComment,String TopCommentUserName,String TimeStamp)
    {
        super();
        this.UserId =UserId;
        this.PostId=PostId;
        this.HelpFull=HelpFull;
        this.NotHelpFull=NotHelpFull;
        this.UserName=UserName;
        this.Category=Category;
        this.Subject=Subject;
        this.PostText=PostText;
        this.TopComment=TopComment;
        this.TopCommentUserName=TopCommentUserName;
        this.TimeStamp=TimeStamp;
    }

    public int getPostId() {return PostId;}

    public void setPostId(int PostId) {this.PostId = PostId;}


    public int getHelpFull() {return HelpFull;}

    public void setHelpFull(int HelpFull) {this.HelpFull = HelpFull;}

    public int getNotHelpFull() {return NotHelpFull;}

    public void setNotHelpFull(int NotHelpFull) {this.NotHelpFull = NotHelpFull;}

    public String getUserId() {return UserId;}

    public void setUserId(String UserId) {this.UserId = UserId;}

    public String getUserName() {return UserName;}

    public void setUserName(String UserName) {this.UserName = UserName;}

    public String getCategory() {return Category;}

    public void setCategory(String Category) {this.Category = Category;}

    public String getSubject() {return Subject;}

    public void setSubject(String Subject) {this.Subject = Subject;}

    public String getPostText() {return PostText;}

    public void setPostText(String PostText) {this.PostText = PostText;}

    public String getTopComment() {return TopComment;}

    public void setTopComment(String TopComment) {this.TopComment = TopComment;}

    public String getTopCommentUserName() {return TopCommentUserName;}

    public void setTopCommentUserName(String TopCommentUserName) {this.TopCommentUserName = TopCommentUserName;}

    public String getTimeStamp() {return TimeStamp;}

    public void setTimeStamp(String TimeStamp) {this.TimeStamp = TimeStamp;}

}
