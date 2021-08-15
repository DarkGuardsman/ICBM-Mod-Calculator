package icbm.classic.app.test.gui.prefab;

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
import java.util.List;

/**
 * Prefab for basic calculation based plot panel
 */
public abstract class PanelCalculator extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";
    public static final String COMMAND_REFRESH = "refresh";

    protected PlotPanel plotPanel;

    protected JLabel runtimeDisplay;

    public PanelCalculator()
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
        initPlotPanel(plotPanel);
        return plotPanel;
    }

    protected void initPlotPanel(final PlotPanel plotPanel)
    {
        plotPanel.setMinimumSize(new Dimension(600, 600));
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

        addVarFields(controlPanel);


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

        addDisplayFields(controlPanel);

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
                //Draw data
                plotPanel.setPlotPointData(runCalculation());
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

    protected void addVarFields(JPanel controlPanel)
    {

    }

    protected void addDisplayFields(JPanel controlPanel)
    {

    }

    /**
     * Helper to quickly add a label and text field to the variable control panel
     *
     * @param controlPanel to add field and label to
     * @param label        name to use
     * @param defaultText  to use
     * @return field create for reference
     */
    protected JTextField addTextField(JPanel controlPanel, String label, String defaultText)
    {
        final JTextField field = new JTextField(6);
        field.setText(defaultText);

        controlPanel.add(new Label(label));
        controlPanel.add(field);

        return field;
    }

    protected double parseDouble(JTextField field)
    {
        try
        {
            return Double.parseDouble(field.getText());
        }
        catch (NumberFormatException ignored)
        {

        }
        return 0;
    }

    protected abstract List<PlotPoint> runCalculation();
}
