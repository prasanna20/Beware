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

import BewareData.Comment;
import BewareData.MasterDetails;


/**
 * Created by Prasanna on 08-09-2015.
 */


public class CommentsAdapter extends ArrayAdapter<Comment> {
    private final Activity context;
    private static List<Comment> list = null;



    public CommentsAdapter(Activity context,
                       List<Comment> list) {
        super(context, R.layout.commentrowview, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        Log.i("a", "a" + list.size());
        return list.size();
    }

    public static String getCommentDetailsPosition(int position) {
        return String.valueOf(list.get(position).getCommentId());
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;
        Log.i("a", "into get view");
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.commentrowview, null);
            final ViewHolder viewHolder = new ViewHolder();


            viewHolder.txtComment = (TextView) view.findViewById(R.id.txtComment);


            viewHolder.txtTimestamp = (TextView) view.findViewById(R.id.txtTimestamp);


            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        Log.i("CommentsAdapter", list.get(position).getUserName().toString());
        Log.i("CommentsAdapter", list.get(position).getCommentText().toString());
        Log.i("CommentsAdapter", list.get(position).getTimeStamp().toString());
        if (!list.get(position).toString().trim().equalsIgnoreCase("")) {

            if(list.get(position).getCommentText() != null) {
                holder.txtComment.setText(list.get(position).getCommentText().toString());
            }
            else
            {
                holder.txtComment.setText("");
            }

            if(list.get(position).getTimeStamp() != null) {
                holder.txtTimestamp.setText(MasterDetails.formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd MMM yyyy HH:mm", list.get(position).getTimeStamp().toString()));
            }
            else
            {
                holder.txtTimestamp.setText("");
            }


        }


        return view;
    }

    static class ViewHolder {
        protected TextView txtComment;
        protected TextView txtTimestamp;

    }


}