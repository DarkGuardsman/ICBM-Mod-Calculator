package icbm.classic.app.test.tools.blast;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;
import icbm.classic.app.test.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Visualizer for ICBM-Classic's Large Explosion alg
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/12/2018.
 */
public class PanelLargeBlast extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";

    PlotPanel plotPanel;

    //Settings field
    JTextField sizeField;

    //Render fields
    JTextField dotSizeField;
    JTextField lineSizeField;

    Label stepsLabel;

    public PanelLargeBlast()
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
        plotPanel.drawLines(1, 1);
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
        controlPanel.add(new Label("size"));
        controlPanel.add(sizeField = new JTextField(6));
        sizeField.setText(50 + "");

        //Spacer
        controlPanel.add(new JPanel());
        controlPanel.add(new JPanel());

        //Spacer
        controlPanel.add(new JLabel("Render Settings"));
        controlPanel.add(new JPanel());

        //Distance field
        controlPanel.add(new Label("Dot Size"));
        controlPanel.add(dotSizeField = new JTextField(6));
        dotSizeField.setText("6");

        controlPanel.add(new Label("Line Size"));
        controlPanel.add(lineSizeField = new JTextField(6));
        lineSizeField.setText("2");

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

        //Steps
        controlPanel.add(new Label("Steps"));
        controlPanel.add(stepsLabel = new Label("--"));

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
                //Get data
                final double size = Double.parseDouble(sizeField.getText().trim());
                final int dotRenderSize =  (int)Math.floor(Double.parseDouble(dotSizeField.getText().trim()));
                final int lineRenderSize =  (int)Math.floor(Double.parseDouble(lineSizeField.getText().trim()));

                double bound_d = size * 2 + Math.ceil(size * 0.2); //20% edge spacer

                //Linked list is faster to add nodes, as an array will resize
                final List<PlotPoint> data = new LinkedList();

                //Draw data
                final double centerX = Math.ceil(bound_d / 2) + 0.5;
                final double centerZ = Math.ceil(bound_d / 2) + 0.5;
                calculateData(data, Color.BLACK, dotRenderSize, lineRenderSize, size, centerX, centerZ, 0);
                //Set phi_n to zero due to being 2D, this should control pitch but we only need yaw

                //Set render bounds
                final int bound_i = (int) Math.ceil(bound_d);
                plotPanel.setPlotSize(bound_i, bound_i);

                //Set data into plot
                plotPanel.setData(data);
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

    /**
     * Calculates the data points for the
     */
    public void calculateData(final List<PlotPoint> data,
                              final Color color, final int dotRenderSize, final int lineRenderSize,
                              final double size, final double cx, final double cz,
                              final int theta_n)
    {
        //How many steps to go per rotation
        final int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / size));
        stepsLabel.setText(steps + "");

        double x;
        double z;

        double dx;
        double dz;

        double yaw;
        double pitch;


        double nx, nz;

        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        for (int phi_n = 0; phi_n < 2 * steps; phi_n++)
        {
            //Get angles for rotation steps
            yaw = Math.PI * 2 / steps * phi_n;
            pitch = Math.PI / steps * theta_n;

            //Debug
            outputDebug(String.format("Step[%s] Yaw: %.4f Pitch: %.4f", phi_n, yaw, pitch));

            //Figure out vector to move for trace (cut in half to improve trace skipping blocks)
            dx = cos(pitch) * cos(yaw) * 0.5;
            //dy = cos(theta) * 0.5;
            dz = cos(pitch) * sin(yaw) * 0.5;

            //Debug
            outputDebug(String.format("\tdx: %.4f dz: %.4f", dx, dz));

            //Reset position to current
            x = cx;
            z = cz;

            double distance = 0;
            PlotPoint previousPlot = centerDot;
            final Color lineColor = Utils.randomColor();
            //Trace from start to end
            while ((dx * dx + dz * dz) > 0 && distance <= size)
            {
                //Calculate distance
                nx = x - cx;
                nz = z - cz;
                distance = Math.sqrt(nx * nx + nz * nz);

                //Debug
                //outputDebug(String.format("\tx: %.4f z: %.4f", x, z));

                //add point to plot data
                final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
                previousPlot.connections.add(new PlotPoint(x, z, lineColor, lineRenderSize)); //TODO add field for line size
                data.add(dot);

                //Move forward
                x += dx;
                //y += dy;
                z += dz;
            }
        }
    }

    protected void outputDebug(String msg)
    {
        System.out.println(msg);
    }
}
