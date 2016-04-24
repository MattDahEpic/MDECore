package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.List;

final class Range<T extends Number & Comparable<T>> {
    public static final Range<Double> MAX_RANGE = Range.of(Double.MIN_VALUE,Double.MAX_VALUE);

    public static <T extends Number & Comparable<T>> Range<T> of(T min, T max) {
        return new Range<T>(min,max);
    }
    public static Range<Double> of (Config.Range rangeAnnot) {
        return new Range<Double>(rangeAnnot.min(),rangeAnnot.max());
    }

    public final T min;
    public final T max;

    private Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public T clamp (T value) {
        return value.doubleValue() < min.doubleValue() ? min : value.doubleValue() > max.doubleValue() ? max : value;
    }
    public List<T> clampArr (List<T> value) {
        List<T> ret = new ArrayList<T>(value.size());
        for (int i = 0; i < value.size(); i++) {
            ret.add(i,clamp(value.get(i)));
        }
        return ret;
    }

    public void apply (Property p) {
        if (this.equals(MAX_RANGE)) return;
        if (p.getType() == Property.Type.INTEGER) {
            Range<Integer> r = of(min.intValue(), max.intValue());
            p.setMinValue(r.min);
            p.setMaxValue(r.max);
        } else if (p.getType() == Property.Type.DOUBLE) {
            Range<Double> r = of(min.doubleValue(), max.doubleValue());
            p.setMinValue(r.min);
            p.setMaxValue(r.max);
        } else {
            throw new IllegalArgumentException(String.format("A mod tried to set range %s on a property that was not either of Integer of Double type.", this));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Range)) return false;
        Range other = (Range)o;
        return other.min.equals(this.min) && other.max.equals(this.max);
    }
    @Override
    public String toString() {
        return "Range(min=" + this.min + ", max=" + this.max + ")";
    }
}
