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
    JComboBox MapChosen;
    JComboBox CrossType;
    JComboBox SelectionType;
    JFormattedTextField popSize;
    JFormattedTextField nGens;
    JFormattedTextField nDrons;
    JFormattedTextField nCamara;
    JFormattedTextField seed;
    JPanel centerPanel;
    JLabel bottomLabel;
    private Color[] droneColors = {Color.red, Color.blue, Color.green, Color.orange, Color.DARK_GRAY};


    boolean[][] mapMatrix;
    int[][] importancia;
    boolean[][] cameraMatrix = new boolean[20][20];
    ArrayList<Integer> cameras = new ArrayList<Integer>();
    List<List<Integer>> paths = null;


    public void setMap(boolean[][] map, int[][] _importancia)
    {
        mapMatrix = map;
        importancia = _importancia;
    }

    public void setCameras(ArrayList<Integer> cam) {
        cameras = cam;
        centerPanel.repaint();
    }
    public boolean elitism()
    {
        return elitismOn.isSelected();
    }
    public int mapChosen()
    {
        return MapChosen.getSelectedIndex() + 1;
    }
    public int seed() {return  (int) seed.getValue();}
    public int numDrons() { return (int) nDrons.getValue();}
    public int numCams() {return (int) nCamara.getValue();}
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
    public void setPaths(List<List<Integer>> paths) {
        this.paths = paths;
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

        JLabel labelmap = new JLabel("mapa:");
        String[] mapas = {"Mapa 1", "Mapa 2", "Mapa 3"};
        MapChosen = new JComboBox(mapas);
        JPanel mapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mapPanel.add(labelmap);
        MapChosen.setSelectedIndex(2);
        mapPanel.add(MapChosen);

        JLabel labelmut = new JLabel("mutación:");
        String[] mutations = {"INSERCION", "INTERCAMBIO", "INVERSION", "HEURISTICA", "CUSTOM"};
        MutationType = new JComboBox(mutations);
        JPanel mutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mutPanel.add(labelmut);
        mutPanel.add(MutationType);

        JLabel labelcross = new JLabel("cruce:");
        String[] cross = {"PMX", "OX", "OXPP", "CX", "CO", "ERX", "CUSTOM"};
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
        JLabel label3 = new JLabel("Nº drones:");
        JLabel label4 = new JLabel("Nº cámaras:");
        JLabel label5 = new JLabel("Semilla:");

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
        nGens.setValue(100);
        nDrons = new JFormattedTextField(formatter);
        nDrons.setColumns(4);
        nDrons.setValue(3);
        nCamara = new JFormattedTextField(formatter);
        nCamara.setColumns(4);
        nCamara.setValue(40);
        seed = new JFormattedTextField(formatter);
        seed.setColumns(4);
        seed.setValue(3000);

        JPanel popPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        popPanel.add(label1);
        popPanel.add(popSize);

        JPanel genPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genPanel.add(label2);
        genPanel.add(nGens);

        JPanel dronsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dronsPanel.add(label3);
        dronsPanel.add(nDrons);

        JPanel camerasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        camerasPanel.add(label4);
        camerasPanel.add(nCamara);

        JPanel seedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        seedPanel.add(label5);
        seedPanel.add(seed);


        leftPanel.add(buttPanel);
        leftPanel.add(popPanel);
        leftPanel.add(genPanel);
        leftPanel.add(dronsPanel);
        leftPanel.add(camerasPanel);
        leftPanel.add(seedPanel);
        leftPanel.add(mapPanel);
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
                        if (mapMatrix[i][j]) {
                            g2.setColor(Color.BLACK);
                        } else {
                            if(importancia[i][j] >= 15)
                            {
                                g2.setColor(Color.red);
                            }
                            else
                            {
                                if(importancia[i][j] >= 5)
                                {
                                    g2.setColor(Color.yellow);
                                }
                                else
                                {
                                    g2.setColor(Color.white);

                                }
                            }

                        }
                        g2.fillRect(j * cellW, i * cellH, cellW, cellH);
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

                //las camaras
                for(int i = 0; i < cameras.size(); i+=2)
                {
                    g2.setColor(new Color(1,1,1));
                    g2.fillOval((int)((cameras.get(i+1)) * cellW) + cellW/4, (int)((cameras.get(i)) * cellH)+ cellH/4, cellW/2, cellH/2);
                }

                if (paths != null) {
                    for (int p = 0; p < paths.size(); p++) {
                        List<Integer> path = paths.get(p);

                        // stable color per path
                        Color color = droneColors[p];
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

        bottomPanel.setPreferredSize(new Dimension(1250, 30));

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

