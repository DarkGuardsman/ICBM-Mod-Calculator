package icbm.classic.app.test.tools.blast.calcs;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.tools.Utils;
import icbm.classic.app.test.tools.blast.CellConsumer;

import java.awt.Color;
import java.util.List;

/**
 * Created by Robin Seifert on 8/11/2021.
 */
public class BlastWallCalculations
{
    public static void calculateRayTraceBoxWalls(final List<PlotPoint> data,
                                          final Color color, final int dotRenderSize, final int lineRenderSize,
                                          final int size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        int[][] offsets = new int[][]{
                new int[]{0, 1},
                new int[]{0, -1},
                new int[]{1, 0},
                new int[]{-1, 0}
        };

        for (int[] offset : offsets)
        {
            loopWall(offset[0], offset[1], size, (xx, zz) -> {
                double x = xx + cx;
                double z = zz + cz;
                final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
                centerDot.connections.add(new PlotPoint(x, z, Utils.randomColor(), lineRenderSize));
                data.add(dot);
            });
        }
    }

    public static void loopWall(int offset_x, int offset_z, int size, CellConsumer consumer)
    {
        for (int step = -size; step <= size; step++)
        {
            final int x = size * offset_x + step * offset_z;
            final int z = size * offset_z + step * offset_x;
            consumer.apply(x, z);
        }
    }
}
