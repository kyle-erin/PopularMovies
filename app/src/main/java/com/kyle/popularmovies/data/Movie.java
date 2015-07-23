package com.kyle.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kyle on 7/21/2015.
 */
public class Movie implements Parcelable
{
  public Integer id;
  public String original_title;
  public String poster_path;
  public String release_date;
  public String vote_average;
  public String overview;

  public Movie()
  {
  }

  public Movie( Parcel in )
  {
    String[] data = new String[ 6 ];

    in.readStringArray( data );
    this.id = Integer.parseInt( data[ 0 ] );
    this.original_title = data[ 1 ];
    this.poster_path = data[ 2 ];
    this.release_date = data[ 3 ];
    this.vote_average = data[ 4 ];
    this.overview = data[ 5 ];
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  @Override
  public void writeToParcel( Parcel dest, int flags )
  {
    dest.writeStringArray( new String[]{ Integer.toString( this.id ), this.original_title, this.poster_path, this.release_date, this.vote_average, this.overview } );
  }

  public static final Parcelable.Creator<Movie> CREATOR
      = new Parcelable.Creator<Movie>()
  {
    public Movie createFromParcel( Parcel in )
    {
      return new Movie( in );
    }

    public Movie[] newArray( int size )
    {
      return new Movie[ size ];
    }
  };

}
