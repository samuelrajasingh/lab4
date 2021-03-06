package com.urk17cs290.userlogin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

public class DashboardActivity extends AppCompatActivity {

  String emailHolder;
  String passwordHolder;
  String phoneHolder;
  String nameHolder;
  MaterialTextView email;
  MaterialTextView phone;
  MaterialTextView name;
  MaterialTextView password;
  Button logout;
  Cursor cursor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    Intent intent = getIntent();
    // Receiving User Email Send By MainActivity.
    emailHolder = intent.getStringExtra("USER_EMAIL");

    email = findViewById(R.id.editEmail);
    name = findViewById(R.id.editName);
    phone = findViewById(R.id.editPhone);
    password = findViewById(R.id.editPassword);

    logout = (Button) findViewById(R.id.button1);
    Log.d(
        "TAG",
        "\n\n45).\n Email - "
            + emailHolder
            + " \n"
            + "Name - "
            + nameHolder
            + "\n"
            + "Phone - "
            + phoneHolder
            + "\n"
            + "Pass - "
            + passwordHolder
            + "\n\n\n");
    SQLiteDatabase db;
    try (SQLiteOpenHelper sqLiteHelper = new SQLiteHelper(DashboardActivity.this)) {
      db = sqLiteHelper.getReadableDatabase();
      Log.d("TAG", "emailHolder before qry: "+emailHolder);
      String query = "select * from users where email = '" + emailHolder + "'";
      cursor = db.rawQuery(query, null);
//      cursor = db.query(SQLiteHelper.TABLE_NAME, null, " " + SQLiteHelper.Table_Column_2_Email + "=?", new String[]{emailHolder}, null, null, null);

      while (cursor.moveToNext()) {
        if (cursor.isFirst()) {
          cursor.moveToFirst();
          // Storing Password associated with entered email.
          nameHolder = cursor.getString(1).trim();
          phoneHolder = cursor.getString(3).trim();
          passwordHolder = cursor.getString(4).trim();

          // Closing cursor.
          cursor.close();
        }
      }
      setAllValues();
      db.close();
    } catch (Exception e) {
      Log.d("TAG", "Exception: " + e.getMessage());
    }
    // Setting up received email to TextView.

    // Adding click listener to Log Out button.
    logout.setOnClickListener(
        view -> {
          // Finishing current DashBoard activity on button click.
          finish();
          Toast.makeText(DashboardActivity.this, "Log Out Successfull", Toast.LENGTH_LONG).show();
        });
  }

  public void setAllValues() {

    email.setText(emailHolder);
    phone.setText(phoneHolder);
    name.setText(nameHolder);
    password.setText(passwordHolder);
    Log.d(
            "TAG",
            "\n\n\n Email - "
                    + emailHolder
                    + " \n"
                    + "Name - "
                    + nameHolder
                    + "\n"
                    + "Phone - "
                    + phoneHolder
                    + "\n"
                    + "Pass - "
                    + passwordHolder
                    + "\n\n\n");
  }
}
