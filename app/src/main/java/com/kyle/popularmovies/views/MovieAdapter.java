package com.kyle.popularmovies.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kyle.popularmovies.activities.MovieInfoActivity;
import com.kyle.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

/**
 * The adapter to a GridView, that displays all the movies fetched.
 */
public class MovieAdapter extends BaseAdapter
{
  /**
   * The url to fetch a smaller image to display in the grid.
   */
  public static final String TMDB_IMG_URL = "http://image.tmdb.org/t/p/w185";

  /**
   * Width of the poster ImageView.
   */
  private static final int POSTER_WIDTH = 200;

  /**
   * Height of the poster ImageView.
   */
  private static final int POSTER_HEIGHT = 200;

  /**
   * Context of the activity that creates this adapter.
   */
  private Context mContext;

  /**
   * Movie data
   */
  private Movie[] mData;

  /**
   * @param context A context of an activity that created this.
   * @param data    The array of movies to display.
   */
  public MovieAdapter( Context context, Movie[] data )
  {
    mContext = context;
    mData = data;
  }

  /**
   * @return How many movies do you have?
   */
  @Override
  public int getCount()
  {
    return mData.length;
  }

  /**
   * No reason to return anything here.
   */
  @Override
  public Object getItem( int position )
  {
    return null;
  }

  /**
   * @param position Position of the item.
   * @return What is the id of the item in this position?
   */
  @Override
  public long getItemId( int position )
  {
    return mData[ position ].id;
  }

  /**
   * @param position    The position of the item in the grid.
   * @param convertView The current view.
   * @param parent      The parent view.
   * @return A view representing a movie, that is clickable.
   */
  @Override
  public View getView( int position, View convertView, ViewGroup parent )
  {
    ImageView imageView;
    if ( convertView == null )
    {
      imageView = new ImageView( mContext );
      imageView.setLayoutParams( new GridView.LayoutParams( POSTER_WIDTH, POSTER_HEIGHT ) );
      imageView.setScaleType( ImageView.ScaleType.FIT_CENTER );
      imageView.setPadding( 2, 2, 2, 2 );
    }
    else
    {
      imageView = (ImageView) convertView;
    }

    final Movie item = mData[ position ];
    if ( item != null )
    {
      // Load poster
      Picasso.with( mContext ).load( TMDB_IMG_URL + item.poster_path ).into( imageView );

      // Set onclick to bring the user to a details page
      imageView.setOnClickListener( new View.OnClickListener()
      {
        @Override
        public void onClick( View v )
        {
          MovieInfoActivity.mSelected = item;
          mContext.startActivity( new Intent( mContext, MovieInfoActivity.class ) );
        }
      } );
    }
    return imageView;
  }
}
