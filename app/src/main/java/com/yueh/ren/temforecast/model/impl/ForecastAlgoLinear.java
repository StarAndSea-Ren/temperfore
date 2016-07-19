package com.yueh.ren.temforecast.model.impl;

import com.yueh.ren.temforecast.model.IForecastAlgo;
import com.yueh.ren.temforecast.model.database.Temper;

import java.util.ArrayList;


/**
 * Created by lixin on 2016/4/19.
 *
 */

public class ForecastAlgoLinear implements IForecastAlgo {

	@Override
	public ArrayList<Temper> forecastTemper(ArrayList<Temper> oldTempers, Long startTime, Long gap, int times) {
		ArrayList<Temper> res = new ArrayList<>();
		Long nextTime = startTime + gap;
		double lastTemp = oldTempers.get(oldTempers.size()-1).getTemperature();
		for (int i = 0; i < times; i++) {
			double temp;
			if (i % 2 == 0){
				temp = lastTemp + 3;
			}else{
				temp = lastTemp - 3;
			}
			Temper Temper = new Temper();
			Temper.setTemperature(temp);
			Temper.setTime(nextTime + "");
			res.add(Temper);
			lastTemp = temp;
			nextTime = nextTime + gap;
		}
		return res;
	}

	@Override
	public double forecastTemper(ArrayList<Temper> oldTempers, Long targetTime) {
		return 0;
	}
}
