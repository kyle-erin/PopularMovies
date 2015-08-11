package com.kyle.popularmovies.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyle.popularmovies.R;
import com.kyle.popularmovies.data.GetTrailersService;
import com.kyle.popularmovies.data.Movie;
import com.kyle.popularmovies.data.Trailer;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Lists detailed information about a movie that was selected.
 */
public class MovieInfoActivity extends Activity
{
  /**
   * The url to fetch a movie poster, which is bigger than in MovieAdapter.
   */
  public static final String TMDB_IMG_URL = "http://image.tmdb.org/t/p/w500";

  /**
   * This activities debug tag.
   */
  private static final String LOG_TAG = MovieInfoActivity.class.getSimpleName();

  /**
   * Key for fetching trailers.
   */
  private static final String DATA_KEY = "trailers";

  /**
   * The selected movie to display information about.
   */
  public static Movie mSelected;

  /**
   * When an item attribute is empty.
   */
  private static final String EMPTY_STR = "N/A";

  /**
   * Displays the poster of the movie.
   */
  @Bind( R.id.movie_poster )
  ImageView mPoster;

  /**
   * Displays the original_title of the movie.
   */
  @Bind( R.id.title )
  TextView mTitle;

  /**
   * Displays the release date of the movie.
   */
  @Bind( R.id.release )
  TextView mRelease;

  /**
   * Displays the rating of the movie.
   */
  @Bind( R.id.vote )
  TextView mVote;

  /**
   * Displays the synopsis/overview of the movie.
   */
  @Bind( R.id.synopsis )
  TextView mSynopsis;
  private Trailer[] mTrailers;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_movie_info );
    ButterKnife.bind( this );
    EventBus.getDefault().register( this );

    // Fetch trailers
    startService( new Intent( this, GetTrailersService.class ) );

    // If the selected movie was set, show all of its data.
    if ( mSelected != null )
    {
      if ( mSelected.poster_path != null && mSelected.poster_path.length() > 0 )
      {
        Picasso.with( this ).load( TMDB_IMG_URL + mSelected.poster_path ).into( mPoster );
      }

      mTitle.setText((mSelected.original_title == null ||  mSelected.original_title.isEmpty()) ? EMPTY_STR : mSelected.original_title );
      mRelease.setText( (mSelected.release_date == null || mSelected.release_date.isEmpty()) ? EMPTY_STR : formatDate( mSelected.release_date ) );
      mVote.setText( (mSelected.vote_average == null || mSelected.vote_average.isEmpty()) ? EMPTY_STR : mSelected.vote_average );
      mSynopsis.setText( (mSelected.overview == null || mSelected.overview.isEmpty()) ? EMPTY_STR : mSelected.overview );
    }
  }

  private String formatDate(String date)
  {
    String[] comps = date.split( "-" );
    return comps.length > 0 ? comps[0] : date;
  }

  @Override
  public void onSaveInstanceState(Bundle state) {
    // Save the current list of data
    state.putParcelableArray( DATA_KEY, mTrailers );

    super.onSaveInstanceState(state);
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // Disable menu
    return false;
  }

  @SuppressWarnings( "UnusedDeclaration" )
  public void onEventMainThread( Trailer[] trailers )
  {
    mTrailers = trailers;
    Log.i(LOG_TAG, trailers.toString());
  }
}
