package com.yueh.ren.temforecast.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yueh.ren.temforecast.R;
import com.yueh.ren.temforecast.app.TFConfiguration;
import com.yueh.ren.temforecast.model.entity.TemperBean;
import com.yueh.ren.temforecast.app.SaveDataService;
import com.yueh.ren.temforecast.presenter.TempForecastPresenter;
import com.yueh.ren.temforecast.util.Constants;
import com.yueh.ren.temforecast.util.TempEvent;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 任跃华 on 2016/3/30 0030.
 */
public class TempForecastActivity extends AppCompatActivity
        implements ITempForecastView {

    private GraphicalView mTempView;
    private FrameLayout mContainer;
    private TempForecastPresenter mPresenter;
    private TextView mConfigInfo1;
    private TextView mConfigInfo2;
    private Timer mTimer;

    public void init() {

        initActionBar();
        mContainer = (FrameLayout) findViewById(R.id.container);
        mPresenter = new TempForecastPresenter(this);
        mPresenter.getData();
        IntentFilter filter = new IntentFilter(Constants.ACTION_CONFIG_CHG);
        MsgReceiver receive = new MsgReceiver();
        registerReceiver(receive, filter);

        mConfigInfo1 = (TextView) findViewById(R.id.info1_text);
        mConfigInfo2 = (TextView) findViewById(R.id.info2_text);

        initConfigInfo();

        mTimer = new Timer();
        setTimerTask();
    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("温度预测");
        toolbar.setLogo(R.drawable.ic_wb_incandescent_black_24dp);
    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mPresenter.getData();
            }
        }, 0, SaveDataService.getSaveGap());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.eventBusUnregister();
    }

    @Override
    public void showData(TempEvent event) {
        mTempView = ChartFactory.getLineChartView(this, event.dataSet, event.renderer);
        mContainer.removeAllViews();
        mContainer.addView(mTempView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_history_data:
                addTestData();
                mPresenter.getData();
                Toast.makeText(this,"已插入一天的温度数据作为测试",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConfigChg = intent.getBooleanExtra(Constants.MSG_CONFIG_CHG, false);
            if (isConfigChg) {
                mPresenter.getData();
                initConfigInfo();
            }
        }
    }

    private void initConfigInfo() {
        Long collectorGap = TFConfiguration.getCollector().getCollectGap();
        Long dataSaveGap = SaveDataService.getSaveGap();
        Long chartTimeGap = TempChart.getTimeGap();
        Double warnLine = TFConfiguration.getTempWarnLine();

        mConfigInfo2.setText("采集周期:  " + collectorGap + "ms" + "\n"
                + "存储周期:  " + dataSaveGap + "ms" + "\n"
                + "展示粒度:  " + chartTimeGap + "ms" + "\n"
                + "警戒温度:  " + warnLine + "°C");

        String collector = "--";
        String forecastAlgo = "--";
        String dataManager = "--";

        switch (TFConfiguration.getCollector().getClass().getName()) {
            case Constants.COLLECTOR_GD:
                collector = "高德定位API";
                break;
            case Constants.COLLECTOR_MYQX:
                collector = "绵阳气象网站";
                break;
            case Constants.COLLECTOR_CPU:
                collector = "CPU温度";
                break;
            default:
                break;
        }
        switch (TFConfiguration.getAlgo().getClass().getName()) {
            case Constants.FORECAST_ALGO_LEASTSQUARE:
                forecastAlgo = "最小二乘法";
                break;
            case Constants.FORECAST_ALGO_LINEAR:
                forecastAlgo = "线性预测";
                break;
            default:
                break;
        }
        switch (TFConfiguration.getDataManager().getClass().getName()) {
            case Constants.DATA_MANAGER_FILE:
                dataManager = "文件存储";
                break;
            case Constants.DATA_MANAGER_SQLITE:
                dataManager = "数据库存储";
                break;
            default:
                break;
        }

        mConfigInfo1.setText("采集器:  " + collector + "\n"
                + "预测算法:  " + forecastAlgo + "\n"
                + "存储方式:  " + dataManager);
    }

    private void addTestData() {
        long currentTime = System.currentTimeMillis();
        long yestTime = currentTime - 86400000;

        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 32));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 31));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 29));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 28));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 27));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 27));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 25));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 25));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 24));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 24));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 23));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 23));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 22));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 23));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 25));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 26));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 27));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 27));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 28));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 28));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 27));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 26));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 26));
        yestTime += 3600000;
        TFConfiguration.getDataManager().smartSaveTemper(new TemperBean(yestTime + "", 25));
    }
}
