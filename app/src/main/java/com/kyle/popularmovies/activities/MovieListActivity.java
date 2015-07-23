package com.kyle.popularmovies.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kyle.popularmovies.data.GetMoviesService;
import com.kyle.popularmovies.data.Movie;
import com.kyle.popularmovies.views.MovieAdapter;
import com.kyle.popularmovies.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Lists movies by descending order based on popularity or rating.
 */
public class MovieListActivity extends Activity implements AdapterView.OnItemSelectedListener
{
  /**
   * The tag used for debugging this class.
   */
  private static final String LOG_TAG = MovieListActivity.class.getSimpleName();

  /**
   * The original_title to display for the sorting option: Most Popular.
   */
  private static final String SORT_POP_TITLE = "Most Popular";

  /**
   * The original_title to display for the sorting option: Highest Rated.
   */
  private static final String SORT_RATING_TITLE = "Highest Rated";

  /**
   * A list of all sorting options to be displayed in the spinner.
   */
  private static final String[] SORT_OPTIONS = { SORT_POP_TITLE, SORT_RATING_TITLE };

  /**
   * An error message if the user somehow clicks an option we didn't expect.
   */
  private static final String UNKNOWN_SORT_ITEM_ERR = "An unknown sorting option was selected.";

  /**
   * Error message shown when there is no internet connection.
   */
  private static final String CONNECTION_ERR_MSG = "There was a problem with your internet connection. Please try again, later.";

  /**
   * Key used for storing/retrieving the movie list from a bundle
   */
  private static final String MOVIES_KEY = "movie_list";

  /**
   * Key used for storing/retrieving the scroll position of the user.
   */
  private static final String GRID_POS = "grid_position";

  /**
   * Error message for an IOException, most likely when Constants.API_KEY isn't set correctly.
   */
  private static final String API_ERR_MSG = "There was an error in retrieving the movie list.";

  /**
   * The GridView that holds all of the movie posters.
   */
  @Bind( R.id.gridview )
  GridView mGridView;

  /**
   * The spinner that holds all of the sorting options.
   */
  @Bind( R.id.sort )
  Spinner mSortSpinner;

  /**
   * The array of movies that are displayed.
   */
  private Movie[] mData;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_movie_list );
    ButterKnife.bind( this );
    EventBus.getDefault().register( this );

    // Load spinner with sorting options
    ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.sort_text_item, SORT_OPTIONS );
    adapter.setDropDownViewResource( R.layout.sort_text_checked_item );
    mSortSpinner.setAdapter( adapter );
    mSortSpinner.setOnItemSelectedListener( this );

    // Start download of data if internet connectivity
    if ( savedInstanceState == null )
    {
      if ( checkInternet() )
      {
        startService( new Intent( this, GetMoviesService.class ) );
      }
      else
      {
        // Tell user of internet issue
        Toast.makeText( this, CONNECTION_ERR_MSG, Toast.LENGTH_LONG ).show();

      }
    }
    else
    {
      // Load saved data
      mData = (Movie[])savedInstanceState.getParcelableArray( MOVIES_KEY );
      EventBus.getDefault().post( mData );
      mGridView.setSelection( savedInstanceState.getInt( GRID_POS ) );
    }
  }

  /**
   * Save position and contents of the GridView, for on orientation change.
   * @param state The current state before change.
   */
  @Override
  public void onSaveInstanceState(Bundle state) {
    // Save the current list of movies
    state.putParcelableArray( MOVIES_KEY, mData );
    // Save the position the user was in the list
    state.putInt( GRID_POS, mGridView.getLastVisiblePosition() );

    super.onSaveInstanceState(state);
  }

  /**
   * @return Do I have an internet connection?
   */
  private boolean checkInternet()
  {
    ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE );
    NetworkInfo info = cm.getActiveNetworkInfo();
    return info != null && info.isConnected();
  }

  /**
   * Loads the movies into the GridView.
   *
   * @param data An array of movies.
   */
  @SuppressWarnings( "UnusedDeclaration" )
  public void onEventMainThread( Movie[] data )
  {
    mData = data;
    mGridView.setAdapter( new MovieAdapter( this, data ) );
  }

  /**
   * Determine which sorting option was selected and start a new service to fetch movies based
   * on that sorting method.
   *
   * @param parent   The parent view.
   * @param view     The current view.
   * @param position The position that the item is in the spinner.
   * @param id       The id of the item in the spinner.
   */
  @Override
  public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
  {
    // Check for internet connection
    if ( checkInternet() )
    {
      String item = (String) parent.getItemAtPosition( position );
      Intent getMovies = new Intent( this, GetMoviesService.class );
      switch ( item )
      {
        case SORT_POP_TITLE:
          // Sort by popularity
          getMovies.putExtra( GetMoviesService.EXTRA_SORT_ORDER, GetMoviesService.SORT_POPULARITY );
          break;
        case SORT_RATING_TITLE:
          // Sort by rating
          getMovies.putExtra( GetMoviesService.EXTRA_SORT_ORDER, GetMoviesService.SORT_RATING );
          break;
        default:
          Log.e( LOG_TAG, UNKNOWN_SORT_ITEM_ERR );
          break;
      }

      // Start service and wait for the movies.
      startService( getMovies );
    }
    else
    {
      Toast.makeText( this, CONNECTION_ERR_MSG, Toast.LENGTH_LONG ).show();
    }

  }

  @Override
  public void onNothingSelected( AdapterView<?> parent )
  {
    // Do nothing
  }
}
