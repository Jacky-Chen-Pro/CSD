/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.incongress.cd.conference.utils;

import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceTableField;

/**
 * Manages alarms and vibe. Runs as a service so that it can continue to play if
 * another activity overrides the AlarmAlert dialog.
 */
public class AlarmKlaxon extends Service {

    /**
     * Play alarm up to 10 minutes before silencing
     */
    private static final int ALARM_TIMEOUT_SECONDS = 60;

    private static final long[] sVibratePattern = new long[]{500, 500};

    private boolean mPlaying = false;
    private Vibrator mVibrator;
    private MediaPlayer mMediaPlayer;
    private AlertBean mCurrentAlarm;
    private long mStartTime;
    private TelephonyManager mTelephonyManager;
    private int mInitialCallState;
    private AudioManager mAudioManager = null;
    private boolean mCurrentStates = true;

    // Internal messages
    private static final int KILLER = 1;
    private static final int FOCUSCHANGE = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KILLER:
                    AlertBean bean = (AlertBean) msg.obj;
                    stop();
                    stopSelf();
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AlarmKlaxon.this);
                    int times = sp.getInt(AlermClock.KEY_TIMES, 2);
                    if (bean != null) {
                        int currentTimes = Integer.parseInt(bean.getRepeattimes());
                        if (times - currentTimes > 0) {
                            bean.setRepeattimes(String.valueOf(++currentTimes));
                            AlermClock.calculateSnoothAlert(bean);
                        } else
                            AlermClock.deleteClock(bean);
                    }
                    break;
                case FOCUSCHANGE:
                    switch (msg.arg1) {
                        case AudioManager.AUDIOFOCUS_LOSS:

                            if (!mPlaying && mMediaPlayer != null) {
                                stop();
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                            if (!mPlaying && mMediaPlayer != null) {
                                mMediaPlayer.pause();
                                mCurrentStates = false;
                            }
                            break;
                        case AudioManager.AUDIOFOCUS_GAIN:

                            if (mPlaying && !mCurrentStates) {
                                play(mCurrentAlarm);
                            }
                            break;
                        default:

                            break;
                    }
                default:
                    break;

            }
        }
    };

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String ignored) {
            if (state != TelephonyManager.CALL_STATE_IDLE
                    && state != mInitialCallState) {
                stop();
                stopSelf();
            }
        }
    };

    @Override
    public void onCreate() {
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        AlarmReceiver.acquireCpuWakeLock(this);
    }

    @Override
    public void onDestroy() {
        stop();
        mTelephonyManager.listen(mPhoneStateListener, 0);
        AlarmReceiver.releaseCpuLock();
        mAudioManager.abandonAudioFocus(mAudioFocusListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }

        final AlertBean alarm = (AlertBean) intent.getSerializableExtra("object");
        if (alarm == null) {
            stopSelf();
            return START_NOT_STICKY;
        }
        int type = alarm.getType();
        String relateId = alarm.getRelativeid();
        int id = Integer.parseInt(relateId);
        int count = 0;
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        String sql = "select * from ";
        if (type == AlertBean.TYPE_SESSTION) {
            sql = sql + ConferenceTables.TABLE_INCONGRESS_SESSION + " where "
                    + ConferenceTableField.SESSION_ATTENTION + " = " + Constants.SMATTENTION + " and "
                    + ConferenceTableField.SESSION_SESSIONGROUPID + " = " + id;
        }
        if (type == AlertBean.TYPE_MEETING) {
            sql = sql + ConferenceTables.TABLE_INCONGRESS_MEETING + " where "
                    + ConferenceTableField.MEETING_ATTENTION + " = " + Constants.SMATTENTION + " and "
                    + ConferenceTableField.MEETING_MEETINGID + " = " + id;
        }
        count = ada.getTableItemCountSql(sql);
        ada.close();
        if (count <= 0) {
            stopSelf();
            return START_NOT_STICKY;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enable = sp.getBoolean(AlermClock.KEY_ENABLE, true);

        if (!enable) {
            int times = sp.getInt(AlermClock.KEY_TIMES, 2);
            int currentTimes = Integer.parseInt(alarm.getRepeattimes());
            if (times - currentTimes > 0) {
                alarm.setRepeattimes(String.valueOf(++currentTimes));
                AlermClock.calculateSnoothAlert(alarm);
            }
            stopSelf();
            return START_STICKY;
        }
        if (mCurrentAlarm != null) {
            stop();
        }
        mCurrentAlarm = alarm;
        play(alarm);
        showView(this, alarm);
        mInitialCallState = mTelephonyManager.getCallState();

        return START_STICKY;
    }

    /**
     * 闹钟 跳出的界面
     *
     * @param context
     * @param note
     */
    private void showView(final Context context, final AlertBean note) {
        final WindowManager wm = (WindowManager) context.getSystemService("window");
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        wmParams.type = 2002;
        wmParams.flags |= 8;
        wmParams.gravity = Gravity.CENTER;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = dm.widthPixels;
        wmParams.height = dm.heightPixels;
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_alert, null);
        Button cancle = (Button) view.findViewById(R.id.dialog_cancel);
        Button submit = (Button) view.findViewById(R.id.dialog_ok);
        TextView content = (TextView) view.findViewById(R.id.dialog_content);
        final LinearLayout layout = new LinearLayout(getApplication());
        layout.setLayoutParams(new LayoutParams(dm.widthPixels, dm.heightPixels));
        layout.setGravity(Gravity.CENTER);
        layout.addView(view);
        String[] titles = note.getTitle().split("#@#");
        System.out.println("note.getTitle()===" + note.getTitle());
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String title = titles[0];
        if (!language.endsWith("zh") && !titles[1].equals("null")) {
            title = titles[1];
        }
        SharedPreferences spPreferences = PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
        int before = spPreferences.getInt(AlermClock.KEY_BEFORE, 5);
        String classroom = "";
        classroom = note.getRoom();
        System.out.println("ClassRoom===" + classroom);
        String time = note.getDate() + " " + note.getStart();
        long alert_time = DateTime.getDate(time + ":00", DateTime.DEFAULT_SECOND).getTime();
        long now = System.currentTimeMillis();
        String tt = (alert_time - now) / (60 * 1000) + 1 + "";
        if (AppApplication.systemLanguage == 1) {
            tt = tt + " 分钟";
        } else {
            tt = tt + " minutes";
        }

        String string = getString(R.string.alert_start, title, tt, classroom);
        content.setText(string);
        cancle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                wm.removeView(layout);
                stop();
                disableKiller();
                stopSelf();
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AlarmKlaxon.this);
                int times = sp.getInt(AlermClock.KEY_TIMES, 2);
                int currentTimes = Integer.parseInt(note.getRepeattimes());
                if (times - currentTimes > 0) {
                    note.setRepeattimes(String.valueOf(++currentTimes));
                    AlermClock.calculateSnoothAlert(note);
                } else
                    AlermClock.deleteClock(note);
            }
        });
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                wm.removeView(layout);
                stop();
                AlermClock.deleteClock(note);
                disableKiller();
                stopSelf();

            }
        });
        wm.addView(layout, wmParams);
    }

    private OnAudioFocusChangeListener mAudioFocusListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            mHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };

    private void play(AlertBean alarm) {
        mAudioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        stop();

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.stop();
                mp.release();
                mMediaPlayer = null;
                return true;
            }
        });
        try {
            mMediaPlayer.setDataSource(this, alert);
            startAlarm(mMediaPlayer);
        } catch (Exception ex) {
        }

        mVibrator.vibrate(sVibratePattern, 0);
        enableKiller(alarm);
        mPlaying = true;
        mStartTime = System.currentTimeMillis();
    }

    private void startAlarm(MediaPlayer player) throws java.io.IOException,
            IllegalArgumentException, IllegalStateException {
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            player.setAudioStreamType(AudioManager.STREAM_ALARM);
            player.setLooping(true);
            player.prepare();
            player.start();
        }
    }

    /**
     * Stops alarm audio and disables alarm if it not snoozed and not repeating
     */
    public void stop() {
        if (mPlaying) {
            mPlaying = false;

            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

            mVibrator.cancel();
        }
        disableKiller();
    }

    private void enableKiller(AlertBean alarm) {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(KILLER, alarm),
                1000 * ALARM_TIMEOUT_SECONDS);
    }

    private void disableKiller() {
        mHandler.removeMessages(KILLER);
    }

}
