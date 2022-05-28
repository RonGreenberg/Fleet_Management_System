package model.algorithms;

import model.CorrelatedFeatureCircle;
import model.CorrelatedFeaturesLine;
import model.statlib.Line;
import model.statlib.Point;
import model.statlib.StatLib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class HybridAlgo implements TimeSeriesAnomalyDetector {


    public HashMap<String, CorrelatedFeatureCircle> hashMapC;
    public HashMap<String, CorrelatedFeaturesLine> hashMapL;
    public HashMap<String, Float> hashMapZ;

    public HybridAlgo() {
        hashMapC = new HashMap<>();
        hashMapL = new HashMap<>();
        hashMapZ = new HashMap<>();
    }

    @Override
    public void learnNormal(TimeSeries ts) {

        float maxp, t, maxdev, threshold = 0;
        float[] arrayX, arrayY;
        int x, y, i, j;
        int size = ts.data.get(0).length;//size of our rows
        Point[] temp;
        Line lin_reg;
        for (i = 0; i < size; i++) {
            maxp = -2;
            x = i;
            y = i;
            arrayX = ts.dataOfFeaturerByNum(i);
            for (j = i + 1; j < size; j++) {
                arrayY = ts.dataOfFeaturerByNum(j);
                t = StatLib.pearson(arrayX, arrayY);

                if (Math.abs(t) >= maxp) {
                    y = j;
                    maxp = Math.abs(t);
                }
            }

            temp = StatLib.ArrayOfPoint(ts.dataOfFeaturerByNum(x), ts.dataOfFeaturerByNum(y));
            if (maxp >= 0.95) {
                lin_reg = StatLib.linear_reg(temp);
                for (Point point : temp) {
                    maxdev = StatLib.dev(point, lin_reg);
                    if (maxdev > threshold)
                        threshold = maxdev;
                }
                threshold = Math.abs(threshold);
                hashMapL.put(ts.namesOfFeatures.get(x), new CorrelatedFeaturesLine(ts.namesOfFeatures.get(x), ts.namesOfFeatures.get(y), maxp, lin_reg, threshold));

            } else if (0.5 <= maxp && maxp < 0.95) {
                hashMapC.put(ts.namesOfFeatures.get(x), new CorrelatedFeatureCircle(ts.namesOfFeatures.get(x), ts.namesOfFeatures.get(y), maxp, SmallestEnclosingCircle.makeCircle(Arrays.asList(temp))));

            } else {
                if (x != y) {
                    hashMapZ.put(ts.namesOfFeatures.get(x), ZScore.findZmax(ts.dataOfFeaturerByNum(x)));
                    hashMapZ.put(ts.namesOfFeatures.get(y), ZScore.findZmax(ts.dataOfFeaturerByNum(y)));
                } else {
                    hashMapZ.put(ts.namesOfFeatures.get(x), ZScore.findZmax(ts.dataOfFeaturerByNum(x)));
                }
            }

        }
    }

    @Override
    public HashMap<String, List<Integer>> detect(TimeSeries ts) {

        HashMap<String, List<Integer>> map = new HashMap<>();
        Point temp;
        for (String f : ts.namesOfFeatures) {

            if (hashMapC.containsKey(f)) {
                float[] fcorrelate1 = ts.dataOfFeatureByName(f);
                String correlate2 = new String(hashMapC.get(f).feature2);
                float[] fcorrelate2 = ts.dataOfFeatureByName(correlate2);
                for (int z = 0; z < fcorrelate1.length; z++) {

                    temp = new Point(fcorrelate1[z], fcorrelate2[z]);
                    if (!hashMapC.get(f).c.contains(temp)) {
                        List<Integer> tempList;
                        if (map.get(f) == null) {
                            tempList = new ArrayList<>();

                        } else {
                            tempList = map.get(f);
                        }
                        tempList.add(z + 1);
                        map.put(f, tempList);

                    }

                }
            } else if (hashMapL.containsKey(f)) {

                //first we create the point by what we know that correlated


                float[] fcorrelate1 = ts.dataOfFeatureByName(f);
                String correlate2 = new String(hashMapL.get(f).feature2);
                float[] fcorrelate2 = ts.dataOfFeatureByName(correlate2);
                for (int z = 0; z < fcorrelate1.length; z++) {

                    temp = new Point(fcorrelate1[z], fcorrelate2[z]);
                    if (StatLib.dev(temp, hashMapL.get(f).lin_reg) > hashMapL.get(f).threshold + 0.015f) {
                        //we find error
                        List<Integer> tempList;
                        if (map.get(f) == null) {
                            tempList = new ArrayList<>();

                        } else {
                            tempList = map.get(f);
                        }
                        tempList.add(z + 1);
                        map.put(f, tempList);
                    }
                }


            } else //in Z score algo
            {
                float tempZScore;
                for (int j = 0; j < ts.totalTime; j++) {
                    tempZScore = ZScore.findZScore(ts.dataOfFeatureByName(f), j);
                    if (tempZScore > hashMapZ.get(f))//we detect problem
                    {
                        List<Integer> tempList;
                        if (map.get(f) == null) {
                            tempList = new ArrayList<>();

                        } else {
                            tempList = map.get(f);
                        }
                        tempList.add(j + 1);
                        map.put(f, tempList);
                    }

                }
            }
        }

        return map;
    }

}
