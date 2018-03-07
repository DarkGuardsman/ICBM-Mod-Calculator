package icbm.classic.app.test.gui.components;


import icbm.classic.app.test.data.PlotPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple panel used to draw 2D plot points
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/26/2018.
 */
public class PlotPanel extends JPanel
{
    /** Data to display in the panel */
    protected List<PlotPoint> data = new ArrayList();
    /** Spacing from each side */
    int PAD = 20;

    int plotSizeX = -1;
    int plotSizeY = -1;

    double plotLineSpacingX = -1;
    double plotLineSpacingY = -1;

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBorder(g2);
        drawGrid(g2);
        drawRuler(g2);
        drawData(g2);
    }

    /**
     * Draws a border around the component to define the edge
     *
     * @param g2
     */
    protected void drawBorder(Graphics2D g2)
    {
        g2.drawRect(1, 1, getWidth() - 2, getHeight() - 2); //TODO why -2?
    }

    protected void drawGrid(Graphics2D g2)
    {
        if (plotLineSpacingX > 0)
        {
            drawGridX(g2);
        }
        if (plotLineSpacingY > 0)
        {
            drawGridY(g2);
        }
    }

    protected void drawGridX(Graphics2D g2)
    {
        double start = 0;
        double end = getDrawMaxX();

        double current = start;
        double xScale = getScaleX();

        while (current < end)
        {
            //Increase
            current += plotLineSpacingX;

            //Get pixel point of x
            int x = PAD + (int) Math.ceil(current * xScale);

            //Draw line
            g2.draw(new Line2D.Double(x, PAD, x, getHeight() - PAD));
        }
    }

    protected void drawGridY(Graphics2D g2)
    {
        double start = 0;
        double end = getDrawMaxY();

        double current = start;
        double xScale = getScaleY();

        while (current < end)
        {
            //Increase
            current += plotLineSpacingY;

            //Get pixel point of x
            int y = PAD + (int) Math.ceil(current * xScale);

            //Draw line
            g2.draw(new Line2D.Double(PAD, y, getWidth() - PAD, y));
        }
    }

    /**
     * Draws the ruler
     *
     * @param g2
     */
    protected void drawRuler(Graphics2D g2)
    {
        //Left line
        g2.draw(new Line2D.Double(PAD, PAD, PAD, getHeight() - PAD));

        //Bottom line
        g2.draw(new Line2D.Double(PAD, getHeight() - PAD, getWidth() - PAD, getHeight() - PAD));
    }

    /**
     * Draws the data points
     *
     * @param g2
     */
    protected void drawData(Graphics2D g2)
    {
        if (data != null && !data.isEmpty())
        {
            //Calculate scale to fit display
            double scaleX = getScaleX();
            double scaleY = getScaleY();

            //Render data points
            for (PlotPoint pos : data)
            {
                //Get pixel position
                double x = PAD + scaleX * pos.x();
                double y = getHeight() - PAD - scaleY * pos.y();

                if (x >= 0 && x <= getWidth() && y <= getHeight())
                {
                    //Set color
                    g2.setPaint(pos.color != null ? pos.color : Color.red);

                    //Draw
                    g2.fill(new Ellipse2D.Double(x - (pos.size / 2), y - (pos.size / 2), pos.size, pos.size));
                }
            }
        }
    }

    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    protected double getScaleX()
    {
        return (double) (getWidth() - 2 * PAD) / getDrawMaxX();
    }

    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    protected double getScaleY()
    {
        return (double) (getHeight() - 2 * PAD) / getDrawMaxY();
    }

    public double getDrawMaxX()
    {
        return plotSizeX > 0 ? plotSizeX : getPointMaxX();
    }

    public double getDrawMaxY()
    {
        return plotSizeY > 0 ? plotSizeY : getPointMaxY();
    }

    /**
     * Max y value in the data set
     *
     * @return
     */
    public double getPointMaxY()
    {
        double max = -Integer.MAX_VALUE;
        for (PlotPoint pos : data)
        {
            if (pos.y() > max)
            {
                max = pos.y();
            }
        }
        return max;
    }

    /**
     * Max x value in the data set
     *
     * @return
     */
    public double getPointMaxX()
    {
        double max = -Integer.MAX_VALUE;
        for (PlotPoint pos : data)
        {
            if (pos.x() > max)
            {
                max = pos.x();
            }
        }
        return max;
    }

    /**
     * Sets the plot size of the display.
     * <p>
     * By default the display will auto scale to match the data.
     * This can be used to ensure the data scales to a defined value.
     *
     * @param x
     * @param y
     */
    public void setPlotSize(int x, int y)
    {
        this.plotSizeY = y;
        this.plotSizeX = x;
    }

    public int getPlotSizeX()
    {
        return plotSizeX;
    }

    public int getPlotSizeY()
    {
        return plotSizeY;
    }

    /**
     * Sets the data to draw
     *
     * @param data
     */
    public void setData(List<PlotPoint> data)
    {
        this.data = data;
    }

    /**
     * Adds data to the display
     *
     * @param data
     */
    public void addData(List<PlotPoint> data)
    {
        if (this.data == null)
        {
            this.data = new ArrayList();
        }
        this.data.addAll(data);
    }

    public void clearDisplay()
    {
        if (this.data != null)
        {
            this.data.clear();
        }
    }

    public void drawLines(double i)
    {
        plotLineSpacingX = i;
        plotLineSpacingY = i;
    }

    public void drawLines(double x, double y)
    {
        plotLineSpacingX = x;
        plotLineSpacingY = y;
    }
}