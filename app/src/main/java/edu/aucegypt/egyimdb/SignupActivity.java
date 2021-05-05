package edu.aucegypt.egyimdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class SignupActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    Button signUp;
    Button clickHere;
    EditText username;
    EditText email;
    EditText gender;
    DatePicker birthdate;
    TextView email_exist;

    String uUserName;
    String uEmail;
    String uGender;
    String uBirthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUp = (Button) findViewById(R.id.signup);
        clickHere = (Button) findViewById(R.id.clickhere);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        birthdate = findViewById(R.id.birthdate);
        radioGroup = findViewById(R.id.level);
        email_exist = findViewById(R.id.exist);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uUserName = username.getText().toString();  //uName has the name
                uEmail = email.getText().toString().trim(); // uEmail has the email
                uGender = gender.getText().toString().trim(); // uPassword has the Password

                int day = birthdate.getDayOfMonth();
                int month = birthdate.getMonth();
                int year = birthdate.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                uBirthdate = sdf.format(calendar.getTime());

                if (TextUtils.isEmpty(uUserName)) {
                    username.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(uEmail)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(uGender)) {
                    gender.setError("Email is required");
                    return;
                }
                new Async().execute();

            }
        });

        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    class Async extends AsyncTask<Void, Void, Void> {
        String records = "",error="";
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();
                String query = "INSERT INTO userinfo VALUES ('" + uEmail + "','" + uUserName + "','" + uBirthdate + "','" + uGender + "')";
                statement.executeUpdate(query);
            }
            catch(Exception e)
            {
                error = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (error.contains("uplicate"))
            {
                email_exist.setVisibility(View.VISIBLE);
            }
            else
            {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            super.onPostExecute(aVoid);
        }
    }
}