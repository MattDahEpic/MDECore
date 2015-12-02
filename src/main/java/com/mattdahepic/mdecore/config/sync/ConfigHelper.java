package com.mattdahepic.mdecore.config.sync;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

import javax.annotation.concurrent.Immutable;
import java.util.Arrays;

public class ConfigHelper {
    public enum RestartReqs {
        /**
         * No restart needed for this config to be applied. Default value.
         */
        NONE,

        /**
         * This config requires the world to be restarted to take effect.
         */
        REQUIRES_WORLD_RESTART,

        /**
         * This config requires the game to be restarted to take effect.
         * {@code REQUIRES_WORLD_RESTART} is implied when using this.
         */
        REQUIRES_MC_RESTART;

        public Property apply(Property prop) {
            if (this == REQUIRES_MC_RESTART) {
                prop.setRequiresMcRestart(true);
            } else if (this == REQUIRES_WORLD_RESTART) {
                prop.setRequiresWorldRestart(true);
            }
            return prop;
        }
    }
    static void setBounds(Property prop, Bound<?> bound) throws IllegalArgumentException {
        if (bound.equals(Bound.MAX_BOUND)) {
            return;
        }
        if (prop.getType() == Type.INTEGER) {
            Bound<Integer> b = Bound.of(bound.min.intValue(), bound.max.intValue());
            prop.setMinValue(b.min);
            prop.setMaxValue(b.max);
        } else if (prop.getType() == Type.DOUBLE) {
            Bound<Double> b = Bound.of(bound.min.doubleValue(), bound.max.doubleValue());
            prop.setMinValue(b.min);
            prop.setMaxValue(b.max);
        } else {
            throw new IllegalArgumentException(String.format("A mod tried to set bounds %s on a property that was not either of Integer of Double type.", bound));
        }
    }
    static int[] bindIntArr(Property prop, Bound<Integer> bound) {
        int[] prev = prop.getIntList();
        int[] res = new int[prev.length];
        for (int i = 0; i < prev.length; i++) {
            res[i] = bound.clamp(prev[i]);
        }
        prop.set(res);
        return res;
    }
    static double[] bindDoubleArr(Property prop, Bound<Double> bound) {
        double[] prev = prop.getDoubleList();
        double[] res = new double[prev.length];
        for (int i = 0; i < prev.length; i++) {
            res[i] = bound.clamp(prev[i]);
        }
        prop.set(res);
        return res;
    }

    @SuppressWarnings("unchecked")
    static <T extends Number & Comparable<T>> T bindValue(Property prop, Bound<T> bound, T defVal) throws IllegalArgumentException {
        Object b = (Object) bound;
        if (defVal instanceof Integer) {
            return (T) bindInt(prop, (Bound<Integer>) b);
        }
        if (defVal instanceof Double) {
            return (T) bindDouble(prop, (Bound<Double>) b);
        }
        if (defVal instanceof Float) {
            return (T) bindFloat(prop, (Bound<Float>) b);
        }
        throw new IllegalArgumentException(bound.min.getClass().getName() + " is not a valid config type.");
    }

    private static Integer bindInt(Property prop, Bound<Integer> bound) {
        prop.set(bound.clamp(prop.getInt()));
        return Integer.valueOf(prop.getInt());
    }

    private static Double bindDouble(Property prop, Bound<Double> bound) {
        prop.set(bound.clamp(prop.getDouble()));
        return Double.valueOf(prop.getDouble());
    }

    private static Float bindFloat(Property prop, Bound<Float> bound) {
        return bindDouble(prop, Bound.of(bound.min.doubleValue(), bound.max.doubleValue())).floatValue();
    }
    static void addCommentDetails(Property prop, Bound<?> bound) {
        prop.comment += (prop.comment.isEmpty() ? "" : "\n");
        if (bound.equals(Bound.MAX_BOUND)) {
            prop.comment += prop.isList() ? Arrays.toString(prop.getDefaults()) : prop.getDefault();
        }
    }
}
