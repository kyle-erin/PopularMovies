package com.kyle.popularmovies.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.kyle.popularmovies.system.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * A service for fetching movies from TheMovieDatabase.
 */
public class GetMoviesService extends IntentService
{
  /**
   * Popularity sort key.
   */
  public static final String SORT_POPULARITY = "popularity.desc";

  /**
   * Rating sort key.
   */
  public static final String SORT_RATING = "vote_average.desc";

  /**
   * An extra key for passing a desired sort to search with.
   */
  public static final String EXTRA_SORT_ORDER = "sort_order";

  /**
   * A tag for logging debug information.
   */
  private static final String LOG_TAG = GetMoviesService.class.getSimpleName();

  /**
   * The base url for fetching movies.
   */
  private static final String TMDB_URL = "http://api.themoviedb.org/3/discover/movie";

  /**
   * Used to separate query parameters in an HTTP request.
   */
  private static final String PARAM_SEP = "&";

  /**
   * The key used to signify a sorting method in the HTTP request.
   */
  private static final String SORT_PARAM = "sort_by";

  /**
   * The key used to signify the api key.
   */
  private static final String API_KEY_PARAM = "api_key";

  /**
   * Used to separate a key/value pair in an HTTP request.
   */
  private static final String PARAM_EQ = "=";

  /**
   * The key that holds the results of the HTTP request.
   */
  private static final String RESULTS_KEY = "results";

  public GetMoviesService()
  {
    super( GetMoviesService.class.getName() );
  }

  /**
   * Looks for an extra to determine which kind of sorting method to use. Then queries
   * The Movie Database for movies based on that sorting method. The results are returned from
   * only the first page.
   * <p/>
   * Results are passed via a publish/subscribe event bus.
   *
   * @param intent The intent that holds data such as the extras.
   */
  @Override
  protected void onHandleIntent( Intent intent )
  {
    // Needed for looking for extras
    Bundle bundle = intent.getExtras();

    String temp;

    // Determine the type of sorting
    try
    {
      temp = bundle.getString( EXTRA_SORT_ORDER );
    } catch ( NullPointerException e )
    {
      // sort order wasn't passed
      temp = SORT_POPULARITY;
      e.printStackTrace();
    }
    final String sortOrder = temp;

    try
    {
      JSONObject json = WebServices.httpRequestJSON( TMDB_URL, new HashMap<String, String>()
      {{
          put( SORT_PARAM, sortOrder );
        }} );

      // Using Google's Gson library to deserialize json data into java objects
      Gson gson = new Gson();
      Movie[] movies = gson.fromJson( json.getString( RESULTS_KEY ), Movie[].class );
      EventBus.getDefault().post( movies );
    } catch ( JSONException e )
    {
      Log.e( LOG_TAG, e.toString() );
    }
  }
}
