package com.yueh.ren.temforecast.view;

import android.graphics.Color;
import android.graphics.Paint;

import com.yueh.ren.temforecast.app.TFConfiguration;
import com.yueh.ren.temforecast.model.database.Temper;
import com.yueh.ren.temforecast.util.Constants;
import com.yueh.ren.temforecast.util.DateUtil;
import com.yueh.ren.temforecast.util.SPUtil;

import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

/**
 * Created by yueh on 2016/6/7.
 */
public class TempChart {
    private static final Long DEFAULT_TIME_GAP = 3600000L;
    private static Long timeGap = null;
    private ArrayList<Point> mHisPoints;
    private ArrayList<Point> mForePoints;
    private XYMultipleSeriesDataset mDateDataSet;
    private XYMultipleSeriesRenderer mRenderer;

    public TempChart(Long hisStartTime, Long hisEndTime, Long foreStartTime, Long foreEndTimes) {
        genHisPoints(hisStartTime, hisEndTime);
        genForePoints(foreStartTime, foreEndTimes);
        genDateDataset(mHisPoints, mForePoints);
        genRenderer(mHisPoints, mForePoints);
    }

    public TempChart(Long hisStartTime, Long hisEndTime, Long foreStartTime, Long foreEndTimes, Long timeGap) {
        this(hisStartTime, hisEndTime, foreStartTime, foreEndTimes);
        this.timeGap = timeGap;
    }

    public XYMultipleSeriesDataset getDateDataset() {
        return mDateDataSet;
    }


    public XYMultipleSeriesRenderer getRenderer() {
        return mRenderer;
    }

    public static void setTimeGap(Long gapMillis) {
        if (gapMillis == null) {
            return;
        }
        timeGap = gapMillis;
        SPUtil.putData(Constants.CONFIG_SP_NAME, Constants.TEMP_CHART_TIME_GAP, gapMillis.toString());
    }

    public static Long getTimeGap() {
        if (timeGap != null) {
            return timeGap;
        }
        String gapStr = SPUtil.getData(Constants.CONFIG_SP_NAME, Constants.TEMP_CHART_TIME_GAP);
        if (gapStr.isEmpty()) {
            timeGap = DEFAULT_TIME_GAP;
        } else {
            timeGap = Long.valueOf(gapStr);
        }
        return timeGap;
    }

    public ArrayList<Point> genHisPoints(Long startTime, Long endTime) {
        Long exp = (endTime - startTime) / getTimeGap();
        ArrayList<Temper> hisTempers = TFConfiguration.getDataManager().getTempers(startTime, endTime, getTimeGap());
        if (hisTempers == null || hisTempers.size() == 0) {
            return null;
        }
        mHisPoints = new ArrayList<>();
        for (int i = 0; i < hisTempers.size(); i++) {
            Temper t = hisTempers.get(i);
            double x = DateUtil.millis2Hour(Long.valueOf(t.getTime()));
            double y = t.getTemperature();
            mHisPoints.add(new Point(x, y));
        }
        return mHisPoints;
    }

    public ArrayList<Point> genForePoints(Long startTime, Long endTime) {
        int times = (int) ((endTime - startTime) / getTimeGap());
        Long hisStartTime = startTime - getTimeGap() * times * 3;//用三倍的历史数据预测
        ArrayList<Temper> hisTempers = TFConfiguration.getDataManager().getTempers(hisStartTime, startTime, getTimeGap());
        if (hisTempers == null || hisTempers.size() == 0) {
            return null;
        }

        ArrayList<Temper> forecastTempers = TFConfiguration.getAlgo().forecastTemper(hisTempers, startTime, getTimeGap(), times);
        if (forecastTempers == null) {
            return null;
        }
        mForePoints = new ArrayList<>();
        for (int i = 0; i < forecastTempers.size(); i++) {
            Temper t = forecastTempers.get(i);
            double x = DateUtil.millis2Hour(Long.valueOf(t.getTime()));
            double y = t.getTemperature();
            mForePoints.add(new Point(x, y));
        }
        return mForePoints;
    }


    public void genDateDataset(ArrayList<Point> hisPoints, ArrayList<Point> forePoints) {
        mDateDataSet = new XYMultipleSeriesDataset();
        XYSeries xySeries1 = new XYSeries("过去12小时温度");
        XYSeries xySeries2 = new XYSeries("未来12小时温度");
        if (hisPoints != null && forePoints != null) {
            int i = 0;
            while (i < hisPoints.size()) {
                xySeries1.add(i, hisPoints.get(i).getY());
                i++;
            }
            int j = 0;
            while (i < hisPoints.size() + forePoints.size()) {
                xySeries2.add(i, forePoints.get(j).getY());
                i++;
                j++;
            }
        }
        mDateDataSet.addSeries(xySeries1);
        mDateDataSet.addSeries(xySeries2);
    }

    /**
     * 描绘器，设置图表整体效果，比如x,y轴效果，缩放比例，颜色设置
     */
    public void genRenderer(ArrayList<Point> hisPoints, ArrayList<Point> forePoints) {

        mRenderer = new XYMultipleSeriesRenderer();

        // 是否显示网格
        mRenderer.setShowGrid(true);
        mRenderer.setMarginsColor(0xff313340);

        mRenderer.setMargins(new int[]{0, -2, 0, 0});//设置外边距，顺序为：上左下右

        //坐标轴设置
        mRenderer.setAxisTitleTextSize(24);//设置坐标轴标题字体的大小
        mRenderer.setYAxisMin(0);//设置y轴的起始值
        mRenderer.setYAxisMax(70);//设置y轴的最大值
        mRenderer.setXAxisMin(0);//设置x轴起始值
        mRenderer.setXAxisMax(24);//设置x轴最大值

        //颜色设置
        mRenderer.setApplyBackgroundColor(true);//是应用设置的背景颜色
        mRenderer.setBackgroundColor(0xff363e4a);//设置图表的背景颜色
        // 设置XY轴颜色
        mRenderer.setAxesColor(0xffEEEEEE);
        mRenderer.setLabelsColor(0xffEEEEEE);


        //图表移动设置
        mRenderer.setPanEnabled(true);//图表是否可以移动

        //坐标轴标签设置
        mRenderer.setLabelsTextSize(24);//设置标签字体大小
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.LEFT);
        mRenderer.setXLabels(0);//显示的x轴标签的个数
        mRenderer.setYLabels(14);

        if (hisPoints != null && forePoints != null) {
            int i = 0;
            while (i < hisPoints.size()) {
                mRenderer.addXTextLabel(i, "" + (int) (hisPoints.get(i).getX()));
                i++;
            }
            int j = 0;
            while (i < hisPoints.size() + forePoints.size()) {
                mRenderer.addXTextLabel(i, "" + (int) (forePoints.get(j).getX()));
                i++;
                j++;
            }
        }
        mRenderer.setPointSize(3);//设置坐标点大小

        mRenderer.setMarginsColor(Color.WHITE);//设置外边距空间的颜色
        mRenderer.setClickEnabled(false);
        mRenderer.setShowLegend(false);

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer1 = new XYSeriesRenderer();
        xySeriesRenderer1.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer1.setPointStrokeWidth(3);//坐标点的大小
        xySeriesRenderer1.setColor(0xFFF46C48);//表示该组数据的图或线的颜色
        xySeriesRenderer1.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer1.setChartValuesTextSize(24);//设置显示的坐标点值的字体大小

        /*某一组数据的描绘器，描绘该组数据的个性化显示效果，主要是字体跟颜色的效果*/
        XYSeriesRenderer xySeriesRenderer2 = new XYSeriesRenderer();
        xySeriesRenderer2.setPointStyle(PointStyle.CIRCLE);//坐标点的显示风格
        xySeriesRenderer2.setPointStrokeWidth(3);//坐标点的大小
        xySeriesRenderer2.setColor(0xFF00C8FF);//表示该组数据的图或线的颜色
        xySeriesRenderer2.setDisplayChartValues(false);//设置是否显示坐标点的y轴坐标值
        xySeriesRenderer2.setChartValuesTextSize(24);//设置显示的坐标点值的字体大小

        mRenderer.addSeriesRenderer(xySeriesRenderer1);
        mRenderer.addSeriesRenderer(xySeriesRenderer2);
    }

    private class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
