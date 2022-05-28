package viewModel;

public class FlightFeature { //TODO change to float
    private String featureName;
    private float min;
    private float max;
    private int featureIndex;

    public FlightFeature(String featureName, float min, float max, int featureIndex) {
        this.featureName = featureName;
        this.min = min;
        this.max = max;
        this.featureIndex = featureIndex;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public int getFeatureIndex() {
        return featureIndex;
    }

    public void setFeatureIndex(int featureIndex) {
        this.featureIndex = featureIndex;
    }

}
