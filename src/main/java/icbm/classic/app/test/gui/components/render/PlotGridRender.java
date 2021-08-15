package icbm.classic.app.test.gui.components.render;

import icbm.classic.app.test.gui.components.PlotPanel;
import icbm.classic.app.test.gui.components.PlotRenderCallback;
import icbm.classic.app.test.gui.components.PlotRenderStages;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * Created by Robin Seifert on 8/13/2021.
 */
public class PlotGridRender implements PlotRenderCallback
{
    private int lineSize = 1;
    private Color lineColor = Color.BLACK;
    private double plotLineSpacingX = 1;
    private double plotLineSpacingY = 1;

    public PlotGridRender setLineColor(Color color)
    {
        this.lineColor = color;
        return this;
    }

    public PlotGridRender setLineSize(int size)
    {
        this.lineSize = size;
        return this;
    }

    public PlotGridRender setSpacing(double x, double y)
    {
        plotLineSpacingX = x;
        plotLineSpacingY = y;
        return this;
    }

    @Override
    public void apply(PlotPanel panel, Graphics2D g2, PlotRenderStages stage, boolean beforeStageDone)
    {
        if (stage == PlotRenderStages.BOARD && !beforeStageDone)
        {
            if (plotLineSpacingX > 0)
            {
                drawGridX(panel, g2);
            }
            if (plotLineSpacingY > 0)
            {
                drawGridY(panel, g2);
            }
        }
    }

    protected void drawGridX(PlotPanel panel, Graphics2D g2)
    {
        double start = 0;
        double end = panel.getDrawMaxX();

        double current = start;
        double xScale = panel.getScaleX();

        while (current < end)
        {
            //Increase
            current += plotLineSpacingX;

            //Get pixel point of x
            int x = panel.getPlotPadding() + (int) Math.ceil(current * xScale);

            //Draw line
            g2.setPaint(lineColor);
            g2.setStroke(new BasicStroke(lineSize));
            g2.draw(new Line2D.Double(x, panel.getPlotPadding(), x, panel.getHeight() - panel.getPlotPadding()));
        }
    }

    protected void drawGridY(PlotPanel panel, Graphics2D g2)
    {
        double start = 0;
        double end = panel.getDrawMaxY();

        double current = start;
        double yScale = panel.getScaleY();

        while (current < end)
        {
            //Get pixel point of x
            int y = panel.getPlotPadding() + (int) Math.ceil(current * yScale);

            //Draw line
            g2.setPaint(lineColor);
            g2.setStroke(new BasicStroke(lineSize));
            g2.draw(new Line2D.Double(panel.getPlotPadding(), y, panel.getWidth() - panel.getPlotPadding(), y));

            //Increase
            current += plotLineSpacingY;
        }
    }
}
