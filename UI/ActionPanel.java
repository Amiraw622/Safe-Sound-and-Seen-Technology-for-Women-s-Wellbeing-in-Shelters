package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

import static UI.ShelterWellnessApp.*;

/**
 * An interactive, animated wellness activity panel.
 * Guides the user step-by-step with gentle animations.
 */
public class ActionPanel extends JPanel {

    private final ShelterWellnessApp app;
    private final String title;
    private final String subtitle;
    private final String[] steps;
    private final Color accent;
    private final String backTo;
    private final String emoji;
    private final Image animalImage;

    // State
    private int currentStep = -1;         // -1 = intro, 0..n = steps, steps.length = done
    private float stepFade = 0f;          // 0..1 fade-in for current step text
    private float breathPhase = 0f;       // 0..2π cycling animation
    private float progressAnim = 0f;      // animated progress 0..1

    // Hit areas
    private final Rectangle backBtn = new Rectangle();
    private final Rectangle actionBtn = new Rectangle();
    private int hov = -1;

    // Timers
    private Timer animTimer;
    private Timer fadeTimer;

    public ActionPanel(ShelterWellnessApp app, String title, String subtitle,
                       String[] steps, Color accent, String backTo,
                       String emoji, Image animalImage) {
        this.app = app;
        this.title = title;
        this.subtitle = subtitle;
        this.steps = steps;
        this.accent = accent;
        this.backTo = backTo;
        this.emoji = emoji;
        this.animalImage = animalImage;

        setOpaque(false);

        // Breathing/pulsing animation (~30fps)
        animTimer = new Timer(33, e -> {
            breathPhase += 0.04f;
            if (breathPhase > Math.PI * 2) breathPhase -= (float)(Math.PI * 2);

            // Smoothly animate progress bar
            float target = (currentStep < 0) ? 0f : (float)(currentStep + 1) / steps.length;
            if (currentStep >= steps.length) target = 1f;
            progressAnim += (target - progressAnim) * 0.08f;

            repaint();
        });
        animTimer.start();

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int prev = hov;
                Point pt = e.getPoint();
                if (backBtn.contains(pt)) hov = 0;
                else if (actionBtn.contains(pt)) hov = 1;
                else hov = -1;

                setCursor(hov >= 0 ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
                if (prev != hov) repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Point pt = e.getPoint();
                if (backBtn.contains(pt)) {
                    resetState();
                    app.navigate(backTo);
                    return;
                }
                if (actionBtn.contains(pt)) {
                    advanceStep();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hov = -1;
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private void advanceStep() {
        currentStep++;
        stepFade = 0f;

        // Fade in the new step text
        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();
        fadeTimer = new Timer(20, e -> {
            stepFade += 0.06f;
            if (stepFade >= 1f) {
                stepFade = 1f;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        fadeTimer.start();
    }

    private void resetState() {
        currentStep = -1;
        stepFade = 0f;
        progressAnim = 0f;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        int w = getWidth(), h = getHeight();
        int cx = w / 2;

        // ── Background gradient ──
        g2.setPaint(new GradientPaint(0, 0, BG_PRIMARY, 0, h, new Color(226, 132, 112)));
        g2.fillRect(0, 0, w, h);

        // ── Soft ambient circles (breathing animation) ──
        float pulse = (float)(Math.sin(breathPhase) * 0.5 + 0.5); // 0..1
        int circleR = (int)(120 + pulse * 30);
        g2.setColor(alphaColor(accent, (int)(15 + pulse * 10)));
        g2.fillOval(cx - circleR, 200 - circleR, circleR * 2, circleR * 2);

        int circleR2 = (int)(80 + pulse * 20);
        g2.setColor(alphaColor(accent, (int)(10 + pulse * 8)));
        g2.fillOval(cx - circleR2, 210 - circleR2, circleR2 * 2, circleR2 * 2);

        // ── Back button ──
        drawBack(g2, 20, 20, backBtn);

        // ── Animal image ──
        if (animalImage != null) {
            int imgH = 100;
            int imgW = (int)(animalImage.getWidth(this) * (imgH / (double) animalImage.getHeight(this)));
            int imgX = cx - imgW / 2;
            int imgY = 60;

            // Gentle bob animation
            float bob = (float)(Math.sin(breathPhase * 0.7) * 4);
            g2.drawImage(animalImage, imgX, (int)(imgY + bob), imgW, imgH, this);
        }

        // ── Title ──
        g2.setFont(FONT_DETAIL_TITLE);
        g2.setColor(TEXT_PRIMARY);
        ctr(g2, title, cx, 195);

        g2.setFont(FONT_SUBTITLE);
        g2.setColor(TEXT_MUTED);
        ctr(g2, subtitle, cx, 218);

        // ── Progress dots ──
        int dotY = 240;
        int dotR = 6;
        int dotGap = 24;
        int dotsW = steps.length * dotGap;
        int dotStartX = cx - dotsW / 2 + dotGap / 2;

        for (int i = 0; i < steps.length; i++) {
            int dx = dotStartX + i * dotGap;
            if (i < currentStep || currentStep >= steps.length) {
                // Completed
                g2.setColor(accent);
                g2.fillOval(dx - dotR, dotY - dotR, dotR * 2, dotR * 2);
                // Checkmark
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                g2.drawString("\u2713", dx - 3, dotY + 3);
            } else if (i == currentStep) {
                // Current - pulsing
                int pr = (int)(dotR + pulse * 3);
                g2.setColor(alphaColor(accent, 60));
                g2.fillOval(dx - pr - 2, dotY - pr - 2, (pr + 2) * 2, (pr + 2) * 2);
                g2.setColor(accent);
                g2.fillOval(dx - dotR, dotY - dotR, dotR * 2, dotR * 2);
            } else {
                // Upcoming
                g2.setColor(alphaColor(accent, 40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(dx - dotR, dotY - dotR, dotR * 2, dotR * 2);
            }
        }

        // ── Step content area ──
        int cardY = 268;
        int cardW = Math.min(420, w - 60);
        int cardX = cx - cardW / 2;
        int cardH = 160;

        // Card background
        g2.setColor(new Color(255, 255, 255, 30));
        g2.fillRoundRect(cardX, cardY, cardW, cardH, 24, 24);
        g2.setColor(alphaColor(accent, 35));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(cardX, cardY, cardW, cardH, 24, 24);

        if (currentStep < 0) {
            // ── INTRO state ──
            g2.setFont(new Font("SansSerif", Font.PLAIN, 40));
            g2.setColor(alphaColor(accent, 120));
            ctr(g2, emoji, cx, cardY + 60);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 15));
            g2.setColor(TEXT_SECONDARY);
            ctr(g2, "Ready when you are.", cx, cardY + 95);
            ctr(g2, "We'll go one step at a time.", cx, cardY + 118);

        } else if (currentStep < steps.length) {
            // ── ACTIVE STEP ──
            int alpha = (int)(stepFade * 255);

            // Step number
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(alphaColor(accent, (int)(stepFade * 180)));
            ctr(g2, "Step " + (currentStep + 1) + " of " + steps.length, cx, cardY + 35);

            // Step text - large and warm
            g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
            g2.setColor(new Color(TEXT_PRIMARY.getRed(), TEXT_PRIMARY.getGreen(), TEXT_PRIMARY.getBlue(), alpha));

            // Word-wrap step text
            FontMetrics fm = g2.getFontMetrics();
            List<String> lines = wrapText(steps[currentStep], fm, cardW - 60);
            int textStartY = cardY + 70;
            if (lines.size() == 1) textStartY = cardY + 80;
            for (int i = 0; i < lines.size(); i++) {
                ctr(g2, lines.get(i), cx, textStartY + i * 28);
            }

            // Gentle encouragement
            g2.setFont(FONT_SMALL);
            g2.setColor(alphaColor(TEXT_MUTED, (int)(stepFade * 150)));
            String encourage;
            if (currentStep == 0) encourage = "Take your time...";
            else if (currentStep == steps.length - 1) encourage = "Almost there...";
            else encourage = "You're doing great...";
            ctr(g2, encourage, cx, cardY + cardH - 20);

        } else {
            // ── DONE state ──
            g2.setFont(new Font("SansSerif", Font.PLAIN, 36));
            g2.setColor(alphaColor(accent, (int)(stepFade * 200)));
            ctr(g2, "\u2728", cx, cardY + 50);

            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.setColor(new Color(TEXT_PRIMARY.getRed(), TEXT_PRIMARY.getGreen(), TEXT_PRIMARY.getBlue(), (int)(stepFade * 255)));
            ctr(g2, "You did it!", cx, cardY + 85);

            g2.setFont(FONT_BODY);
            g2.setColor(alphaColor(TEXT_SECONDARY, (int)(stepFade * 200)));
            ctr(g2, "Even small steps matter. Be proud of yourself.", cx, cardY + 115);
        }

        // ── Action button ──
        int btnW = 220, btnH = 48;
        int btnX = cx - btnW / 2;
        int btnY = cardY + cardH + 24;

        actionBtn.setBounds(btnX, btnY, btnW, btnH);

        // Button style changes per state
        String btnText;
        Color btnColor;
        if (currentStep < 0) {
            btnText = "Let's begin \u2192";
            btnColor = accent;
        } else if (currentStep < steps.length - 1) {
            btnText = "Next step \u2192";
            btnColor = accent;
        } else if (currentStep == steps.length - 1) {
            btnText = "I did it! \u2713";
            btnColor = new Color(80, 160, 100);
        } else {
            btnText = "Back to home";
            btnColor = ACCENT_PURPLE;
        }

        // Button with gentle hover glow
        if (hov == 1) {
            g2.setColor(alphaColor(btnColor, 15));
            g2.fillRoundRect(btnX - 4, btnY - 4, btnW + 8, btnH + 8, 28, 28);
        }

        g2.setColor(hov == 1 ? alphaColor(btnColor, 40) : alphaColor(btnColor, 18));
        g2.fillRoundRect(btnX, btnY, btnW, btnH, 22, 22);
        g2.setColor(alphaColor(btnColor, hov == 1 ? 120 : 70));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(btnX, btnY, btnW, btnH, 22, 22);

        g2.setFont(FONT_BUTTON);
        g2.setColor(btnColor);
        ctr(g2, btnText, cx, btnY + 30);

        // ── Progress bar at bottom ──
        int barH = 4;
        int barY = h - barH - 20;
        int barW = Math.min(300, w - 100);
        int barX = cx - barW / 2;

        g2.setColor(alphaColor(accent, 25));
        g2.fillRoundRect(barX, barY, barW, barH, barH, barH);

        int fillW = (int)(barW * progressAnim);
        if (fillW > 0) {
            g2.setColor(alphaColor(accent, 120));
            g2.fillRoundRect(barX, barY, fillW, barH, barH, barH);
        }

        g2.dispose();
    }

    private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
        List<String> lines = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() == 0) {
                line.append(word);
            } else {
                String test = line + " " + word;
                if (fm.stringWidth(test) <= maxWidth) {
                    line.append(" ").append(word);
                } else {
                    lines.add(line.toString());
                    line = new StringBuilder(word);
                }
            }
        }
        if (line.length() > 0) lines.add(line.toString());
        return lines;
    }

    /** Clean up timers when panel is removed */
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animTimer != null) animTimer.stop();
        if (fadeTimer != null) fadeTimer.stop();
    }
}