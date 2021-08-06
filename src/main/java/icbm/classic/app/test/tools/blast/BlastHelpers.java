package icbm.classic.app.test.tools.blast;

/**
 * Copied from ICBM classic and converted to 2D for testing
 */
public final class BlastHelpers
{
    private BlastHelpers()
    {
        //Empty as this is a helper class only
    }

    public static interface CellConsumer
    {
        void apply(int x, int z);
    }

    /**
     * Loops a cube with the size given then only returns the values inside the radius
     *
     * @param radius   - xyz size, will ceil then ignore outside
     * @param consumer - callback for the xyz, return false to stop looping
     */
    public static void forEachPosInRadiusUntil(double radius, CellConsumer consumer)
    {
        final int size = (int) Math.ceil(radius);
        forEachPosInCube(size, size, (x, z) ->
        {
            final double radiusSQ = radius * radius;
            final double distanceSQ = x * x + z * z;
            if (distanceSQ <= radiusSQ)
            {
                consumer.apply(x, z);
            }
        });
    }

    /**
     * loops a cube from -size to size
     * <p>
     * If a value of (1x, 1y, 1z) is provided the output cube will be 3x3x3. As
     * it will go from -1 to 1 in each axis.
     *
     * @param xSize    - size to loop in the x
     * @param zSize    - size to loop in the z
     * @param consumer - callback for the xyz, returning false in the callback will cancel the loop
     */
    public static void forEachPosInCube(int xSize, int zSize, CellConsumer consumer)
    {
        for (int x = -xSize; x <= xSize; x++)
        {
            for (int z = -zSize; z <= zSize; z++)
            {
                consumer.apply(x, z);
            }
        }
    }
}
