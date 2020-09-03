package com.urk17cs290.userlogin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout email;
    TextInputLayout password;
    TextInputLayout name;
    TextInputLayout phone;
    Button register;
    String nameHolder;
    String emailHolder;
    String passwordHolder;
    String phoneHolder;
    Boolean editTextEmptyHolder;
    SQLiteDatabase db;
    String sqlitedatabasequeryholder;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String fResult = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.buttonRegister);

        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);

        sqLiteHelper = new SQLiteHelper(this);

        // Adding click listener to register button.
        register.setOnClickListener(view -> {

            // Creating SQLite database if dose n't exists
            sqlitedatabasebuild();

            // Creating SQLite table if dose n't exists.
            sqlitetablebuild();

            // Checking EditText is empty or Not.
            checkEditTextStatus();

            // Method to check Email is already exists or not.
            checkingEmailAlreadyExistsOrNot();

            // Empty EditText After done inserting process.
            emptyEditTextAfterDataInsert();


        });

    }

    // SQLite database build method.
    public void sqlitedatabasebuild(){

        db = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    // SQLite table build method.
    public void sqlitetablebuild() {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + SQLiteHelper.TABLE_NAME + "(" + SQLiteHelper.Table_Column_ID + " PRIMARY KEY AUTOINCREMENT NOT NULL, " + SQLiteHelper.Table_Column_1_Name + " VARCHAR, " + SQLiteHelper.Table_Column_2_Email + " VARCHAR, "+SQLiteHelper.Table_Column_3_Phone+"VARCHAR, " + SQLiteHelper.Table_Column_4_Password + " VARCHAR);");

    }

    // Insert data into SQLite database method.
    public void insertDataIntoSQLiteDatabase(){

        // If editText is not empty then this block will executed.
        if(editTextEmptyHolder) {
            // SQLite query to insert data into table.
            sqlitedatabasequeryholder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (name,email,phone,password) VALUES('"+ nameHolder +"', '"+ emailHolder+"', '"+ phoneHolder +"', '"+ passwordHolder +"');";

            // Executing query.
            db.execSQL(sqlitedatabasequeryholder);

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
    public void emptyEditTextAfterDataInsert(){
        Objects.requireNonNull(name.getEditText()).getText().clear();
        Objects.requireNonNull(email.getEditText()).getText().clear();
        Objects.requireNonNull(password.getEditText()).getText().clear();

    }

    // Method to check EditText is empty or Not.
    public void checkEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        nameHolder = Objects.requireNonNull(name.getEditText()).getText().toString() ;
        emailHolder = Objects.requireNonNull(email.getEditText()).getText().toString();
        passwordHolder = Objects.requireNonNull(password.getEditText()).getText().toString();
        phoneHolder= Objects.requireNonNull(phone.getEditText()).getText().toString();

        editTextEmptyHolder = !TextUtils.isEmpty(nameHolder) && !TextUtils.isEmpty(emailHolder) && !TextUtils.isEmpty(phoneHolder) && !TextUtils.isEmpty(passwordHolder);
    }

    // Checking Email is already exists or not.
    public void checkingEmailAlreadyExistsOrNot(){

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
        checkFinalResult();

    }


    // Checking result
    public void checkFinalResult(){

        // Checking whether email is already exists or not.
        if(fResult.equalsIgnoreCase("Email Found"))
        {

            // If email is exists then toast msg will display.
            Toast.makeText(RegisterActivity.this,"Email Already Exists",Toast.LENGTH_LONG).show();

        }
        else {

            // If email already dose n't exists then user registration details will entered to SQLite database.
            insertDataIntoSQLiteDatabase();

        }

        fResult = "Not_Found" ;

    }

}