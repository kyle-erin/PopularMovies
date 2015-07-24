package com.kyle.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kyle on 7/24/2015.
 */
public class Trailer implements Parcelable
{
  /**
   * Unique ID for this trailer.
   */
  public String id;

  /**
   * The key that corresponds to the trailer on 'site'.
   */
  public String key;

  /**
   * The name of the trailer.
   */
  public String name;

  /**
   * The site the trailer is hosted on.
   */
  public String site;

  /**
   * The size of the file.
   */
  public String size;

  /**
   * The type of video.
   */
  public String type;

  public Trailer()
  {
  }

  /**
   * Create a trailer object from a parcel object.
   * @param in The parcel containing a serialized Trailer object.
   */
  public Trailer( Parcel in )
  {
    String[] data = new String[ 6 ];
    in.readStringArray( data );

    this.id = data[ 0 ];
    this.key = data[ 1 ];
    this.name = data[ 2 ];
    this.site = data[ 3 ];
    this.size = data[ 4 ];
    this.type = data[ 5 ];
  }

  @Override
  public int describeContents()
  {
    return 0;
  }

  /**
   * Write all data to string array.
   * @param dest The parcel to write data to.
   */
  @Override
  public void writeToParcel( Parcel dest, int flags )
  {
    dest.writeStringArray( new String[]{ id, key, name, site, size, type } );
  }

  public static final Parcelable.Creator<Trailer> CREATOR
      = new Parcelable.Creator<Trailer>()
  {
    public Trailer createFromParcel( Parcel in )
    {
      return new Trailer( in );
    }

    public Trailer[] newArray( int size )
    {
      return new Trailer[ size ];
    }
  };
}
