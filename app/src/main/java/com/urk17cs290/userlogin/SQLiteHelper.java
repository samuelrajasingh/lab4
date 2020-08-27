package com.urk17cs290.userlogin;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME="college";

    public static final String TABLE_NAME="users";

    public static final String Table_Column_ID="id";

    public static final String Table_Column_1_Name="name";

    public static final String Table_Column_2_Email="email";

    public static final String Table_Column_3_Phone="phone";
    public static final String Table_Column_4_Password="password";

    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

    String CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME
            + " ("
            + Table_Column_ID
            + " INTEGER PRIMARY KEY, "
            + Table_Column_1_Name
            + " VARCHAR, "
            + Table_Column_2_Email
            + " VARCHAR, "
            + Table_Column_3_Phone
            + " VARCHAR,"
            + Table_Column_4_Password + " VARCHAR)";
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

}