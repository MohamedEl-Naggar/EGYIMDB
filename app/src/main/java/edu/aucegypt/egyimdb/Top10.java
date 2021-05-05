package edu.aucegypt.egyimdb;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Top10 extends AppCompatActivity {
    private ProfileListAdapter adapter;
    private static final String TAG = "AuthenticatedAddition";
    String email;
    private ArrayList<ProfileCard> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);
        recyclerView = findViewById(R.id.movies_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            email = bundle.getString("email");

        new Async().execute();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }


    class Async extends AsyncTask<Void, Void, Void> {
        String error="";
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM movie ORDER BY revenue DESC LIMIT 10";
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    list.add(new ProfileCard(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5).substring(0, resultSet.getString(5).length()-4) + " " + resultSet.getString(5).substring(resultSet.getString(5).length()-4), resultSet.getString(6)+" EGP", resultSet.getString(7)));
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
            adapter = new ProfileListAdapter(list);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ProfileListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(Top10.this, MovieInfo.class);
                    intent.putExtra("movie_info", list.get(position));
                    startActivity(intent);
                }
            });

        }
    }


}