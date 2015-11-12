package net.datafans.common.util;

/**
 * Created by zhonganyun on 15/11/12.
 */
public class DistanceUtil {

    private final static double PI = 3.14159265358979323; // 圆周率
    private final static double R = 6371229; // 地球的半径

    public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double x, y, distance;
        x = (lng2 - lng1) * PI * R
                * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
        y = (lat2 - lat1) * PI * R / 180;
        distance = Math.hypot(x, y);
        return distance;
    }


    public static double getDistanceByInt(int lng1, int lat1, int lng2, int lat2) {

        return getDistance(lng1/1000000.0, lat1/1000000.0, lng2/1000000.0, lat2/1000000.0);
    }

    public static String getDistance(double distance) {
        if (distance < 1000)
            return String.format("%dm", (int) distance);
        else
            return String.format("%.1fkm", (float) (distance / 1000.0));
    }


    public static void main(String[] args) {
        System.err.print(getDistance(1400.888));
    }
}
