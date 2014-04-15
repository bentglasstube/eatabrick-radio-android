package org.eatabrick.radio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class PlayerReceiver extends BroadcastReceiver {
  private final static String TAG = "PlayerReceiver";

  public final static String ACTION_STOP = "org.eatabrick.radio.PlayerReceiver.ACTION_STOP";
  public final static String ACTION_SKIP = "org.eatabrick.radio.PlayerReceiver.ACTION_SKIP";

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    Log.d(TAG, "Got broadcast intent: " + action);

    if (action.equals(ACTION_STOP)) {
      sendAction(context, PlayerService.ACTION_STOP);
    } else if (action.equals(ACTION_SKIP)) {
      sendAction(context, PlayerService.ACTION_SKIP);
    } else if (action.equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
      sendAction(context, PlayerService.ACTION_STOP);
    }
  }

  private void sendAction(Context context, String action) {
    context.startService(new Intent(action, null, context, PlayerService.class));
  }
}
