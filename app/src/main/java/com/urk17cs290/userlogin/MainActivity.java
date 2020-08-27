package com.urk17cs290.userlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {

    private static final String USER_EMAIL = "";
    Button LogInButton, RegisterButton ;
    TextInputLayout Email, Password ;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase db;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String tempPassword = "NOT_FOUND" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogInButton = (Button)findViewById(R.id.buttonLogin);

        RegisterButton = (Button)findViewById(R.id.buttonRegister);

        Email = findViewById(R.id.editEmail);

        Password = findViewById(R.id.editPassword);

        sqLiteHelper = new SQLiteHelper(this);

        //Adding click listener to log in button.
        LogInButton.setOnClickListener(view -> {
            // Calling EditText is empty or no method.
            CheckEditTextStatus();
            // Calling login method.
            LoginFunction();
        });

        // Adding click listener to register button.
        RegisterButton.setOnClickListener((v)->{
//          Opening new user registration activity using intent on button click.
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

        });

    }

    // Login function starts from here.
    public void LoginFunction(){

        if(EditTextEmptyHolder) {

            // Opening SQLite database write permission.
            db = sqLiteHelper.getWritableDatabase();

            // Adding search email query to cursor.
            cursor = db.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{EmailHolder}, null, null, null);

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
            CheckFinalResult();
        }
        else {
            //If any of login EditText empty then this block will be executed.
            Toast.makeText(MainActivity.this,"Please Enter All Details",Toast.LENGTH_LONG).show();
        }
    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus(){

        // Getting value from All EditText and storing into String Variables.
        EmailHolder = Email.getEditText().getText().toString();
        PasswordHolder = Password.getEditText().getText().toString();

        // Checking EditText is empty or no using TextUtils.
        if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }

    // Checking entered password from SQLite database email associated password.
    public void CheckFinalResult(){
        if(tempPassword.equalsIgnoreCase(PasswordHolder)) {
            Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
            // Going to Dashboard activity after login success message.
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(USER_EMAIL, EmailHolder);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();
        }
        tempPassword = "NOT_FOUND" ;
    }
}