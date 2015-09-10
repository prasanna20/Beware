package BewareData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
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
    public static String UpdateHelpFull= "http://collegemateapp.com/Beware/UpdateHelpFull.php";


    public ArrayList<String> GetCityDetails()
    {
        ArrayList<String> City = new ArrayList<String>();
        City.add(0,"Adambakkam");
        City.add(1,"Adyar");
        City.add(2,"Alandur");
        City.add(3,"Alwarpet");
        City.add(4,"Alwarthirunagar");
        City.add(5,"Ambattur");
        City.add(6,"Aminjikarai");
        City.add(7,"Anna Nagar");
        City.add(8,"Annanur");
        City.add(9,"Arumbakkam");
        City.add(10,"Ashok Nagar");
        City.add(11,"Avadi");
        City.add(12,"Ayanavaram");
        City.add(13,"Besant Nagar");
        City.add(14,"Basin Bridge");
        City.add(15,"Chepauk");
        City.add(16,"Chetput");
        City.add(17,"Chintadripet");
        City.add(18,"Chitlapakkam");
        City.add(19,"Choolai");
        City.add(20,"Choolaimedu");
        City.add(21,"Chrompet");
        City.add(22,"Egmore");
        City.add(23,"Ekkaduthangal");
        City.add(24,"Ennore");
        City.add(25,"Foreshore Estate");
        City.add(26,"Fort St. George");
        City.add(27,"George Town");
        City.add(28,"Gopalapuram");
        City.add(29,"Government Estate");
        City.add(30,"Guindy");
        City.add(31,"Guduvanchery");
        City.add(32,"IIT Madras");
        City.add(33,"Injambakkam");
        City.add(34,"ICF");
        City.add(35,"Iyyapanthangal");
        City.add(36,"Jafferkhanpet");
        City.add(37,"Karapakkam");
        City.add(38,"Kattivakkam");
        City.add(39,"Kazhipattur");
        City.add(40,"K.K. Nagar");
        City.add(41,"Keelkattalai");
        City.add(42,"Kelambakkam");
        City.add(43,"Kilpauk");
        City.add(44,"Kodambakkam");
        City.add(45,"Kodungaiyur");
        City.add(46,"Kolathur");
        City.add(47,"Korattur");
        City.add(48,"Korukkupet");
        City.add(49,"Kottivakkam");
        City.add(50,"Kotturpuram");
        City.add(51,"Kottur");
        City.add(52,"Kovalam");
        City.add(53,"Kovilambakkam");
        City.add(54,"Koyambedu");
        City.add(55,"Kundrathur");
        City.add(56,"Madhavaram");
        City.add(57,"Madhavaram Milk Colony");
        City.add(58,"Madipakkam");
        City.add(59,"Madambakkam");
        City.add(60,"Maduravoyal");
        City.add(61,"Manali");
        City.add(62,"Manali New Town");
        City.add(63,"Manapakkam");
        City.add(64,"Mandaveli");
        City.add(65,"Mangadu");
        City.add(66,"Mannadi");
        City.add(67,"Mathur");
        City.add(68,"Medavakkam");
        City.add(69,"Meenambakkam");
        City.add(70,"Minjur");
        City.add(71,"Mogappair");
        City.add(72,"MKB Nagar");
        City.add(73,"Mount Road");
        City.add(74,"Moolakadai");
        City.add(75,"Moulivakkam");
        City.add(76,"Mugalivakkam");
        City.add(77,"Mylapore");
        City.add(78,"Nandanam");
        City.add(79,"Nanganallur");
        City.add(80,"Navalur");
        City.add(81,"Neelankarai");
        City.add(82,"Nemilichery");
        City.add(83,"Nesapakkam");
        City.add(84,"Nolambur");
        City.add(85,"Noombal");
        City.add(86,"Nungambakkam");
        City.add(87,"Ottery");
        City.add(88,"Padi");
        City.add(89,"Pakkam");
        City.add(90,"Palavakkam");
        City.add(91,"Pallavaram");
        City.add(92,"Pallikaranai");
        City.add(93,"Pammal");
        City.add(94,"Park Town");
        City.add(95,"Parry's Corner");
        City.add(96,"Pattabiram");
        City.add(97,"Pattaravakkam");
        City.add(98,"Pazhavanthangal");
        City.add(99,"Peerkankaranai");
        City.add(100,"Perambur");
        City.add(101,"Peravallur");
        City.add(102,"Perumbakkam");
        City.add(103,"Perungalathur");
        City.add(104,"Perungudi");
        City.add(105,"Pozhichalur");
        City.add(106,"Poonamallee");
        City.add(107,"Porur");
        City.add(108,"Pudupet");
        City.add(109,"Purasaiwalkam");
        City.add(110,"Puthagaram");
        City.add(111,"Puzhal");
        City.add(112,"Puzhuthivakkam");
        City.add(113,"Raj Bhavan");
        City.add(114,"Ramavaram");
        City.add(115,"Red Hills");
        City.add(116,"Royapettah");
        City.add(117,"Royapuram");
        City.add(118,"Saidapet");
        City.add(119,"Saligramam");
        City.add(120,"Santhome");
        City.add(121,"Selaiyur");
        City.add(122,"Shenoy Nagar");
        City.add(123,"Sholavaram");
        City.add(124,"Sholinganallur");
        City.add(125,"Sithalapakkam");
        City.add(126,"Sowcarpet");
        City.add(127,"St.Thomas Mount");
        City.add(128,"Tambaram");
        City.add(129,"Teynampet");
        City.add(130,"Tharamani");
        City.add(131,"T. Nagar");
        City.add(132,"Thirumangalam");
        City.add(133,"Thirumullaivoyal");
        City.add(134,"Thiruneermalai");
        City.add(135,"Thiruninravur");
        City.add(136,"Thiruvanmiyur");
        City.add(137,"Tiruverkadu");
        City.add(138,"Thiruvotriyur");
        City.add(139,"Tirusulam");
        City.add(140,"Tiruvallikeni");
        City.add(141,"Tondiarpet");
        City.add(142,"United India Colony");
        City.add(143,"Urapakkam");
        City.add(144,"Vandalur");
        City.add(145,"Vadapalani");
        City.add(146,"Valasaravakkam");
        City.add(147,"Vallalar Nagar");
        City.add(148,"Vanagaram");
        City.add(149,"Velachery");
        City.add(150,"Veppampattu");
        City.add(151,"Villivakkam");
        City.add(152,"Virugambakkam");
        City.add(153,"Vyasarpadi");
        City.add(154,"Washermanpet");
        City.add(155,"West Mambalam");
        return  City;
    }

    public  ArrayList<String> GetPostCategory()
    {
        ArrayList<String> Category = new ArrayList<String>();
        Category.add(0,"Places" );
        Category.add(1,"Food" );
        Category.add(2,"Girls Safety" );
        Category.add(3,"Others");
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

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        java.util.Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            try {
                parsed = df_input.parse(inputDate);
            } catch (java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            outputDate = df_output.format(parsed);
            Log.i("date", outputDate);

        } catch (ParseException e) {
            Log.i("date", "ParseException - dateFormat");
        }

        return outputDate;

    }

}
