package icbm.classic.app.test.data;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

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
    public final Color color;
    public final int size;

    public Supplier<Boolean> shouldRender;

    public final List<PlotPoint> connections = new LinkedList();


    public PlotPoint(double x, double y, Color color)
    {
       this(x, y, color, 4);
    }

    public PlotPoint(double x, double y, Color color, int size)
    {
        this.x = x;
        this.y = y;
        this.color = color;
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

    public PlotPoint connect(PlotPoint plotPoint) {
        connections.add(plotPoint);
        return this;
    }
}
