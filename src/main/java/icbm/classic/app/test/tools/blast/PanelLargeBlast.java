package icbm.classic.app.test.tools.blast;

import icbm.classic.app.test.data.PlotPoint;
import icbm.classic.app.test.gui.components.PlotPanel;
import icbm.classic.app.test.gui.components.PlotRenderStages;
import icbm.classic.app.test.tools.Utils;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

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
    JComboBox<String> algSelected;
    Checkbox normalizeVectorCheckbox;

    //Render fields
    JTextField dotSizeField;
    JTextField lineSizeField;
    Checkbox renderDotsCheckbox;
    Checkbox renderLinesCheckbox;
    Checkbox renderHeatmapCheckbox;

    Label stepsLabel;
    Label rotationCountLabel;
    Label lineCountLabel;

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
        plotPanel.addRendersToRun((plot, g2, stage, stageDone) -> {
            if (stage == PlotRenderStages.GRID && !stageDone && plot.plotPointData.size() > 0 && renderHeatmapCheckbox.getState())
            {
                for (int x = 0; x < plot.getPlotSizeX(); x++)
                {
                    for (int y = 0; y < plot.getPlotSizeY(); y++)
                    {
                        final int minX = x;
                        final int maxX = x + 1;
                        final int minY = y;
                        final int maxY = y + 1;
                        final int dotCount = (int) plot.plotPointData.stream()
                                .filter(dot -> dot.x >= minX && dot.x <= maxX && dot.y >= minY && dot.y <= maxY)
                                .count();

                        if (dotCount > 0)
                        { //TODO scale color based on number of hits
                            Color color = getColorForValue(dotCount, 0, 10);
                            plot.drawBox(g2, color, x, y + 1, plot.getScaleX(), plot.getScaleY(), true);
                        }
                    }
                }
            }
        });

        return plotPanel;
    }

    private Color getColorForValue(double value, float min, float max)
    {

        final float BLUE_HUE = Color.RGBtoHSB(0, 0, 255, new float[3])[0];
        final float RED_HUE = Color.RGBtoHSB(255, 0, 0, new float[3])[0];

        if (value < min || value > max)
        {
            return Color.BLACK;
        }
        final float hue = BLUE_HUE + (float) ((RED_HUE - BLUE_HUE) * (value - min) / (max - min));
        return Color.getHSBColor(hue, 1.0f, 1.0f);
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

        //Alg selection
        controlPanel.add(new Label("Alg"));
        controlPanel.add(algSelected = new JComboBox<String>
                (
                        new String[]{
                                "Blast Large",
                                "E TNT",
                                "RAY CUBE",
                                "RAY CIRCLE",
                                "EDGE CUBE",
                                "EDGE WALLS"
                        }
                ));
        algSelected.setSelectedIndex(0);

        //Distance field
        controlPanel.add(new Label("Size"));
        controlPanel.add(sizeField = new JTextField(6));
        sizeField.setText("10");

        //TODO hide or disable if not selecting TNT
        controlPanel.add(new Label("Normalize Vector"));
        controlPanel.add(normalizeVectorCheckbox = new Checkbox());
        normalizeVectorCheckbox.setState(true);

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

        controlPanel.add(new Label("Render Dots"));
        controlPanel.add(renderDotsCheckbox = new Checkbox());
        renderDotsCheckbox.setState(true);

        controlPanel.add(new Label("Render Lines"));
        controlPanel.add(renderLinesCheckbox = new Checkbox());
        renderLinesCheckbox.setState(true);

        controlPanel.add(new Label("Render Heatmap"));
        controlPanel.add(renderHeatmapCheckbox = new Checkbox());
        renderHeatmapCheckbox.setState(true);

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

        //rotations or large lines
        controlPanel.add(new Label("Rotations"));
        controlPanel.add(rotationCountLabel = new Label("--"));

        //small line count
        controlPanel.add(new Label("Lines"));
        controlPanel.add(lineCountLabel = new Label("--"));

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
                final int dotRenderSize = (int) Math.floor(Double.parseDouble(dotSizeField.getText().trim()));
                final int lineRenderSize = (int) Math.floor(Double.parseDouble(lineSizeField.getText().trim()));

                //Linked list is faster to add nodes, as an array will resize
                final List<PlotPoint> data = new LinkedList();

                //Draw data
                final double centerX = Math.ceil(size / 2) + 0.5;
                final double centerZ = Math.ceil(size / 2) + 0.5;

                final int selectedAlg = algSelected.getSelectedIndex();

                //Blast large
                if (selectedAlg == 0)
                {
                    //Set phi_n to zero due to being 2D, this should control pitch but we only need yaw
                    calculateLargeBlast(data, Color.BLACK, dotRenderSize, lineRenderSize, size, centerX, centerZ, 0);
                }
                //Vanilla TNT with ICBM spice
                else if (selectedAlg == 1)
                {
                    calculateTntBlast(data, Color.BLACK, dotRenderSize, lineRenderSize, (float) size, centerX, centerZ);
                }
                //Badly optimized redmatter that used a ray trace per cube
                else if (selectedAlg == 2)
                {
                    calculateRayTraceEveryBlockAsBox(data, Color.BLACK, dotRenderSize, lineRenderSize, (float) size, centerX, centerZ);
                }
                //Badly optimized redmatter that used a ray trace per cube
                else if (selectedAlg == 3)
                {
                    calculateRayTraceEveryBlockAsCircle(data, Color.BLACK, dotRenderSize, lineRenderSize, (float) size, centerX, centerZ);
                }
                //Ray trace edge blocks only
                else if (selectedAlg == 4)
                {
                    calculateRayTraceEdgeBlockAsBox(data, Color.BLACK, dotRenderSize, lineRenderSize, (float) size, centerX, centerZ);
                }
                //Ray trace edge blocks only, used a faster alg then above
                else if (selectedAlg == 5)
                {
                    calculateRayTraceBoxWalls(data, Color.BLACK, dotRenderSize, lineRenderSize, (int) Math.floor(size), centerX, centerZ);
                }
                else
                {
                    return;
                }


                //Set render bounds
                final int maxX = (int) Math.ceil(data.stream().map(p -> p.x).max(Comparator.comparingDouble(p -> p)).get());
                final int minX = (int) Math.ceil(data.stream().map(p -> p.x).min(Comparator.comparingDouble(p -> p)).get());
                final int maxY = (int) Math.ceil(data.stream().map(p -> p.y).max(Comparator.comparingDouble(p -> p)).get());
                final int minY = (int) Math.ceil(data.stream().map(p -> p.y).min(Comparator.comparingDouble(p -> p)).get());

                plotPanel.setPlotSize(maxX - minX + 2, maxY - minY + 2); //+2 for edge

                final int translateX = -minX + 1; //+1 for edge
                final int translateY = -minY + 1;

                final List<PlotPoint> relocatedData = data.stream().map(point -> {
                    final PlotPoint dot = new PlotPoint(point.x + translateX, point.y + translateY, point.color, point.size);
                    dot.shouldRender = this::shouldRenderDots;
                    point.connections.forEach(connection -> {
                        final PlotPoint line = new PlotPoint(connection.x + translateX, connection.y + translateY, connection.color, connection.size);
                        line.shouldRender = this::shouldRenderLines;
                        dot.connections.add(line);
                    });
                    return dot;
                }).collect(Collectors.toList());

                //Set data into plot
                plotPanel.setPlotPointData(relocatedData);


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

    public boolean shouldRenderLines()
    {
        return renderLinesCheckbox.getState();
    }

    public boolean shouldRenderDots()
    {
        return renderDotsCheckbox.getState();
    }

    public void calculateTntBlast(final List<PlotPoint> data,
                                  final Color color, final int dotRenderSize, final int lineRenderSize,
                                  final float size, final double cx, final double cz)
    {

        final Random random = new Random();
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        final int raysPerAxis = 16;
        for (int xs = 0; xs < raysPerAxis; ++xs)
        {
            //for (int ys = 0; ys < this.raysPerAxis; ++ys)
            //final int ys = 0;
            for (int zs = 0; zs < raysPerAxis; ++zs)
            {
                if (xs == 0 || xs == raysPerAxis - 1 || /*ys == 0 || ys == raysPerAxis - 1 ||*/ zs == 0 || zs == raysPerAxis - 1)
                {
                    //Debug
                    outputDebug(String.format("Step[%s,%s]: ", xs, zs));

                    //Step calculation, between -1 to 1 creating edge slices of a cube
                    double xStep = xs / (raysPerAxis - 1.0F) * 2.0F - 1.0F;
                    //double yStep = ys / (raysPerAxis - 1.0F) * 2.0F - 1.0F;
                    double zStep = zs / (raysPerAxis - 1.0F) * 2.0F - 1.0F;

                    outputDebug(String.format("\txStep: %.4f zStep: %.4f", xStep, zStep));

                    //Distance
                    final double magnitude = Math.sqrt(xStep * xStep + /*yStep * yStep +*/ zStep * zStep);

                    //normalize, takes it from a box shape to a circle shape
                    if (normalizeVectorCheckbox.getState())
                    {
                        xStep /= magnitude;
                        //yStep /= diagonalDistance;
                        zStep /= magnitude;
                    }

                    outputDebug(String.format("\tdx: %.4f dz: %.4f", xStep, zStep));

                    //Get energy
                    float radialEnergy = size; //* (0.7F + random.nextFloat() * 0.6F);

                    //Get starting point for ray
                    double x = cx;
                    //double y = this.location.y();
                    double z = cz;

                    PlotPoint previousPlot = centerDot;
                    final Color lineColor = Utils.randomColor();

                    for (float step = 0.3F; radialEnergy > 0.0F; radialEnergy -= step * 0.75F)
                    {
                        final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
                        previousPlot.connections.add(new PlotPoint(x, z, lineColor, lineRenderSize)); //TODO add field for line size
                        data.add(dot);

                        //Iterate location
                        x += xStep * step;
                        //y += yStep * step;
                        z += zStep * step;
                    }
                }
            }
        }
    }

    /**
     * Calculates the data points for the
     */
    public void calculateLargeBlast(final List<PlotPoint> data,
                                    final Color color, final int dotRenderSize, final int lineRenderSize,
                                    final double size, final double cx, final double cz,
                                    final int theta_n)
    {
        //How many steps to go per rotation
        final int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / size));

        double x;
        double z;

        double dx;
        double dz;

        double yaw;
        double pitch;


        double nx, nz;

        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        int lineCount = 0;
        int stepCount = 0;

        final int lineDensityScale = 2;
        for (int yawSlices = 0; yawSlices < lineDensityScale * steps; yawSlices++)
        {
            //Get angles for rotation steps
            yaw = (Math.PI / steps) * yawSlices;
            pitch = (Math.PI / steps) * theta_n;

            //Debug
            outputDebug(String.format("Step[%s] Yaw: %.4f (%.4f) Pitch: %.4f (%.4f)", yawSlices, yaw, toDegrees(yaw) % 360, pitch, toDegrees(pitch)));

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

                //Track number of lines created
                lineCount++;
            }

            //Track rotation
            stepCount++;
        }

        //Update labels
        stepsLabel.setText(steps + "");
        lineCountLabel.setText("" + lineCount);
        rotationCountLabel.setText("" + stepCount);
    }


    public void calculateRayTraceEveryBlockAsBox(final List<PlotPoint> data,
                                                 final Color color, final int dotRenderSize, final int lineRenderSize,
                                                 final double size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        BlastHelpers.forEachPosInCube((int) size, (int) size, (xx, zz) -> {
            double x = xx + cx;
            double z = zz + cz;
            final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
            centerDot.connections.add(new PlotPoint(x, z, Utils.randomColor(), lineRenderSize));
            data.add(dot);
        });
    }

    public void calculateRayTraceBoxWalls(final List<PlotPoint> data,
                                          final Color color, final int dotRenderSize, final int lineRenderSize,
                                          final int size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        int[][] offsets = new int[][]{
                new int[]{0, 1},
                new int[]{0, -1},
                new int[]{1, 0},
                new int[]{-1, 0}
        };

        for (int[] offset : offsets)
        {
            loopWall(offset[0], offset[1], size, (xx, zz) -> {
                double x = xx + cx;
                double z = zz + cz;
                final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
                centerDot.connections.add(new PlotPoint(x, z, Utils.randomColor(), lineRenderSize));
                data.add(dot);
            });
        }
    }

    private void loopWall(int offset_x, int offset_z, int size, CellConsumer consumer)
    {
        for (int step = -size; step <= size; step++)
        {
            final int x = size * offset_x + step * offset_z;
            final int z = size * offset_z + step * offset_x;
            consumer.apply(x, z);
        }
    }

    public void calculateRayTraceEdgeBlockAsBox(final List<PlotPoint> data,
                                                final Color color, final int dotRenderSize, final int lineRenderSize,
                                                final double size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        final int rad = (int) Math.floor(size);

        BlastHelpers.forEachPosInCube(rad, rad, (xx, zz) -> {
            if (xx == -rad || xx == rad || zz == -rad || zz == rad)
            {
                data.add(new PlotPoint(xx + cx, zz + cz, color, dotRenderSize * 2));

                //Normalize, converts into a vector from center
                double mag = Math.sqrt(xx * xx + zz * zz);
                double xVector = xx / mag;
                double zVector = zz / mag;

                //Debug
                outputDebug(String.format("x: %s y: %s | dx: %.4f dz: %.4f", xx, zz, xVector, zVector));

                double x = 0;
                double z = 0;

                PlotPoint prevPoint = centerDot;
                while (x <= size && x >= -size && z <= size && z >= -size)
                {

                    final PlotPoint dot = new PlotPoint(x + cx, z + cz, color, dotRenderSize);
                    prevPoint.connections.add(new PlotPoint(x + cx, z + cz, Utils.randomColor(), lineRenderSize));
                    data.add(dot);

                    outputDebug(String.format("\tx: %.4f y: %.4f", x, z));

                    x += xVector * 0.5;
                    z += zVector * 0.5;
                }
            }
        });
    }

    public void calculateRayTraceEveryBlockAsCircle(final List<PlotPoint> data,
                                                    final Color color, final int dotRenderSize, final int lineRenderSize,
                                                    final double size, final double cx, final double cz)
    {
        final PlotPoint centerDot = new PlotPoint(cx, cz, color, dotRenderSize * 2);
        data.add(centerDot);

        BlastHelpers.forEachPosInRadiusUntil((int) size, (xx, zz) -> {
            double x = xx + cx;
            double z = zz + cz;
            final PlotPoint dot = new PlotPoint(x, z, color, dotRenderSize);
            centerDot.connections.add(new PlotPoint(x, z, Utils.randomColor(), lineRenderSize));
            data.add(dot);
        });
    }

    protected void outputDebug(String msg)
    {
        System.out.println(msg);
    }
}
