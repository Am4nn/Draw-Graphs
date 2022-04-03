import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Main class of the simple function plotter. Contains the
 * main method and creates the GUI
 */

public class SimplePlotMain
{
    /**
     * Entry point
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        // Create the GUI on the Event-Dispatch-Thread
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    /**
     * Creates the frame containing the simple plotter
     */
    private static void createAndShowGUI()
    {
        // Create the main frame
        JFrame frame = new JFrame("SimplePlot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setSize(800,600);

        // Create the SimplePlotPanel and add it to the frame
        SimplePlotPanel plotPanel = new SimplePlotPanel();
        frame.getContentPane().add(plotPanel, BorderLayout.CENTER);

        // Create the Function that should be plotted, and assign
        // it to the SimplePlotPanel
        Function function = new Function()
        {
            @Override
            public double compute(double argument)
            {
                return Math.sqrt(100-argument*argument);
            }
        };
        plotPanel.setFunction(function);

        // Create a simple control panel and add it to the frame
        JComponent controlPanel = createControlPanel(plotPanel);
        frame.getContentPane().add(controlPanel, BorderLayout.EAST);

        // As the last action: Make the frame visible
        frame.setVisible(true);
    }

    /**
     * Creates a panel containing some Spinners that allow defining
     * the area in which the function should be shown
     *
     * @param plotPanel The SimplePlotPanel, to which the settings
     * will be transferred
     * @return The control-panel
     */
    private static JComponent createControlPanel(
            final SimplePlotPanel plotPanel)
    {
        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(0,2));
        controlPanel.add(panel, BorderLayout.NORTH);

        // Create spinners for the minimum and maximum
        // X- and Y-values
        final JSpinner minXSpinner = new JSpinner(
                new SpinnerNumberModel(-1.0, -1000.0, 1000.0, 0.1));
        final JSpinner maxXSpinner = new JSpinner(
                new SpinnerNumberModel( 1.0, -1000.0, 1000.0, 0.1));
        final JSpinner minYSpinner = new JSpinner(
                new SpinnerNumberModel(-1.0, -1000.0, 1000.0, 0.1));
        final JSpinner maxYSpinner = new JSpinner(
                new SpinnerNumberModel( 1.0, -1000.0, 1000.0, 0.1));

        // Add the spinners and some labels to the panel
        panel.add(new JLabel("minX"));
        panel.add(minXSpinner);
        panel.add(new JLabel("maxX"));
        panel.add(maxXSpinner);
        panel.add(new JLabel("minY"));
        panel.add(minYSpinner);
        panel.add(new JLabel("maxY"));
        panel.add(maxYSpinner);

        // Create a ChangeListener that will be added to all spinners,
        // and which transfers the settings to the SimplePlotPanel
        ChangeListener changeListener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent event)
            {
                double minX = ((Double)minXSpinner.getValue()).doubleValue();
                double maxX = ((Double)maxXSpinner.getValue()).doubleValue();
                double minY = ((Double)minYSpinner.getValue()).doubleValue();
                double maxY = ((Double)maxYSpinner.getValue()).doubleValue();
                plotPanel.setRangeX(minX, maxX);
                plotPanel.setRangeY(minY, maxY);
            }
        };
        minXSpinner.addChangeListener(changeListener);
        maxXSpinner.addChangeListener(changeListener);
        minYSpinner.addChangeListener(changeListener);
        maxYSpinner.addChangeListener(changeListener);

        // Set some default values for the Spinners
        minXSpinner.setValue(-10.0);
        maxXSpinner.setValue( 10.0);
        minYSpinner.setValue(-10.0);
        maxYSpinner.setValue( 10.0);

        return controlPanel;
    }
}


/**
 * Interface for a general function that may be plotted with
 * the SimplePlotPanel
 */
interface Function
{
    /**
     * Compute the value of the function for the given argument
     *
     * @param argument The function argument
     * @return The function value
     */
    double compute(double argument);
}



/**
 * The panel in which the function will be plotted
 */
class SimplePlotPanel extends JPanel
{
    private static final long serialVersionUID = -6588061082489436970L;

    /**
     * The function that will be plotted
     */
    private Function function;

    /**
     * The minimal x value that is shown
     */
    private double minX = -1.0f;

    /**
     * The maximal x value that is shown
     */
    private double maxX = 1.0f;

    /**
     * The minimal y value that is shown
     */
    private double minY = -1.0f;

    /**
     * The maximal y value that is shown
     */
    private double maxY = 1.0f;

    /**
     * Set the Function that should be plotted
     *
     * @param function The Function that should be plotted
     */
    public void setFunction(Function function)
    {
        this.function = function;
        repaint();
    }

    /**
     * Set the x-range that should be plotted
     *
     * @param minX The minimum x-value
     * @param maxX The maximum y-value
     */
    public void setRangeX(double minX, double maxX)
    {
        this.minX = minX;
        this.maxX = maxX;
        repaint();
    }

    /**
     * Set the y-range that should be plotted
     *
     * @param minY The minimum y-value
     * @param maxY The maximum y-value
     */
    public void setRangeY(double minY, double maxY)
    {
        this.minY = minY;
        this.maxY = maxY;
        repaint();
    }

    /**
     * Overridden method from JComponent: Paints this panel - that
     * is, paints the function into the given graphics object
     */
    @Override
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g = (Graphics2D)gr;
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        paintAxes(g);
        paintFunction(g);
    }

    /**
     * Converts an x-coordinate of the function into an x-value of this panel
     *
     * @param x The x-coordinate of the function
     * @return The x-coordinate on this panel
     */
    private int toScreenX(double x)
    {
        double relativeX = (x-minX)/(maxX-minX);
        int screenX = (int)(getWidth() * relativeX);
        return screenX;
    }

    /**
     * Converts an y-coordinate of the function into an y-value of this panel
     *
     * @param y The y-coordinate of the function
     * @return The y-coordinate on this panel
     */
    private int toScreenY(double y)
    {
        double relativeY = (y-minY)/(maxY-minY);
        int screenY = getHeight() - 1 - (int)(getHeight() * relativeY);
        return screenY;
    }

    /**
     * Converts an x-coordinate on this panel into an x-coordinate
     * for the function
     *
     * @param x The x-coordinate on the panel
     * @return The x-coordinate for the function
     */
    private double toFunctionX(int x)
    {
        double relativeX = (double)x/getWidth();
        double functionX = minX + relativeX * (maxX - minX);
        return functionX;
    }


    /**
     * Paints some coordinate axes into the given Graphics
     *
     * @param g The graphics
     */
    private void paintAxes(Graphics2D g)
    {
        int x0 = toScreenX(0);
        int y0 = toScreenY(0);
        g.setColor(Color.BLACK);
        g.drawLine(0,y0,getWidth(),y0);
        g.drawLine(x0,0,x0,getHeight());
    }

    /**
     * Paints the function into the given Graphics
     *
     * @param g The graphics
     */
    private void paintFunction(Graphics2D g)
    {
        g.setColor(Color.BLUE);

        int previousScreenX = 0;
        double previousFunctionX = toFunctionX(previousScreenX);
        double previousFunctionY = function.compute(previousFunctionX);
        int previousScreenY = toScreenY(previousFunctionY);

        for (int screenX=1; screenX<getWidth(); screenX++)
        {
            double functionX = toFunctionX(screenX);
            double functionY = function.compute(functionX);
            int screenY = toScreenY(functionY);

            g.drawLine(previousScreenX, previousScreenY, screenX, screenY);
            previousScreenX = screenX;
            previousScreenY = screenY;
        }
    }


}