package com.kyle.popularmovies.data;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.kyle.popularmovies.system.Constants;

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

import de.greenrobot.event.EventBus;

/**
 * Created by kyle on 7/21/2015.
 */
public class GetMoviesService extends IntentService
{
  private static final String LOG_TAG = GetMoviesService.class.getSimpleName();
  private static final String TMDB_URL = "http://api.themoviedb.org/3/discover/movie?";
  private static final String PARAM_SEP = "&";
  private static final String SORT_PARAM = "sort_by";
  private static final String API_KEY_PARAM = "api_key";
  private static final String PARAM_EQ = "=";
  private static final String SORT_PARAM_POP = "popularity.desc";
  private static final String RESULTS_KEY = "results";

  public GetMoviesService()
  {
    super( GetMoviesService.class.getName() );
  }

  @Override
  protected void onHandleIntent( Intent intent )
  {
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    ArrayList<Movie> ret = new ArrayList<>();

    try
    {
      StringBuilder urlStr = new StringBuilder(  );
      urlStr.append( TMDB_URL );
      urlStr.append( API_KEY_PARAM );
      urlStr.append( PARAM_EQ );
      urlStr.append( Constants.API_KEY );
      urlStr.append( PARAM_SEP );
      urlStr.append( SORT_PARAM );
      urlStr.append( PARAM_EQ );
      urlStr.append( SORT_PARAM_POP );
      String finalurl = urlStr.toString();
      URL url = new URL( urlStr.toString() );

      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod( "GET" );
      connection.connect();

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
        Gson gson = new Gson();
        Movie[] movies = gson.fromJson( json.getString( RESULTS_KEY ), Movie[].class );
        EventBus.getDefault().post( movies );
      } catch ( JSONException e )
      {
        Log.e(LOG_TAG, e.toString());
        EventBus.getDefault().post( e );
      }

      // Return data
    } catch ( IOException e )
    {
      Log.e(LOG_TAG, e.toString());
      EventBus.getDefault().post( e );
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
          EventBus.getDefault().post( e );
        }
      }
    }
  }
}
