package com.kyle.popularmovies.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.kyle.popularmovies.R;
import com.kyle.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by kyle on 7/21/2015.
 */
public class MovieAdapter extends BaseAdapter
{
  public static final String TMDB_IMG_URL = "http://image.tmdb.org/t/p/w185";
  /**
   * Context of the activity that creates this adapter.
   */
  private Context mContext;

  /**
   * Movie data
   */
  private Movie[] mData;

  public MovieAdapter( Context context, Movie[] data )
  {
    mContext = context;
    mData = data;
  }

  @Override
  public int getCount()
  {
    return mData.length;
  }

  @Override
  public Object getItem( int position )
  {
    return null;
  }

  @Override
  public long getItemId( int position )
  {
    return 0;
  }

  @Override
  public View getView( int position, View convertView, ViewGroup parent )
  {
    ImageView imageView;
    if ( convertView == null )
    {
      imageView = new ImageView( mContext );
      imageView.setLayoutParams( new GridView.LayoutParams( 200, 200 ) );
//      imageView.setAdjustViewBounds( true );
      imageView.setScaleType( ImageView.ScaleType.FIT_CENTER);
      imageView.setPadding( 2, 2, 2, 2 );
    }
    else
    {
      imageView = (ImageView) convertView;
    }

    final Movie item = mData[position];
    if(item != null)
    {
      String url = TMDB_IMG_URL + item.poster_path;
      Picasso.with( mContext ).load( TMDB_IMG_URL + item.poster_path ).into( imageView );
    }
    return imageView;
  }
}
