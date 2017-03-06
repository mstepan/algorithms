package com.max.algs.compact;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Compact date representation.
 */
public final class CompactDate implements java.io.Serializable {

    private static final long serialVersionUID = -4375409187057986645L;

    private static final int DAY_BITS = 5; // [1; 31]

    private static final int MONTH_BITS = 4; // [1; 12]
    private static final int MONTH_MASK = (1 << MONTH_BITS) - 1;

    private static final int INTEGER_SIZE_WITHOUT_SIGN_BIT = 31;
    public static final int YEAR_BITS = INTEGER_SIZE_WITHOUT_SIGN_BIT - (DAY_BITS + MONTH_BITS); // [0; 4_194_303]
    private static final int YEAR_MASK = (1 << YEAR_BITS) - 1;

    private final int data;

    public CompactDate(int day, int month, int year) {
        checkRange(month, "day", 1, 31);
        checkRange(month, "month", 1, 12);
        checkRange(month, "year", 1970, 3000);
        this.data = encode(day, month, year);
    }

    public CompactDate() {
        this(1, 1, 1970);
    }

    public int getDay() {
        return data >> (YEAR_BITS + MONTH_BITS);
    }

    public int getMonth() {
        return (data >> YEAR_BITS) & MONTH_MASK;
    }

    public int getYear() {
        return data & YEAR_MASK;
    }

    @Override
    public String toString() {
        return getDay() + "/" + getMonth() + "/" + getYear();
    }

    private int encode(int day, int month, int year) {

        int res = day;

        res <<= MONTH_BITS;
        res |= month;

        res <<= YEAR_BITS;
        res |= year;

        return res;
    }

    private static void checkRange(int value, String name, int lower, int upper) {
        checkArgument(value >= lower && value <= upper, "'%' value is incorrect should be in range [%s; %s], found %s",
                      name, lower, upper, value);
    }
}
