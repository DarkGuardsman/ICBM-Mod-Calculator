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
public class PanelSinGenerator extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";

    PlotPanel plotPanel;

    JTextField timeField;
    JTextField amplitudeField;
    JTextField frequencyField;
    JTextField phaseField;
    JTextField biasField;

    JLabel maxYLabel;

    public PanelSinGenerator()
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

        //https://www.mathworks.com/help/simulink/slref/sinewave.html?s_tid=gn_loc_drop

        //Distance field
        controlPanel.add(new Label("Time"));
        controlPanel.add(timeField = new JTextField(6));
        timeField.setText("100");

        controlPanel.add(new Label("Amplitude"));
        controlPanel.add(amplitudeField = new JTextField(6));
        amplitudeField.setText("1");

        controlPanel.add(new Label("Frequency"));
        controlPanel.add(frequencyField = new JTextField(6));
        frequencyField.setText("10");

        controlPanel.add(new Label("Phase"));
        controlPanel.add(phaseField = new JTextField(6));
        phaseField.setText("0");

        controlPanel.add(new Label("Bias"));
        controlPanel.add(biasField = new JTextField(6));
        biasField.setText("0");

        //Calculate button
        controlPanel.add(new JPanel());
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setActionCommand(COMMAND_CALCULATE);
        calculateButton.addActionListener(this);
        controlPanel.add(calculateButton);

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
                int time = (int) Double.parseDouble(timeField.getText().trim());
                double amp = Double.parseDouble(amplitudeField.getText().trim());
                double hz = Double.parseDouble(frequencyField.getText().trim());
                double phase = Double.parseDouble(phaseField.getText().trim());
                double bias = Double.parseDouble(biasField.getText().trim());

                //Reset panel state
                plotPanel.drawLines(hz, amp / 2);
                plotPanel.clearDisplay();

                //Get data
                calculateData(time, amp, hz, phase, bias);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //Draw
        plotPanel.repaint();
    }

    /**
     * Calculates the data points for the
     *
     * @param time
     * @param amp
     * @param hz
     * @param phase
     * @param bias
     */
    public void calculateData(int time, double amp, double hz, double phase, double bias)
    {
        //https://www.mathworks.com/help/simulink/slref/sinewave.html?s_tid=gn_loc_drop
        //https://en.wikipedia.org/wiki/Waveform
        List<PlotPoint> data = new ArrayList(time);

        for (int t = 0; t < time; t++)
        {
            double value = 2 * Math.PI * t - phase;
            value /= hz;
            double sine = Math.sin(value);
            double y = amp * sine + bias;

            data.add(new PlotPoint(t, y + amp, Color.GREEN)); //amp added to fix negative on display
        }

        plotPanel.setData(data);
    }
}
