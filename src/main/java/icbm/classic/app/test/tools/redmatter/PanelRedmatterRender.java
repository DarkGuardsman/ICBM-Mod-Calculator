package icbm.classic.app.test.tools.redmatter;

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
public class PanelRedmatterRender extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";
    public static final String COMMAND_REFRESH = "refresh";

    PlotPanel plotPanel;

    JTextField ticksField;
    JTextField scaleField;

    Checkbox ticksCheckbox;
    Checkbox beamsCheckbox;
    Checkbox timeScaleCheckbox;
    Checkbox sizeScaleCheckbox;


    public PanelRedmatterRender()
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
        plotPanel.drawLines(5, 50);
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
        controlPanel.add(new Label("Ticks"));
        controlPanel.add(ticksField = new JTextField(6));
        ticksField.setText("400");

        controlPanel.add(new Label("Scale"));
        controlPanel.add(scaleField = new JTextField(6));
        scaleField.setText("1.0");


        //Spacer
        controlPanel.add(new JPanel());
        controlPanel.add(new JPanel());


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


        controlPanel.add(new JLabel("Display Options"));
        controlPanel.add(new JPanel());

        controlPanel.add(ticksCheckbox = new Checkbox("Ticks"));
        ticksCheckbox.setState(true);

        controlPanel.add(beamsCheckbox = new Checkbox("Beams"));
        beamsCheckbox.setState(true);

        controlPanel.add(sizeScaleCheckbox = new Checkbox("Size Scale"));
        sizeScaleCheckbox.setState(true);

        controlPanel.add(timeScaleCheckbox = new Checkbox("Time Scale"));
        timeScaleCheckbox.setState(true);

        //Calculate button

        JButton clearButton = new JButton("Clear");
        clearButton.setActionCommand(COMMAND_CLEAR);
        clearButton.addActionListener(this);
        controlPanel.add(clearButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setActionCommand(COMMAND_REFRESH);
        refreshButton.addActionListener(this);
        controlPanel.add(refreshButton);

        //Add and return
        westPanel.add(controlPanel);
        return westPanel;
    }

    protected JPanel buildEastSection()
    {
        JPanel westPanel = new JPanel();

        //Controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(0, 2));

        //Header
        controlPanel.add(new Label("Field"));
        controlPanel.add(new Label("Value"));

        //controlPanel.add(new Label("Max Y"));
        //controlPanel.add(maxYLabel = new JLabel("-- m"));

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
                int count = (int) Math.floor(Double.parseDouble(ticksField.getText().trim()));
                float scale = Float.parseFloat(scaleField.getText().trim());

                //Draw data
                List<PlotPoint> data = new ArrayList(count);
                calculateData(data, count, scale);
                plotPanel.setData(data);
                plotPanel.repaint();
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
            plotPanel.repaint();
        }
        else if (event.getActionCommand().equalsIgnoreCase(COMMAND_REFRESH))
        {
            plotPanel.repaint();
        }
    }

    /**
     * Calculates the data points for the
     */
    public void calculateData(List<PlotPoint> data, int ticksToRun, float scale)
    {
        //Get tick value
        for (int ticksAlive = 0; ticksAlive < ticksToRun; ticksAlive++)
        {

            float ticks = ticksAlive;
            while (ticks > 200)
            {
                ticks -= 100;
            }

            float timeScale = (5 + ticks) / 200.0F;
            float sizeScale = 0.0F;

            if (timeScale > 0.8F)
            {
                sizeScale = (timeScale - 0.8F) / 0.2F;
            }

            int beamCount = (int) ((timeScale + timeScale * timeScale) / 2.0F * 60.0F);


            //Add data to display
            if (ticksCheckbox.getState())
            {
                data.add(new PlotPoint(ticksAlive, ticks, Color.RED));
            }
            if (timeScaleCheckbox.getState())
            {
                data.add(new PlotPoint(ticksAlive, timeScale, Color.YELLOW));
            }
            if (sizeScaleCheckbox.getState())
            {
                data.add(new PlotPoint(ticksAlive, sizeScale, Color.BLUE));
            }
            if (beamsCheckbox.getState())
            {
                data.add(new PlotPoint(ticksAlive, beamCount, Color.GREEN));
            }

            //Debug data
            System.out.println("T = " + ticksAlive);
            System.out.println("\tTicks = " + ticks);
            System.out.println("\tBeams = " + beamCount);
            System.out.println("\tTime Scale = " + timeScale);
            System.out.println("\tSize Scale = " + sizeScale);
        }
    }
}
