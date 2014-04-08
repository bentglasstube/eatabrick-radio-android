package org.eatabrick.radio;

import android.app.Activity;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ListFragment;

public class QueueFragment extends ListFragment {
  private static final String TAG = "QueueFragment";

  private SongListAdapter mAdapter;

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    Log.d(TAG, "Activity created");

    super.onActivityCreated(savedInstanceState);

    mAdapter = ((MainActivity) getActivity()).getQueueAdapter();
    setListAdapter(mAdapter);
  }
}
