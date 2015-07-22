package com.kyle.popularmovies.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyle.popularmovies.R;
import com.kyle.popularmovies.data.Movie;
import com.kyle.popularmovies.views.MovieAdapter;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

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
   * The selected movie to display information about.
   */
  public static Movie mSelected;

  /**
   * Displays the poster of the movie.
   */
  @Bind( R.id.poster )
  ImageView mPoster;

  /**
   * Displays the title of the movie.
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

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_movie_info );
    ButterKnife.bind( this );

    // If the selected movie was set, show all of its data.
    if ( mSelected != null )
    {
      Picasso.with( this ).load( TMDB_IMG_URL + mSelected.poster_path ).into( mPoster );
      mTitle.setText( mSelected.title );
      mRelease.setText( mSelected.release_date );
      mVote.setText( mSelected.vote_average );
      mSynopsis.setText( mSelected.overview );
    }
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // Disable menu
    return false;
  }
}
