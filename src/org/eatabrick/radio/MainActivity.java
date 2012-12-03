package org.eatabrick.radio;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

public class MainActivity extends SherlockActivity implements ActionBar.TabListener {
  private static final String TAG = "NowPlayingActivity";

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.playing);

    ((SeekBar) findViewById(R.id.seek)).setEnabled(false);

    getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    ActionBar.Tab tab;

    tab = getSupportActionBar().newTab().setTabListener(this).setText(getString(R.string.tab_now_playing));
    getSupportActionBar().addTab(tab);
  }

  @Override public void onTabReselected(Tab tab, FragmentTransaction trans) {
    Log.d(TAG, "Tab reselected: " + tab.getText());
  }

  @Override public void onTabSelected(Tab tab, FragmentTransaction trans) {
    Log.d(TAG, "Tab selected: " + tab.getText());
  }

  @Override public void onTabUnselected(Tab tab, FragmentTransaction trans) {
    Log.d(TAG, "Tab unselected: " + tab.getText());
  }
}
