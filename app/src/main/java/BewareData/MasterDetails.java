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
