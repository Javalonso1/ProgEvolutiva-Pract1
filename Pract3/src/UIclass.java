import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class UIclass extends JFrame {

    private GraphPanel graphPanel;

    JRadioButton elitismOn;
    JRadioButton elitismOff;

    public JButton simulateButton;
    JComboBox MutationType;
    JComboBox CrossType;
    JComboBox SelectionType;
    JFormattedTextField popSize;
    JFormattedTextField nGens;
    JFormattedTextField seed;
    JFormattedTextField profundidad;
    JComboBox InicializacionFull;
    JPanel centerPanel;
    JLabel bottomLabel;


    int[][] mapMatrix;
    ArrayList<Integer> path = new ArrayList<>();


    public void setMap(int[][] map)
    {
        mapMatrix = map;
    }

    public boolean elitism()
    {
        return elitismOn.isSelected();
    }
    public int seed() {return  (int) seed.getValue();}
    public int profundidad() { return (int) profundidad.getValue();}
    public GeneticManager.MUTATION_TYPE mutation()
    {
        return GeneticManager.MUTATION_TYPE.values()[MutationType.getSelectedIndex()];
    }
    public GeneticManager.CROSS_METHOD cross()
    {
        return GeneticManager.CROSS_METHOD.values()[CrossType.getSelectedIndex()];
    }
    public GeneticManager.SELECTION_METHOD selection()
    {
        return GeneticManager.SELECTION_METHOD.values()[SelectionType.getSelectedIndex()];
    }
    public boolean inicializacionFull()
    {
        return InicializacionFull.getSelectedIndex() == 0;
    }
    public int getPopSize()
    {
        return (int) popSize.getValue();

    }
    public int getGenNumber()
    {
        return (int) nGens.getValue();
    }

    public void setBottomText(String htmlText) {
        SwingUtilities.invokeLater(() -> {
            bottomLabel.setText("<html>" + htmlText + "</html>");
        });
    }
    public void setPath(ArrayList<Integer> path) {
        this.path = path;
        repaint();
    }


    public UIclass() {
        setTitle("Practica 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 600);
        setLayout(new BorderLayout());

        simulateButton = new JButton("Evolve");


        // === LEFT PANEL: A / B Selection ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        elitismOn = new JRadioButton("Elitism On", true);
        elitismOff = new JRadioButton("Elitism Off", false);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(elitismOn);
        group1.add(elitismOff);

        JPanel buttPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttPanel.add(elitismOn);
        buttPanel.add(elitismOff);
        buttPanel.setPreferredSize(new Dimension(300, 30));

        JLabel labelmap = new JLabel("Generación:");
        String[] inicial = {"Full", "Grow"};
        InicializacionFull = new JComboBox(inicial);
        JPanel inicialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inicialPanel.add(labelmap);
        InicializacionFull.setSelectedIndex(0);
        inicialPanel.add(InicializacionFull);

        JLabel labelmut = new JLabel("mutación:");
        String[] mutations = {"SUBARBOL", "FUNCIONAL", "TERMINAL", "PODA", "ALEATORIA"};
        MutationType = new JComboBox(mutations);
        JPanel mutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mutPanel.add(labelmut);
        mutPanel.add(MutationType);

        JLabel labelcross = new JLabel("cruce:");
        String[] cross = {"SUBARBOL"};
        CrossType = new JComboBox(cross);
        JPanel crossPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        crossPanel.add(labelcross);
        crossPanel.add(CrossType);

        JLabel labelsel = new JLabel("selections:");
        String[] selections = {"RULETA", "TORNEOS", "ESTOCASTICO", "TRUNCAMIENTO", "RESTOS", "RANKING"};
        SelectionType = new JComboBox(selections);
        JPanel selPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selPanel.add(labelsel);
        selPanel.add(SelectionType);

        // Label
        JLabel label1 = new JLabel("Population:");
        JLabel label2 = new JLabel("Nº generations:");
        JLabel label3 = new JLabel("Prof. árboles:");
        JLabel label4 = new JLabel("Semilla:");

        // Number formatter
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(false); // blocks letters
        formatter.setMinimum(0); // optional

        // Input field

        popSize = new JFormattedTextField(formatter);
        popSize.setColumns(4);
        popSize.setValue(100);
        nGens = new JFormattedTextField(formatter);
        nGens.setColumns(4);
        nGens.setValue(200);
        profundidad = new JFormattedTextField(formatter);
        profundidad.setColumns(4);
        profundidad.setValue(5);
        seed = new JFormattedTextField(formatter);
        seed.setColumns(4);
        seed.setValue(3000);

        JPanel popPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        popPanel.add(label1);
        popPanel.add(popSize);

        JPanel genPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genPanel.add(label2);
        genPanel.add(nGens);

        JPanel profPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profPanel.add(label3);
        profPanel.add(profundidad);


        JPanel seedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seedPanel.add(label4);
        seedPanel.add(seed);


        leftPanel.add(buttPanel);
        leftPanel.add(popPanel);
        leftPanel.add(genPanel);
        leftPanel.add(profPanel);
        leftPanel.add(inicialPanel);
        leftPanel.add(seedPanel);
        leftPanel.add(mutPanel);
        leftPanel.add(crossPanel);
        leftPanel.add(selPanel);
        leftPanel.add(simulateButton);



        leftPanel.setPreferredSize(new Dimension(200, 600));

        add(leftPanel, BorderLayout.WEST);

        // === CENTER PANEL

        centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();

                int rows = mapMatrix.length;
                int cols = mapMatrix[0].length;
                int cellW = width / cols;
                int cellH = height / rows;

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        switch (mapMatrix[i][j]){
                            case 1:
                                g2.setColor(new Color(0.6f,0f,0f));
                                g2.fillRect(j * cellW, i * cellH, cellW, cellH);
                                g2.setColor(Color.red);
                                g2.drawLine(j * cellW, i * cellH, (j +1) * cellW, (i +1)* cellH);
                                g2.drawLine((j +1) * cellW, i * cellH, j * cellW, (i +1)* cellH);
                                break;
                            case 2:
                                g2.setColor(Color.orange);
                                g2.fillRect(j * cellW, i * cellH, cellW, cellH);
                                break;
                            case 3:
                                g2.setColor(Color.darkGray);
                                g2.fillRect(j * cellW, i * cellH, cellW, cellH);
                                g2.setColor(Color.yellow);
                                g2.fillOval(j * cellW + cellW/4, i * cellH + cellH/4, cellW/2, cellH/2);
                                break;
                            default:
                                g2.setColor(Color.darkGray);
                                g2.fillRect(j * cellW, i * cellH, cellW, cellH);
                                break;
                        }
                    }
                }

                // Draw grid lines
                g2.setColor(Color.BLACK);
                for (int i = 0; i <= rows; i++) {
                    g2.drawLine(0, i * cellH, width, i * cellH); // horizontal
                }
                for (int j = 0; j <= cols; j++) {
                    g2.drawLine(j * cellW, 0, j * cellW, height); // vertical
                }


                // stable color per path
                Color color = Color.BLACK;
                g2.setColor(color);
                g2.setStroke(new BasicStroke(3));

                // loop through all points
                for (int k = 0; k < path.size() - 3; k += 2) {
                    int x1 = path.get(k);
                    int y1 = path.get(k + 1);
                    int x2 = path.get(k + 2);
                    int y2 = path.get(k + 3);

                    int px1 = x1 * cellW + cellW / 2;
                    int py1 = y1 * cellH + cellH / 2;
                    int px2 = x2 * cellW + cellW / 2;
                    int py2 = y2 * cellH + cellH / 2;

                    g2.drawLine(px1, py1, px2, py2);

                }

            }
        };
        centerPanel.setPreferredSize(new Dimension(50, 50));
        add(centerPanel, BorderLayout.CENTER);

        // === RIGHT PANEL: Graph with Two Points ===
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.EAST);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLabel = new JLabel("Ready"); // default text
        bottomPanel.add(bottomLabel);

        bottomLabel.setFont(new Font("Arial", Font.BOLD, 15));
        bottomPanel.setPreferredSize(new Dimension(1250, 90));

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public GraphPanel getGraphPanel() {
        return graphPanel;
    }

    // --- Inner class: Graph panel ---
    class GraphPanel extends JPanel {

        private double[][] dataSeries;   // Each row = one series
        private Color[] colors;       // Colors for each series
        private String[] labels = {"generationBest", "absoluteBest", "generationAverage"};
        double maxValue, minValue;

        public GraphPanel() {
            setPreferredSize(new Dimension(500, 600));
            dataSeries = new double[0][0];  // initially empty
            colors = new Color[]{Color.red, Color.blue, Color.green};
        }

        public void setData(double[] series1, double[] series2, double[] series3) {
            dataSeries = new double[][] {series1, series2, series3};

            maxValue = Double.MIN_VALUE;
            minValue = Double.MAX_VALUE;
            for (double[] series : dataSeries) {
                for (double v : series) {
                    if (v > maxValue) {
                        maxValue = v;
                    }
                    if (v < minValue && v != 0) {
                        minValue = v;
                    }
                }
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int width = getWidth();
            int height = getHeight();

            // Draw axes
            g2.setColor(Color.BLACK);
            g2.drawLine(50, height - 50, width - 50, height - 50); // x-axis
            g2.drawLine(50, height - 50, 50, 50);                  // y-axis

            int marginX = 50;
            int marginY = 50;
            int plotWidth = width - 2 * marginX;
            int plotHeight = height - 2 * marginY;

            if (dataSeries.length == 0) return;

            int nPoints = dataSeries[0].length; // assume all series same length
            double scale = plotHeight / (maxValue + 10);
            int numTicks = 10;
            double stepValue = (maxValue + 10) / numTicks;
            Font normalFont = new Font("Arial", Font.PLAIN, 12);
            Font bigFont = new Font("Arial", Font.BOLD, 18);

            for (int i = 0; i <= numTicks; i++) {

                double value = i * stepValue;
                int y = (int) (height - marginY - value * scale);

                // Tick mark
                g2.drawLine(marginX - 5, y, marginX, y);

                // If this tick matches the highest last value → make it bigger
                if (Math.abs(value - minValue) < stepValue / 2) {
                    g2.setFont(bigFont);
                } else {
                    g2.setFont(normalFont);
                }

                g2.drawString(String.format("%.0f", value), marginX - 40, y + 5);
            }

            for (int s = 0; s < dataSeries.length; s++) {
                g2.setColor(colors[s % colors.length]);
                double[] series = dataSeries[s];

                for (int i = 1; i < series.length-1; i++) {
                    double x1 = marginX + i * plotWidth / (nPoints - 1);
                    double x2 = marginX + (i + 1) * plotWidth / (nPoints - 1);

                    double y1 = height - marginY - series[i] * scale;     // scale
                    double y2 = height - marginY - series[i + 1] * scale; // scale

                    g2.setStroke(new BasicStroke(1));
                    g2.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
                    //g2.drawString(labels[i], i*10 + 10, 12);
                }
            }

            int legendX = width - 160;
            int legendY = 60;
            int boxSize = 15;

            for (int i = 0; i < dataSeries.length; i++) {
                g2.setColor(colors[i % colors.length]);
                g2.fillRect(legendX, legendY + i * 25, boxSize, boxSize);

                g2.setColor(Color.BLACK);
                g2.drawRect(legendX, legendY + i * 25, boxSize, boxSize);
                g2.drawString(labels[i], legendX + boxSize + 10, legendY + i * 25 + 12);
            }
        }
    }
}

