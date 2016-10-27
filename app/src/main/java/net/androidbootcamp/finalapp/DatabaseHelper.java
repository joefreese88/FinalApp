package net.androidbootcamp.finalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // create table rows, names and headings
    public static final String DATABASE_NAME = "GRADES.db";
    public static final String TABLE_NAME = "STUDENT_TABLE";
    public static final String COL_1 = "STUDENT_ID";
    public static final String COL_2 = "STUDENT_FIRST_NAME";
    public static final String COL_3 = "STUDENT_LAST_NAME";
    public static final String COL_4 = "CLASS_ID";
    public static final String COL_5 = "CLASS_NAME";
    public static final String COL_6 = "STUDENT_CLASS_GRADE";
    public static final String COL_7 = "STUDENT_LETTERGRADE";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (STUDENT_ID VARCHAR(8),STUDENT_FIRST_NAME VARCHAR(20),STUDENT_LAST_NAME VARCHAR(20),CLASS_ID VARCHAR(4),CLASS_NAME VARCHAR(20),STUDENT_CLASS_GRADE INTEGER(3),STUDENT_LETTERGRADE VARCHAR(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // pass data to add to the database
    public boolean insertData(String studentID, String firstName, String lastName, String classID,
                              String className, int classGrade, String letterGrade)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, studentID);
        contentValues.put(COL_2, firstName);
        contentValues.put(COL_3, lastName);
        contentValues.put(COL_4, classID);
        contentValues.put(COL_5, className);
        contentValues.put(COL_6, classGrade);
        contentValues.put(COL_7, letterGrade);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    // method to retrieve all information from database
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
}
