package com.mattdahepic.mdecore.config.v2;

import com.mattdahepic.mdecore.config.sync.Bound;
import com.mattdahepic.mdecore.config.v2.annot.Config;
import net.minecraftforge.common.config.Property;

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
        return value.compareTo(min) < 0 ? min : value.compareTo(max) > 0 ? max : value;
    }

    public void apply (Property p) {
        if (this == MAX_RANGE) return;
        if (p.getType() == Property.Type.INTEGER) {
            Bound<Integer> b = Bound.of(min.intValue(), max.intValue());
            p.setMinValue(b.min);
            p.setMaxValue(b.max);
        } else if (p.getType() == Property.Type.DOUBLE) {
            Bound<Double> b = Bound.of(min.doubleValue(), max.doubleValue());
            p.setMinValue(b.min);
            p.setMaxValue(b.max);
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
