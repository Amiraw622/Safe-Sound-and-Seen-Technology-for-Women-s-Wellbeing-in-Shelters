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
        { "Honey Lemon Tea", "Warm, soothing, and easy to make",
                    "Ingredients:\n  - 1 cup hot water\n  - 1 tbsp honey\n  - Juice of half a lemon\n\nStir honey into hot water.\nAdd lemon juice. Sip slowly." },
        { "Banana Oat Pancakes", "Simple, healthy, and comforting",
                    "Ingredients:\n  - 1 ripe banana\n  - 1/2 cup oats\n  - 1 egg\n  - Pinch of cinnamon\n\nMash banana, mix all together.\nCook small pancakes on low heat." },
        { "Veggie Soup", "Nourishing and warming for the soul",
                    "Ingredients:\n  - 2 carrots, 1 potato, 1 onion\n  - 4 cups broth\n  - Salt, pepper, herbs\n\nChop veggies, simmer in broth\n20 min until tender." },
        { "Fruit & Yogurt Bowl", "Fresh, light, and energizing",
                    "Ingredients:\n  - 1 cup yogurt\n  - Handful of berries\n  - 1 tbsp granola\n  - Drizzle of honey\n\nLayer yogurt, fruit, granola.\nDrizzle honey on top." },
        { "Cinnamon Toast", "Quick comfort with a warm aroma",
                    "Ingredients:\n  - 2 slices bread\n  - Butter\n  - Cinnamon + sugar\n\nToast bread, spread butter.\nSprinkle cinnamon sugar.\nEnjoy the warm aroma." },
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

    private int todayMusic, todayRecipe;

    public void nextRecipe() {
        todayRecipe = (todayRecipe + 1) % DAILY_RECIPES.length;
        // refreshRecipeDetail();
    }

    public void nextMusic() {
        todayMusic = (todayMusic + 1) % DAILY_MUSIC.length;
        // refreshMusicDetail();
    }

    static Color alphaColor(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    static void ctr(Graphics2D g, String t, int cx, int y) {
        FontMetrics f = g.getFontMetrics();
        g.drawString(t, cx - f.stringWidth(t) / 2, y);
    }

    void refreshRecipeDetail() {
        cardPanel.remove(2);
        cardPanel.add(new DetailPanel(this,
                DAILY_RECIPES[todayRecipe][0],
                DAILY_RECIPES[todayRecipe][1],
                DAILY_RECIPES[todayRecipe][2],
                "\u2615",
                ACCENT_ROSE,
                "TODAY'S RECIPE",
                "\u2665  Show me another one"), "recipedetail", 2);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    void refreshMusicDetail() {
        cardPanel.remove(1);
        cardPanel.add(new DetailPanel(this, 
                DAILY_MUSIC[todayMusic][0],
                DAILY_MUSIC[todayMusic][1],
                DAILY_MUSIC[todayMusic][2],
                "\u266B",
                ACCENT_WARM,
                "TODAY'S MUSIC",
                "\u25B6  Play now"), "musicdetail", 1);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new ShelterWellnessApp().setVisible(true);
        });
    }

    static Graphics2D setup(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        return g2;
    }

    static void drawBack(Graphics2D g, int x, int y, Rectangle b) {
        int bw = 80, bh = 30;
        b.setBounds(x, y, bw, bh);
        g.setColor(new Color(255, 255, 255, 12));
        g.fillRoundRect(x, y, bw, bh, 10, 10);
        g.setColor(CARD_BORDER);
        g.drawRoundRect(x, y, bw, bh, 10, 10);
        g.setFont(new Font("SansSerif", Font.BOLD, 12));
        g.setColor(TEXT_SECONDARY);
        g.drawString("\u2190 Back", x + 14, y + 20);
    }

    void drawAnimal(Graphics2D g, int cx, int ty, float s, boolean happy) {
        if (animals == null || animals.length == 0)
            return;

        Image img = animals[2];

        int imgW = (int) (260 * s);
        int imgH = (int) (200 * s);

        int x = cx - imgW / 2;
        int y = ty - 40;

        g.drawImage(img, x, y, imgW, imgH, this);
    }

    static void drawCard(Graphics2D g, int x, int y, int w, int h, boolean hov, Color ac) {
        if (hov) {
            g.setColor(alphaColor(ac, 10));
            g.fillRoundRect(x - 3, y - 3, w + 6, h + 6, 22, 22);
        }
        g.setColor(hov ? alphaColor(ac, 15) : CARD_BG);
        g.fillRoundRect(x, y, w, h, 18, 18);
        g.setColor(alphaColor(ac, hov ? 80 : 25));
        g.setStroke(new BasicStroke(1));
        g.drawRoundRect(x, y, w, h, 18, 18);
    }

    static void cardIcon(Graphics2D g, int x, int y, String ic, Color ac, boolean hov) {
        g.setFont(new Font("SansSerif", Font.PLAIN, 28));
        g.setColor(ac);
        g.drawString(ic, x + 22, y + 48);
    }

    static void cardText(Graphics2D g, int x, int y, String t, String h, Color ac, boolean hov) {
        g.setFont(FONT_CARD_TITLE);
        g.setColor(hov ? ac : TEXT_PRIMARY);
        g.drawString(t, x + 64, y + 33);

        g.setFont(FONT_SMALL);
        g.setColor(new Color(160, 120, 130));
        g.drawString(h, x + 64, y + 53);
    }
}