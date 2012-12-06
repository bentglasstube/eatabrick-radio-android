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

  private ImageView mArt;
  private TextView  mTitle;
  private TextView  mArtist;
  private TextView  mAlbum;
  private TextView  mElapsed;
  private TextView  mLength;
  private SeekBar   mSeek;

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.playing, container, false);

    mArt     = (ImageView) view.findViewById(R.id.playing_art);
    mTitle   = (TextView)  view.findViewById(R.id.playing_title);
    mArtist  = (TextView)  view.findViewById(R.id.playing_artist);
    mAlbum   = (TextView)  view.findViewById(R.id.playing_album);
    mElapsed = (TextView)  view.findViewById(R.id.seek_current);
    mLength  = (TextView)  view.findViewById(R.id.seek_total);
    mSeek    = (SeekBar)   view.findViewById(R.id.seek);

    return view;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    ((MainActivity) getActivity()).requestSongInfo();
  }

  public void updateSongInfo(String title, String artist, String album) {
    // TODO get album art

    mTitle.setText(title);
    mArtist.setText(artist);
    mAlbum.setText(album);
  }

  public void updateProgress(int elapsed, int length) {
    mLength.setText(String.format("%d:%02d", length / 60, length % 60));
    mSeek.setMax(length);

    updateProgress(elapsed);
  }

  public void updateProgress(int elapsed) {
    mElapsed.setText(String.format("%d:%02d", elapsed / 60, elapsed % 60));
    mSeek.setProgress(elapsed);
  }
}
