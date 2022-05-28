package model;

import model.statlib.Line;
import model.statlib.Line;

public class CorrelatedFeaturesLine extends CorrelatedFeature{
   
    public final Line lin_reg;
    public final float threshold;

    public CorrelatedFeaturesLine(String feature1, String feature2, float correlation, Line lin_reg, float threshold) {
       super( feature1, feature2, correlation);
        this.lin_reg = lin_reg;
        this.threshold = threshold;
    }

}
