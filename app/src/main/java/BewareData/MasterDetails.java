package BewareData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Created by Prasanna on 05-09-2015.
 */
public class MasterDetails {

   public static String sender_id="830753459977";
    public static String registeruser ="http://collegemateapp.com/Beware/StoreUserDetails.php";
    public static String CreatePost ="http://collegemateapp.com/Beware/CreatePost.php";
    public static String GetPolls= "http://collegemateapp.com/Beware/GetPost.php";
    public static String GetComments= "http://collegemateapp.com/Beware/GetComments.php";
    public static String PostComments= "http://collegemateapp.com/Beware/PostComment.php";

    public ArrayList<String> GetCityDetails()
    {
        ArrayList<String> City = new ArrayList<String>();
        City.add(0,"Velachery" );
        City.add(1,"Adayar" );
        City.add(2,"Nandambakkam" );
        City.add(3,"Adayar" );
        City.add(4,"Guindy" );
        return  City;
    }

    public  ArrayList<String> GetPostCategory()
    {
        ArrayList<String> Category = new ArrayList<String>();
        Category.add(0,"Places" );
        Category.add(1,"Hotels" );
        Category.add(2,"Health" );
        Category.add(3,"Girls Safety");
        Category.add(4,"Company");
        Category.add(2,"People" );
        Category.add(5,"Others");
        return  Category;
    }


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
