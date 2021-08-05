package icbm.classic.app.test.tools.redmatter;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/1/2018.
 */
public class PanelRedmatterBlast extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";
    public static final String COMMAND_REFRESH = "refresh";

    PlotPanel plotPanel;

    JTextField radiusField;

    JLabel runtimeDisplay;

    public PanelRedmatterBlast()
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

        controlPanel.add(new Label("Radius"));
        controlPanel.add(radiusField = new JTextField(6));
        radiusField.setText("70");


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

        controlPanel.add(new Label("Runtime"));
        controlPanel.add(runtimeDisplay = new JLabel("-- m"));

        //Add and return
        westPanel.add(controlPanel);
        return westPanel;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        if (event.getActionCommand().equalsIgnoreCase(COMMAND_CALCULATE))
        {
            final long startTime = System.nanoTime();
            try
            {
                final float radius = Float.parseFloat(radiusField.getText().trim());

                //Draw data
                List<PlotPoint> data = new ArrayList((int) Math.floor(radius * radius * radius));
                calculateData(data, radius);
                plotPanel.setData(data);
                plotPanel.repaint();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            final long runTime = System.nanoTime() - startTime;
            runtimeDisplay.setText(runTime + "ns");
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
    public void calculateData(List<PlotPoint> data, float radius)
    {
        //data.add(new PlotPoint(ticksAlive, ticks, Color.RED));
    }
}
