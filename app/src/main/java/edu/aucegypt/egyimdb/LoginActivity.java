package edu.aucegypt.egyimdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    EditText uEmail;
    Button uLogin, uSignup;
    TextView messageofLogin;
    String email;
    TextView email_not_exist;
    EmailClass emailClass = EmailClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uEmail = findViewById(R.id.email);
        uSignup = findViewById(R.id.signup);
        uLogin = findViewById(R.id.login);
        messageofLogin = (TextView) findViewById(R.id.messageofLogin);
        email_not_exist = findViewById(R.id.email_not_exist);


        uSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        uLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = uEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    uEmail.setError("Email is required");
                    return;
                }
                new Async().execute();
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
                String query = "SELECT * FROM userinfo " + "WHERE Email = '"+email+"'";
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
                }
            }
            catch(Exception e)
            {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (!records.isEmpty())
            {
                emailClass.setEmail(email);
                Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
            }
            else
            {
//                Intent intent= new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intent);
                email_not_exist.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(aVoid);
        }
    }
}