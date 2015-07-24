package com.kyle.popularmovies.data;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * A service for fetching trailers for a specific movie.
 */
public class GetTrailersService extends IntentService
{
  /**
   * The key for id in the url
   */
  private static final String ID_KEY = "{id}";

  /**
   * The url for making a request for trailers.
   */
  private static final String TRAILER_URL = "http://api.themoviedb.org/3/movie/" + ID_KEY + "/videos";
  private static final String RESULTS_KEY = "results";


  public GetTrailersService()
  {
    super( GetTrailersService.class.getName() );
  }

  @Override
  protected void onHandleIntent( Intent intent )
  {
    JSONObject object = WebServices.httpRequestJSON( TRAILER_URL.replace( ID_KEY, "135397" ), new HashMap<String, String>() );
    Gson gson = new Gson();
    Trailer[] trailers = new Trailer[0];
    try
    {
      trailers = gson.fromJson( object.getString( RESULTS_KEY ), Trailer[].class );
    } catch ( JSONException e )
    {
      e.printStackTrace();
    }

    EventBus.getDefault().post( trailers );
  }
}
