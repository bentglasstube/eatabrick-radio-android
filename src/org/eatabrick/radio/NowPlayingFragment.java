package org.eatabrick.radio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class NowPlayingFragment extends SherlockFragment implements MainActivity.UpdateListener {
  private static final String TAG = "NowPlayingFragment";

  private Bitmap          mAlbumArt;
  private GetAlbumArtTask mAlbumArtTask;
  private ProgressBar     mDownloading;
  private ImageView       mArt;
  private TextView        mTitle;
  private TextView        mArtist;
  private TextView        mAlbum;
  private TextView        mElapsed;
  private TextView        mLength;
  private ProgressBar     mSeek;

  private class GetAlbumArtTask extends AsyncTask<String, Void, Void> {
    protected Void doInBackground(String... strings) {
      String artist = strings[0];
      String album  = strings[1];

      if (artist.equals("") || album.equals("")) return null;

      DefaultHandler handler = new DefaultHandler() {
        private boolean inElem = false;
        private String  data = "";

        public void startDocument() throws SAXException { }
        public void endDocument() throws SAXException {
          if (!data.equals("")) {
            Log.d(TAG, "Album art found at " + data);

            try {
              URL url = new URL(data);
              InputStream is = url.openConnection().getInputStream();
              Bitmap art = BitmapFactory.decodeStream(is);
              setAlbumArt(art);
            } catch (Exception e) {
              Log.d(TAG, "Error: " + e.getMessage());
            }
          } else {
            Log.d(TAG, "No album art found");
          }

        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
          if (localName.equalsIgnoreCase("image")) {
            String size = attributes.getValue("size");
            if (size.equals("extralarge")) {
              inElem = true;
            }
          }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
          if (inElem) inElem = false;
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
          if (inElem) {
            data = data + new String(ch, start, length);
          }
        }
      };

      try {
        Log.d(TAG, "Looking for art for " + artist + " - " + album);

        Uri.Builder builder = Uri.parse("http://ws.audioscrobbler.com").buildUpon();
        builder.path("/2.0/");
        builder.appendQueryParameter("method", "album.getinfo");
        builder.appendQueryParameter("api_key", "4827e70daf0106ae5a88b268c083e65b");
        builder.appendQueryParameter("artist", artist);
        builder.appendQueryParameter("album", album);

        URL albumInfo = new URL(builder.build().toString());

        XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        reader.setContentHandler(handler);
        InputSource input = new InputSource(albumInfo.openStream());
        reader.parse(input);
      } catch (Exception e) {
        e.printStackTrace();
      }

      return null;
    }

    protected void onPreExecute() {
      mArt.setImageResource(R.drawable.album);
      setAlbumArt(null);

      if (mDownloading != null) mDownloading.setVisibility(View.VISIBLE);
    }

    protected void onPostExecute(Void result) {
      if (getAlbumArt() != null) {
        mArt.setImageBitmap(getAlbumArt());
      }

      if (mDownloading != null) mDownloading.setVisibility(View.GONE);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.playing, container, false);

    mDownloading = (ProgressBar) view.findViewById(R.id.downloading);
    mArt         = (ImageView)   view.findViewById(R.id.playing_art);
    mTitle       = (TextView)    view.findViewById(R.id.playing_title);
    mArtist      = (TextView)    view.findViewById(R.id.playing_artist);
    mAlbum       = (TextView)    view.findViewById(R.id.playing_album);
    mElapsed     = (TextView)    view.findViewById(R.id.seek_current);
    mLength      = (TextView)    view.findViewById(R.id.seek_total);
    mSeek        = (ProgressBar) view.findViewById(R.id.seek);

    if (mAlbumArtTask != null) {
      AsyncTask.Status status = mAlbumArtTask.getStatus();
      if (status == AsyncTask.Status.FINISHED) {
        mDownloading.setVisibility(View.GONE);
      } else {
        mDownloading.setVisibility(View.VISIBLE);
      }
    }

    return view;
  }

  @Override public void onResume() {
    super.onResume();

    ((MainActivity) getActivity()).addUpdateListener(this);
  }

  @Override public void onPause() {
    super.onPause();

    ((MainActivity) getActivity()).removeUpdateListener(this);
  }

  public void onSongUpdate(String title, String artist, String album, int elapsed, int length) {
    mTitle.setText(title);
    mArtist.setText(artist);
    mAlbum.setText(album);

    mElapsed.setText(String.format("%d:%02d", elapsed / 60, elapsed % 60));
    mSeek.setProgress(elapsed);

    mLength.setText(String.format("%d:%02d", length / 60, length % 60));
    mSeek.setMax(length);

    if (mAlbumArtTask != null) mAlbumArtTask.cancel(true);
    mAlbumArtTask = new GetAlbumArtTask();
    mAlbumArtTask.execute(artist, album);
  }

  public void onPositionUpdate(int elapsed) {
    mElapsed.setText(String.format("%d:%02d", elapsed / 60, elapsed % 60));
    mSeek.setProgress(elapsed);
  }

  public void setAlbumArt(Bitmap art) {
    mAlbumArt = art;
  }

  public Bitmap getAlbumArt() {
    return mAlbumArt;
  }
}
