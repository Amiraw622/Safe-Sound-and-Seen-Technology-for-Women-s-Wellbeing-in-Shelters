package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ShelterWellnessApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Image[] animals;
    private MusicPlayer musicPlayer;
    private RecipeLoader recipeLoader;
    private String[] currentRecipe; // [name, subtitle, body]
    private List<String[]> matchedRecipes;  // filtered results
    private int matchedIndex = 0;

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
        { "bathroom chill background music",
          "Soft background music for a calm moment",
          "A gentle chill track for quiet rest." },
        { "morning garden acoustic chill",
          "Light acoustic music for a peaceful mood",
          "A soft acoustic piece that feels fresh and gentle." },
        { "sakura",
          "A calm reflective track",
          "A peaceful song with a soft and soothing atmosphere." },
        { "easy lifestyle",
          "Comfortable background music for daily relaxation",
          "A light and easy track for resting your mind." },
        { "spring background",
          "Warm spring feeling with soft energy",
          "A peaceful background song with a fresh mood." },
        { "spring in bloom",
          "Gentle spring-inspired music",
          "A soft relaxing piece that feels bright and comforting." }
    };

    private int todayMusic;

    public ShelterWellnessApp() {
        super("A small space for you");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 720);
        setMinimumSize(new Dimension(820, 650));
        setLocationRelativeTo(null);

        animals = new Image[] {
                new ImageIcon("public/images/animal/drink.png").getImage(),
                new ImageIcon("public/images/animal/eat.PNG").getImage(),
                new ImageIcon("public/images/animal/first.PNG").getImage(),
                new ImageIcon("public/images/animal/move.PNG").getImage(),
                new ImageIcon("public/images/animal/music.PNG").getImage(),
                new ImageIcon("public/images/animal/rest.PNG").getImage(),
                new ImageIcon("public/images/animal/shy.PNG").getImage(),
                new ImageIcon("public/images/animal/stretch.png").getImage(),
                new ImageIcon("public/images/animal/talk.PNG").getImage(),
                new ImageIcon("public/images/animal/think.PNG").getImage()
        };

        String[] songs = {
                "public/Songs/chill_background-bathroom-chill-background-music-14977.wav",
                "public/Songs/folk_acoustic-morning-garden-acoustic-chill-15013.wav",
                "public/Songs/jon_nathan_21-sakura-117030.wav",
                "public/Songs/music_for_video-easy-lifestyle-137766.wav",
                "public/Songs/soundgallerybydmitrytaras-spring-background-500175.wav",
                "public/Songs/valeriigomeniuk-spring-in-bloom-495309.wav"
        };

        musicPlayer = new MusicPlayer(songs);

        // Load recipe dataset
        recipeLoader = new RecipeLoader("public/data/all_recipies.csv");
        currentRecipe = recipeLoader.getRandom();

        Random rand = new Random();
        todayMusic = rand.nextInt(DAILY_MUSIC.length);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_PRIMARY);

        cardPanel.add(new HomePanel(this), "home");                          // 0
        cardPanel.add(new DetailPanel(                                       // 1 - music
                this,
                DAILY_MUSIC[todayMusic][0],
                DAILY_MUSIC[todayMusic][1],
                DAILY_MUSIC[todayMusic][2],
                "\u266B", ACCENT_WARM, "TODAY'S MUSIC",
                "\u25B6  Play now"), "musicdetail");

        cardPanel.add(new DetailPanel(                                       // 2 - recipe
                this,
                currentRecipe[0],
                currentRecipe[1],
                currentRecipe[2],
                "\u2615", ACCENT_ROSE, "TODAY'S RECIPE",
                "\u2665  Show me another one"), "recipedetail");

        cardPanel.add(new IngredientPanel(this), "ingredients");             // 3 - NEW
        cardPanel.add(new SupportChoicePanel(this), "supportChoice");
        cardPanel.add(new HelpResourcesPanel(this), "help");
        cardPanel.add(new ChatPanel(this, "Talk", ACCENT_CORAL, true, "home"), "talk");
        cardPanel.add(new ChatPanel(this, "Chat", ACCENT_TEAL, false, "home"), "freechat");
        cardPanel.add(createStretchScreen(), "stretch");
        cardPanel.add(createBreathScreen(), "breath");
        cardPanel.add(createWaterScreen(), "water");
        cardPanel.add(createMoveScreen(), "move");

        add(cardPanel);
        cardLayout.show(cardPanel, "home");
    }

    // ── Called from HomePanel when "Cook" card is clicked ──
    public void openCookFlow() {
        navigate("ingredients");
    }

    // ── Called from IngredientPanel with selected ingredients ──
    public void findRecipesByIngredients(List<String> ingredients) {
        matchedRecipes = recipeLoader.findByIngredients(ingredients);
        matchedIndex = 0;

        if (matchedRecipes.isEmpty()) {
            // No match — fall back to random
            currentRecipe = recipeLoader.getRandom();
        } else {
            currentRecipe = matchedRecipes.get(0);
        }

        refreshRecipeDetail();
        navigate("recipedetail");
    }

    // ── Called from IngredientPanel "Just surprise me" ──
    public void showRandomRecipe() {
        matchedRecipes = null;
        matchedIndex = 0;
        currentRecipe = recipeLoader.getRandom();
        refreshRecipeDetail();
        navigate("recipedetail");
    }

    // ── Called when "Show me another one" is clicked ──
    public void nextRecipe() {
        if (matchedRecipes != null && !matchedRecipes.isEmpty()) {
            matchedIndex = (matchedIndex + 1) % matchedRecipes.size();
            currentRecipe = matchedRecipes.get(matchedIndex);
        } else {
            currentRecipe = recipeLoader.getRandom();
        }
        refreshRecipeDetail();
    }

    // ═══════════════════════════════════════════════════════════
    // Everything below is unchanged from your existing code
    // ═══════════════════════════════════════════════════════════

    private JPanel createActionScreen(String title, String subtitle, String body, Color accent, String backTo) {
        return new GradientPanel() {
            int hov = -1;
            final Rectangle backBtn = new Rectangle();
            final Rectangle doneBtn = new Rectangle();

            {
                MouseAdapter ma = new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        hov = backBtn.contains(e.getPoint()) ? 0
                                : doneBtn.contains(e.getPoint()) ? 1 : -1;
                        setCursor(hov >= 0 ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
                        repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (backBtn.contains(e.getPoint())) navigate(backTo);
                        else if (doneBtn.contains(e.getPoint())) navigate("home");
                    }

                    @Override
                    public void mouseExited(MouseEvent e) { hov = -1; repaint(); }
                };
                addMouseListener(ma);
                addMouseMotionListener(ma);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = setup(g);
                int w = getWidth(), cx = w / 2;
                int boxW = 430, boxH = 190, boxX = cx - boxW / 2, boxY = 220;

                drawBack(g2, 20, 20, backBtn);

                g2.setFont(FONT_DETAIL_TITLE);
                g2.setColor(TEXT_PRIMARY);
                ctr(g2, title, cx, 140);
                g2.setFont(FONT_SUBTITLE);
                g2.setColor(TEXT_MUTED);
                ctr(g2, subtitle, cx, 170);

                g2.setColor(CARD_BG);
                g2.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
                g2.setColor(alphaColor(accent, 70));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(boxX, boxY, boxW, boxH, 20, 20);

                g2.setFont(FONT_BODY);
                g2.setColor(TEXT_SECONDARY);
                int textY = boxY + 36;
                for (String line : body.split("\n")) {
                    g2.drawString(line, boxX + 24, textY);
                    textY += 28;
                }

                int btnW = 180, btnH = 42, btnX = cx - btnW / 2, btnY = boxY + boxH + 24;
                doneBtn.setBounds(btnX, btnY, btnW, btnH);
                g2.setColor(hov == 1 ? alphaColor(accent, 28) : CARD_BG);
                g2.fillRoundRect(btnX, btnY, btnW, btnH, 18, 18);
                g2.setColor(alphaColor(accent, 80));
                g2.drawRoundRect(btnX, btnY, btnW, btnH, 18, 18);
                g2.setFont(FONT_BUTTON);
                g2.setColor(accent);
                ctr(g2, "Done", cx, btnY + 27);
                g2.dispose();
            }
        };
    }

    public void openMusicDetail() {
        todayMusic = musicPlayer.getCurrentIndex();
        refreshMusicDetail();
        if (!musicPlayer.isPlaying()) musicPlayer.playCurrent();
        navigate("musicdetail");
    }

    private JPanel createStretchScreen() {
        return createActionScreen("Stretch with me", "Just one quiet minute",
                "1. Roll your shoulders slowly.\n2. Reach your arms up gently.\n3. Turn your neck left and right.\n4. Let your body loosen a little.",
                ACCENT_PURPLE, "supportChoice");
    }

    private JPanel createBreathScreen() {
        return createActionScreen("Take a slow breath", "We can go gently",
                "1. Breathe in for 4.\n2. Hold for 2.\n3. Breathe out for 6.\n4. Repeat a few times.",
                ACCENT_TEAL, "supportChoice");
    }

    private JPanel createWaterScreen() {
        return createActionScreen("A sip of water", "A tiny reset is enough",
                "1. Take one small sip.\n2. Sit for a moment.\n3. Notice your breathing.\n4. You do not need to rush.",
                ACCENT_WARM, "supportChoice");
    }

    private JPanel createMoveScreen() {
        return createActionScreen("Change your spot", "A small change can help",
                "1. Stand up slowly.\n2. Take a few steps.\n3. Sit somewhere that feels better.\n4. Let your body settle there.",
                ACCENT_ROSE, "supportChoice");
    }

    void addWarmBubble(JPanel area, String text, boolean fromUser, Color accent) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        if (fromUser) row.add(Box.createHorizontalGlue());

        JPanel bubble = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight(), arc = 20;
                if (fromUser) {
                    g2.setPaint(new GradientPaint(0, 0, new Color(220, 140, 120), w, h, new Color(200, 120, 105)));
                    g2.fillRoundRect(0, 0, w, h, arc, arc);
                } else {
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
            public Dimension getMaximumSize() { return getPreferredSize(); }
        };
        bubble.setLayout(new BorderLayout());
        bubble.setOpaque(false);

        JLabel label = new JLabel("<html><div style='padding:10px 14px;width:220px;font-size:12px;color:"
                + (fromUser ? "#FFFFFF" : "#6B4A3C") + ";'>" + text + "</div></html>");
        label.setOpaque(false);
        bubble.add(label);
        row.add(bubble);
        if (!fromUser) row.add(Box.createHorizontalGlue());

        area.add(row);
        area.add(Box.createVerticalStrut(10));
    }

    void refreshRecipeDetail() {
        cardPanel.remove(2);
        cardPanel.add(new DetailPanel(this,
                currentRecipe[0], currentRecipe[1], currentRecipe[2],
                "\u2615", ACCENT_ROSE, "TODAY'S RECIPE",
                "\u2665  Show me another one"), "recipedetail", 2);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    void refreshMusicDetail() {
        cardPanel.remove(1);
        cardPanel.add(new DetailPanel(this,
                DAILY_MUSIC[todayMusic][0], DAILY_MUSIC[todayMusic][1], DAILY_MUSIC[todayMusic][2],
                "\u266B", ACCENT_WARM, "TODAY'S MUSIC",
                "\u25B6  Play now"), "musicdetail", 1);
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    String reply(String m, boolean s) {
        String l = m.toLowerCase();
        if (l.contains("sad") || l.contains("cry") || l.contains("hurt"))
            return "I hear you. It\u2019s okay to feel this way. I\u2019m right here with you.";
        if (l.contains("scared") || l.contains("afraid"))
            return "It\u2019s okay to feel scared. You are safe here.";
        if (l.contains("angry") || l.contains("mad"))
            return "Your anger is valid \u2014 you deserve better.";
        if (l.contains("alone") || l.contains("lonely"))
            return "You are not alone. I\u2019m here, and people care about you.";
        if (l.contains("tired") || l.contains("exhausted"))
            return "Rest is so important. Be gentle with yourself.";
        if (l.contains("help") || l.contains("support"))
            return "I\u2019m here for you. Would you like me to show you some support resources?";
        if (l.contains("thank"))
            return "You don\u2019t need to thank me. You deserve kindness.";
        if (l.contains("hello") || l.contains("hi") || l.contains("hey"))
            return "Hello! I\u2019m glad you\u2019re here. How are you feeling today?";
        if (l.contains("good") || l.contains("happy") || l.contains("great"))
            return "That\u2019s wonderful to hear! What made your day bright?";

        if (s) {
            String[] r = { "I\u2019m listening. Take your time.", "You are stronger than you realize.",
                    "It\u2019s okay to not be okay. I\u2019m here.", "Be gentle with yourself.", "You matter." };
            return r[(int) (Math.random() * r.length)];
        } else {
            String[] r = { "Tell me more, I\u2019m listening.", "I\u2019m here for you, whatever you need.",
                    "What else is on your mind?", "I\u2019m glad you\u2019re sharing with me.",
                    "Take your time. No rush." };
            return r[(int) (Math.random() * r.length)];
        }
    }

    void navigate(String s) { cardLayout.show(cardPanel, s); }

    static Graphics2D setup(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        return g2;
    }

    static void ctr(Graphics2D g, String t, int cx, int y) {
        FontMetrics f = g.getFontMetrics();
        g.drawString(t, cx - f.stringWidth(t) / 2, y);
    }

    static Color alphaColor(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
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
        if (animals == null || animals.length == 0) return;
        Image img = animals[2];
        int imgW = (int) (260 * s), imgH = (int) (200 * s);
        g.drawImage(img, cx - imgW / 2, ty - 40, imgW, imgH, this);
    }

    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, BG_PRIMARY, 0, getHeight(), new Color(226, 132, 112)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void nextMusic() {
        todayMusic = (todayMusic + 1) % DAILY_MUSIC.length;
        refreshMusicDetail();
    }

    public Image[] getAnimals() { return animals; }
    public MusicPlayer getMusicPlayer() { return musicPlayer; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new ShelterWellnessApp().setVisible(true);
        });
    }
}