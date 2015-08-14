package com.kyle.popularmovies.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kyle.popularmovies.R;
import com.kyle.popularmovies.data.Trailer;

/**
 * The adapter to a ListView, that displays all the trailers.
 */
public class TrailerAdapter extends BaseAdapter
{
  /**
   * Context of the activity that creates this adapter.
   */
  private final Context mContext;

  /**
   * The trailers to list.
   */
  private Trailer[] mData;

  public TrailerAdapter( Context context, Trailer[] trailers )
  {
    mData = trailers;
    mContext = context;
  }


  /**
   * @return How many trailers do I have?
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
    return Long.parseLong( mData[ position ].id );
  }

  @Override
  public View getView( int position, View convertView, ViewGroup parent )
  {
    TrailerHolder holder;
    if ( convertView == null )
    {
      LayoutInflater inflater = ( (Activity) mContext ).getLayoutInflater();
      convertView = inflater.inflate( R.layout.trailer_list_item, parent, false );

      holder = new TrailerHolder();
      holder.btnPlay = (ImageButton) convertView.findViewById( R.id.trailer_play );
      holder.txtTitle = (TextView) convertView.findViewById( R.id.trailer_title );

      convertView.setTag( holder );
    }
    else
    {
      holder = (TrailerHolder) convertView.getTag();
    }

    final Trailer item = mData[ position ];
    if ( item != null )
    {
      holder.btnPlay.setOnClickListener( new View.OnClickListener()
      {
        @Override
        public void onClick( View v )
        {
          // TODO Launch trailer
        }
      } );

      holder.txtTitle.setText( item.name );
    }
    return convertView;
  }

  private class TrailerHolder
  {
    ImageButton btnPlay;
    TextView txtTitle;
  }
}
