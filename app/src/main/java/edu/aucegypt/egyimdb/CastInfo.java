package edu.aucegypt.egyimdb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class CastInfo extends AppCompatActivity {
    TextView CastName;
    TextView Birthdate;
    TextView Biography;
    TextView Nationality;
    Button Roles;
    String CastID;
    String cast_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_info);

        CastName = (TextView) findViewById(R.id.CastName);
        Birthdate = (TextView) findViewById(R.id.Birthdate);
        Nationality = (TextView) findViewById(R.id.Nationality);
        Biography = (TextView) findViewById(R.id.Biography);
        Roles = (Button) findViewById(R.id.RolesButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            ProfileCard cast_info = (ProfileCard) bundle.get("cast_info");
            CastID = cast_info.getMovieID();
            CastName.setText(cast_info.getMovieName());
            Biography.setText(cast_info.getDescr());
            Nationality.setText(cast_info.getPG());
            Birthdate.setText(cast_info.getReleaseDate());
            cast_name = cast_info.getMovieName();
        }

        Roles.setOnClickListener(view->{
            Intent intent = new Intent(CastInfo.this, CastRoles.class);
            intent.putExtra("cast_id", CastID);
            intent.putExtra("cast_name", cast_name);
            startActivity(intent);
        });
    }
}