package org.eatabrick.radio;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.app.Activity;;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.List;
import java.util.Vector;
import org.bff.javampd.objects.MPDSong;

public class MainActivity extends Activity implements PlayerService.PlayerListener {
  private static final String TAG = "MainActivity";

  public interface UpdateListener {
    public void onSongUpdate(String title, String artist, String album, int elapsed, int length);
    public void onPositionUpdate(int elapsed);
  }

  private PlayerService mService;
  private SongListAdapter mAdapter;
  private PagerAdapter mPagerAdapter;
  private List<UpdateListener> mListeners;

  private ServiceConnection mConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder binder) {
      Log.d(TAG, "Connected to service");
      mService = ((PlayerService.PlayerBinder) binder).getService();
      mService.addPlayerListener(MainActivity.this);
      invalidateOptionsMenu();

      sendSongUpdate(mService.getTitle(), mService.getArtist(), mService.getAlbum(), mService.getElapsed(), mService.getLength());
    }

    public void onServiceDisconnected(ComponentName className) {
      Log.d(TAG, "Disconnected from service");
      mService = null;
      mService.removePlayerListener(MainActivity.this);
      invalidateOptionsMenu();
    }
  };

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);


    List<Fragment> fragments = new Vector<Fragment>();
    fragments.add(Fragment.instantiate(this, NowPlayingFragment.class.getName()));
    fragments.add(Fragment.instantiate(this, QueueFragment.class.getName()));

    mPagerAdapter = new PagerAdapter(this, getFragmentManager(), fragments);

    ((ViewPager) findViewById(R.id.pager)).setAdapter(mPagerAdapter);

    mAdapter = new SongListAdapter(this);
    mListeners = new Vector<UpdateListener>();
  }

  @Override public void onResume() {
    super.onResume();
    bindService(new Intent(this, PlayerService.class), mConnection, BIND_AUTO_CREATE);
  }

  @Override public void onPause() {
    super.onPause();
    unbindService(mConnection);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_play:
        startService(new Intent(PlayerService.ACTION_PLAY, null, this, PlayerService.class));
        return true;
      case R.id.menu_skip:
        startService(new Intent(PlayerService.ACTION_SKIP, null, this, PlayerService.class));
        return true;
      case R.id.menu_stop:
        startService(new Intent(PlayerService.ACTION_STOP, null, this, PlayerService.class));
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    if (mService == null) {
      setMenuVisible(menu, R.id.menu_play, false);
      setMenuVisible(menu, R.id.menu_stop, false);
      setMenuVisible(menu, R.id.menu_skip, false);
    } else {
      boolean playing = mService.isPlaying();

      setMenuVisible(menu, R.id.menu_play, !playing);
      setMenuVisible(menu, R.id.menu_stop, playing);
      setMenuVisible(menu, R.id.menu_skip, true);
    }

    return true;
  }

  public void onSongChange(final String title, final String artist, final String album, final int elapsed, final int length) {
    runOnUiThread(new Runnable() {
      public void run() {
        sendSongUpdate(title, artist, album, elapsed, length);
      }
    });
  }

  public void onPositionChange(final int elapsed) {
    runOnUiThread(new Runnable() {
      public void run() {
        sendPositionUpdate(elapsed);
      }
    });
  }

  public void onStatusChange(boolean playing) {
    runOnUiThread(new Runnable() {
      public void run() {
        invalidateOptionsMenu();
      }
    });
  }

  public void onQueueChange(final List<MPDSong> songList, final int pos) {
    runOnUiThread(new Runnable() {
      public void run() {
        mAdapter.clear();
        for (MPDSong song : songList) {
          mAdapter.add(song);
        }
        mAdapter.setCurrentSong(pos);
        mAdapter.notifyDataSetChanged();
      }
    });
  }

  public SongListAdapter getQueueAdapter() {
    return mAdapter;
  }

  public void addUpdateListener(UpdateListener listener) {
    mListeners.add(listener);
  }

  public void removeUpdateListener(UpdateListener listener) {
    mListeners.remove(listener);
  }

  private void setMenuVisible(Menu menu, int menuId, boolean visible) {
    ((MenuItem) menu.findItem(menuId)).setVisible(visible);
  }

  private void sendSongUpdate(String title, String artist, String album, int elapsed, int length) {
    for (UpdateListener listener : mListeners) {
      listener.onSongUpdate(title, artist, album, elapsed, length);
    }
  }

  private void sendPositionUpdate(int elapsed) {
    for (UpdateListener listener : mListeners) {
      listener.onPositionUpdate(elapsed);
    }
  }
}
