package icbm.classic.app.test.gui;

import icbm.classic.app.test.tools.impact.PanelMissileAccuracy;
import icbm.classic.app.test.tools.math.PanelWave;
import icbm.classic.app.test.tools.path.PanelMissilePath;
import icbm.classic.app.test.tools.redmatter.PanelRedmatterRender;
import icbm.classic.app.test.tools.spawning.PanelEntitySpawning;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/1/2018.
 */
public class MainFrame extends JFrame implements ActionListener
{
    public MainFrame()
    {
        //Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setMinimumSize(new Dimension(800, 800));
        setLocation(200, 200);
        setTitle("Missile Calculator and Visualizer");

        add(buildCenter());

        pack();
    }

    protected JTabbedPane buildCenter()
    {
        JTabbedPane tabbedPane = new JTabbedPane();

        //Path test calculator
        tabbedPane.addTab("Path", null, new PanelMissilePath(),  "Calculator and visualizer for missile paths");

        //Impact calculator
        tabbedPane.addTab("Accuracy", null, new PanelMissileAccuracy(),  "Calculator and visualizer for missile impact accuracy");

        //Entity Spawn
        tabbedPane.addTab("Spawning", null, new PanelEntitySpawning(),  "Calculator and visualizer for MC1.12 entity random spawn point generator");

        //Redmatter Render
        tabbedPane.addTab("Redmatter Render", null, new PanelRedmatterRender(),  "Calculator and visualizer for redmatter render code");

        //Sin vs cos
        tabbedPane.addTab("Sin & cos", null, new PanelWave(),  "Calculator and visualizer for cos and sin function");


        return tabbedPane;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
}
