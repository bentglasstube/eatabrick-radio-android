package org.eatabrick.radio;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.content.Context;
import android.util.Log;
import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {
  private final static String TAG = "PagerAdapter";

  private Context mContext;
  private List<Fragment> mFragments;

  public PagerAdapter(Context context, FragmentManager manager, List<Fragment> fragments) {
    super(manager);

    mContext = context;
    mFragments = fragments;
  }

  @Override public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override public int getCount() {
    return mFragments.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return mContext.getString(R.string.tab_now_playing);
      case 1:
        return mContext.getString(R.string.tab_play_queue);
      default:
        return "";
    }
  }
}
