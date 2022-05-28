package model;

public abstract class CorrelatedFeature {
	
	 public final String feature1, feature2;
	 public final float correlation;
	 public CorrelatedFeature(String feature1, String feature2, float corrlation) {
		// TODO Auto-generated constructor stub
		  this.feature1 = feature1;
	        this.feature2 = feature2;
	        this.correlation = corrlation;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(correlation);
		result = prime * result + ((feature1 == null) ? 0 : feature1.hashCode());
		result = prime * result + ((feature2 == null) ? 0 : feature2.hashCode());
		return result;
	}
	
	 
	 
	 
	 
	 
}
