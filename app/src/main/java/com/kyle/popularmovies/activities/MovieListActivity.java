package com.kyle.popularmovies.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.kyle.popularmovies.data.GetMoviesService;
import com.kyle.popularmovies.data.Movie;
import com.kyle.popularmovies.views.MovieAdapter;
import com.kyle.popularmovies.R;

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
   * The title to display for the sorting option: Most Popular.
   */
  private static final String SORT_POP_TITLE = "Most Popular";

  /**
   * The title to display for the sorting option: Highest Rated.
   */
  private static final String SORT_RATING_TITLE = "Highest Rated";

  /**
   * A list of all sorting options to be displayed in the spinner.
   */
  private static final String[] SORT_OPTIONS = {SORT_POP_TITLE, SORT_RATING_TITLE};

  /**
   * An error message if the user somehow clicks an option we didn't expect.
   */
  private static final String UNKNOWN_SORT_ITEM_ERR = "An unknown sorting option was selected.";

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
    mSortSpinner.setAdapter(adapter);
    mSortSpinner.setOnItemSelectedListener( this );

    // Start download of data
    startService( new Intent( this, GetMoviesService.class ) );
  }

  /**
   * Loads the movies into the GridView.
   *
   * @param data An array of movies.
   */
  @SuppressWarnings( "UnusedDeclaration" )
  public void onEventMainThread( Movie[] data )
  {
    mGridView.setAdapter( new MovieAdapter( this, data ) );
  }

  /**
   * Determine which sorting option was selected and start a new service to fetch movies based
   * on that sorting method.
   *
   * @param parent The parent view.
   * @param view The current view.
   * @param position The position that the item is in the spinner.
   * @param id The id of the item in the spinner.
   */
  @Override
  public void onItemSelected( AdapterView<?> parent, View view, int position, long id )
  {
    String item = (String)parent.getItemAtPosition( position );
    Intent getMovies = new Intent(this, GetMoviesService.class);
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
        Log.e(LOG_TAG, UNKNOWN_SORT_ITEM_ERR );
        break;
    }

    // Start service and wait for the movies.
    startService( getMovies );

  }

  @Override
  public void onNothingSelected( AdapterView<?> parent )
  {
    // Do nothing
  }
}
