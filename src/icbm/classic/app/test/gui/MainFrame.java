package icbm.classic.app.test.gui;

import icbm.classic.app.test.impact.PanelMissileAccuracy;
import icbm.classic.app.test.path.PanelMissilePath;

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


        return tabbedPane;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }
}
