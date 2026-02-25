import javax.swing.*;
import java.awt.*;

public class UIclass extends JFrame {

    private GraphPanel graphPanel;
    JRadioButton optionBinary;
    JRadioButton optionReal;
    public JButton simulateButton;

    boolean[][] mapMatrix;
    boolean[][] cameraMatrix = new boolean[5][5];       // true = gray, false = white (only for white squares)
    public void setMap(boolean[][] map)
    {
        mapMatrix = map;
    }
    public void setCameras(boolean c) {


    }
    public boolean isBinary()
    {
        return optionBinary.isSelected();
    }


    public UIclass() {
        setTitle("Practica 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        simulateButton = new JButton("Evolve");


        // === LEFT PANEL: A / B Selection ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        optionBinary = new JRadioButton("Binary", true);
        optionReal = new JRadioButton("Real");

        ButtonGroup group = new ButtonGroup();
        group.add(optionBinary);
        group.add(optionReal);

        leftPanel.add(optionBinary);
        leftPanel.add(optionReal);
        leftPanel.add(simulateButton);

        leftPanel.setPreferredSize(new Dimension(200, 600));

        add(leftPanel, BorderLayout.WEST);

        // === CENTER PANEL

        JPanel centerPanel = new JPanel() {
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
                            g2.setColor(Color.WHITE);
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
            }
        };
        centerPanel.setPreferredSize(new Dimension(50, 50));
        add(centerPanel, BorderLayout.CENTER);

        // === RIGHT PANEL: Graph with Two Points ===
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.EAST);

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
        double maxValue;

        public GraphPanel() {
            setPreferredSize(new Dimension(500, 600));
            dataSeries = new double[0][0];  // initially empty
            colors = new Color[]{Color.BLUE, Color.GREEN, Color.MAGENTA};
        }

        public void setData(double[] series1, double[] series2, double[] series3) {
            dataSeries = new double[][] {series1, series2, series3};

            maxValue = Double.MIN_VALUE;
            for (double[] series : dataSeries) {
                for (double v : series) {
                    if (v > maxValue) {
                        maxValue = v;
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

            for (int s = 0; s < dataSeries.length; s++) {
                g2.setColor(colors[s % colors.length]);
                double[] series = dataSeries[s];

                for (int i = 0; i < series.length-1; i++) {
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

