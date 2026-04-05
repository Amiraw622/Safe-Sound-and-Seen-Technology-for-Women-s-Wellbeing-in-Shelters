package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static UI.ShelterWellnessApp.*;

public class IngredientPanel extends JPanel {

    private final ShelterWellnessApp app;

    // Ingredient categories and items
    private static final String[][] CATEGORIES = {
        { "Protein",    "chicken", "beef", "pork", "fish", "salmon", "shrimp", "egg", "tofu", "bacon", "sausage", "turkey" },
        { "Dairy",      "cheese", "butter", "milk", "cream", "yogurt" },
        { "Grains",     "rice", "pasta", "bread", "flour", "oat", "noodle", "corn" },
        { "Vegetables", "potato", "tomato", "onion", "garlic", "broccoli", "carrot", "mushroom", "spinach", "pepper", "bean", "lettuce", "cucumber", "avocado" },
        { "Fruits",     "lemon", "apple", "banana" },
        { "Pantry",     "sugar", "honey", "chocolate" },
    };

    private final Set<String> selected = new LinkedHashSet<>();
    private final Map<String, Rectangle> checkBoxes = new LinkedHashMap<>();
    private final Rectangle findBtn = new Rectangle();
    private final Rectangle backBtn = new Rectangle();
    private final Rectangle skipBtn = new Rectangle();
    private int hoverIndex = -1; // index into flat list
    private boolean hoverFind = false;
    private boolean hoverBack = false;
    private boolean hoverSkip = false;

    private final List<String> flatList = new ArrayList<>();

    public IngredientPanel(ShelterWellnessApp app) {
        this.app = app;

        // Build flat list for hover tracking
        for (String[] cat : CATEGORIES) {
            for (int i = 1; i < cat.length; i++) {
                flatList.add(cat[i]);
            }
        }

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point pt = e.getPoint();
                int prevHover = hoverIndex;
                boolean prevFind = hoverFind;
                boolean prevBack = hoverBack;
                boolean prevSkip = hoverSkip;

                hoverIndex = -1;
                hoverFind = findBtn.contains(pt);
                hoverBack = backBtn.contains(pt);
                hoverSkip = skipBtn.contains(pt);

                int idx = 0;
                for (Map.Entry<String, Rectangle> entry : checkBoxes.entrySet()) {
                    if (entry.getValue().contains(pt)) {
                        hoverIndex = idx;
                        break;
                    }
                    idx++;
                }

                boolean anyHover = hoverFind || hoverBack || hoverSkip || hoverIndex >= 0;
                setCursor(anyHover
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());

                if (hoverIndex != prevHover || hoverFind != prevFind
                        || hoverBack != prevBack || hoverSkip != prevSkip) {
                    repaint();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Point pt = e.getPoint();

                if (backBtn.contains(pt)) {
                    app.navigate("home");
                    return;
                }

                if (skipBtn.contains(pt)) {
                    // Skip = random recipe
                    app.showRandomRecipe();
                    return;
                }

                if (findBtn.contains(pt) && !selected.isEmpty()) {
                    app.findRecipesByIngredients(new ArrayList<>(selected));
                    return;
                }

                // Check ingredient clicks
                for (Map.Entry<String, Rectangle> entry : checkBoxes.entrySet()) {
                    if (entry.getValue().contains(pt)) {
                        String ing = entry.getKey();
                        if (selected.contains(ing)) {
                            selected.remove(ing);
                        } else {
                            selected.add(ing);
                        }
                        repaint();
                        return;
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverIndex = -1;
                hoverFind = false;
                hoverBack = false;
                hoverSkip = false;
                repaint();
            }
        };

        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth(), h = getHeight();

        // Background gradient
        g2.setPaint(new GradientPaint(0, 0, BG_PRIMARY, 0, h, new Color(226, 132, 112)));
        g2.fillRect(0, 0, w, h);

        int cx = w / 2;

        // Back button
        drawBack(g2, 20, 20, backBtn);

        // Title
        g2.setFont(FONT_TITLE);
        g2.setColor(TEXT_PRIMARY);
        ctr(g2, "What do you have at home?", cx, 75);

        g2.setFont(FONT_SUBTITLE);
        g2.setColor(TEXT_MUTED);
        ctr(g2, "Pick what you have, and I'll find something you can make", cx, 100);

        // Draw ingredient grid by category
        int startY = 125;
        int colWidth = 110;
        int rowHeight = 32;
        int checkSize = 16;
        int maxCols = Math.max(3, (w - 120) / colWidth);
        int gridLeft = 60;

        checkBoxes.clear();
        int y = startY;

        for (String[] cat : CATEGORIES) {
            String categoryName = cat[0];

            // Category label
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(alphaColor(ACCENT_ROSE, 180));
            g2.drawString(categoryName, gridLeft, y + 14);
            y += 24;

            // Items in this category
            int col = 0;
            for (int i = 1; i < cat.length; i++) {
                String ing = cat[i];
                int x = gridLeft + col * colWidth;

                boolean isSelected = selected.contains(ing);
                boolean isHovered = false;

                // Find hover state
                int flatIdx = 0;
                for (String fi : flatList) {
                    if (fi.equals(ing)) break;
                    flatIdx++;
                }
                isHovered = (flatIdx == hoverIndex);

                Rectangle r = new Rectangle(x, y - 2, colWidth - 8, rowHeight - 4);
                checkBoxes.put(ing, r);

                // Checkbox background on hover
                if (isHovered) {
                    g2.setColor(alphaColor(ACCENT_PURPLE, 12));
                    g2.fillRoundRect(r.x - 4, r.y, r.width + 8, r.height, 8, 8);
                }

                // Checkbox
                int cbX = x, cbY = y + 2;
                if (isSelected) {
                    g2.setColor(ACCENT_ROSE);
                    g2.fillRoundRect(cbX, cbY, checkSize, checkSize, 4, 4);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                    g2.drawString("\u2713", cbX + 2, cbY + 13);
                } else {
                    g2.setColor(alphaColor(TEXT_MUTED, 100));
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(cbX, cbY, checkSize, checkSize, 4, 4);
                }

                // Label
                g2.setFont(FONT_SMALL);
                g2.setColor(isSelected ? TEXT_PRIMARY : TEXT_MUTED);
                String displayName = ing.substring(0, 1).toUpperCase() + ing.substring(1);
                g2.drawString(displayName, cbX + checkSize + 6, cbY + 13);

                col++;
                if (col >= maxCols) {
                    col = 0;
                    y += rowHeight;
                }
            }
            if (col > 0) y += rowHeight; // finish partial row
            y += 8; // gap between categories
        }

        // Selected count
        y += 4;
        if (!selected.isEmpty()) {
            g2.setFont(FONT_SMALL);
            g2.setColor(ACCENT_ROSE);
            ctr(g2, selected.size() + " ingredient" + (selected.size() > 1 ? "s" : "") + " selected", cx, y);
            y += 24;
        }

        // Find Recipes button
        int btnW = 240, btnH = 44;
        int btnX = cx - btnW / 2;
        findBtn.setBounds(btnX, y, btnW, btnH);

        boolean canFind = !selected.isEmpty();
        Color btnColor = canFind ? ACCENT_ROSE : TEXT_MUTED;

        g2.setColor(hoverFind && canFind ? alphaColor(btnColor, 35) : CARD_BG);
        g2.fillRoundRect(btnX, y, btnW, btnH, 20, 20);
        g2.setColor(alphaColor(btnColor, canFind ? 80 : 40));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(btnX, y, btnW, btnH, 20, 20);
        g2.setFont(FONT_BUTTON);
        g2.setColor(btnColor);
        ctr(g2, "\uD83C\uDF73  Find recipes for me!", cx, y + 28);

        y += btnH + 14;

        // Skip link
        int skipW = 180, skipH = 32;
        int skipX = cx - skipW / 2;
        skipBtn.setBounds(skipX, y, skipW, skipH);
        g2.setFont(FONT_SMALL);
        g2.setColor(hoverSkip ? ACCENT_PURPLE : TEXT_MUTED);
        ctr(g2, "Just surprise me \u2728", cx, y + 20);

        g2.dispose();
    }
}