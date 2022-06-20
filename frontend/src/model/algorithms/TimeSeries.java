package model.algorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class TimeSeries {

    public ArrayList<String> namesOfFeatures = new ArrayList<String>();
    public ArrayList<float[]> data = new ArrayList<float[]>();
    public float threshold; //the threshold we choose
    public int totalTime;


    public TimeSeries(String Path) {

        this.threshold = (float) 0.9;
        String line = "";
        try {

            Path p = Paths.get(Path);
            BufferedReader br = new BufferedReader(new FileReader(p.toString()));
            if ((line = br.readLine()) != null) {
                //Reading the file for the first time
                //Creates new Feature for each column

                String[] s1 = line.split(",");
                int len = s1.length;
                for (int c = 0; c < len; c++) {
                    namesOfFeatures.add(s1[c]);//what will be the feature

                }
                int counter = 0;
                while ((line = br.readLine()) != null) {

                    data.add(new float[len]);//add new line
                    totalTime++;
                    s1 = line.split(",");
                    for (int i = 0; i < len; i++) {
                        data.get(counter)[i] = (Float.parseFloat(s1[i]));//fill the line with data

                    }
                    counter++;
                }
                this.totalTime = counter;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addNewFeatureAndData(String fName, float[] values) {

        if (fName != null && values.length > 0) {
            namesOfFeatures.add(fName);
            data.add(values);
        }
    }

    public int featurePlace(String s) {

        return namesOfFeatures.indexOf(s);
    }

    public float[] dataOfFeatureByName(String s) {
        int n = featurePlace(s);
        float[] f = new float[data.size()];
        int i = 0;
        for (float[] element : data) {
            f[i] = element[n];
            i++;
        }

        return f;

    }

    public float[] dataOfFeaturerByNum(int s) {
        float[] f = new float[data.size()];
        int i = 0;
        for (float[] element : data) {
            f[i] = element[s];
            i++;
        }

        return f;

    }

    public float getMaxByFeature(String s) {
        float t[] = this.dataOfFeatureByName(s);
        float max = t[0];
        for (float value : t) {
            if (max < value) {
                max = value;
            }
        }
        return max;


    }

    public float getMinByFeature(String s) {
        float t[] = this.dataOfFeatureByName(s);
        float min = t[0];
        for (float value : t) {
            if (min > value) {
                min = value;
            }
        }
        return min;


    }


    public float getValAtSpecificTime(int time, String request)
    //we get key and feature name
    //we return the feature at that time
    {

        int sPlace = featurePlace(request);
        if (time-1 < 0){
            return 0;
        }
        if (sPlace == -1) {
            return -1;
        } else return data.get(time - 1)[sPlace];

    }

    public float getValAtSpecificTime(int time, int request)
    //we get key and feature name
    //we return the feature at that time
    {


        if (request == -1) {
            return -1;
        } else return data.get(time - 1)[request - 1];

    }

    public float getCoral() {
        return threshold;
    }

    public void setCoral(float coral) {
        this.threshold = coral;
    }


  /*public ArrayList<CorrelationType> CreateMaxListOfCoral() {


        float maxp, t;
        float[] arrayX, arrayY;
        int y, i, j;
        ArrayList<CorrelationType> dataCoral = new ArrayList<CorrelationType>();
        int size = this.data.get(0).length;//size of our rows


        for (i = 0; i < size; i++) {

            maxp = 0;
            y = i; //the feature with max pearson to feature i
            arrayX = this.dataOfFeaturerByNum(i);
            for (j = i + 1; j < size; j++) {
                arrayY = this.dataOfFeaturerByNum(j);
                t = Math.abs(StatLib.pearson(arrayX, arrayY));

                if (t > maxp) {
                    y = j;
                    maxp = t;
                }
            }

            if (maxp >= 0.95) {
                dataCoral.add(new CorrelationType(this.namesOfFeatures.get(i), this.namesOfFeatures.get(y)
                        , typeAlgo.Line, maxp));

            } else if (0.5 <= maxp && maxp < 0.95) {
                dataCoral.add(new CorrelationType(this.namesOfFeatures.get(i), this.namesOfFeatures.get(y)
                        , typeAlgo.Circle, maxp));

            } else {
                dataCoral.add(new CorrelationType(this.namesOfFeatures.get(i), this.namesOfFeatures.get(y)
                        , typeAlgo.zScore, maxp));
            }


        }
        return dataCoral;
    }

   /* public float[] getCoralFeature(String fea) {

        float[] t = null;
        if (fea != null) {

            for (CorrelationType temp : this.dataCoral) {
                if (temp.CoralA.equals(fea))
                    t = this.dataOfFeatureByName(temp.CoralB);
            }

        }
        return t;

    }

    public ArrayList<CorrelationType> findCoral() {


        float t;
        float[] arrayX, arrayY;
        int y, i, j;
        ArrayList<CorrelationType> dataCoral = new ArrayList<CorrelationType>();
        int size = this.data.get(0).length;//size of our rows


        for (i = 0; i < size; i++) {
            arrayX = this.dataOfFeaturerByNum(i);
            for (j = i + 1; j < size; j++) {
                arrayY = this.dataOfFeaturerByNum(j);
                t = Math.abs(StatLib.pearson(arrayX, arrayY));

                if (t >= 0.95) {
                    dataCoral.add(new CorrelationType(this.namesOfFeatures.get(i), this.namesOfFeatures.get(j)
                            , typeAlgo.Line, t));

                } else if (0.5 <= t && t < 0.95) {
                    dataCoral.add(new CorrelationType(this.namesOfFeatures.get(i), this.namesOfFeatures.get(j)
                            , typeAlgo.Circle, t));

                } else {
                    dataCoral.add(new CorrelationType(this.namesOfFeatures.get(i), this.namesOfFeatures.get(j)
                            , typeAlgo.zScore, t));
                }

            }


        }
        return dataCoral;
    }
   	*/
}
