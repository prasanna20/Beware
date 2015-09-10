package BewareAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fyshadows.beware.CommentActivity;
import com.fyshadows.beware.Home;
import com.fyshadows.beware.JSONParser;
import com.fyshadows.beware.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.UserDetails;
import BewareDatabase.BewareDatabase;

/**
 * Created by Prasanna on 06-09-2015.
 */
public class PostAdapter extends ArrayAdapter<Post> {
    private final Activity context;
    private static List<Post> list = null;
    private int PostID;



    public PostAdapter(Activity context,
                            List<Post> list) {
        super(context, R.layout.postrowview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static String getPostDetailsPosition(int position) {
        return String.valueOf(list.get(position).getPostId());
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.postrowview, null);
            final ViewHolder viewHolder = new ViewHolder();


            viewHolder.txtCategory = (TextView) view.findViewById(R.id.txtCategory);
            viewHolder.txtCategory.setTextColor(Color.WHITE);

            viewHolder.txtPost = (TextView) view.findViewById(R.id.txtPost);
            viewHolder.txtPost.setTextColor(Color.WHITE);

            viewHolder.txtSubject = (TextView) view.findViewById(R.id.txtSubject);
            viewHolder.txtSubject.setTextColor(Color.WHITE);

            viewHolder.txtComment = (TextView) view.findViewById(R.id.txtComment);
            viewHolder.txtComment.setTextColor(Color.WHITE);

            viewHolder.txtCommentBy = (TextView) view.findViewById(R.id.txtCommentBy);
            viewHolder.txtCommentBy.setTextColor(Color.WHITE);

            viewHolder.txtHelpFull = (TextView) view.findViewById(R.id.txtHelpFull);
            viewHolder.txtHelpFull.setTextColor(Color.WHITE);

            viewHolder.txtNotHelpFull = (TextView) view.findViewById(R.id.txtNotHelpFull);
            viewHolder.txtNotHelpFull.setTextColor(Color.WHITE);

            viewHolder.txtTimeStamp = (TextView) view.findViewById(R.id.txtTimeStamp);
            viewHolder.txtTimeStamp.setTextColor(Color.WHITE);

            viewHolder.btnHelpFull = (Button) view.findViewById(R.id.btnHelpFull);
            viewHolder.btnHelpFull.setTextColor(Color.WHITE);

            viewHolder.btnNotHelpFull = (Button) view.findViewById(R.id.btnNotHelpFull);
            viewHolder.btnNotHelpFull.setTextColor(Color.WHITE);

            viewHolder.catImage = (ImageView) view.findViewById(R.id.categoryimage);

            viewHolder.topbase = (RelativeLayout) view.findViewById(R.id.topbase);

            viewHolder.bottombar = (LinearLayout) view.findViewById(R.id.bottombar);



            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (!list.get(position).toString().trim().equalsIgnoreCase("")) {
            if(list.get(position).getCategory().toString() != null) {
                holder.txtCategory.setText(list.get(position).getCategory().toString());
            }
            else
            {
                holder.txtCategory.setText("Category");
            }

            if(list.get(position).getPostText() != null) {
                if(list.get(position).getPostText().toString().length()>150 )//&& myposition != position
                {
                    String desc=	list.get(position).getPostText().toString().substring(0, 150)+ "  Read more...";
                    Spannable WordtoSpan = new SpannableString(desc);
                    WordtoSpan.setSpan(new ForegroundColorSpan(Color.rgb(248, 186, 20)),  101, 112, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.txtPost.setText(WordtoSpan);
                }
                else
                {
                    holder.txtPost.setText(list.get(position).getPostText().toString());
                }
                holder.txtPost.setText(list.get(position).getPostText().toString());
            }
            else
            {
                holder.txtPost.setText("");
            }

            if(list.get(position).getSubject().toString()!= null) {
                holder.txtSubject.setText(list.get(position).getSubject().toString());
            }
            else
            {
                holder.txtSubject.setText("");
            }
            Log.i("postAdapter",list.get(position).getTopComment().toString());
            if(!list.get(position).getTopComment().equalsIgnoreCase("null")) {
                holder.txtComment.setText(list.get(position).getTopComment().toString());
            }
            else
            {
                holder.txtComment.setText("");
            }

            Log.i("postAdapter",list.get(position).getTopCommentUserName().toString());
            if(!list.get(position).getTopCommentUserName().toString().equalsIgnoreCase("null")) {
                holder.txtCommentBy.setText(list.get(position).getTopCommentUserName().toString());
            }
            else
            {
                holder.txtCommentBy.setText("");
            }

            if(list.get(position).getTimeStamp().toString() != null) {
                holder.txtTimeStamp.setText(list.get(position).getTimeStamp().toString());
            }
            else
            {
                holder.txtTimeStamp.setText("");
            }



          holder.txtHelpFull.setText(String.valueOf(list.get(position).getHelpFull()));
          holder.txtNotHelpFull.setText(String.valueOf(list.get(position).getNotHelpFull()));


            //Start : To Set Icon for category
           
            if(list.get(position).getCategory().toString().equalsIgnoreCase("Places")) {
                holder.catImage.setImageResource(R.drawable.places);
                holder.topbase.setBackgroundColor(Color.parseColor("#53d37e"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#47c772"));
            }
            else if (list.get(position).getCategory().toString().equalsIgnoreCase("Hotels"))
            {
                holder.catImage.setImageResource(R.drawable.hotels);
                holder.topbase.setBackgroundColor(Color.parseColor("#ce5250"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#c94d4d"));
            }
            else if (list.get(position).getCategory().toString().equalsIgnoreCase("Health"))
            {
                holder.catImage.setImageResource(R.drawable.health);
                holder.topbase.setBackgroundColor(Color.parseColor("#58bbb8"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#4fb2af"));
            }
            else if (list.get(position).getCategory().toString().equalsIgnoreCase("Girls Safety"))
            {
                holder.catImage.setImageResource(R.drawable.safety);
                holder.topbase.setBackgroundColor(Color.parseColor("#e2e02f"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#cecc26"));
            }
            else if (list.get(position).getCategory().toString().equalsIgnoreCase("Company"))
            {
                holder.catImage.setImageResource(R.drawable.company);
                holder.topbase.setBackgroundColor(Color.parseColor("#eea043"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#d88b31"));
            }
            else if (list.get(position).getCategory().toString().equalsIgnoreCase("People"))
            {
                holder.catImage.setImageResource(R.drawable.people);
                holder.topbase.setBackgroundColor(Color.parseColor("#c66dd6"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#b24bc4"));
            }
            else if (list.get(position).getCategory().toString().equalsIgnoreCase("Others"))
            {
                holder.catImage.setImageResource(R.drawable.others);
                holder.topbase.setBackgroundColor(Color.parseColor("#7988e5"));
                holder.bottombar.setBackgroundColor(Color.parseColor("#5969c8"));
            }
            //End : To set Icon for Category


        }

        holder.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("PostId", list.get(position).getPostId());
                i.putExtras(bundle);
                context.startActivity(i);
            }

        });

        holder.btnHelpFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new asyncGetLatestPost(list.get(position).getPostId(),1).execute();
                list.get(position).setHelpFull(list.get(position).getHelpFull() + 1);
            }

        });

        holder.btnNotHelpFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new asyncGetLatestPost(list.get(position).getPostId(),2).execute();
                list.get(position).setNotHelpFull(list.get(position).getNotHelpFull() + 1);
            }

        });

        holder.txtPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setPostText(list.get(position).getPostText().toString());
            }

        });
        return view;
    }

    static class ViewHolder {
        protected TextView txtCategory;
        protected TextView txtSubject;
        protected TextView txtPost;
        protected TextView txtComment;
        protected TextView txtCommentBy;
        protected TextView txtHelpFull;
        protected TextView txtNotHelpFull;
        protected TextView txtTimeStamp;
        protected Button btnHelpFull;
        protected Button btnNotHelpFull;
        protected ImageView catImage;
        protected RelativeLayout topbase;
        protected LinearLayout bottombar;
    }

    public class asyncGetLatestPost extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();
        int PostId;
        int Flag;
        public asyncGetLatestPost(int oPostId,int oflag ) {
            super();
            PostId=oPostId;
            Flag=oflag;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                MasterDetails MasterDetails = new MasterDetails();
                BewareDatabase db = new BewareDatabase(getContext());


                int success;
                List<NameValuePair> params = new ArrayList<NameValuePair>();


                params.clear();

                params.add(new BasicNameValuePair("PostId", String.valueOf(PostId)));
                params.add(new BasicNameValuePair("Flag", String.valueOf(Flag)));

                JSONObject  json = jsonParser.makeHttpRequest(MasterDetails.UpdateHelpFull, "GET", params);
                Log.i("SplashScreen", "got json");
                if (json.length() > 0) {
                    // json success tag
                    success = json.getInt("success");
                    if (success == 1) {
                        Log.i("SplashScreen", "Success");

                    }
                }
            } catch (JSONException e) {
                Log.i("SplashScreen", "Executing Async activity");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

}