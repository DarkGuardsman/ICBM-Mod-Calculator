package icbm.classic.app.test.tools.blast;

/**
 * Created by Robin Seifert on 8/7/2021.
 */
@FunctionalInterface
public interface CellConsumer
{
    void apply(int x, int z);
}
