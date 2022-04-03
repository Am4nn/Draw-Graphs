import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;

public class Frame extends JFrame implements ComponentListener {

    EquationText Eqn;
    GraphPanel gPanel;

    Frame() {
        //Detect windows changes
        this.addComponentListener(this);

        gPanel = new GraphPanel(this);
        Thread graphThread = new Thread(gPanel);
        graphThread.start();
        Eqn = new EquationText(gPanel);
        this.add(Eqn);
        this.add(gPanel);
        gPanel.eqnText = this.Eqn;


        Image Logo = new ImageIcon(".File_en\\logo.jpg").getImage();
        this.setIconImage(Logo);
        this.getContentPane().setBackground(Color.BLACK);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1080,720);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        gPanel.frameTitle = "Draw Graphs  |  "+e.paramString();
        gPanel.frameResized(this.getWidth(), this.getHeight());
        Eqn.frameResized(this.getWidth(), this.getHeight());
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}

class EquationText extends JPanel implements ActionListener, KeyListener {

    public String equation;
    JTextField eqn = new JTextField();
    JLabel l1 = new JLabel();
    JButton b1 = new JButton();
    JButton b2 = new JButton();
    JButton b3 = new JButton();
    GraphPanel gPanel;
    public int PWidth = 1080;
    public int PHeight = 70;
    JLabel lImg = new JLabel();
    ImageIcon bg = new ImageIcon(new ImageIcon(".File_en\\background_EqnPanel.jpg").getImage().getScaledInstance(1550,1036,Image.SCALE_DEFAULT));


    EquationText(GraphPanel gPanel) {
        this.gPanel = gPanel;

        frameResized(PWidth,PHeight);

        eqn.setHorizontalAlignment(JLabel.LEADING);
        eqn.setForeground(Color.GREEN);
        eqn.setBackground(Color.BLACK);
        eqn.setBorder(null);
        eqn.setCaretColor(Color.CYAN);
        this.add(eqn);

        l1.setText(" y = ");
        l1.setHorizontalAlignment(JLabel.LEADING);
        l1.setVerticalAlignment(JLabel.CENTER);
        l1.setForeground(Color.GREEN);
        l1.setBackground(Color.BLACK);
        l1.setOpaque(true);
        this.add(l1);

        b1.setText("Draw");
        b1.setFocusable(false);
        b1.addActionListener(this);
//        this.add(b1);


        b2.setFocusable(false);
        b2.addActionListener(this);
        this.add(b2);


        b3.setFocusable(false);
        b3.addActionListener(this);
        this.add(b3);

        lImg.setIcon(bg);
        lImg.setBounds(0,0,1550,830);
        this.add(lImg);

        eqn.addKeyListener(this);
        this.setLayout(null);
        this.setBackground(Color.ORANGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            this.actions();
        }
        if (e.getSource() == b3) {
            openWebpage("https://docs.google.com/forms/d/e/1FAIpQLSe0EVrBCXSAwbKqS7iJiI3qjPc0eA3328C--XUC_zh7iLn6UQ/viewform?usp=sf_link");
        }
        if (e.getSource() == b2) {
            String[] options = {"Ok"};
            String message = """
                    Enter equation in TextField and Press 'enter' to draw graph
                    
                    Math Operators:     (Only use these and any combination of these operators !)
                                                          ( Example : (ln(x)/2)+sin(x)^2 )
                            (+) --> Addition
                            (-) --> Subtraction
                            (*) --> Multiplication
                            (/) --> Division
                            (%) --> Modulus
                            (^) --> Power
                            (sqrt(x)) --> Square Root of x
                            (sin(x)) --> Sine of x
                            (cos(x)) --> Cosine of x
                            (tan(x)) --> Tangent of x
                            (sec(x)) --> secant of x
                      *    (coSec(x)) --> coSecant of x
                            (cot(x)) --> cotangent of x
                            (ln(x)) --> natural log of x or log of x with base e
                            (log(x)) --> log of x with base 10
                            (-) --> Negative Number
                            
                            *Note : Here Text is Case-Sensitive
                            
                             Many times curve's line doesn't actually reach infinity.
                            
                            
                    """;
            JOptionPane.showOptionDialog(null,message,"Instructions", JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,options,0);
        }
    }

    public void actions() {
        equation = eqn.getText();
        if (Objects.equals(equation, "")) {
            equation = "0";
        }
        gPanel.equation = equation;
        gPanel.initiate();
    }

    public void frameResized(int FWidth, int FHeight) {
        this.PWidth = FWidth;
        this.PHeight = (int)(FHeight / 9.6);
        eqn.setBounds((int)(PWidth/6.3529),(int)(PHeight/14.0),(int)(PWidth/1.44),(int)(PHeight/1.16667));
        eqn.setFont(new Font("MV Boli", Font.BOLD, (int)(PHeight/1.8421))); // 38

        l1.setBounds((int)(PWidth/18.0),(int)(PHeight/14.0),(int)(PWidth/1.44),(int)(PHeight/1.16667));
        l1.setFont(new Font("MV Boli", Font.BOLD, (int)(PHeight/1.8421))); // 38

//        b1.setBounds((int)(PWidth/1.3333),(int)(PHeight/14.0),(int)(PWidth/6.35294),(int)(PHeight/1.16667));
//        b1.setFont(new Font("MV Boli", Font.BOLD,  (int)(PHeight*PWidth/1989.47368)));
        this.setBounds(0,0,PWidth,PHeight);


        b2.setBounds((int)(PWidth/1.102040),(int)(PHeight/14.0),(int)(PWidth/36.0),(int)(PHeight/2.3333));
        ImageIcon instructions = new ImageIcon(new ImageIcon(".File_en\\instructions.jpg").getImage().getScaledInstance(b2.getWidth(),b2.getHeight(),Image.SCALE_DEFAULT));
        b2.setIcon(instructions);

        b3.setBounds((int)(PWidth/1.102040),(int)(PHeight/1.75),(int)(PWidth/36.0),(int)(PHeight/2.3333));
        ImageIcon feedback = new ImageIcon(new ImageIcon(".File_en\\feedback.png").getImage().getScaledInstance(b2.getWidth(),b2.getHeight(),Image.SCALE_DEFAULT));
        b3.setIcon(feedback);
    }

    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == '\n') {
            this.actions();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}

class GraphPanel extends JPanel implements Runnable, MouseWheelListener, MouseMotionListener, MouseListener {

    public int PWidth = 1080;
    public int PHeight = 640;
    public int yPositionOfGraphPanelInFrame = 75;
    Point origin = new Point(PWidth/2.0,PHeight/2.0);

    double scale = 35;
    String equation = "0";
    float strokeSizeOfAxes = 2.0f;
    float strokeSizeOfCurve = 1.6f;
    Point[] points;
    int numberOfPoints  = PWidth;
    boolean running = false;
    Frame instanceOfFrame;
    String frameTitle;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    Point cursorLocation = new Point(PWidth, PHeight);
    String xLocationString = Double.toString(cursorLocation.x);
    String yLocationString = Double.toString(cursorLocation.y);
    Point prevForDrag = new Point();
    Graphics g;
    EquationText eqnText;

    GraphPanel(Frame instanceOfFrame) {
        this.instanceOfFrame = instanceOfFrame;
        frameTitle = instanceOfFrame.getTitle();

        this.setBounds(new Rectangle(0,yPositionOfGraphPanelInFrame,PWidth,PHeight));
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.addMouseWheelListener(this);
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        initiate();
    }

    public void frameResized(int FWidth, int FHeight) {
        this.PWidth = FWidth;
        this.PHeight = (int)(FHeight/1.125);
        this.yPositionOfGraphPanelInFrame = (int)(FHeight/9.6);
        this.origin.equate(new Point(this.PWidth/2.0,this.PHeight/2.0));
        this.numberOfPoints  = this.PWidth;
        this.setBounds(new Rectangle(0,this.yPositionOfGraphPanelInFrame,this.PWidth,this.PHeight));

        initiate();
    }

    public void paint(Graphics g) {
        this.g = g;
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);

        g.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(strokeSizeOfAxes, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        g.drawLine(origin.x_int,0, origin.x_int, PHeight);
        g.drawLine(0, origin.y_int, PWidth, origin.y_int);

        g.setColor(Color.BLUE);
        int r = 4;
        g.fillOval(origin.x_int-r, origin.y_int-r,2*r,2*r);

        labelingGraph();

        g.setColor(Color.RED);
        g2D.setStroke(new BasicStroke(strokeSizeOfCurve, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        for (int i = 1; i < points.length;i++) {
            Point now = points[i];
            Point prev = points[i-1];

            if (i-2 >= 0 && i+1 < points.length) {
                Point next = points[i + 1];
                Point pTp = points[i - 2];

//                System.out.println("("+now.x+", "+now.y+")"+"                        "+"("+xReal(now)+", "+yReal(now)+")"+"          "+scale+"     "+(prev.y-now.y)+"     "+(yReal(prev)-yReal(now)));
//                System.out.println("Slope : "+((now.y- prev.y)/(now.x- prev.x)) +"               Visible : "+points[i].visible);

                double slope1 = ((prev.y- pTp.y)/(prev.x- pTp.x));
                double slope2 = ((now.y- prev.y)/(now.x- prev.x));
                double slope3 = ((next.y- now.y)/(next.x- now.x));

                if ((slope1>0 && slope2<0 && slope3>0) || (slope1<0 && slope2>0 && slope3<0)) {
                    now.visible = false;
                }
            }

//            if (Double.isInfinite(points[i].y) && points[i].visible && points[i-1].visible)
//                g.drawLine(xReal(prev),yReal(prev),xReal(now),yReal(now));

            r = 2;
            if (points[i].visible && (Double.isNaN(points[i-1].y) || Double.isInfinite(points[i-1].y)))
                g.fillOval(xReal(now)-r, yReal(now)-r,2*r,2*r);
            else if(points[i].visible)
                g.drawLine(xReal(prev),yReal(prev),xReal(now),yReal(now));
        }

        if(scale>= 1) {
            markingUpdate();
        }
    }

    public int xReal(Point point) {
        return (int)(origin.x+point.x*scale);
    }

    public int yReal(Point point) {
        return (int)(origin.y-point.y*scale);
    }

    public void markingUpdate() {
        g.setColor(Color.BLUE);
        double i = -(int)(origin.x);
        double j = -(int)(origin.y);
        while (i < (-origin.x+PWidth)) {
            g.fillRect(xReal(new Point(i, 0)), yReal(new Point(0,0)),1,8);
            g.fillRect(xReal(new Point(0, 0)), yReal(new Point(0,j)),8,1);
            i++;
            j++;
        }
    }

    public void labelingGraph() {
        g.setFont(new Font("MV Boli", Font.BOLD, (int)(PHeight/35.55)));

        // Cursor's Location
        g.setColor(Color.BLACK);
        g.drawString("Cursor's x : "+xLocationString, (int)(PWidth/1.255), (int)(PHeight/35.55));
        g.drawString("Cursor's y : "+yLocationString, (int)(PWidth/1.255), (int)(PHeight/35.55)*2);

        // X-axis, Y-axis Marking
        g.setColor(Color.orange);
        g.drawString("Y-axis", origin.x_int+(PWidth/54),(PHeight/32));
        g.drawString("X-axis",(PWidth-(int)(PWidth/13.5)), origin.y_int+(int)(PHeight/22.85714));
    }

    public void initiate() {
        points = new Point[numberOfPoints];
        initializePoints();
        equationUpdate();
        repaint();
    }

    public void initializePoints() {
        int i = 0;
        double j = -(origin.x/scale);
        while (i < points.length) {
            points[i] = new Point(j,0);
            i++;
            j += 1/scale;
        }
    }

    public void equationUpdate() {
        for (Point point : points) {
            double x = point.x;
            double y;

            DecimalFormat decimalFormat2 = new DecimalFormat("#.##########");
            x = Double.parseDouble(decimalFormat2.format(x));
            String equation = "0 + " + this.equation.replaceAll("x", Double.toString(x)).replaceAll("X", Double.toString(x));
            try {
                y = EvaluateExpressionMaster.equateEquation(equation);
                if (Double.isInfinite(y) || Double.isNaN(y)) {
                    point.visible = false;
                }
            } catch (Exception e) {
                if (e.getMessage().equals("INVALID INPUT")) {
                    System.out.print( "\nINVALID INPUT (No match from the list of operators or expression or digit)\n");
                    eqnText.eqn.setText("");
                    eqnText.equation = "";
                    eqnText.actions();
                    String[] options = {"Ok"};
                    JOptionPane.showOptionDialog(null,"INVALID INPUT (No match from the list of operators or expression or digit)","Invalid Input", JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE,null,options,0);
                    break;
                }
                y = Double.NaN;
                point.visible = false;
            }

            point.y = y;
        }
    }

    public void render() {
        // Nothing here yet, because working on only updating when an event occurs.
//        repaint();
    }

    public void update() {
        // Nothing here yet, because working on only updating when an event occurs.
    }

    @Override
    public void run() {

        running = true;

        long lastTime = System.nanoTime();
        double fps = 60.0;
        long nanoSecond = 1000000000;
        double nanoSecIn1fps = nanoSecond / fps;
        double delta = 0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while(running) {
            long nowTime = System.nanoTime();
            delta += (nowTime - lastTime) / nanoSecIn1fps;
            lastTime = nowTime;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                instanceOfFrame.setTitle(frameTitle+"  |  "+frames+" fps  |  "+updates+" updates/sec"+"                    ~Aman Arya");
                updates = 0;
                frames = 0;
            }
        }
    }

    // MouseWheelListener
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double unit = e.getPreciseWheelRotation(); // for zoom-in = -1, zoom-out = +1

//        if (scale == 1 && unit < 0) scale -= unit;
//        else if (scale > 1) scale -= unit;
        scale -= unit;
        if(scale <= 1) scale = 1;

        scale = Double.parseDouble(decimalFormat.format(scale));

        initiate();
        updateCursorLocation(e.getPoint());
    }

    // MouseMotionListener
    @Override
    public void mouseMoved(MouseEvent e) {
        updateCursorLocation(e.getPoint());
        repaint();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        updateCursorLocation(e.getPoint());
        Point current = Point.directConvert(e.getPoint());
        origin.translate(new Point((current.x- prevForDrag.x),(current.y-prevForDrag.y)));
        prevForDrag.equate(current);
        initiate();
    }

    // MouseListener
    @Override
    public void mousePressed(MouseEvent e) {
        prevForDrag = Point.directConvert(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void updateCursorLocation(java.awt.Point point) {
        cursorLocation = PointWithRespectToOrigin(Point.directConvert(point));

        xLocationString = decimalFormat.format(cursorLocation.x);
        yLocationString = decimalFormat.format(cursorLocation.y);
    }

    public Point PointWithRespectToOrigin(Point point) {
        point.x = (point.x - origin.x) / scale;
        point.y = (-point.y + origin.y) / scale;
        return point;
    }
}

class Point {

    public double x,y;
    public int x_int, y_int;
    public boolean visible = true;

    Point() {}
    Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.x_int = (int)x;
        this.y_int = (int)y;
    }
    static public Point directConvert(java.awt.Point point) {
        return new Point(point.x, point.y);
    }
    public void equate(Point p) {
        this.x = p.x;
        this.y = p.y;
        this.x_int = p.x_int;
        this.y_int = p.y_int;
    }
    public void translate(Point p) {
        this.x += p.x;
        this.y += p.y;
        this.x_int += p.x_int;
        this.y_int += p.y_int;
    }
}