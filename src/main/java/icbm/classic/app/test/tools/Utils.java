package icbm.classic.app.test.tools;

import java.awt.Color;

/**
 * Created by Robin Seifert on 8/5/2021.
 */
public class Utils
{
    //Creates a random color for use
    public static Color randomColor()
    {
        return new Color((int) (255f * Math.random()), (int) (255f * Math.random()), (int) (255f * Math.random()));
    }
}
