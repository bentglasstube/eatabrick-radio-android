package org.eatabrick.radio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;

public class NowPlayingFragment extends SherlockFragment {
  private static final String TAG = "NowPlayingFragment";

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.playing, container, false);
  }
}
