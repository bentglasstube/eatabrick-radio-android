package org.eatabrick.radio;

import android.os.Bundle;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity {
  private static final String TAG = "MainActivity";


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

    if (savedInstanceState != null) {
      getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("selectedTab"));
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("selectedTab", getSupportActionBar().getSelectedTab().getPosition());
  }
}
