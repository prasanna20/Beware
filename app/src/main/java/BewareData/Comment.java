package BewareData;

/**
 * Created by Prasanna on 07-09-2015.
 */
public class Comment {

    private String UserName, CommentText, TimeStamp;
    private int CommentId;

    public Comment()
    {

    }

    public Comment(Comment Comment) {

        this.CommentId = Comment.CommentId;
        this.UserName = Comment.UserName;
        this.CommentText = Comment.CommentText;
        this.TimeStamp = Comment.TimeStamp;

    }

    public int getCommentId() {return CommentId;}

    public void setCommentId(int PostId) {this.CommentId = CommentId;}

    public String getUserName() {return UserName;}

    public void setUserName(String UserName) {this.UserName = UserName;}

    public String getCommentText() {return CommentText;}

    public void setCommentText(String CommentText) {this.CommentText = CommentText;}

    public String getTimeStamp() {return TimeStamp;}

    public void setTimeStamp(String TimeStamp) {this.TimeStamp = TimeStamp;}

}
