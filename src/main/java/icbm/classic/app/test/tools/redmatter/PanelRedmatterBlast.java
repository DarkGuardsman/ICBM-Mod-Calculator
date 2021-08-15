package icbm.classic.app.test.tools.redmatter;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;
import icbm.classic.app.test.gui.components.PlotRenderStages;
import icbm.classic.app.test.gui.components.StageDrivenRender;
import icbm.classic.app.test.gui.components.render.PlotGridRender;
import icbm.classic.app.test.gui.prefab.PanelCalculator;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel used to toy with scale settings for redmatter logic.
 * <p>
 * In theory the defaults and logic should match current in dev implementation
 */
public class PanelRedmatterBlast extends PanelCalculator
{
    /** Size of the redmatter and main driving variable */
    JTextField sizeField;
    /** Multiplier of size to get render size */
    JTextField renderScaleField;
    /** Multiplier of size to get kill range for entities */
    JTextField killRangeScaleField;
    /** Multiplier of size to get block harvest range, usually 1:1 */
    JTextField blockRangeScaleField;
    /** Multiplier of size to get entity gravity range */
    JTextField entityRangeScaleField;

    @Override
    protected void initPlotPanel(final PlotPanel plotPanel)
    {
        super.initPlotPanel(plotPanel);
        plotPanel.setBackground(Color.BLACK);
        plotPanel.addRendersToRun(new PlotGridRender().setLineColor(Color.WHITE));
        plotPanel.addRendersToRun(new StageDrivenRender(PlotRenderStages.END, false, this::renderRedmatterDisk));
        plotPanel.addRendersToRun(new StageDrivenRender(PlotRenderStages.END, false, this::renderRedmatterOrb));
        plotPanel.addRendersToRun(new StageDrivenRender(PlotRenderStages.END, false, this::renderRedmatterBlockRange));
        plotPanel.addRendersToRun(new StageDrivenRender(PlotRenderStages.END, false, this::renderRedmatterEntityRange));
    }

    @Override
    protected void addVarFields(JPanel controlPanel)
    {
        sizeField = addTextField(controlPanel, "Size", "5");
        renderScaleField = addTextField(controlPanel, "RenderScale", "0.1");
        killRangeScaleField = addTextField(controlPanel, "KillScale", "0.08");
        blockRangeScaleField = addTextField(controlPanel, "BlockScale", "1");
        entityRangeScaleField = addTextField(controlPanel, "GravityScale", "2");
    }

    private int centerX()
    {
        double width = parseDouble(sizeField) * 2;
        double fullSize = width + 2;
        return (int) Math.ceil(fullSize / 2);
    }

    private int centerZ()
    {
        double width = parseDouble(sizeField) * 2;
        double fullSize = width + 2;
        return (int) Math.ceil(fullSize / 2);
    }

    @Override
    protected List<PlotPoint> runCalculation()
    {
        final List<PlotPoint> data = new ArrayList();

        double size = parseDouble(sizeField);

        plotPanel.setPlotSize((int) Math.ceil(size * 2) + 2, (int) Math.ceil(size * 2) + 2);

        return data;
    }


    private void renderRedmatterOrb(final PlotPanel plot, Graphics2D g2)
    {
        final double renderScale = parseDouble(renderScaleField) * parseDouble(sizeField);
        g2.setStroke(new BasicStroke(2));
        plot.drawCircle(g2, Color.BLUE, centerX(), centerZ(), renderScale, true, true);
    }

    private void renderRedmatterDisk(final PlotPanel plot, Graphics2D g2)
    {
        final double renderScale = (parseDouble(renderScaleField) * 2) * parseDouble(sizeField);
        g2.setStroke(new BasicStroke(2));
        plot.drawCircle(g2, Color.CYAN, centerX(), centerZ(), renderScale, true, true);
    }

    private void renderRedmatterBlockRange(final PlotPanel plot, Graphics2D g2)
    {
        final double renderScale = parseDouble(blockRangeScaleField) * parseDouble(sizeField);
        g2.setStroke(new BasicStroke(2));
        plot.drawCircle(g2, Color.RED, centerX(), centerZ(), renderScale, false, true);
    }

    private void renderRedmatterEntityRange(final PlotPanel plot, Graphics2D g2)
    {
        final double renderScale = parseDouble(entityRangeScaleField) * parseDouble(sizeField);
        g2.setStroke(new BasicStroke(2));
        plot.drawCircle(g2, Color.yellow, centerX(), centerZ(), renderScale, false, true);
    }
}
