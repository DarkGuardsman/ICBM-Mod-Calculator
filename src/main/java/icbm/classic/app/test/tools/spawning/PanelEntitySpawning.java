package icbm.classic.app.test.tools.spawning;

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
public class PanelEntitySpawning extends JPanel implements ActionListener
{
    public static final String COMMAND_CALCULATE = "calculate";
    public static final String COMMAND_CLEAR = "clear";

    PlotPanel plotPanel;

    JTextField entitySpawnCountField;
    JTextField heightField;

    JLabel maxYLabel;

    public PanelEntitySpawning()
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
        plotPanel.setPlotSize(16, 255);
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
        controlPanel.add(new Label("Entities"));
        controlPanel.add(entitySpawnCountField = new JTextField(6));
        entitySpawnCountField.setText(10 + "");

        controlPanel.add(new Label("Chunk Height"));
        controlPanel.add(heightField = new JTextField(6));
        heightField.setText(255 + "");

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
                int count = (int) Math.floor(Double.parseDouble(entitySpawnCountField.getText().trim()));
                int height = (int) Math.floor(Double.parseDouble(heightField.getText().trim()));

                //Draw data
                calculateData(count, height);
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
    public void calculateData(int entities, int height)
    {
        //net.minecraft.world.WorldEntitySpawner#findChunksForSpawning(...)
        List<PlotPoint> data = new ArrayList(entities);

        Random random = new Random();
        final int maxY = roundUp(height, 16);
        maxYLabel.setText(maxY + "m");

        //Generate points
        for (int i = 0; i < entities; i++)
        {
            //random color per run
            Color color = randomColor();

            //Generate random position
            int randomChunkX = random.nextInt(16);
            int randomChunkY = random.nextInt(maxY);

            for (int k2 = 0; k2 < 3; ++k2)
            {
                //Loop randomly
                int l3 = (int) Math.ceil(Math.random() * 4.0D);
                for (int i4 = 0; i4 < l3; ++i4)
                {
                    //Create random offset
                    randomChunkX += random.nextInt(6) - random.nextInt(6);
                    randomChunkY += random.nextInt(1) - random.nextInt(1);

                    //Add data
                    data.add(new PlotPoint(randomChunkX + 0.5, randomChunkY + 0.5, color, 8));
                }
            }
        }
        plotPanel.setPlotPointData(data);
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
