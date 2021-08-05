package icbm.classic.app.test.gui.components;

import java.awt.Graphics2D;

/**
 * Created by Robin Seifert on 8/5/2021.
 */
@FunctionalInterface
public interface PlotRenderCallback
{
    /**
     * @param panel           - plot being rendered
     * @param g2              - graphics to render
     * @param stage           - current render stage
     * @param beforeStageDone - true if before stage has been rendered
     */
    void apply(PlotPanel panel, Graphics2D g2, PlotRenderStages stage, boolean beforeStageDone);
}
