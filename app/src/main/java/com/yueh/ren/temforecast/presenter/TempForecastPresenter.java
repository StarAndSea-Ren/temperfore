package com.yueh.ren.temforecast.presenter;

import com.yueh.ren.temforecast.model.ITempForecastModel;
import com.yueh.ren.temforecast.model.impl.TempForecastModelImpl;
import com.yueh.ren.temforecast.util.TempEvent;
import com.yueh.ren.temforecast.view.ITempForecastView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by 任跃华 on 2016/3/30 0030.
 */
public class TempForecastPresenter {
    private ITempForecastModel model;
    private ITempForecastView view;

    public TempForecastPresenter(ITempForecastView view){
        this.view = view;
        model = new TempForecastModelImpl();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEvent(TempEvent event){
        if (event.what == TempEvent.OK){
            view.showData(event);
        }
    }

    public void eventBusUnregister(){
        EventBus.getDefault().unregister(this);
    }

    public void getData(){
        model.getTempData();
    }
}
