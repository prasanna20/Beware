package BewareDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

import com.fyshadows.beware.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import BewareData.MasterDetails;
import BewareData.Post;
import BewareData.UserDetails;

/**
 * Created by Prasanna on 03-09-2015.
 */


public class BewareDatabase extends SQLiteOpenHelper {

    JSONParser jsonParser = new JSONParser();


    public BewareDatabase(Context context) {

        super(context, "BewareDatabase", null, 1);// version number is given at

    }

    private boolean doesDatabaseExist(ContextWrapper context) {
        File dbFile = context.getDatabasePath("BewareDatabase");
        return dbFile.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase myDB) {

        myDB.execSQL("CREATE TABLE if not exists bw_UserDetails(UserId varchar(50),UserName varchar(250),EmailId varchar(250),Location varchar(50),GcmId Text,TimeStamp  REAL DEFAULT (datetime('now','localtime'))  );");

        myDB.execSQL("CREATE TABLE if not exists bw_Post(PostId int,UserId  varchar(50),UserName varchar(250),Category varchar(250),Subject varchar(250),PostText Text,HelpFull Int ,NotHelpFull int,TopComment varchar(250),TopCommentUserName varchar(250),helpFlag int Default 0 ,TimeStamp  REAL DEFAULT (datetime('now','localtime')) );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /*Start :- bw_UserDetails */
    public Boolean InsertUserDetails(UserDetails UserDetails) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("UserId", UserDetails.getUserId());
            values.put("UserName", UserDetails.getUserName());
            values.put("EmailId", UserDetails.getEmailId());
            values.put("Location", UserDetails.getLocation());
            values.put("GcmId", UserDetails.getGcmId());

            db.delete("bw_UserDetails", null, null);
            db.insert("bw_UserDetails", null, values);



            //Store it in server
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("UserId", UserDetails.getUserId()));
            params.add(new BasicNameValuePair("UserName", UserDetails.getUserName()));
            params.add(new BasicNameValuePair("EmailId", UserDetails.getEmailId()));
            params.add(new BasicNameValuePair("Location", UserDetails.getLocation()));
            params.add(new BasicNameValuePair("GcmId", UserDetails.getGcmId()));
            JSONObject json = jsonParser.makeHttpRequest(
                    MasterDetails.registeruser, "GET", params);


            int success = json.getInt("success");


            if (success == 1) {
                return true;
            } else if (success == 2) {
                String UserId= json.getString("UserId");
                String sql = "UPDATE bw_UserDetails  SET UserId= '"+UserId+"'" ;
                db.execSQL(sql);
                if (db.isOpen()) {
                    db.close();
                }
                return true;
            } else {
                if (db.isOpen()) {
                    db.close();
                }
                return false;
            }

        } catch (JSONException e) {
            Log.i("BewareDatabase", "Insert Failed");
            return false;
        }

    }


    public ArrayList<UserDetails> getUserDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<UserDetails> list = new ArrayList<UserDetails>();

        String selectQuery = "select UserId ,UserName ,EmailId ,Location ,GcmId from bw_UserDetails ";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (null != cursor && cursor.moveToFirst()) {

            int _UserId = cursor.getColumnIndex("UserId");
            int _UserName = cursor.getColumnIndex("UserName");
            int _EmailId = cursor.getColumnIndex("EmailId");
            int _Location = cursor.getColumnIndex("Location");
            int _GcmId = cursor.getColumnIndex("GcmId");

            if (cursor.moveToFirst()) {

                String UserId = cursor.getString(_UserId);
                String UserName = cursor.getString(_UserName);
                String EmailId = cursor.getString(_EmailId);
                String Location = cursor.getString(_Location);
                String GcmId = cursor.getString(_GcmId);

                list.add(new UserDetails(UserId, UserName, EmailId, Location, GcmId));
            }
        }
        return list;

    }

    public String GetUserId() {
        String UserId;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Post> PostList = new ArrayList<Post>();
        Cursor mCursor = db.query(true, "bw_UserDetails", new String[]{
                        "UserId"},
                null,
                null,
                null,
                null,
                null,
                null);

        if (mCursor.moveToFirst()) {
            UserId = mCursor.getString(mCursor.getColumnIndexOrThrow("UserId"));
        } else {
            UserId = "Anonymous";
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return UserId;

    }

/*END :- bw_UserDetails */

    /*Start :- bw_Post*/
    public void InsertPost(int PostId, int HelpFull, int NotHelpFull, String UserName, String Category, String Subject, String PostText, String TopComment, String TopCommentUserName, String TimeStamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("PostId", PostId);
        values.put("HelpFull", HelpFull);
        values.put("NotHelpFull", NotHelpFull);
        values.put("UserName", UserName);
        values.put("Category", Category);
        values.put("Subject", Subject);
        values.put("PostText", PostText);
        values.put("TopComment", TopComment);
        values.put("TopCommentUserName", TopCommentUserName);
        values.put("TimeStamp", TimeStamp);

        db.insert("bw_Post", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }

    public void InsertPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("PostId", post.getPostId());
        values.put("UserId", post.getUserId());
        values.put("HelpFull", post.getHelpFull());
        values.put("NotHelpFull", post.getNotHelpFull());
        values.put("UserName", post.getUserName());
        values.put("Category", post.getCategory());
        values.put("Subject", post.getSubject());
        values.put("PostText", post.getPostText());
        values.put("TopComment", post.getTopComment());
        values.put("TopCommentUserName", post.getTopCommentUserName());
        values.put("TimeStamp", post.getTimeStamp());
        values.put("helpFlag", 0);

        db.insert("bw_Post", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }

    /*Reference
    * public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs,
    * String groupBy, String having, String orderBy, String limit)*/

    public ArrayList<Post> getPostOnCategory(String Category) throws SQLException {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Post> PostList = new ArrayList<Post>();
        String selectQuery;


        if (Category.equalsIgnoreCase("No")) {
            selectQuery = "SELECT  PostId,UserId,HelpFull,NotHelpFull,UserName,Category,Subject,PostText,TopComment,TopCommentUserName,TimeStamp,helpFlag from bw_Post  Order By TimeStamp desc";

        } else if (Category.equalsIgnoreCase("MyPost")) {
            selectQuery = "SELECT  PostId,UserId,HelpFull,NotHelpFull,UserName,Category,Subject,PostText,TopComment,TopCommentUserName,TimeStamp,helpFlag from bw_Post where UserId ='" + GetUserId() + "'  Order By TimeStamp desc";
        } else {
            selectQuery = "SELECT  PostId,UserId,HelpFull,NotHelpFull,UserName,Category,Subject,PostText,TopComment,TopCommentUserName,TimeStamp,helpFlag from bw_Post where Category='" + Category + "'";
        }

        Log.i("BewareDatabase", selectQuery);
        Cursor mCursor = db.rawQuery(selectQuery, null);


        if (mCursor.moveToFirst()) {
            do {
                Post objPost = new Post();
                objPost.setPostId(mCursor.getInt(mCursor.getColumnIndexOrThrow("PostId")));
                objPost.setUserId(mCursor.getString(mCursor.getColumnIndexOrThrow("UserId")));
                objPost.setHelpFull(mCursor.getInt(mCursor.getColumnIndexOrThrow("HelpFull")));
                objPost.setNotHelpFull(mCursor.getInt(mCursor.getColumnIndexOrThrow("NotHelpFull")));
                objPost.setUserName(mCursor.getString(mCursor.getColumnIndexOrThrow("UserName")));
                objPost.setCategory(mCursor.getString(mCursor.getColumnIndexOrThrow("Category")));
                objPost.setSubject(mCursor.getString(mCursor.getColumnIndexOrThrow("Subject")));
                objPost.setPostText(mCursor.getString(mCursor.getColumnIndexOrThrow("PostText")));
                objPost.setTopComment(mCursor.getString(mCursor.getColumnIndexOrThrow("TopComment")));
                objPost.setTopCommentUserName(mCursor.getString(mCursor.getColumnIndexOrThrow("TopCommentUserName")));
                objPost.setTimeStamp(mCursor.getString(mCursor.getColumnIndexOrThrow("TimeStamp")));
                objPost.sethelpFlag(mCursor.getInt(mCursor.getColumnIndexOrThrow("helpFlag")));

                Log.i("BewareDatabaseTop", mCursor.getString(mCursor.getColumnIndexOrThrow("TopComment")));

                Log.i("BewareDatabase", mCursor.getString(mCursor.getColumnIndexOrThrow("Subject")));

                PostList.add(objPost);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return PostList;

    }

    public int getMaxPostId() throws SQLException {

        int PostId = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Post> PostList = new ArrayList<Post>();

        String selectQuery;

        selectQuery = "SELECT  max(PostId) PostId from bw_Post";

        Cursor mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {

            PostId = mCursor.getInt(mCursor.getColumnIndexOrThrow("PostId"));
        }

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        return PostId;

    }

/*End : bw_Post*/


    /* Start : Update Helpfull/Not helpfull*/
    public Boolean UpdateVote(int postId,int HelpFlag) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql;

            if(HelpFlag==1) {
                sql = "UPDATE bw_Post  SET HelpFull = HelpFull + 1,helpFlag=1 where PostId=" + postId;
            }else {
                sql = "UPDATE bw_Post  SET NotHelpFull= NotHelpFull + 1,helpFlag=2 where PostId=" + postId;
            }

            Log.i("Database",sql);
           db.execSQL(sql);
                if (db.isOpen()) {
                    db.close();
                }
                return true;


        } catch (Exception e) {
            Log.i("BewareDatabase", "Insert Failed");
            return false;
        }

    }

    /* End : Update Helpfull/Not helpfull*/

    /*Start  Update Comment Count */
    public Boolean UpdateCommentCount(int postId,int Count,int flag) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql;

            if(flag==1) {
                sql = "UPDATE bw_Post SET TopComment = TopComment + 1 where PostId=" + postId;
            }else {
                sql = "UPDATE bw_Post SET TopComment= " + Count + " where PostId=" + postId;
            }

            Log.i("Comment Database",sql);
            db.execSQL(sql);
            if (db.isOpen()) {
                db.close();
            }
            return true;


        } catch (Exception e) {
            Log.i("BewareDatabase", "Insert Failed");
            return false;
        }

    }
    /*End Update Comment Count */
}
