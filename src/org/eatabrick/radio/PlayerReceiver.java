package org.eatabrick.radio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;

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
    } else if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {
      KeyEvent event = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
      if (event.getAction() != KeyEvent.ACTION_DOWN) return;

      switch (event.getKeyCode()) {
        case KeyEvent.KEYCODE_MEDIA_PLAY:
          sendAction(context, PlayerService.ACTION_PLAY);
          break;
        case KeyEvent.KEYCODE_MEDIA_PAUSE:
          sendAction(context, PlayerService.ACTION_PAUSE);
          break;
        case KeyEvent.KEYCODE_HEADSETHOOK:
        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
          sendAction(context, PlayerService.ACTION_TOGGLE);
          break;
        case KeyEvent.KEYCODE_MEDIA_NEXT:
          sendAction(context, PlayerService.ACTION_SKIP);
          break;
        case KeyEvent.KEYCODE_MEDIA_STOP:
          sendAction(context, PlayerService.ACTION_STOP);
          break;
        default:
          Log.d(TAG, "Unhandled key code: " + event.getKeyCode());
          break;
      }
    }
  }

  private void sendAction(Context context, String action) {
    context.startService(new Intent(action, null, context, PlayerService.class));
  }
}
