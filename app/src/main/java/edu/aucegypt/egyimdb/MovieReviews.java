package edu.aucegypt.egyimdb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieReviews extends AppCompatActivity {

    GridView gridView;
    ArrayList<ProfileCard>list=new ArrayList<>();
    MainAdapter adapter;
    TextView MovieName;
    TextView EmptyReview;
    TextView DuplicateReview;
    EditText WriteReview;
    Button SubmitReview;
    Button SubmitRating;
    RatingBar ratingBar;
    String MovieID;
    String review;
    EmailClass emailClass = EmailClass.getInstance();
    String UEmail = emailClass.getEmail();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        MovieName = (TextView) findViewById(R.id.MovieName);
        EmptyReview = (TextView) findViewById(R.id.empty_review);
        DuplicateReview = (TextView) findViewById(R.id.duplicate_review);
        WriteReview = (EditText) findViewById(R.id.write_review);
        SubmitReview = (Button) findViewById(R.id.submit_review);
        SubmitRating = (Button) findViewById(R.id.submit_rating);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            MovieName.setText(bundle.getString("movie_name") + "'s Reviews");
            MovieID = bundle.getString("movie_id");
        }

        new GetReviews().execute();

        SubmitReview.setOnClickListener(view->{
            review = WriteReview.getText().toString();
            if (review.trim().equals(""))
            {
                EmptyReview.setVisibility(View.VISIBLE);
            }
            else
            {
                EmptyReview.setVisibility(View.INVISIBLE);
                new InsertReview().execute();
            }
        });

        new GetRating().execute();

        new CheckUserRating().execute();

    }


    class InsertReview extends AsyncTask<Void, Void, Void> {
        String records = "",error="";
        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();
                String query = "INSERT INTO reviews VALUES ('" + emailClass.getEmail() + "','" + MovieID + "','" + review + "')";
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
                DuplicateReview.setVisibility(View.VISIBLE);
            }
            else
            {
                list.clear();
                new GetReviews().execute();
            }
            super.onPostExecute(aVoid);
        }
    }

    class GetReviews extends AsyncTask<Void, Void, Void> {
        String error="";
        String query;
        String cast_id;

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();

                Bundle bundle = getIntent().getExtras();
                cast_id = bundle.getString("cast_id");
                query = "select u.username, r.review from reviews r inner join userinfo u on u.email = r.email where r.movieid = "+MovieID;

                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    list.add(new ProfileCard("", resultSet.getString(2), resultSet.getString(1), "", "", "", ""));
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
            adapter = new MainAdapter(MovieReviews.this, list);
            gridView = findViewById(R.id.grid);
            gridView.setAdapter(adapter);
        }
    }


    class SetRating extends AsyncTask<Void, Void, Void> {
        String error="";
        String query;
        String cast_id;

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();

                Bundle bundle = getIntent().getExtras();
                cast_id = bundle.getString("cast_id");
                query = "INSERT INTO ratings VALUES ('" + emailClass.getEmail() + "','" + MovieID + "','" + ratingBar.getRating() + "')";
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
            super.onPostExecute(aVoid);
            if (error == "")
                new GetRating().execute();
        }
    }


    class CheckUserRating extends AsyncTask<Void, Void, Void> {
        String records = "", error = "";
        int count;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();
                String query = "select count(*) from ratings where email = \""+UEmail+"\" and movieid = "+MovieID;
                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            } catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (count == 0)
                ratingBar.setIsIndicator(false);
            else
                ratingBar.setIsIndicator(true);

            if (!ratingBar.isIndicator()) {
                SubmitRating.setOnClickListener(view -> {
                    new SetRating().execute();
                });
            }
            super.onPostExecute(aVoid);
        }
    }

    class GetRating extends AsyncTask<Void, Void, Void> {
        String error="";
        String query;
        String cast_id;
        double rating = 0.0;

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();

                Bundle bundle = getIntent().getExtras();
                cast_id = bundle.getString("cast_id");
                query = "select avg(r.rating) from ratings r inner join userinfo u on u.email = r.email where r.movieid = "+MovieID;

                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    rating = resultSet.getDouble(1);
                }            }
            catch(Exception e)
            {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ratingBar.setRating((float) rating);
            new CheckUserRating().execute();
        }
    }


    public class MainAdapter extends BaseAdapter implements Filterable {

        private List<ProfileCard>itemsModelList;
        private List<ProfileCard> itemsModelListfiltered;
        private Context context;

        public MainAdapter(Context c,List<ProfileCard> itemsModelList){
            this.context=c;
            this.itemsModelList=itemsModelList;
            this.itemsModelListfiltered=itemsModelList;
        }

        @Override
        public int getCount() {
            return itemsModelListfiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view=getLayoutInflater().inflate(R.layout.grid_item, null);

            TextView textView=view.findViewById(R.id.item);
            TextView textView1=view.findViewById(R.id.price);

            textView.setText(itemsModelListfiltered.get(position).getDescr());
            textView1.setText(itemsModelListfiltered.get(position).getMovieName());

            return view;
        }

        @Override
        public Filter getFilter() {

            Filter filter=new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults=new FilterResults();

                    if(constraint==null||constraint.length()==0){
                        filterResults.count=itemsModelList.size();
                        filterResults.values=itemsModelList;
                    }else{
                        String searchstr=constraint.toString().toLowerCase();
                        List<ProfileCard>resultData=new ArrayList<>();

                        for(ProfileCard itemsModel:itemsModelList){
                            if(itemsModel.getMovieName().toLowerCase().contains(searchstr)){
                                resultData.add(itemsModel);
                            }
                            filterResults.count=resultData.size();
                            filterResults.values=resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    itemsModelListfiltered=(List<ProfileCard>)results.values;
                    notifyDataSetChanged();

                }
            };

            return filter;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem menuItem = menu.findItem(R.id.search_icon);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return true;
            }
        });

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return true;
    }
}