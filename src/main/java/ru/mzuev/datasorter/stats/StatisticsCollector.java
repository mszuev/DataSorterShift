package ru.mzuev.datasorter.stats;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class StatisticsCollector {

    private int intCount = 0;
    private BigInteger intMin = null;
    private BigInteger intMax = null;
    private BigInteger intSum = BigInteger.ZERO;

    private int floatCount = 0;
    private BigDecimal floatMin = null;
    private BigDecimal floatMax = null;
    private BigDecimal floatSum = BigDecimal.ZERO;

    private int stringCount = 0;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = 0;

    private StatMode statMode = StatMode.NONE;

    public void addInteger(String value) {
        BigInteger num = new BigInteger(value);
        intCount++;

        if (intMin == null || num.compareTo(intMin) < 0) {
            intMin = num;
        }
        if (intMax == null || num.compareTo(intMax) > 0) {
            intMax = num;
        }
        intSum = intSum.add(num);
    }

    public void addFloat(String value) {
        BigDecimal num = new BigDecimal(value);
        floatCount++;

        if (floatMin == null || num.compareTo(floatMin) < 0) {
            floatMin = num;
        }
        if (floatMax == null || num.compareTo(floatMax) > 0) {
            floatMax = num;
        }
        floatSum = floatSum.add(num);
    }

    public void addString(String value) {
        stringCount++;
        int length = value.length();
        if (length < minLength) {
            minLength = length;
        }
        if (length > maxLength) {
            maxLength = length;
        }
    }

    public void printShortStatistics() {
        System.out.println("\n===== SHORT STATISTICS =====");
        System.out.println("Integers: " + intCount);
        System.out.println("Floats: " + floatCount);
        System.out.println("Strings: " + stringCount);
    }

    public void printFullStatistics() {
        System.out.println("\n===== FULL STATISTICS =====");

        System.out.println("Integers:");
        System.out.println("  Count: " + intCount);
        if (intCount > 0) {
            System.out.println("  Min: " + intMin);
            System.out.println("  Max: " + intMax);
            System.out.println("  Sum: " + intSum);
            BigDecimal avg = new BigDecimal(intSum)
                    .divide(BigDecimal.valueOf(intCount), 2, RoundingMode.HALF_UP);
            System.out.println("  Average: " + avg);
        }

        System.out.println("\nFloats:");
        System.out.println("  Count: " + floatCount);
        if (floatCount > 0) {
            System.out.println("  Min: " + floatMin);
            System.out.println("  Max: " + floatMax);
            System.out.println("  Sum: " + floatSum);
            BigDecimal avg = floatSum.divide(
                    BigDecimal.valueOf(floatCount), 2, RoundingMode.HALF_UP);
            System.out.println("  Average: " + avg);
        }

        System.out.println("\nStrings:");
        System.out.println("  Count: " + stringCount);
        if (stringCount > 0) {
            System.out.println("  Min length: " + minLength);
            System.out.println("  Max length: " + maxLength);
        }
    }

    public StatMode getStatMode() {
        return statMode;
    }

    public void setStatMode(StatMode mode) {
        this.statMode = mode;
    }
}