package org.eatabrick.radio;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
  private static final String TAG = "TabListener";
  private Fragment fragment;
  private final SherlockFragmentActivity activity;
  private final String tag;
  private final Class<T> klass;

  public TabListener(SherlockFragmentActivity activity, String tag, Class<T> klass) {
    this.activity = activity;
    this.tag = tag;
    this.klass = klass;
  }

  @Override public void onTabReselected(Tab tab, FragmentTransaction trans) {
    Log.d(TAG, "Tab reselected: " + tab.getText());
  }

  @Override public void onTabSelected(Tab tab, FragmentTransaction trans) {
    Log.d(TAG, "Tab selected: " + tab.getText());

    // for some reason the support library gives a garbage transaction
    if (trans == null) {
      trans = ((FragmentManager) activity.getSupportFragmentManager()).beginTransaction();
      trans.commit();
    }

    if (fragment == null) {
      fragment = SherlockFragment.instantiate(activity, klass.getName());
      fragment.setRetainInstance(false);
      trans.add(android.R.id.content, fragment, tag);
    } else {
      trans.attach(fragment);
    }
  }

  @Override public void onTabUnselected(Tab tab, FragmentTransaction trans) {
    Log.d(TAG, "Tab unselected: " + tab.getText());

    // for some reason the support library gives a garbage transaction
    if (trans == null) {
      trans = ((FragmentManager) activity.getSupportFragmentManager()).beginTransaction();
      trans.commit();
    }

    if (fragment != null) {
      trans.detach(fragment);
    }
  }
}
