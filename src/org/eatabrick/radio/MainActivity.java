package org.eatabrick.radio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import java.util.List;
import org.bff.javampd.objects.MPDSong;

public class MainActivity extends SherlockFragmentActivity implements PlayerService.PlayerListener {
  private static final String TAG = "MainActivity";

  private PlayerService mService;
  private SongListAdapter mAdapter;

  private ServiceConnection mConnection = new ServiceConnection() {
    public void onServiceConnected(ComponentName className, IBinder binder) {
      Log.d(TAG, "Connected to service");
      mService = ((PlayerService.PlayerBinder) binder).getService();
      mService.addPlayerListener(MainActivity.this);
      invalidateOptionsMenu();
      requestSongInfo();
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

    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    ActionBar.Tab tab;

    tab = getSupportActionBar().newTab();
    tab.setText(getString(R.string.tab_now_playing));
    tab.setTabListener(new TabListener<NowPlayingFragment> (this, "playing", NowPlayingFragment.class));
    getSupportActionBar().addTab(tab);

    tab = getSupportActionBar().newTab();
    tab.setText(getString(R.string.tab_play_queue));
    tab.setTabListener(new TabListener<QueueFragment> (this, "queue", QueueFragment.class));
    getSupportActionBar().addTab(tab);

    /* not yet implemented
    tab = getSupportActionBar().newTab();
    tab.setText(getString(R.string.tab_search));
    tab.setTabListener(new TabListener<FutureFragment> (this, "search", FutureFragment.class));
    getSupportActionBar().addTab(tab);
    */

    if (savedInstanceState != null) {
      getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("selectedTab"));
    }

    mAdapter = new SongListAdapter(this);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putInt("selectedTab", getSupportActionBar().getSelectedTab().getPosition());
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
    MenuInflater inflater = getSupportMenuInflater();
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

  public void requestSongInfo() {
    if (mService != null) {
      NowPlayingFragment playingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentByTag("playing");
      if (playingFragment != null) {
        playingFragment.updateSongInfo(mService.getTitle(), mService.getArtist(), mService.getAlbum());
        playingFragment.updateProgress(mService.getElapsed(), mService.getLength());
      }
    }
  }

  public void onSongChange(final String title, final String artist, final String album, final int elapsed, final int length) {
    final NowPlayingFragment playingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentByTag("playing");
    if (playingFragment != null) {
      runOnUiThread(new Runnable() {
        public void run() {
          playingFragment.updateSongInfo(title, artist, album);
          playingFragment.updateProgress(elapsed, length);
        }
      });
    }
  }

  public void onPositionChange(final int elapsed) {
    final NowPlayingFragment playingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentByTag("playing");
    if (playingFragment != null) {
      runOnUiThread(new Runnable() {
        public void run() {
          playingFragment.updateProgress(elapsed);
        }
      });
    }
  }

  public void onStatusChange(final boolean playing) {
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
        mAdapter.addAll(songList);
        mAdapter.setCurrentSong(pos);
        mAdapter.notifyDataSetChanged();
      }
    });
  }

  public SongListAdapter getQueueAdapter() {
    return mAdapter;
  }

  private void setMenuVisible(Menu menu, int menuId, boolean visible) {
    ((MenuItem) menu.findItem(menuId)).setVisible(visible);
  }
}
