package icbm.classic.app.test.tools.math;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/1/2018.
 */
public class PanelWave extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";

    PlotPanel plotPanel;

    JTextField segmentsField;
    JTextField runsField;

    JLabel maxYLabel;

    public PanelWave()
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
        controlPanel.add(new Label("Segments"));
        controlPanel.add(segmentsField = new JTextField(6));
        segmentsField.setText("360");

        controlPanel.add(new Label("Runs"));
        controlPanel.add(runsField = new JTextField(6));
        runsField.setText("10");

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

        controlPanel.add(new Label("Max Y"));
        controlPanel.add(maxYLabel = new JLabel("-- m"));

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
                int segments = (int) Math.floor(Double.parseDouble(segmentsField.getText().trim()));
                int runs = (int) Math.floor(Double.parseDouble(runsField.getText().trim()));

                plotPanel.drawLines(segments, -1);

                //Get data
                calculateData(segments, runs);
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

        //Draw
        plotPanel.repaint();
    }

    /**
     * Calculates the data points for the
     */
    public void calculateData(int segments, int runs)
    {
        //Reference to confirm output http://setosa.io/ev/sine-and-cosine/
        List<PlotPoint> data = new ArrayList(segments);

        double degreePerSegment = 360.0 / (double) segments;

        for (int r = 0; r < runs; r++)
        {
            for (int s = 0; s < segments; s++)
            {
                int tick = r * segments + s;
                double degree = degreePerSegment * s;
                double radian = Math.toRadians(degree);

                double sine = Math.sin(radian);
                double cos = Math.cos(radian);

                data.add(new PlotPoint(tick, sine + 1, Color.RED)); //1 offset to fix plot negative
                data.add(new PlotPoint(tick, cos + 1, Color.BLUE)); //1 offset to fix plot negative
            }
        }

        plotPanel.setData(data);
    }

    public static int roundUp(int number, int interval)
    {
        if (interval == 0)
        {
            return 0;
        }
        else if (number == 0)
        {
            return interval;
        }
        else
        {
            if (number < 0)
            {
                interval *= -1;
            }

            int i = number % interval;
            return i == 0 ? number : number + interval - i;
        }
    }

    protected void outputDebug(String msg)
    {
        System.out.println(msg);
    }
}
