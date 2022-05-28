package model.statlib;

public class Line {
    public final float a, b;

    public Line(float a, float b) {
        this.a = a;
        this.b = b;
    }

    public float valueInTime(float x) {
        return ((a * x) + b);
    }

}
