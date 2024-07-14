package com.apps.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.Transliterator;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final String DATABASE_NAME = "DATABASE.DB";
    public final static String DATABASE_PATH ="/data/data/com.apps.myapplication/databases/";
    public static final int DATABASE_VERSION = 1;
    int pos;

    //Creating Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }

    //Create a empty database on the system
    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDataBase();

        if(dbExist)
        {
            Log.v("DB Exists", "db exists");
            // By calling this method here onUpgrade will be called on a
            // writeable database, but only if the version number has been
            // bumped
            //onUpgrade(myDataBase, DATABASE_VERSION_old, DATABASE_VERSION);
        }

        boolean dbExist1 = checkDataBase();
        if(!dbExist1)
        {
            this.getReadableDatabase();
            try
            {
                this.close();
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    //Check database already exist or not
    private boolean checkDataBase()
    {
        boolean checkDB = false;
        try
        {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkDB = dbfile.exists();
        }
        catch(SQLiteException e)
        {
        }
        return checkDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    //Copies your database from your local assets-folder to the just created empty database in the system folder
    private void copyDataBase() throws IOException
    {
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0)
        {
            myOutput.write(buffer, 0, length);
        }
        myInput.close();
        myOutput.flush();
        myOutput.close();
    }

    //delete database
    public void db_delete()
    {
        File file = new File(DATABASE_PATH + DATABASE_NAME);
        if(file.exists())
        {
            file.delete();
            System.out.println("delete database file.");
        }
    }

    //Open database
    public void openDatabase() throws SQLException
    {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void closeDataBase()throws SQLException
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
        {
            Log.v("Database Upgrade", "Database version higher than old.");
            db_delete();
        }
    }

//    public String getPartNo (String id) {
//        SQLiteDatabase dbHelper = this.getWritableDatabase();
//
//        String PartNo=new String();
//        Cursor partNo = dbHelper.rawQuery("select * from PartNumber where GUN_ID = ?", new String[]{id});
//        while (partNo.moveToNext()) {
//            PartNo = partNo.getString(pos + 1);
//        }
//        return PartNo;
//    }
//    public String getSrmNo (String id) {
//        SQLiteDatabase dbHelper = this.getWritableDatabase();
//
//        MainActivity.Position = pos;
//        String SrmNo = new String();
//        Cursor srmNo = dbHelper.rawQuery("select * from SRMNumber where GUN_ID = ?", new String[]{id});
//        while (srmNo.moveToNext()) {
//            SrmNo = srmNo.getString(pos + 1);
//        }
//        return SrmNo;
//    }
//
//        public String getLocation (String id) {
//            SQLiteDatabase dbHelper = this.getWritableDatabase();
//            MainActivity.Position = pos;
//            String Loc = new String();
//        Cursor loc = dbHelper.rawQuery("select * from Location where GUN_ID = ?", new String[]{id});
//        while (loc.moveToNext()) {
//            Loc = loc.getString(pos + 1);
//        }
//        return  Loc;
//    }
    public Cursor getPartNo(String id){
        SQLiteDatabase dbHelper = this.getWritableDatabase();
        return dbHelper.rawQuery("select * from PartNumber where GUN_ID = ?", new String[]{id});
    }
    public Cursor getSrmNo (String id){
        SQLiteDatabase dbHelper = this.getWritableDatabase();
        return dbHelper.rawQuery("select * from SRMNumber where GUN_ID = ?", new String[]{id});
    }
    public Cursor getLocation(String id){
        SQLiteDatabase dbHelper = this.getWritableDatabase();
        return dbHelper.rawQuery("select * from Location where GUN_ID = ?", new String[]{id});

    }
    public List<String> getList(){
        List<String> GunID = new ArrayList<String>();
        String selectQuery = "select * from PartNumber";
        SQLiteDatabase dbHelper = this.getWritableDatabase();

        Cursor cursor = dbHelper.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do{
                GunID.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close(); dbHelper.close();
        return GunID;
    }
}
