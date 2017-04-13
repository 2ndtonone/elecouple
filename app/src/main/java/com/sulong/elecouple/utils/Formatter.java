package com.sulong.elecouple.utils;

/**
 * Created by ydh on 2016/6/29.
 */
public class Formatter {

    public static final int FLAG_SHORTER = 1 << 0;
    public static final int FLAG_CALCULATE_ROUNDED = 1 << 1;
    public static final long KB_IN_BYTES = 1024;
    public static final long MB_IN_BYTES = KB_IN_BYTES * 1024;
    public static final long GB_IN_BYTES = MB_IN_BYTES * 1024;
    public static final long TB_IN_BYTES = GB_IN_BYTES * 1024;
    public static final long PB_IN_BYTES = TB_IN_BYTES * 1024;

    public static String formatFileSize(long sizeBytes) {
        final BytesResult res = formatBytes(sizeBytes, 0);
        return res.value + " " + res.units;
    }

    private static BytesResult formatBytes(long sizeBytes, int flags) {
        float result = sizeBytes;
        String units = "B";
        long mult = 1;
        if (result > 900) {
            units = "KB";
            mult = KB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            units = "MB";
            mult = MB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            units = "GB";
            mult = GB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            units = "TB";
            mult = TB_IN_BYTES;
            result = result / 1024;
        }
        if (result > 900) {
            units = "PB";
            mult = PB_IN_BYTES;
            result = result / 1024;
        }
        // Note we calculate the rounded long by ourselves, but still let String.format()
        // compute the rounded value. String.format("%f", 0.1) might not return "0.1" due to
        // floating point errors.
        final int roundFactor;
        final String roundFormat;
        if (result < 1) {
            roundFactor = 100;
            roundFormat = "%.2f";
        } else if (result < 10) {
            if ((flags & FLAG_SHORTER) != 0) {
                roundFactor = 10;
                roundFormat = "%.1f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        } else if (result < 100) {
            if ((flags & FLAG_SHORTER) != 0) {
                roundFactor = 1;
                roundFormat = "%.0f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        } else {
            roundFactor = 1;
            roundFormat = "%.0f";
        }
        final String roundedString = String.format(roundFormat, result);

        // Note this might overflow if result >= Long.MAX_VALUE / 100, but that's like 80PB so
        // it's okay (for now)...
        final long roundedBytes =
                (flags & FLAG_CALCULATE_ROUNDED) == 0 ? 0
                        : (((long) Math.round(result * roundFactor)) * mult / roundFactor);
        return new BytesResult(roundedString, units, roundedBytes);
    }

    public static class BytesResult {
        public final String value;
        public final String units;
        public final long roundedBytes;

        public BytesResult(String value, String units, long roundedBytes) {
            this.value = value;
            this.units = units;
            this.roundedBytes = roundedBytes;
        }
    }
}
