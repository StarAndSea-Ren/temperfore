package com.yueh.ren.temforecast.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yueh.ren.temforecast.R;
import com.yueh.ren.temforecast.model.entity.TemperBean;
import com.yueh.ren.temforecast.util.Constants;
import com.yueh.ren.temforecast.util.SPUtil;
import com.yueh.ren.temforecast.view.TempForecastActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 任跃华 on 2016/4/17 0017.
 */
public class SaveDataService extends Service {

    private static final Long DEFAULT_SAVE_GAP = 1000L;
    private static Long saveGap = null;
    private Timer mTimer;

    public static void setSaveGap(Long gapMillis) {
        if (gapMillis == null) {
            return;
        }
        saveGap = gapMillis;
        SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.DATA_MANAGER_SAVE_DATA_GAP, gapMillis.toString());
    }

    public static Long getSaveGap() {
        if (saveGap != null) {
            return saveGap;
        }
        String gapStr = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.DATA_MANAGER_SAVE_DATA_GAP);
        if (gapStr.isEmpty()) {
            saveGap = DEFAULT_SAVE_GAP;
        } else {
            saveGap = Long.valueOf(gapStr);
        }
        return saveGap;
    }

    private void warnNotif(double temp) {
        if (temp < TFConfiguration.getTempWarnLine()) {
            return;
        }
        Notification.Builder notification = new Notification.Builder(getApplicationContext());
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle("警报");
        notification.setDefaults(Notification.DEFAULT_SOUND);
        Intent intent = new Intent(this, TempForecastActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification.setContentText("温度超过警戒线");
        notification.setContentIntent(pendingIntent);
        notificationManager.notify(1, notification.build());
    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TemperBean temperBean = TFConfiguration.getCollector().getTemperature();
                if (temperBean == null) {
                    Log.e("Debug", "温度采集器可能坏了，未采集到数据");
                } else {
                    warnNotif(temperBean.getmTempreature());
                    TFConfiguration.getDataManager().smartSaveTemper(temperBean);
                    Log.i("Debug", "此时温度为" + temperBean.getmTempreature());
                }
            }
        }, 0, getSaveGap());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        setTimerTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
