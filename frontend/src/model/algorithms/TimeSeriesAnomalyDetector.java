package model.algorithms;

import java.util.HashMap;
import java.util.List;

public interface TimeSeriesAnomalyDetector {
    void learnNormal(TimeSeries ts);

    HashMap<String, List<Integer>> detect(TimeSeries ts);
}
