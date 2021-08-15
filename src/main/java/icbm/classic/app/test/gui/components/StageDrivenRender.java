package icbm.classic.app.test.gui.components;

import java.awt.Graphics2D;
import java.util.function.BiConsumer;

/**
 * Created by Robin Seifert on 8/12/2021.
 */
public class StageDrivenRender implements PlotRenderCallback
{
    private final PlotRenderStages stage;
    private final boolean beforeStageDone;
    private final BiConsumer<PlotPanel, Graphics2D> consumer;

    public StageDrivenRender(PlotRenderStages stage, boolean beforeStageDone, BiConsumer<PlotPanel, Graphics2D> consumer)
    {
        this.stage = stage;
        this.beforeStageDone = beforeStageDone;
        this.consumer = consumer;
    }

    @Override
    public void apply(PlotPanel panel, Graphics2D g2, PlotRenderStages stage, boolean beforeStageDone)
    {
        if (stage == this.stage && beforeStageDone == this.beforeStageDone)
        {
            consumer.accept(panel, g2);
        }
    }
}
