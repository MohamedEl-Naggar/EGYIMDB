package edu.aucegypt.egyimdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeAdapter myAdapter;
        final ArrayList<Item> homeList=new ArrayList<Item>();

        GridView grid = (GridView) findViewById(R.id.homeGrid);
        homeList.add(new Item("Search Movie",R.drawable.search_movie));
        homeList.add(new Item("Search Movie by Genre",R.drawable.movie_genre));
        homeList.add(new Item("Search Cast",R.drawable.search_cast));
        homeList.add(new Item("Top 10",R.drawable.top10));

        myAdapter = new homeAdapter(this, R.layout.home_view_items, homeList);
        grid.setAdapter(myAdapter);

////        Bundle bundle = getIntent().getExtras();
//        if (bundle != null)
//        {
//            email = bundle.getString("email");
//        }

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = homeList.get(position);
                if(item.getbuttonName().equals("Search Movie")) {
                    Intent intent = new Intent(getApplicationContext(), SearchMovies.class);
//                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                else if(item.getbuttonName().equals("Search Movie by Genre")) {
                    Intent intent = new Intent(getApplicationContext(), SearchByGenre.class);
//                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                else if (item.getbuttonName().equals("Search Cast")) {
                    Intent intent = new Intent(getApplicationContext(), SearchCast.class);
//                    intent.putExtra("email", email);
                    startActivity(intent);
                }
                else if (item.getbuttonName().equals("Top 10")) {
                    Intent intent = new Intent(getApplicationContext(), Top10.class);
//                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });
    }

}