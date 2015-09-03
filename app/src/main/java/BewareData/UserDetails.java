package BewareData;

/**
 * Created by Prasanna on 03-09-2015.
 */
public class UserDetails {

    public UserDetails() {
    }

    private String  UserId,  UserName,  EmailId,  Location,  GcmId;

    public UserDetails(String UserId, String UserName, String EmailId, String Location, String GcmId)
    {
        super();
        this.UserId=UserId;
        this.UserName=UserName;
        this.EmailId=EmailId;
        this.Location=Location;
        this.GcmId=GcmId;

    }

    public String getUserId() {return UserId;}

    public void setUserId(String UserId) {this.UserId = UserId;}

    public String getUserName() {return UserName;}

    public void setUserName(String UserName) {this.UserName = UserName;}

    public String getEmailId() {return EmailId;}

    public void setEmailId(String EmailId) {this.EmailId = EmailId;}

    public String getLocation() {return Location;}

    public void setLocation(String Location) {this.Location = Location;}

    public String getGcmId() {return GcmId;}

    public void setGcmId(String GcmId) {this.GcmId = GcmId;}


}
