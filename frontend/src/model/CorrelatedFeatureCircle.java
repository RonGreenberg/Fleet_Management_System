package model;

import model.algorithms.Circle;

public class CorrelatedFeatureCircle extends CorrelatedFeature {
public final Circle c;

	public CorrelatedFeatureCircle(String feature1, String feature2, float correlation, Circle c) {
		super(feature1, feature2, correlation);
		this.c = c;
	}

}
