package edu.aucegypt.egyimdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieInfo extends AppCompatActivity {
    TextView MovieName;
    TextView Duration;
    TextView ReleaseDate;
    TextView Revenue;
    TextView PG;
    TextView Description;
    TextView Genre;
    Button Reviews;
    String MovieID;
    ProfileCard movie_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        MovieName = (TextView) findViewById(R.id.MovieName);
        Duration = (TextView) findViewById(R.id.MovieDuration);
        Description = (TextView) findViewById(R.id.MovieDescription);
        PG = (TextView) findViewById(R.id.MoviePG);
        Revenue = (TextView) findViewById(R.id.MovieRevenue);
        ReleaseDate = (TextView) findViewById(R.id.ReleaseDate);
        Genre = (TextView) findViewById(R.id.Genres);
        Reviews = (Button) findViewById(R.id.ReviewsButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            movie_info = (ProfileCard) bundle.get("movie_info");
            MovieID = movie_info.getMovieID();
            MovieName.setText(movie_info.getMovieName());
            Duration.setText(movie_info.getDuration());
            Description.setText(movie_info.getDescr());
            PG.setText(movie_info.getPG());
            ReleaseDate.setText(movie_info.getReleaseDate());
            if (movie_info.getRevenue().contains("null"))
                Revenue.setText(null);
            else
                Revenue.setText(movie_info.getRevenue());
        }

        Reviews.setOnClickListener(view->{
            Intent intent = new Intent(MovieInfo.this, MovieReviews.class);
            intent.putExtra("movie_id", MovieID);
            intent.putExtra("movie_name", movie_info.getMovieName());
            startActivity(intent);
        });

        new Async().execute();
    }

    class Async extends AsyncTask<Void, Void, Void> {
        String error="";
        String query;
        String genres = "";
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();
                query = "SELECT genre FROM moviegenre WHERE movieid="+MovieID;
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    genres += resultSet.getString(1) +", ";
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
            super.onPostExecute(aVoid);
            Genre.setText(genres.substring(0, genres.length()-2));

        }
    }


}