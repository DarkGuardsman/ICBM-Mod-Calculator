package icbm.classic.app.test.tools.gas;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;
import icbm.classic.app.test.gui.components.PlotRenderStages;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/16/2018.
 */
public class PanelGasCloud extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";

    PlotPanel plotPanel;

    JTextField radiusField;
    JTextField particlesField;
    JTextField runsField;

    public PanelGasCloud()
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
        controlPanel.add(new Label("Radius"));
        controlPanel.add(radiusField = new JTextField(6));
        radiusField.setText("20");

        //Distance field
        controlPanel.add(new Label("Particles"));
        controlPanel.add(particlesField = new JTextField(6));
        particlesField.setText("200");

        //Distance field
        controlPanel.add(new Label("Runs"));
        controlPanel.add(runsField = new JTextField(6));
        runsField.setText("120");

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
                //Reset each run
                plotPanel.clearDisplay();

                //Get data
                double radius = Double.parseDouble(radiusField.getText().trim());
                int count = (int) Math.floor(Double.parseDouble(particlesField.getText().trim()));
                int runs = (int) Math.floor(Double.parseDouble(runsField.getText().trim()));

                //Scale plot size with data
                double plotSize = Math.max(radius, runs / 10.0);
                plotSize += plotSize * 0.1; //10% edge to make plot easier to see

                plotPanel.setPlotSize((int) plotSize, (int) plotSize);

                //Draw data
                for (int i = 0; i < runs; i++)
                {
                    calculateData(randomColor(), radius, count, i);
                }

                //Draw radius
                plotPanel.addRendersToRun((panel, g2, stage, stageDone) -> {
                    if (stage == PlotRenderStages.END)
                    {
                        drawRadius(g2, plotPanel.getPlotSizeX() / 2.0, plotPanel.getPlotSizeY() / 2.0, radius, Color.BLACK);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
    public void calculateData(Color color, double radius, int count, int callCount)
    {
        List<PlotPoint> data = new ArrayList(count + 1);

        double cx = plotPanel.getPlotSizeX() / 2.0;
        double cy = plotPanel.getPlotSizeY() / 2.0;
        //Set target to center of plot
        PlotPoint target = new PlotPoint(cx, cy, color, 8);
        data.add(target);

        double scale = Math.min(radius, callCount) / 10;

        //Generate points
        for (int i = 0; i < count; i++)
        {
            double x = (Math.random() * radius / 2 - radius / 4) * scale;
            double y = (Math.random() * radius / 2 - radius / 4) * scale;

            if (Math.sqrt(x * x + y * y) <= radius)
            {
                data.add(new PlotPoint(cx + x, cy + y, color, 4));
            }
        }
        plotPanel.addData(data);
    }

    protected void drawRadius(Graphics2D g2, double x, double y, double radius, Color color)
    {
        plotPanel.drawEllipse(g2, color, x, y, radius * plotPanel.getScaleX(), radius * plotPanel.getScaleY(), false);
    }
}
