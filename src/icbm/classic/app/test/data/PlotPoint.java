package icbm.classic.app.test.data;

import java.awt.*;

/**
 * 2D data point with color
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/27/2018.
 */
public class PlotPoint
{
    public final double x;
    public final double y;
    public Color color;
    public int size = 4;


    public PlotPoint(double x, double y, Color color)
    {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public PlotPoint(double x, double y, Color color, int size)
    {
        this(x, y, color);
        this.size = size;
    }

    public double x()
    {
        return x;
    }

    public double y()
    {
        return y;
    }
}
