package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testFuzzyCompare() {
        Assert.assertTrue(Util.fuzzyCompare(1.0, 1.0));
        Assert.assertFalse(Util.fuzzyCompare(1.0, 1.01));
        Assert.assertFalse(Util.fuzzyCompare(1.0, 1.001));
        Assert.assertFalse(Util.fuzzyCompare(1.0, 1.0001));
        Assert.assertFalse(Util.fuzzyCompare(1.0, 1.00001));
        Assert.assertTrue(Util.fuzzyCompare(0.0, 0.0));
        Assert.assertTrue(Util.fuzzyCompare(0.0, -0.0));
        Assert.assertTrue(Util.fuzzyCompare(-0.0, 0.0));
        Assert.assertTrue(Util.fuzzyCompare(0.0, -0.0));
        Assert.assertTrue(Util.fuzzyCompare(2.0, Math.sqrt(4.0)));
        Assert.assertTrue(Util.fuzzyCompare(100.0, Math.sqrt(10000.0)));
        Assert.assertTrue(Util.fuzzyCompare(7.9, 3.0 * 3.0 - 1.1));
        Assert.assertTrue(Util.fuzzyCompare(Math.PI, 4.0 * Math.atan(1.0)));
        Assert.assertTrue(Util.fuzzyCompare(0.0, Math.sin(0.0)));
        Assert.assertTrue(Util.fuzzyCompare(1.0, Math.cos(0.0)));
        Assert.assertTrue(Util.fuzzyCompare(1.41421356237, Math.sqrt(2)));
    }

    @Test
    public void testAlmostZero() {
        Assert.assertTrue(Util.isFuzzyZero(Math.sqrt(2) - 1.41421356237));
        Assert.assertTrue(Util.isFuzzyZero(Math.sqrt(4) - 2.0));
        Assert.assertTrue(Util.isFuzzyZero(Math.atan(1) - 0.25 * Math.PI));
        Assert.assertTrue(Util.isFuzzyZero(1.0 - 1.000000000000001));
    }

    @Test
    public void testFuzzyBetween() {
        Assert.assertTrue(Util.isBetween(0.0, 0.00001, 1.0));
        Assert.assertTrue(Util.isBetween(0.0, 0.0000001, 0.000001));
        Assert.assertTrue(Util.isBetween(0.0, 0, 0.000001));
        Assert.assertFalse(Util.isBetween(2 * Math.sqrt(4), 3.99999, 4.00000001));
    }
}
