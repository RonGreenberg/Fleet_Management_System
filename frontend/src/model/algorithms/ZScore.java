package model.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import model.statlib.StatLib;


public class ZScore implements TimeSeriesAnomalyDetector {


   private HashMap<String, Float> hashMap;



    public ZScore() {

        hashMap = new HashMap<>();
    }

    static public float findZScore(float[] colom, int size) {

        if (size <= 1)
            return 0;
        float[] arr =Arrays.copyOfRange(colom, 0, size-1);
       /* for (int j = 0; j < size-1; j++) {
            arr[j] = colom[j];
        }*/
        float temp = StatLib.var(arr);
        float stia = (float) Math.pow(temp, 0.5);
        if(stia==0) {
        	return 0;
        }
        float avg = StatLib.avg(arr);
        return Math.abs((colom[size - 1] - avg) / stia);
    }

    static public float findZmax(float[] fs) {
        if (fs.length < 0)
            return -1;
        float max = findZScore(fs, 1);
        float temp;
        for (int i = 2; i <= fs.length; i++) {
            temp = findZScore(fs, i);
            if (temp > max)
                max = temp;
        }
        return max;
    }

    @Override
    public void learnNormal(TimeSeries ts) {

        for (String col : ts.namesOfFeatures) {

        	this.hashMap.put(col, findZmax(ts.dataOfFeatureByName(col)));

        }
       

    }

    @Override
    public  HashMap<String, List<Integer>> detect(TimeSeries ts) {
        // TODO Auto-generated method stub
        float tempZScore;
        HashMap<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        int totalTime = ts.totalTime;
        for (String key : hashMap.keySet()) {

            for (int j = 0; j < totalTime; j++) {
                tempZScore = findZScore(ts.dataOfFeatureByName(key), j);
                if (tempZScore > hashMap.get(key))//we detect problem
                {
                    List<Integer> temp;
                    if(map.get(key)==null)
                    {
                         temp= new ArrayList<>();

                    }
                    else{
                        temp=map.get(key);
                    }
                   temp.add(j+1);
                    map.put( key, temp);
                }

            }

        }

        return map;
    }


    public HashMap<String, Float> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Float> hashMap) {
        this.hashMap = hashMap;
    }
}
