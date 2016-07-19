package com.yueh.ren.temforecast.util;

import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

/**
 * Created by chge on 2016/4/27.
 */
public class TempEvent {
    public static final int FAIL = 0;
    public static final int OK = 1;

    public int what;
    public XYMultipleSeriesDataset dataSet;
    public XYMultipleSeriesRenderer renderer;
}
