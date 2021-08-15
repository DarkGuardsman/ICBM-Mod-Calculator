package icbm.classic.app.test.tools.blast.calcs;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.tools.Utils;
import icbm.classic.app.test.tools.blast.BlastHelpers;

import java.awt.Color;
import java.util.List;

/**
 * Created by Robin Seifert on 8/11/2021.
 */
public class EveryBlockCalculations
{

    public static void calculateRayTraceEdgeBlockAsBox(final List<PlotPoint> data,
                                                final Color color, final int dotRenderSize, final int lineRenderSize,
                                                final double size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        final int rad = (int) Math.floor(size);

        BlastHelpers.forEachPosInCube(rad, rad, (xx, zz) -> {
            if (xx == -rad || xx == rad || zz == -rad || zz == rad)
            {
                data.add(new PlotPoint(xx + cx, zz + cz, color, dotRenderSize * 2));

                //Normalize, converts into a vector from center
                double mag = Math.sqrt(xx * xx + zz * zz);
                double xVector = xx / mag;
                double zVector = zz / mag;

                double x = 0;
                double z = 0;

                PlotPoint prevPoint = centerDot;
                while (x <= size && x >= -size && z <= size && z >= -size)
                {

                    final PlotPoint dot = new PlotPoint(x + cx, z + cz, color, dotRenderSize);
                    prevPoint.connections.add(new PlotPoint(x + cx, z + cz, Utils.randomColor(), lineRenderSize));
                    data.add(dot);

                    x += xVector * 0.5;
                    z += zVector * 0.5;
                }
            }
        });
    }

    public static void calculateRayTraceEveryBlockAsCircle(final List<PlotPoint> data,
                                                    final Color color, final int dotRenderSize, final int lineRenderSize,
                                                    final double size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        BlastHelpers.forEachPosInRadiusUntil((int) size, (xx, zz) -> {
            double x = xx + cx;
            double z = zz + cz;
            final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
            centerDot.connections.add(new PlotPoint(x, z, Utils.randomColor(), lineRenderSize));
            data.add(dot);
        });
    }
}
