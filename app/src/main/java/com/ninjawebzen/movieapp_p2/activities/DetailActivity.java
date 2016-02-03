package com.ninjawebzen.movieapp_p2.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninjawebzen.movieapp_p2.MovieItem;
import com.ninjawebzen.movieapp_p2.R;
import com.squareup.picasso.Picasso;

/**
 * Created by slavkurochkin on 10/17/15.
 */
public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MovieItem.EXTRA_MOVIE)) {
            MovieItem movie = new MovieItem(intent.getBundleExtra(MovieItem.EXTRA_MOVIE));
            ((TextView)findViewById(R.id.movie_title)).setText(movie.title);
            ((TextView)findViewById(R.id.movie_rating)).setText(movie.getRating());
            ((TextView)findViewById(R.id.movie_overview)).setText(movie.overview);
            ((TextView)findViewById(R.id.movie_release_date)).setText(movie.release_date);


            Uri posterUri = movie.buildPosterUri(getString(R.string.api_poster_default_size));
            Picasso.with(this)
                    .load(posterUri)
                    .into((ImageView)findViewById(R.id.movie_poster));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
