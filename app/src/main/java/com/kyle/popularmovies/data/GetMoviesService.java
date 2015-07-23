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
  private static final String TMDB_URL = "http://api.themoviedb.org/3/discover/movie?";

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
   *
   * Results are passed via a publish/subscribe event bus.
   *
   * @param intent The intent that holds data such as the extras.
   */
  @Override
  protected void onHandleIntent( Intent intent )
  {
    HttpURLConnection connection = null;
    BufferedReader reader = null;

    // The list of movies
    ArrayList<Movie> ret = new ArrayList<>();

    // Needed for looking for extras
    Bundle bundle = intent.getExtras();
    String sortOrder;

    // Determine the type of sorting
    try
    {
      sortOrder = bundle.getString( EXTRA_SORT_ORDER );
    } catch ( NullPointerException e )
    {
      // sort order wasn't passed
      sortOrder = SORT_POPULARITY;
      e.printStackTrace();
    }

    try
    {
      // Build the query string
      StringBuilder urlStr = new StringBuilder(  );
      urlStr.append( TMDB_URL );
      urlStr.append( API_KEY_PARAM );
      urlStr.append( PARAM_EQ );
      urlStr.append( Constants.API_KEY );
      urlStr.append( PARAM_SEP );
      urlStr.append( SORT_PARAM );
      urlStr.append( PARAM_EQ );
      urlStr.append( sortOrder );
      URL url = new URL( urlStr.toString() );

      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod( "GET" );
      connection.connect();

      // Grab data and build into a JSONObject
      InputStream inputStream = connection.getInputStream();
      StringBuilder buffer = new StringBuilder();

      if ( inputStream == null )
      {
        // Update with no data
        EventBus.getDefault().post( ret ); // ret should be empty
        return;
      }

      reader = new BufferedReader( new InputStreamReader( inputStream ) );

      String line;
      while ( ( line = reader.readLine() ) != null )
      {
        buffer.append( line );
        buffer.append( "\n" );
      }

      if ( buffer.length() == 0 )
      {
        // Update with no data
        EventBus.getDefault().post( ret ); // ret should be empty
        return;
      }

      try
      {
        JSONObject json = new JSONObject( buffer.toString() );

        // Using google's Gson library to deserialize json data into java objects
        Gson gson = new Gson();
        Movie[] movies = gson.fromJson( json.getString( RESULTS_KEY ), Movie[].class );
        EventBus.getDefault().post( movies );
      } catch ( JSONException e )
      {
        Log.e(LOG_TAG, e.toString());
      }

      // Return data
    }
    catch (UnknownHostException e)
    {
      // Either no internet connection or the api is down
      EventBus.getDefault().post( e );
    }
    catch ( IOException e )
    {
      Log.e(LOG_TAG, e.toString());
    }  finally
    {
      if ( connection != null ) connection.disconnect();

      if ( reader != null )
      {
        try
        {
          reader.close();
        } catch ( IOException e )
        {
          Log.e( LOG_TAG, e.toString() );
        }
      }
    }
  }
}
