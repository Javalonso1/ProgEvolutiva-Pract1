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

    // --- Inner class: Graph panel ---
    class GraphPanel extends JPanel {

        private int y1 = 10;
        private int y2 = 50;

        public GraphPanel() {
            setPreferredSize(new Dimension(300, 600));
        }

        public void setData(int y1, int y2) {
            this.y1 = y1;
            this.y2 = y2;
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
            g2.drawLine(50, height - 50, width - 50, height - 50);
            g2.drawLine(50, height - 50, 50, 50);

            // Data points
            int x1 = 100;
            int x2 = 200;
            int scaledY1 = height - 50 - y1 * 4;
            int scaledY2 = height - 50 - y2 * 4;

            g2.setColor(Color.BLUE);
            g2.drawLine(x1, scaledY1, x2, scaledY2);
        }
    }
}

