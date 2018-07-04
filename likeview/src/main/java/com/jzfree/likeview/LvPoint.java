package com.jzfree.likeview;

import android.os.Parcel;

/**
 * Created by Wang Jiuzhou on 2018/7/2 19:56
 */
public class LvPoint {
    float x;
    float y;

    public LvPoint() {
    }

    public LvPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public LvPoint(LvPoint src) {
        this.x = src.x;
        this.y = src.y;
    }

    protected LvPoint(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }

    /**
     * Set the point's x and y coordinates
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LvPoint lvPoint = (LvPoint) o;
        return Float.compare(lvPoint.x, x) == 0 &&
                Float.compare(lvPoint.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.floatToIntBits(x);
        result = 31 * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public String toString() {
        return "LvPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
