package com.kyle.popularmovies;

import android.app.Activity;
import android.os.Bundle;


public class MovieListActivity extends Activity
{

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_movie_list );
  }
}
