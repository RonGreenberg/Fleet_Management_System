package model.statlib;

public class StatLib {


    //return the Arry sum
    public static float sumArray(float[] x) {

        float sum = 0;
        for (float f : x) {
            sum += f;
        }
        return sum;
    }

    // simple average
    public static float avg(float[] x) {

        return (sumArray(x) / x.length);
    }


    // returns the variance of X
    public static float var(float[] x) {
        float av = avg(x);
        float sum = 0;
        float temp;
        for (float v : x) {
            temp = v - av;
            sum += Math.pow(temp, 2);
        }
        return ((sum / x.length));
    }


    // returns the covariance of X and Y
    public static float cov(float[] x, float[] y) {

        float sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * y[i];
        }
        sum /= x.length;

        return sum - avg(x) * avg(y);

    }


    // returns the Pearson correlation coefficient of X and Y
    public static float pearson(float[] x, float[] y) {


        float cov1 = cov(x, y);
        float s1 = (float) Math.pow(var(x), 0.5);
        float s2 = (float) Math.pow(var(y), 0.5);
        if(s1==0||s2==0)
            return 0;
        return (cov1 / (s1 * s2));


    }

    // performs a linear regression and returns the line equation
    public static Line linear_reg(Point[] points) {

        int len = points.length;
        float[] x = new float[len];
        float[] y = new float[len];
        for (int i = 0; i < len; i++) {
            x[i] = points[i].x;
            y[i] = points[i].y;
        }
        float a = cov(x, y) / var(x);
        float b = avg(y) - a * avg(x);
        return (new Line(a, b));


    }

    // returns the deviation between point p and the line equation of the points
    public static float dev(Point p, Point[] points) {
        Line l = linear_reg(points);
        return dev(p, l);


    }

    // returns the deviation between point p and the line
    public static float dev(Point p, Line l) {
        float Fx = l.valueInTime(p.x);
        float dis = Fx - p.y;
        return Math.abs(dis);
    }


    public static Point[] ArrayOfPoint(float[] x, float[] y) {
        Point[] p_array = new Point[x.length];
        for (int i = 0; i < p_array.length; i++) {
            p_array[i] = new Point(x[i], y[i]);
        }
        return p_array;
    }
}

