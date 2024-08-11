package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testFuzzyCompare() {
        Assert.assertTrue(Util.fuzzyCompare(1.0f, 1.0f));

        Assert.assertFalse(Util.fuzzyCompare(1.0f, 1.01f));
        Assert.assertFalse(Util.fuzzyCompare(1.0f, 1.001f));
        Assert.assertFalse(Util.fuzzyCompare(1.0f, 1.0001f));
        Assert.assertFalse(Util.fuzzyCompare(1.0f, 1.00001f));

        Assert.assertTrue(Util.fuzzyCompare(0.0f, 0.0f));
        Assert.assertTrue(Util.fuzzyCompare(0.0f, -0.0f));
        Assert.assertTrue(Util.fuzzyCompare(-0.0f, 0.0f));
        Assert.assertTrue(Util.fuzzyCompare(0.0f, -0.0f));

        Assert.assertTrue(Util.fuzzyCompare(2.0f, Math.sqrt(4.0f)));
        Assert.assertTrue(Util.fuzzyCompare(100.0f, Math.sqrt(10000.0f)));

        Assert.assertTrue(Util.fuzzyCompare(7.9f, 3.0f * 3.0f - 1.1f));

        Assert.assertTrue(Util.fuzzyCompare(Math.PI, 4.0f * Math.atan(1.0f)));
        Assert.assertTrue(Util.fuzzyCompare(0.0f, Math.sin(0.0f)));
        Assert.assertTrue(Util.fuzzyCompare(1.0f, Math.cos(0.0f)));

        Assert.assertTrue(Util.fuzzyCompare(1.41421356237, Math.sqrt(2)));

    }

    @Test
    public void testAlmostZero() {
        Assert.assertTrue(Util.isFuzzyZero(Math.sqrt(2) - 1.41421356237));
        Assert.assertTrue(Util.isFuzzyZero(Math.sqrt(4) - 2.0f));
        Assert.assertTrue(Util.isFuzzyZero(Math.atan(1) - 0.25f * Math.PI));
        Assert.assertTrue(Util.isFuzzyZero(1.0 - 1.000000000000001));
    }

    @Test
    public void testFuzzyBetween(){
        Assert.assertTrue(Util.isFuzzyBetween(0.0, 0.00001, 1.0));
        Assert.assertTrue(Util.isFuzzyBetween(0.0, 0.0000001, 0.000001));
        Assert.assertTrue(Util.isFuzzyBetween(0.0, 0, 0.000001));
        Assert.assertFalse(Util.isFuzzyBetween(2 * Math.sqrt(4), 3.99999, 4.00000001));
    }
}
