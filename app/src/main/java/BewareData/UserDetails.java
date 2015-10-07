package BewareData;

/**
 * Created by Prasanna on 03-09-2015.
 */
public class UserDetails {

    public UserDetails() {
    }

    private String  UserId,  UserName,  EmailId ,State ,District ,GcmId;

    public UserDetails(String UserId, String UserName, String EmailId, String State,String District, String GcmId)
    {
        super();
        this.UserId=UserId;
        this.UserName=UserName;
        this.EmailId=EmailId;
        this.State=State;
        this.District=District;
        this.GcmId=GcmId;

    }

    public String getUserId() {return UserId;}

    public void setUserId(String UserId) {this.UserId = UserId;}

    public String getUserName() {return UserName;}

    public void setUserName(String UserName) {this.UserName = UserName;}

    public String getEmailId() {return EmailId;}

    public void setEmailId(String EmailId) {this.EmailId = EmailId;}

    public String getState() {return State;}

    public void setState(String State) {this.State = State;}

    public String getDistrict() {return District;}

    public void setDistrict(String District) {this.District = District;}

    public String getGcmId() {return GcmId;}

    public void setGcmId(String GcmId) {this.GcmId = GcmId;}


}
