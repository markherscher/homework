package com.target.dealbrowserpoc.dealbrowser.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This class is a little hacky and lazy (all static, no way to customize) but it works for this
 * demo.
 */
public class DollarFormatter {
    private static final int CENTS_PER_DOLLAR = 100;
    private static final NumberFormat centsFormat = new DecimalFormat("$0.00");

    public static String fromCents(int cents) {
        return centsFormat.format(cents / (float) CENTS_PER_DOLLAR);
    }
}
