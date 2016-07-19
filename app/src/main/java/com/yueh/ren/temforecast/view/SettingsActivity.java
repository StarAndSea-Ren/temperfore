package com.yueh.ren.temforecast.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yueh.ren.temforecast.R;
import com.yueh.ren.temforecast.app.TFConfiguration;
import com.yueh.ren.temforecast.app.SaveDataService;
import com.yueh.ren.temforecast.util.Constants;


/**
 * Created by 任跃华 on 2016/5/30 0030.
 */
public class SettingsActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener
        , RadioGroup.OnCheckedChangeListener {

    private SeekBar mCollectorGapSBar;
    private SeekBar mDataSaveGapSBar;
    private SeekBar mChartTimeGapSBar;
    private SeekBar mWarnLineSBar;

    private RadioButton mCollectorGDRBtn;
    private RadioButton mCollectorMYQXRBtn;
    private RadioButton mCollectorCPURBtn;
    private RadioButton mAlgoLineRBtn;
    private RadioButton mAlgoLSRBtn;
    private RadioButton mSaveFileRBtn;
    private RadioButton mSaveSqlRBtn;

    private RadioGroup mCollectorChoose;
    private RadioGroup mAlgoChoose;
    private RadioGroup mSaveChoose;

    private TextView mCollectorGapText;
    private TextView mDataSaveGapText;
    private TextView mChartTimeGapText;
    private TextView mWarnLineText;

    private Long mCollectorGap;
    private Long mDataSaveGap;
    private Long mChartTimeGap;
    private Double mWarnLine;

    private String mDataManager;
    private String mForecastAlgo;
    private String mCollector;

    private Boolean mIsConfigChg = false;

    private void initData() {
        mCollectorGap = TFConfiguration.getCollector().getCollectGap();
        mDataSaveGap = SaveDataService.getSaveGap();
        mChartTimeGap = TempChart.getTimeGap();
        mWarnLine = TFConfiguration.getTempWarnLine();

        mDataManager = TFConfiguration.getDataManager().getClass().getName();
        mForecastAlgo = TFConfiguration.getAlgo().getClass().getName();
        mCollector = TFConfiguration.getCollector().getClass().getName();
    }

    private void initView() {
        mCollectorGapSBar = (SeekBar) findViewById(R.id.collector_gap);
        mDataSaveGapSBar = (SeekBar) findViewById(R.id.data_save_gap);
        mChartTimeGapSBar = (SeekBar) findViewById(R.id.chart_time_gap);
        mWarnLineSBar = (SeekBar) findViewById(R.id.warn_line);

        mCollectorGapSBar.setOnSeekBarChangeListener(this);
        mDataSaveGapSBar.setOnSeekBarChangeListener(this);
        mChartTimeGapSBar.setOnSeekBarChangeListener(this);
        mWarnLineSBar.setOnSeekBarChangeListener(this);

        mCollectorGapText = (TextView) findViewById(R.id.collector_gap_value);
        mDataSaveGapText = (TextView) findViewById(R.id.data_save_gap_value);
        mChartTimeGapText = (TextView) findViewById(R.id.chart_time_gap_value);
        mWarnLineText = (TextView) findViewById(R.id.warn_line_value);

        mCollectorGapText.setText(mCollectorGap + "ms");
        mDataSaveGapText.setText(mDataSaveGap + "ms");
        mChartTimeGapText.setText(mChartTimeGap + "ms");
        mWarnLineText.setText(mWarnLine + "°C");

        mCollectorGapSBar.setProgress((int) (mCollectorGap / 1000));
        mDataSaveGapSBar.setProgress((int) (mDataSaveGap / 1000));
        mChartTimeGapSBar.setProgress((int) (mChartTimeGap / 1000));
        mWarnLineSBar.setProgress(mCollectorGap.intValue());

        mCollectorChoose = (RadioGroup) findViewById(R.id.collector_choose);
        mAlgoChoose = (RadioGroup) findViewById(R.id.algo_choose);
        mSaveChoose = (RadioGroup) findViewById(R.id.data_save_choose);

        mCollectorChoose.setOnCheckedChangeListener(this);
        mAlgoChoose.setOnCheckedChangeListener(this);
        mSaveChoose.setOnCheckedChangeListener(this);

        mCollectorGDRBtn = (RadioButton) findViewById(R.id.collector_gd_radio);
        mCollectorMYQXRBtn = (RadioButton) findViewById(R.id.collector_myqx_radio);
        mCollectorCPURBtn = (RadioButton) findViewById(R.id.collector_cpu_radio);
        switch (mCollector) {
            case Constants.COLLECTOR_GD:
                mCollectorGDRBtn.setChecked(true);
                mCollectorMYQXRBtn.setChecked(false);
                mCollectorCPURBtn.setChecked(false);
                break;
            case Constants.COLLECTOR_MYQX:
                mCollectorMYQXRBtn.setChecked(true);
                mCollectorGDRBtn.setChecked(false);
                mCollectorCPURBtn.setChecked(false);
                break;
            case Constants.COLLECTOR_CPU:
                mCollectorCPURBtn.setChecked(true);
                mCollectorMYQXRBtn.setChecked(false);
                mCollectorGDRBtn.setChecked(false);
            default:
                break;
        }
        mAlgoLineRBtn = (RadioButton) findViewById(R.id.algo_line_radio);
        mAlgoLSRBtn = (RadioButton) findViewById(R.id.algo_ls_radio);
        switch (mForecastAlgo) {
            case Constants.FORECAST_ALGO_LEASTSQUARE:
                mAlgoLSRBtn.setChecked(true);
                mAlgoLineRBtn.setChecked(false);
                break;
            case Constants.FORECAST_ALGO_LINEAR:
                mAlgoLSRBtn.setChecked(false);
                mAlgoLineRBtn.setChecked(true);
                break;
            default:
                break;
        }
        mSaveFileRBtn = (RadioButton) findViewById(R.id.file_save_radio);
        mSaveSqlRBtn = (RadioButton) findViewById(R.id.sql_save_radio);
        switch (mDataManager) {
            case Constants.DATA_MANAGER_FILE:
                mSaveFileRBtn.setChecked(true);
                mSaveSqlRBtn.setChecked(false);
                break;
            case Constants.DATA_MANAGER_SQLITE:
                mSaveFileRBtn.setChecked(false);
                mSaveSqlRBtn.setChecked(true);
                break;
            default:
                break;
        }
        mIsConfigChg = false;
    }

    private void checkConfigStatus(Boolean isConfigChg) {
        if (isConfigChg) {
            TFConfiguration.getCollector().setCollectGap(mCollectorGap);
            SaveDataService.setSaveGap(mDataSaveGap);
            TempChart.setTimeGap(mChartTimeGap);
            TFConfiguration.setTempWarnLine(mWarnLine);

            TFConfiguration.setCollector(mCollector);
            TFConfiguration.setAlgo(mForecastAlgo);

            //文件存储未完成，暂不可切换
            //TFConfiguration.setDataManager(mDataManager);

            Intent broadcast = new Intent();
            broadcast.setAction(Constants.ACTION_CONFIG_CHG);
            broadcast.putExtra(Constants.MSG_CONFIG_CHG, isConfigChg);
            sendBroadcast(broadcast);

            Toast.makeText(this, "配置已生效", Toast.LENGTH_SHORT).show();
            mIsConfigChg = false;
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("注意");
        builder.setMessage("是否使更改的配置生效?");
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkConfigStatus(mIsConfigChg);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "已取消", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("设置");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsConfigChg) {
                    showDialog();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initActionBar();
        initData();
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mIsConfigChg) {
            showDialog();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                checkConfigStatus(mIsConfigChg);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressChanged(SeekBar SeekBar, int progress, boolean fromUser) {
        switch (SeekBar.getId()) {
            case R.id.collector_gap:
                mCollectorGapText.setText((mCollectorGapSBar.getProgress()) * 1000 + "ms");
                break;
            case R.id.data_save_gap:
                mDataSaveGapText.setText((mDataSaveGapSBar.getProgress()) * 1000 + "ms");
                break;
            case R.id.chart_time_gap:
                mChartTimeGapText.setText((mChartTimeGapSBar.getProgress()) * 1000 + "ms");
                break;
            case R.id.warn_line:
                mWarnLineText.setText((mWarnLineSBar.getProgress()) + "°C");
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar SeekBar) {
        switch (SeekBar.getId()) {
            case R.id.collector_gap:
                break;
            case R.id.data_save_gap:
                break;
            case R.id.chart_time_gap:
                break;
            case R.id.warn_line:
                break;
            default:
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar SeekBar) {
        mIsConfigChg = true;
        switch (SeekBar.getId()) {
            case R.id.collector_gap:
                mCollectorGap = Long.valueOf((mCollectorGapSBar.getProgress()) * 1000);
                if (mCollectorGap == 0) {
                    mCollectorGap = 1000L;
                }
                mCollectorGapText.setText(mCollectorGap + "ms");
                break;
            case R.id.data_save_gap:
                mDataSaveGap = Long.valueOf((mDataSaveGapSBar.getProgress()) * 1000);
                if (mDataSaveGap == 0) {
                    mDataSaveGap = 1000L;
                }
                mDataSaveGapText.setText(mDataSaveGap + "ms");
                break;
            case R.id.chart_time_gap:
                mChartTimeGap = Long.valueOf((mChartTimeGapSBar.getProgress()) * 1000);
                if (mChartTimeGap == 0) {
                    mChartTimeGap = 1000L;
                }
                mChartTimeGapText.setText(mChartTimeGap + "ms");
                break;
            case R.id.warn_line:
                mWarnLine = Double.valueOf(mWarnLineSBar.getProgress());
                if (mWarnLine == 0) {
                    mWarnLine = 1D;
                }
                mWarnLineText.setText(mWarnLine + "°C");
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mIsConfigChg = true;
        switch (group.getId()) {
            case R.id.collector_choose:
                if (checkedId == R.id.collector_gd_radio) {
                    mCollector = Constants.COLLECTOR_GD;
                } else if (checkedId == R.id.collector_myqx_radio) {
                    mCollector = Constants.COLLECTOR_MYQX;
                } else if (checkedId == R.id.collector_cpu_radio) {
                    mCollector = Constants.COLLECTOR_CPU;
                }
                break;
            case R.id.algo_choose:
                if (checkedId == R.id.algo_line_radio) {
                    mForecastAlgo = Constants.FORECAST_ALGO_LINEAR;
                } else if (checkedId == R.id.algo_ls_radio) {
                    mForecastAlgo = Constants.FORECAST_ALGO_LEASTSQUARE;
                }
                break;
            case R.id.data_save_choose:
                if (checkedId == R.id.file_save_radio) {
                    mDataManager = Constants.DATA_MANAGER_FILE;
                } else if (checkedId == R.id.sql_save_radio) {
                    mDataManager = Constants.DATA_MANAGER_SQLITE;
                }
                break;
            default:
                break;
        }
    }
}
