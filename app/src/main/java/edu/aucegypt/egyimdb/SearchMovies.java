package edu.aucegypt.egyimdb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
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

public class SearchMovies extends AppCompatActivity {

    GridView gridView;
    ArrayList<ProfileCard>list=new ArrayList<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        new Async().execute();
    }

    class Async extends AsyncTask<Void, Void, Void> {
        String error="";
        String query;
        String genre;

        @Override
        protected Void doInBackground(Void... voids) {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(getResources().getString(R.string.db_url), getResources().getString(R.string.user_name), getResources().getString(R.string.password));
                Statement statement = connection.createStatement();

                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    genre = bundle.getString("movie_genre");
                    query = "select * from movie m, moviegenre g where m.movieid = g.movieid and g.genre = '" + genre + "'";
                }
                else
                    query = "SELECT * FROM movie";

                ResultSet resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    list.add(new ProfileCard(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)+" EGP", resultSet.getString(7)));
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
            adapter = new MainAdapter(SearchMovies.this, list);
            gridView = findViewById(R.id.grid);
            gridView.setAdapter(adapter);
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

            textView.setText(itemsModelListfiltered.get(position).getMovieName());
            textView1.setText(itemsModelListfiltered.get(position).getReleaseDate());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchMovies.this, MovieInfo.class);
                    intent.putExtra("movie_info", itemsModelListfiltered.get(position));
                    startActivity(intent);
                }
            });

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


//resultSet.getString(5).substring(0, resultSet.getString(5).length()-4) + " " + resultSet.getString(5).substring(resultSet.getString(5).length()-4)