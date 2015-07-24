package com.kyle.popularmovies.data;

import android.util.Log;

import com.kyle.popularmovies.system.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds functions for performing web services.
 */
public class WebServices
{
  /**
   * A tag for debugging information in this class.
   */
  private static final String LOG_TAG = WebServices.class.getSimpleName();

  /**
   * Used to separate query parameters in an HTTP request.
   */
  private static final String PARAM_SEP = "&";

  /**
   * The key used to signify the api key.
   */
  private static final String API_KEY_PARAM = "api_key";

  /**
   * Used to separate a key/value pair in an HTTP request.
   */
  private static final String PARAM_EQ = "=";

  /**
   * Used to separate a key/value pair in an HTTP request.
   */
  private static final String URL_PARAM_SEP = "?";

  /**
   * Makes an http request expecting a JSON return type.
   *
   * @param tUrl The url to make the request to.
   * @return What's the JSON data for this url?
   */
  public static JSONObject httpRequestJSON( String tUrl, HashMap<String, String> params )
  {
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    JSONObject ret = new JSONObject();

    try
    {
      // Build query string
      StringBuilder urlStr = new StringBuilder();
      urlStr.append( tUrl );
      urlStr.append( URL_PARAM_SEP );
      urlStr.append( API_KEY_PARAM );
      urlStr.append( PARAM_EQ );
      urlStr.append( Constants.API_KEY );
      urlStr.append( PARAM_SEP );

      for(Map.Entry<String, String> param : params.entrySet())
      {
        urlStr.append( param.getKey() );
        urlStr.append( PARAM_EQ );
        urlStr.append( param.getValue() );
      }

      String test = urlStr.toString();

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
        return ret;
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
        // Return with no data
        return ret;
      }

      try
      {
        // Create JSONObject from json string.
        ret = new JSONObject( buffer.toString() );
      } catch ( JSONException e )
      {
        e.printStackTrace();
      }
    } catch ( IOException e )
    {
      Log.e( LOG_TAG, e.toString() );
    } finally
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

    return ret;
  }
}
