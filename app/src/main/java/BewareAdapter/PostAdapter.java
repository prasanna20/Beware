package BewareAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fyshadows.beware.CommentActivity;
import com.fyshadows.beware.R;

import java.util.List;

import BewareData.Post;

/**
 * Created by Prasanna on 06-09-2015.
 */
public class PostAdapter extends ArrayAdapter<Post> {
    private final Activity context;
    private static List<Post> list = null;



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
            viewHolder.txtCategory.setTextColor(Color.BLACK);

            viewHolder.txtPost = (TextView) view.findViewById(R.id.txtPost);
            viewHolder.txtPost.setTextColor(Color.BLACK);

            viewHolder.txtSubject = (TextView) view.findViewById(R.id.txtSubject);
            viewHolder.txtSubject.setTextColor(Color.BLACK);

            viewHolder.txtComment = (TextView) view.findViewById(R.id.txtComment);
            viewHolder.txtComment.setTextColor(Color.BLACK);

            viewHolder.txtCommentBy = (TextView) view.findViewById(R.id.txtCommentBy);
            viewHolder.txtCommentBy.setTextColor(Color.BLACK);

            viewHolder.txtHelpFull = (TextView) view.findViewById(R.id.txtHelpFull);
            viewHolder.txtHelpFull.setTextColor(Color.BLACK);

            viewHolder.txtNotHelpFull = (TextView) view.findViewById(R.id.txtNotHelpFull);
            viewHolder.txtNotHelpFull.setTextColor(Color.BLACK);

            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        if (!list.get(position).toString().trim().equalsIgnoreCase("")) {
            if(list.get(position).getCategory() != null) {
                holder.txtCategory.setText(list.get(position).getCategory().toString());
            }
            else
            {
                holder.txtCategory.setText("Category");
            }

            if(list.get(position).getPostText() != null) {
                holder.txtPost.setText(list.get(position).getPostText().toString());
            }
            else
            {
                holder.txtPost.setText("");
            }

            if(list.get(position).getSubject() != null) {
                holder.txtSubject.setText(list.get(position).getSubject().toString());
            }
            else
            {
                holder.txtSubject.setText("");
            }
            Log.i("postAdapter",list.get(position).getTopComment().toString());
            if(list.get(position).getTopComment() != null) {
                holder.txtComment.setText(list.get(position).getTopComment().toString());
            }
            else
            {
                holder.txtComment.setText("");
            }

            Log.i("postAdapter",list.get(position).getTopCommentUserName().toString());
            if(list.get(position).getTopCommentUserName() != null) {
                holder.txtCommentBy.setText(list.get(position).getTopCommentUserName().toString());
            }
            else
            {
                holder.txtCommentBy.setText("");
            }

          holder.txtHelpFull.setText(String.valueOf(list.get(position).getHelpFull()));
          holder.txtNotHelpFull.setText(String.valueOf(list.get(position).getNotHelpFull()));
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
    }

}