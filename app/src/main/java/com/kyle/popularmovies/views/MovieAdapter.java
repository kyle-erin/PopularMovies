package com.kyle.popularmovies.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kyle.popularmovies.R;
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
  public static final String TMDB_IMG_URL = "http://image.tmdb.org/t/p/w500";

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
    MovieHolder holder;
    if ( convertView == null )
    {
      LayoutInflater inflater = (( Activity)mContext).getLayoutInflater();
      convertView = inflater.inflate( R.layout.movie_list_item, parent, false );

      holder = new MovieHolder();
      holder.imgPoster = (ImageView)convertView.findViewById( R.id.movie_poster );

      convertView.setTag( holder );
    }
    else
    {
      holder = (MovieHolder)convertView.getTag();
    }

    final Movie item = mData[ position ];
    if ( item != null )
    {
      // Load poster
      Picasso.with( mContext ).load( TMDB_IMG_URL + item.poster_path ).into( holder.imgPoster );

      // Set onclick to bring the user to a details page
      holder.imgPoster.setOnClickListener( new View.OnClickListener()
      {
        @Override
        public void onClick( View v )
        {
          MovieInfoActivity.mSelected = item;
          mContext.startActivity( new Intent( mContext, MovieInfoActivity.class ) );
        }
      } );
    }
    return convertView;
  }

  /**
   * Stores all the components used to display a movie poster.
   */
  private class MovieHolder
  {
    ImageView imgPoster;
    public MovieHolder(){}
  }
}
