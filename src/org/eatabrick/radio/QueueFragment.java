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

  private static final class Radio {
    public static final String[] TITLES = {
      "Rockman 4: All Stage Clear",
      "Wish Upon a Star ~ Broken Promise",
      "Fort Condor",
      "Breath of Fire",
      "Fairy's Doorway"
    };
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Radio.TITLES));
  }
}
