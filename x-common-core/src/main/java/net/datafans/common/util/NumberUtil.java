package net.datafans.common.util;

/**
 * Created by zhonganyun on 16/12/2.
 */
public class NumberUtil {

    public static float transfer(float num, int length) {
        int i = (int) Math.pow(10, length);
        return (float) (Math.round(num * i)) / i;
    }

    public static float transfer(float num) {
        return transfer(num, 2);
    }
}
