import javax.swing.*;
import java.awt.*;

public class UIclass extends JFrame {

    private GraphPanel graphPanel;

    public UIclass() {
        setTitle("Practica 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout());

        // === LEFT PANEL: A / B Selection ===
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JRadioButton optionA = new JRadioButton("A", true);
        JRadioButton optionB = new JRadioButton("B");

        ButtonGroup group = new ButtonGroup();
        group.add(optionA);
        group.add(optionB);

        leftPanel.add(optionA);
        leftPanel.add(optionB);

        add(leftPanel, BorderLayout.WEST);

        // === CENTER PANEL: 5x5 Grid + Translucent Red Triangle ===
        JPanel centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int width = getWidth();
                int height = getHeight();

                int rows = 5;
                int cols = 5;
                int cellW = width / cols;
                int cellH = height / rows;

                // Fill each row with a different color
                for (int i = 0; i < rows; i++) {
                    g2.setColor(Color.getHSBColor(i / (float) rows, 1f, 1f)); // different hue per row
                    g2.fillRect(0, i * cellH, width, cellH);
                }

                // Optional: draw cell borders
                g2.setColor(Color.BLACK);
                for (int i = 0; i <= rows; i++) {
                    g2.drawLine(0, i * cellH, width, i * cellH); // horizontal
                }
                for (int j = 0; j <= cols; j++) {
                    g2.drawLine(j * cellW, 0, j * cellW, height); // vertical
                }

                // Translucent red triangle on top
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.setColor(Color.RED);
                int[] xPoints = {width / 2, width / 4, 3 * width / 4};
                int[] yPoints = {height / 4, 3 * height / 4, 3 * height / 4};
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            }
        };
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

        private int[][] dataSeries;   // Each row = one series
        private Color[] colors;       // Colors for each series

        public GraphPanel() {
            setPreferredSize(new Dimension(300, 600));
            dataSeries = new int[0][0];  // initially empty
            colors = new Color[]{Color.BLUE, Color.GREEN, Color.MAGENTA};
        }

        public void setData(int[] series1, int[] series2, int[] series3) {
            dataSeries = new int[][] {series1, series2, series3};
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

            for (int s = 0; s < dataSeries.length; s++) {
                g2.setColor(colors[s % colors.length]);
                int[] series = dataSeries[s];

                for (int i = 0; i < series.length - 1; i++) {
                    int x1 = marginX + i * plotWidth / (nPoints - 1);
                    int x2 = marginX + (i + 1) * plotWidth / (nPoints - 1);

                    int y1 = height - marginY - series[i] * 4;     // scale
                    int y2 = height - marginY - series[i + 1] * 4; // scale

                    g2.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }
}

