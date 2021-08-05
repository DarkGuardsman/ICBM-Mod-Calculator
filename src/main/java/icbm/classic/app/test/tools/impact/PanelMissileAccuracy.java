package icbm.classic.app.test.tools.impact;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/1/2018.
 */
public class PanelMissileAccuracy extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";

    PlotPanel plotPanel;

    JTextField plotSizeXField;
    JTextField plotSizeYField;
    JTextField distanceField;
    JTextField missilesToFireField;

    Label maxHeightLabel;

    public PanelMissileAccuracy()
    {
        setLayout(new BorderLayout());

        //Output data
        add(buildEastSection(), BorderLayout.EAST);

        //Add plot panel to left side
        add(buildMainDisplay(), BorderLayout.CENTER);

        //Setup control panel (Contains input fields and action buttons)
        add(buildWestSection(), BorderLayout.WEST);

        //Debug panel TODO add later
        JPanel debugPanel = new JPanel();
        debugPanel.setMinimumSize(new Dimension(800, 200));
        add(debugPanel, BorderLayout.SOUTH);
    }

    protected JPanel buildMainDisplay()
    {
        plotPanel = new PlotPanel();
        plotPanel.setMinimumSize(new Dimension(600, 600));
        return plotPanel;
    }

    protected JPanel buildWestSection()
    {
        JPanel westPanel = new JPanel();

        //Controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 2));

        //Spacer
        controlPanel.add(new JLabel("Variables"));
        controlPanel.add(new JPanel());

        //Distance field
        controlPanel.add(new Label("Inaccuracy"));
        controlPanel.add(distanceField = new JTextField(6));
        distanceField.setText(30 + "");

        //Distance field
        controlPanel.add(new Label("Missiles"));
        controlPanel.add(missilesToFireField = new JTextField(6));
        missilesToFireField.setText(1000 + "");

        //Calculate button
        controlPanel.add(new JPanel());
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setActionCommand(COMMAND_CALCULATE);
        calculateButton.addActionListener(this);
        controlPanel.add(calculateButton);

        //Spacer
        controlPanel.add(new JPanel());
        controlPanel.add(new JPanel());

        //---------------------------------------------------------------

        //Spacer
        controlPanel.add(new JLabel("Draw Options"));
        controlPanel.add(new JPanel());

        //Plot size fields
        controlPanel.add(new Label("Plot Size X"));
        controlPanel.add(plotSizeXField = new JTextField(6));
        plotSizeXField.setText("100");

        controlPanel.add(new Label("Plot Size Z"));
        controlPanel.add(plotSizeYField = new JTextField(6));
        plotSizeYField.setText("100");

        //Spacer
        controlPanel.add(new JPanel());
        controlPanel.add(new JPanel());

        //---------------------------------------------------------------


        //Calculate button
        controlPanel.add(new JPanel());
        JButton clearButton = new JButton("Clear Display");
        clearButton.setActionCommand(COMMAND_CLEAR);
        clearButton.addActionListener(this);
        controlPanel.add(clearButton);

        //Add and return
        westPanel.add(controlPanel);
        return westPanel;
    }

    protected JPanel buildEastSection()
    {
        JPanel westPanel = new JPanel();

        //Controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(11, 2));

        //Header
        controlPanel.add(new Label("Field"));
        controlPanel.add(new Label("Value"));

        //Add and return
        westPanel.add(controlPanel);
        return westPanel;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        if (event.getActionCommand().equalsIgnoreCase(COMMAND_CALCULATE))
        {
            try
            {
                //Set plot size
                plotPanel.setPlotSize((int) Double.parseDouble(plotSizeXField.getText().trim()), (int) Double.parseDouble(plotSizeYField.getText().trim()));

                //Get data
                double maxInaccuracy = Double.parseDouble(distanceField.getText().trim());
                int count = (int) Math.floor(Double.parseDouble(missilesToFireField.getText().trim()));

                //Draw data
                calculateData(randomColor(), maxInaccuracy, count, true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (event.getActionCommand().equalsIgnoreCase(COMMAND_CLEAR))
        {
            //Clear data
            plotPanel.clearDisplay();
        }

        plotPanel.repaint();
    }

    //Creates a random color for use
    private Color randomColor()
    {
        return new Color((int) (255f * Math.random()), (int) (255f * Math.random()), (int) (255f * Math.random()));
    }

    /**
     * Calculates the data points for the
     */
    public void calculateData(Color color, double maxInaccuracy, int missiles, boolean addData)
    {
        List<PlotPoint> data = new ArrayList(missiles + 1);

        //Set target to center of plot
        PlotPoint target = new PlotPoint(plotPanel.getPlotSizeX() / 2, plotPanel.getPlotSizeY() / 2, color, 8);
        data.add(target);

        Random random = new Random();

        //Generate points
        for (int i = 0; i < missiles; i++)
        {
            data.add(applyInaccuracy(target, random, maxInaccuracy));
        }

        if (addData)
        {
            plotPanel.addData(data);
        }
        else
        {
            plotPanel.setPlotPointData(data);
        }
    }

    public static PlotPoint applyInaccuracy(PlotPoint target, Random random, double maxInaccuracy)
    {
        final float degrees = 360;

        //Randomize distance
        double inaccuracy = maxInaccuracy * random.nextFloat();

        //Randomize radius drop
        double yaw = Math.toRadians(random.nextFloat() * degrees);
        double pitch = 0; //pitch is never used, left in to reduce confusion

        //Equation for x & z are more complex to match the actual missile calculations
        double x = -Math.sin(yaw) * Math.cos(pitch);
        double z = Math.sin(-Math.cos(yaw) * Math.cos(pitch));

        //Update target
        return new PlotPoint(target.x + x * inaccuracy, target.y + z * inaccuracy, target.color);
    }

    protected void outputDebug(String msg)
    {
        System.out.println(msg);
    }
}
