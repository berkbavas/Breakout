package com.github.berkbavas.breakout.math;

import org.junit.Assert;
import org.junit.Test;

public class Rectangle2DTest {
    @Test
    public void testConstructor() {

        Point2D leftTop = new Point2D(0, 1);
        Point2D rightTop = new Point2D(1, 0);
        Point2D leftBottom = new Point2D(-1, 0);
        Point2D rightBottom = new Point2D(0, -1);

        Rectangle2D rect = new Rectangle2D(leftTop, leftBottom, rightTop, rightBottom);

        Assert.assertEquals(Math.sqrt(2), rect.getWidth(), Util.EPSILON);
        Assert.assertEquals(Math.sqrt(2), rect.getHeight(), Util.EPSILON);

        leftTop = new Point2D(0, 4);
        rightTop = new Point2D(3, 0);
        leftBottom = new Point2D(-3, 0);
        rightBottom = new Point2D(0, -4);

        rect = new Rectangle2D(leftTop, leftBottom, rightTop, rightBottom);

        Assert.assertEquals(5.0, rect.getWidth(), Util.EPSILON);
        Assert.assertEquals(5.0, rect.getHeight(), Util.EPSILON);
    }

    @Test
    public void testIsPointInside() {
        Point2D leftTop = new Point2D(0, 3);
        Point2D rightTop = new Point2D(3, 0);
        Point2D leftBottom = new Point2D(-3, 0);
        Point2D rightBottom = new Point2D(0, -3);

        Rectangle2D rect = new Rectangle2D(leftTop, leftBottom, rightTop, rightBottom);

        Assert.assertTrue(rect.isPointInside(new Point2D(0, 0)));
        Assert.assertTrue(rect.isPointInside(new Point2D(0, 3)));
        Assert.assertTrue(rect.isPointInside(new Point2D(3, 0)));
        Assert.assertTrue(rect.isPointInside(new Point2D(-3, 0)));
        Assert.assertTrue(rect.isPointInside(new Point2D(0, -3)));

        Assert.assertFalse(rect.isPointInside(new Point2D(1, 4)));
    }

    @Test
    public void testIsRectangle2D() {
        Point2D leftTop = new Point2D(0, 3);
        Point2D rightTop = new Point2D(3, 0);
        Point2D leftBottom = new Point2D(-3, 0);
        Point2D rightBottom = new Point2D(0, -3);

        Assert.assertTrue(Rectangle2D.isRectangle2D(leftTop, leftBottom, rightTop, rightBottom));

        leftTop = new Point2D(0, 4);
        rightTop = new Point2D(3, 0);
        leftBottom = new Point2D(-3, 0);
        rightBottom = new Point2D(0, -4);

        Assert.assertFalse(Rectangle2D.isRectangle2D(leftTop, leftBottom, rightTop, rightBottom));
    }
}
