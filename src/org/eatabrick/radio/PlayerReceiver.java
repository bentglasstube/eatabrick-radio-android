package org.eatabrick.radio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PlayerReceiver extends BroadcastReceiver {
  private final static String TAG = "PlayerReceiver";

  public final static String ACTION_STOP = "org.eatabrick.radio.PlayerReceiver.ACTION_STOP";
  public final static String ACTION_SKIP = "org.eatabrick.radio.PlayerReceiver.ACTION_SKIP";

  @Override public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    Log.d(TAG, "Got broadcast intent: " + action);

    if (action.equals(ACTION_STOP)) {
      context.startService(new Intent(PlayerService.ACTION_STOP, null, context, PlayerService.class));
    } else if (action.equals(ACTION_SKIP)) {
      context.startService(new Intent(PlayerService.ACTION_SKIP, null, context, PlayerService.class));
    }
  }
}
