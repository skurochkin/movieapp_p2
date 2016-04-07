package com.ninjawebzen.movieapp_p2.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ninjawebzen.movieapp_p2.ListViewForEmbeddingInScrollView;
import com.ninjawebzen.movieapp_p2.MovieItem;
import com.ninjawebzen.movieapp_p2.R;
import com.ninjawebzen.movieapp_p2.Trailer;
import com.ninjawebzen.movieapp_p2.adapters.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by slavkurochkin on 10/17/15.
 */
public class DetailActivityFragment extends Fragment {

    private static final String API_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String API_PARAM_KEY = "api_key";
    private static final String LOG_TAG = "movieApp";

    private View mView;
    private MovieItem mMovieItem;
    private List<Trailer> mTrailerList = new ArrayList<>();

    private TrailerAdapter mTrailerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_detail, container, false);
        setData();
        return mView;
    }

    private void setData(){
        Bundle bundle = getActivity().getIntent().getExtras();
        mMovieItem = (MovieItem) bundle.getSerializable(MovieItem.EXTRA_MOVIE);
        if (mMovieItem != null) fillFields();
    }

    private void fillFields(){
        ((TextView)mView.findViewById(R.id.movie_title)).setText(mMovieItem.title);
        ((TextView)mView.findViewById(R.id.movie_rating)).setText(mMovieItem.getRating());
        ((TextView)mView.findViewById(R.id.movie_overview)).setText(mMovieItem.overview);
        ((TextView)mView.findViewById(R.id.movie_release_date)).setText(mMovieItem.release_date);

        Uri posterUri = mMovieItem.buildPosterUri(getString(R.string.api_poster_default_size));
        Picasso.with(getContext())
                .load(posterUri)
                .into((ImageView)mView.findViewById(R.id.movie_poster));

        mView.findViewById(R.id.show_trailers_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTrailers();
            }
        });
    }

    private void loadTrailers(){
        mView.findViewById(R.id.trailers_progress_bar).setVisibility(View.VISIBLE);
        mView.findViewById(R.id.show_trailers_button).setVisibility(View.GONE);
        new LoadTrailersTask().execute();
    }

    private class LoadTrailersTask extends AsyncTask<Void, Void, List<Trailer>>{

        @Override
        protected List<Trailer> doInBackground(Void... params) {
            if (mMovieItem.id == 0) return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseJsonStr = null;

            try {

                Uri builtUri = Uri.parse(API_BASE_URL + mMovieItem.id + "/videos").buildUpon()
                        .appendQueryParameter(API_PARAM_KEY, getString(R.string.api_key))
                        .build();
                Log.d(LOG_TAG, "QUERY URI: " + builtUri.toString());
                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb api, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                responseJsonStr = buffer.toString();

            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error", ex);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return fetchTrailersFromJson(responseJsonStr);
            } catch (JSONException ex) {
                Log.d(LOG_TAG, "Can't parse JSON: " + responseJsonStr, ex);
                return null;
            }
        }

        private List<Trailer> fetchTrailersFromJson(String jsonStr) throws JSONException {
            final String KEY_MOVIES = "results";

            JSONObject json  = new JSONObject(jsonStr);
            JSONArray trailers = json.getJSONArray(KEY_MOVIES);
            ArrayList<Trailer> result = new ArrayList<>();

            for (int i = 0; i < trailers.length(); i++) {
                result.add(Trailer.fromJson(trailers.getJSONObject(i)));
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<Trailer> xs) {
            if (xs == null) {
                Toast.makeText(
                        getActivity(),
                        getString(R.string.msg_server_error),
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                mTrailerList = xs;
            }
            showTrailers();
        }
    }

    private void showTrailers(){
        mView.findViewById(R.id.trailers_progress_bar).setVisibility(View.GONE);
        mView.findViewById(R.id.show_trailers_button).setVisibility(View.GONE);
        mTrailerAdapter = new TrailerAdapter(mTrailerList, getContext());
        ((ListViewForEmbeddingInScrollView) mView.findViewById(R.id.movie_trailer_list)).setAdapter(mTrailerAdapter);
    }
}
