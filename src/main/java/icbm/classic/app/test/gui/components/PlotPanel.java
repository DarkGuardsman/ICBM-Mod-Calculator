package icbm.classic.app.test.gui.components;


import icbm.classic.app.test.data.PlotPoint;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
    public List<PlotPoint> plotPointData = new ArrayList(); //TODO add getter and make private
    protected List<PlotRenderCallback> rendersToRun = new ArrayList(); //TODO convert into a sorted render layer

    /** Spacing from each side */
    private int plotPadding = 20;

    private int plotSizeX = -1;
    private int plotSizeY = -1;

    public PlotPanel()
    {
        addComponentListener(new ResizeListenerBoxSize());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.START, true));

        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.BOARD, false));
        drawBorder(g2); //TODO Convert to render class
        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.BOARD, true));

        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.RULER, false));
        drawRuler(g2); //TODO Convert to render class
        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.RULER, true));

        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.DATA, false));
        drawData(g2); //TODO Convert to render class
        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.DATA, true));

        rendersToRun.forEach(func -> func.apply(this, g2, PlotRenderStages.END, false));
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



    /**
     * Draws the ruler
     *
     * @param g2
     */
    protected void drawRuler(Graphics2D g2)
    {
        //Left line
        g2.draw(new Line2D.Double(plotPadding, plotPadding, plotPadding, getHeight() - plotPadding));

        //Bottom line
        g2.draw(new Line2D.Double(plotPadding, getHeight() - plotPadding, getWidth() - plotPadding, getHeight() - plotPadding));
    }

    /**
     * Draws the data points
     *
     * @param g2
     */
    protected void drawData(Graphics2D g2)
    {
        if (plotPointData != null && !plotPointData.isEmpty())
        {
            //Render data points
            for (PlotPoint pos : plotPointData)
            {
                if (pos.shouldRender == null || pos.shouldRender.get())
                {
                    drawCircle(g2, pos.color, pos.x, pos.y, pos.size, true);
                }
                for (PlotPoint connection : pos.connections)
                {
                    if (connection.shouldRender == null || connection.shouldRender.get())
                    {
                        drawLine(g2, pos, connection);
                    }
                }
            }
        }
    }

    public void drawLine(Graphics2D g2, PlotPoint pointData, PlotPoint lineData)
    {

        //Calculate scale to fit display
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        //Get pixel position
        double startX = plotPadding + scaleX * pointData.x;
        double startY = getHeight() - plotPadding - scaleY * pointData.y;
        double endX = plotPadding + scaleX * lineData.x;
        double endY = getHeight() - plotPadding - scaleY * lineData.y;

        g2.setPaint(lineData.color != null ? lineData.color : Color.red);
        g2.setStroke(new BasicStroke(lineData.size));
        g2.draw(new Line2D.Double(startX, startY, endX, endY));
    }

    /**
     * Draws a circle on the plot
     * @param g2
     * @param color
     * @param point_x
     * @param point_y
     * @param size
     * @param fill
     */
    public void drawCircle(Graphics2D g2, Color color, double point_x, double point_y, double size, boolean fill)
    {
        drawEllipse(g2, color, point_x, point_y, size, size, fill);
    }

    public void drawCircle(Graphics2D g2, Color color, double point_x, double point_y, double size, boolean fill, boolean scale)
    {
        if (scale)
        {
            drawEllipse(g2, color, point_x, point_y, size * getScaleX(), size * getScaleX(), fill);
        }
        else
        {
            drawEllipse(g2, color, point_x, point_y, size, size, fill);
        }
    }

    /**
     * Draws an ellipse on the plot
     *
     * @param g2 graphics engine for drawing
     * @param color to draw with
     * @param point_x on the plot to render
     * @param point_y on the plot to render
     * @param size_x size relative to plot to render
     * @param size_y size relative to plot to render
     * @param fill false will draw an outline only
     */
    public void drawEllipse(Graphics2D g2, Color color, double point_x, double point_y, double size_x, double size_y, boolean fill)
    {
        //Calculate scale to fit display
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        //Get pixel position
        double x = plotPadding + scaleX * point_x;
        double y = getHeight() - plotPadding - scaleY * point_y;

        if (x >= 0 && x <= getWidth() && y <= getHeight())
        {
            //Generate circle
            Ellipse2D circle = new Ellipse2D.Double(x - (size_x / 2), y - (size_y / 2), size_x, size_y);

            //Set color
            g2.setPaint(color != null ? color : Color.red);

            //Draw
            if (fill)
            {
                g2.fill(circle);
            }
            else
            {
                g2.draw(circle);
            }
        }
    }

    public void drawBox(Graphics2D g2, Color color, double point_x, double point_y, double size_x, double size_y, boolean fill)
    {
        //Calculate scale to fit display
        double scaleX = getScaleX();
        double scaleY = getScaleY();

        //Get pixel position
        double x = plotPadding + scaleX * point_x;
        double y = getHeight() - plotPadding - scaleY * point_y;

        if (x >= 0 && x <= getWidth() && y <= getHeight())
        {
            //Generate circle
            Rectangle2D circle = new Rectangle2D.Double(x, y, size_x, size_y);

            //Set color
            g2.setPaint(color != null ? color : Color.red);

            //Draw
            if (fill)
            {
                g2.fill(circle);
            }
            else
            {
                g2.draw(circle);
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
    public double getScaleX()
    {
        return (double) (getWidth() - 2 * plotPadding) / getDrawMaxX();
    }

    /**
     * Scale to draw the data on the screen.
     * <p>
     * Modifies the position to correspond to the pixel location
     *
     * @return scale of view ((width - padding) / size)
     */
    public double getScaleY()
    {
        return (double) (getHeight() - 2 * plotPadding) / getDrawMaxY();
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
        for (PlotPoint pos : plotPointData)
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
        for (PlotPoint pos : plotPointData)
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
     * @param plotPointData
     */
    public void setPlotPointData(List<PlotPoint> plotPointData)
    {
        this.plotPointData = plotPointData;
    }

    /**
     * Adds data to the display
     *
     * @param data
     */
    public void addData(List<PlotPoint> data)
    {
        if (this.plotPointData == null)
        {
            this.plotPointData = new ArrayList();
        }
        this.plotPointData.addAll(data);
    }

    public void clearDisplay()
    {
        if (this.plotPointData != null)
        {
            this.plotPointData.clear();
        }
    }

    public void addRendersToRun(PlotRenderCallback renderFunction)
    {
        rendersToRun.add(renderFunction);
    }

    public int getPlotPadding() {
        return plotPadding;
    }
}