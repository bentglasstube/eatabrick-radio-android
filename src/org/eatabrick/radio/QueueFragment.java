package org.eatabrick.radio;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;

public class QueueFragment extends SherlockListFragment {
  private static final String TAG = "QueueFragment";

  private SongListAdapter mAdapter;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    Log.d(TAG, "Activity created");

    super.onActivityCreated(savedInstanceState);

    mAdapter = ((MainActivity) getActivity()).getQueueAdapter();
    setListAdapter(mAdapter);
  }
}
