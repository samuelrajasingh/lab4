package com.urk17cs290.userlogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Button logInButton;
    Button registerButton;
    TextInputLayout email;
    TextInputLayout password;
    String emailHolder;
    String passwordHolder;
    Boolean editTextEmptyHolder = false;
    SQLiteDatabase db;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String tempPassword = "NOT_FOUND" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main);

        logInButton = findViewById(R.id.buttonLogin);

        registerButton = findViewById(R.id.buttonRegister);

        email = findViewById(R.id.editEmail);

        password = findViewById(R.id.editPassword);

        sqLiteHelper = new SQLiteHelper(this);

        //Adding click listener to log in button.
        logInButton.setOnClickListener(view -> {
            // Calling EditText is empty or no method.
            checkEditTextStatus();
            // Calling login method.
            loginFunction();
        });

        // Adding click listener to register button.
        registerButton.setOnClickListener(v ->{
//          Opening new user registration activity using intent on button click.
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

        });

    }

    // Login function starts from here.
    public void loginFunction(){

        if(editTextEmptyHolder) {

            // Opening SQLite database write permission.
            db = sqLiteHelper.getWritableDatabase();

            // Adding search email query to cursor.
            cursor = db.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{emailHolder}, null, null, null);

            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    cursor.moveToFirst();
                    // Storing Password associated with entered email.
                    tempPassword = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_4_Password));
                    // Closing cursor.
                    cursor.close();
                }
            }
            // Calling method to check final result ..
            checkFinalResult();
        }
        else {
            //If any of login EditText empty then this block will be executed.
            Toast.makeText(MainActivity.this,"Please Enter All Details",Toast.LENGTH_LONG).show();
        }
    }

    // Checking EditText is empty or not.
    public void checkEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        emailHolder = Objects.requireNonNull(email.getEditText()).getText().toString();
        passwordHolder = Objects.requireNonNull(password.getEditText()).getText().toString();

        // Checking EditText is empty or no using TextUtils.
        editTextEmptyHolder = !TextUtils.isEmpty(emailHolder) && !TextUtils.isEmpty(passwordHolder);
    }

    // Checking entered password from SQLite database email associated password.
    public void checkFinalResult(){
        if(tempPassword.equalsIgnoreCase(passwordHolder)) {
            Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            // Sending Email to Dashboard Activity using intent.
            intent.putExtra("USER_EMAIL", emailHolder);
            intent.putExtra("USER_PASS",tempPassword);
              Log.d("TAG", "checkFinalResult: emailHolder : "+ emailHolder);

            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();
        }
        tempPassword = "NOT_FOUND" ;
    }
}