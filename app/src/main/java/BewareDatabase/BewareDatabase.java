package BewareDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import BewareData.Post;
import BewareData.UserDetails;

/**
 * Created by Prasanna on 03-09-2015.
 */


public class BewareDatabase extends SQLiteOpenHelper {


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

        myDB.execSQL("CREATE TABLE if not exists bw_Post(PostId int,UserName varchar(250),Category varchar(250),Subject varchar(250),PostText Text,HelpFull Int ,NotHelpFull int,TopComment varchar(50),TopCommentUserName varchar(250),TimeStamp  REAL DEFAULT (datetime('now','localtime')) );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    /*Start :- bw_UserDetails */
    public void InsertUserDetails(String UserId, String UserName, String EmailId, String Location, String GcmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("UserId", UserId);
        values.put("UserName", UserName);
        values.put("EmailId", EmailId);
        values.put("Location", Location);
        values.put("GcmId", GcmId);

        db.insert("bw_UserDetails", null, values);

        if (db.isOpen()) {
            db.close();
        }
    }

    public ArrayList<UserDetails> getUserDetails(String ExamDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<UserDetails> list = new ArrayList<UserDetails>();

        String selectQuery = "select Top 1 UserId ,UserName ,EmailId ,Location ,GcmId from bw_UserDetails";

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

    /*Reference
    * public Cursor query (boolean distinct, String table, String[] columns, String selection, String[] selectionArgs,
    * String groupBy, String having, String orderBy, String limit)*/

    public ArrayList<Post> getPostOnCategory(String Category) throws SQLException {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Post> PostList = new ArrayList<Post>();
        Cursor mCursor = db.query(true, "bw_Post", new String[]{
                        "PostId",
                        "HelpFull",
                        "NotHelpFull",
                        "UserName",
                        "Category",
                        "Subject",
                        "PostText",
                        "TopComment",
                        "TopCommentUserName",
                        "TimeStamp"},
                "Category" + "=?"  ,
                new String[]{Category},
                null,
                null,
                "TimeStamp DESC",
                null);


        if (mCursor.moveToFirst()) {
            do {
                Post objPost = new Post();
                objPost.setPostId(mCursor.getInt(mCursor.getColumnIndexOrThrow("PostId")));
                objPost.setHelpFull(mCursor.getInt(mCursor.getColumnIndexOrThrow("HelpFull")));
                objPost.setNotHelpFull(mCursor.getInt(mCursor.getColumnIndexOrThrow("NotHelpFull")));
                objPost.setUserName(mCursor.getString(mCursor.getColumnIndexOrThrow("UserName")));
                objPost.setCategory(mCursor.getString(mCursor.getColumnIndexOrThrow("Category")));
                objPost.setSubject(mCursor.getString(mCursor.getColumnIndexOrThrow("Subject")));
                objPost.setPostText(mCursor.getString(mCursor.getColumnIndexOrThrow("PostText")));
                objPost.setTopComment(mCursor.getString(mCursor.getColumnIndexOrThrow("TopComment")));
                objPost.setTopCommentUserName(mCursor.getString(mCursor.getColumnIndexOrThrow("TopCommentUserName")));
                objPost.setTimeStamp(mCursor.getString(mCursor.getColumnIndexOrThrow("TimeStamp")));

                PostList.add(objPost);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return PostList;

    }

/*End :- bw_Post*/
}