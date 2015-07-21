package com.kyle.popularmovies.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.kyle.popularmovies.data.GetMoviesService;
import com.kyle.popularmovies.data.Movie;
import com.kyle.popularmovies.views.MovieAdapter;
import com.kyle.popularmovies.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;


public class MovieListActivity extends Activity
{
  private static final String LOG_TAG = MovieListActivity.class.getSimpleName();
  @Bind( R.id.gridview )
  GridView mGridView;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_movie_list );
    ButterKnife.bind( this );
    EventBus.getDefault().register( this );
    startService( new Intent( this, GetMoviesService.class ) );



//    mGridView.setOnItemClickListener( new AdapterView.OnItemClickListener()
//    {
//      @Override
//      public void onItemClick( AdapterView<?> parent, View view, int position, long id )
//      {
//        Toast.makeText( MovieListActivity.this, "" + position, Toast.LENGTH_SHORT ).show();
//      }
//    } );
  }

  @SuppressWarnings( "UnusedDeclaration" )
  public void onEventMainThread( Movie[] data )
  {
    mGridView.setAdapter( new MovieAdapter( this, data ) );
  }

  @SuppressWarnings( "UnusedDeclaration" )
  public void onEventMainThread( Exception e )
  {
    Log.v( LOG_TAG, e.toString());
  }
}
