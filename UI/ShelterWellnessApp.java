package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ShelterWellnessApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Image[] animals;

    public static final Color BG_PRIMARY = new Color(255, 255, 255);
    public static final Color BG_SECONDARY = new Color(35, 30, 48);
    public static final Color TEXT_PRIMARY = new Color(140, 66, 45);
    public static final Color TEXT_SECONDARY = new Color(170, 120, 135);
    public static final Color TEXT_MUTED = new Color(180, 122, 130);
    public static final Color ACCENT_TEAL = new Color(103, 20, 65);
    public static final Color ACCENT_WARM = new Color(39, 0, 100);
    public static final Color ACCENT_ROSE = new Color(220, 20, 40);
    public static final Color ACCENT_PINK = new Color(232, 63, 86);
    public static final Color ACCENT_PURPLE = new Color(140, 80, 180);
    public static final Color ACCENT_CORAL = new Color(216, 90, 48);
    public static final Color CARD_BG = new Color(255, 255, 255, 10);
    public static final Color CARD_HOVER = new Color(255, 255, 255, 20);
    public static final Color CARD_BORDER = new Color(255, 255, 255, 20);

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_CARD_TITLE = new Font("SansSerif", Font.BOLD, 17);
    public static final Font FONT_CARD_DESC = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_CHAT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_DETAIL_TITLE = new Font("SansSerif", Font.BOLD, 22);

    static final String[][] DAILY_MUSIC = {
            // ...;
    };

    static final String[][] DAILY_RECIPES = {
            // ...;
    };


    public ShelterWellnessApp() {
            super("A small space for you");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(900, 720);
            setMinimumSize(new Dimension(820, 650));
            setLocationRelativeTo(null);
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, BG_PRIMARY, 0, getHeight(), new Color(226, 132, 112)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ─── Beautiful warm chat bubbles ───
    void addWarmBubble(JPanel area, String text, boolean fromUser, Color accent) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        if (fromUser) {
            row.add(Box.createHorizontalGlue());
        }

        JPanel bubble = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int arc = 20;

                if (fromUser) {
                    // User bubble: warm coral/pink gradient
                    g2.setPaint(new GradientPaint(0, 0, new Color(220, 140, 120), w, h, new Color(200, 120, 105)));
                    g2.fillRoundRect(0, 0, w, h, arc, arc);
                } else {
                    // Friend bubble: soft cream with warm border
                    g2.setColor(new Color(255, 255, 255));
                    g2.fillRoundRect(0, 0, w, h, arc, arc);
                    g2.setColor(new Color(230, 200, 185, 120));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
                }
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                return new Dimension(Math.min(d.width, 280), d.height);
            }

            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        bubble.setLayout(new BorderLayout());
        bubble.setOpaque(false);

        JLabel label = new JLabel("<html><div style='padding:10px 14px;width:220px;font-size:12px;color:"
                + (fromUser ? "#FFFFFF" : "#6B4A3C")
                + ";'>" + text + "</div></html>");
        label.setOpaque(false);
        bubble.add(label);

        row.add(bubble);

        if (!fromUser) {
            row.add(Box.createHorizontalGlue());
        }

        area.add(row);
        area.add(Box.createVerticalStrut(10));
    }

    void navigate(String s) {
        cardLayout.show(cardPanel, s);
    }

    public Image[] getAnimals() {
        return animals;
    }
}