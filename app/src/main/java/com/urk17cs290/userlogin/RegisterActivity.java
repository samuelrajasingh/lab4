package com.urk17cs290.userlogin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout Email, Password, Name,Phone ;
    Button Register;
    String nameHolder;
    String emailHolder;
    String passwordHolder;
    String phoneHolder;
    Boolean editTextEmptyHolder;
    SQLiteDatabase db;
    String SQLiteDataBaseQueryHolder ;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String fResult = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = findViewById(R.id.buttonRegister);

        Email = findViewById(R.id.editEmail);
        Password = findViewById(R.id.editPassword);
        Name = findViewById(R.id.editName);
        Phone = findViewById(R.id.editPhone);

        sqLiteHelper = new SQLiteHelper(this);

        // Adding click listener to register button.
        Register.setOnClickListener(view -> {

            // Creating SQLite database if dose n't exists
            SQLiteDataBaseBuild();

            // Creating SQLite table if dose n't exists.
            SQLiteTableBuild();

            // Checking EditText is empty or Not.
            CheckEditTextStatus();

            // Method to check Email is already exists or not.
            CheckingEmailAlreadyExistsOrNot();

            // Empty EditText After done inserting process.
            EmptyEditTextAfterDataInsert();


        });

    }

    // SQLite database build method.
    public void SQLiteDataBaseBuild(){

        db = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void SQLiteTableBuild() {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email + " VARCHAR, "+SQLiteHelper.Table_Column_3_Phone+"VARCHAR, " + SQLiteHelper.Table_Column_4_Password + " VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void InsertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(editTextEmptyHolder == true)
        {

            // SQLite query to insert data into table.
            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,email,phone,password) VALUES('"+ nameHolder +"', '"+ emailHolder+"', '"+ phoneHolder +"', '"+ passwordHolder +"');";

            // Executing query.
            db.execSQL(SQLiteDataBaseQueryHolder);

            // Closing SQLite database object.
            db.close();

            finish();
            // Printing toast message after done inserting.
            Toast.makeText(RegisterActivity.this,"User Registered Successfully", Toast.LENGTH_LONG).show();

        }
        // This block will execute if any of the registration EditText is empty.
        else {

            // Printing toast message if any of EditText is empty.
            Toast.makeText(RegisterActivity.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    // Empty edittext after done inserting process method.
    public void EmptyEditTextAfterDataInsert(){
        Name.getEditText().getText().clear();
        Email.getEditText().getText().clear();
        Password.getEditText().getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        nameHolder = Name.getEditText().getText().toString() ;
        emailHolder = Email.getEditText().getText().toString();
        passwordHolder = Password.getEditText().getText().toString();
        phoneHolder=Phone.getEditText().getText().toString();

        if(TextUtils.isEmpty(nameHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(phoneHolder) || TextUtils.isEmpty(passwordHolder)){
            editTextEmptyHolder = false ;
        }
        else {

            editTextEmptyHolder = true ;
        }
    }

    // Checking Email is already exists or not.
    public void CheckingEmailAlreadyExistsOrNot(){

        // Opening SQLite database write permission.
        db = sqLiteHelper.getWritableDatabase();

        // Adding search email query to cursor.
        cursor = db.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{emailHolder}, null, null, null);

        while (cursor.moveToNext()) {

            if (cursor.isFirst()) {

                cursor.moveToFirst();

                // If Email is already exists then Result variable value set as Email Found.
                fResult = "Email Found";

                // Closing cursor.
                cursor.close();
            }
        }

        // Calling method to check final result and insert data into SQLite database.
        CheckFinalResult();

    }


    // Checking result
    public void CheckFinalResult(){

        // Checking whether email is already exists or not.
        if(fResult.equalsIgnoreCase("Email Found"))
        {

            // If email is exists then toast msg will display.
            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            InsertDataIntoSQLiteDatabase();

        }

        fResult = "Not_Found" ;

    }

}