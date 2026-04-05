package UI;

import javax.swing.JPanel;
import javax.swing.Timer;

import UI.ShelterWellnessApp.GradientPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DetailPanel extends JPanel {
    private ShelterWellnessApp app;

    public DetailPanel(ShelterWellnessApp app, String title, String subtitle, String body,
            String icon, Color accent, String label, String actionText) {
        this.app = app;
        setLayout(new BorderLayout());
        add(createDetailScreen(title, subtitle, body, icon, accent, label, actionText), BorderLayout.CENTER);
    }

    /**
     * Word-wrap a single line to fit within maxWidth pixels using the given FontMetrics.
     */
    private static List<String> wrapLine(String line, FontMetrics fm, int maxWidth) {
        List<String> wrapped = new ArrayList<>();
        if (line.isEmpty()) {
            wrapped.add("");
            return wrapped;
        }

        String[] words = line.split(" ");
        StringBuilder current = new StringBuilder();

        for (String word : words) {
            if (current.length() == 0) {
                current.append(word);
            } else {
                String test = current + " " + word;
                if (fm.stringWidth(test) <= maxWidth) {
                    current.append(" ").append(word);
                } else {
                    wrapped.add(current.toString());
                    current = new StringBuilder(word);
                }
            }
        }
        if (current.length() > 0) {
            wrapped.add(current.toString());
        }
        return wrapped;
    }

    /**
     * Word-wrap the entire body text (respecting \n) and cap at maxLines.
     */
    private static List<String> wrapBody(String body, FontMetrics fm, int maxWidth, int maxLines) {
        List<String> allLines = new ArrayList<>();
        for (String paragraph : body.split("\n")) {
            List<String> lines = wrapLine(paragraph, fm, maxWidth);
            allLines.addAll(lines);
        }
        // Cap total lines
        if (allLines.size() > maxLines) {
            allLines = new ArrayList<>(allLines.subList(0, maxLines));
            // Add ellipsis to last line
            String last = allLines.get(maxLines - 1);
            if (!last.endsWith("...")) {
                allLines.set(maxLines - 1, last + "...");
            }
        }
        return allLines;
    }

    private JPanel createDetailScreen(String title, String subtitle, String body,
            String icon, Color accent, String label, String actionText) {
        boolean isMusic = label.equals("TODAY'S MUSIC");

        return new GradientPanel() {
            int hov = -1;
            boolean musicStarted = false;

            final Rectangle backBtn = new Rectangle();
            final Rectangle noBtn = new Rectangle();
            final Rectangle actBtn = new Rectangle();
            final Rectangle pauseBtn = new Rectangle();
            final Rectangle nextBtn = new Rectangle();

            {
                MouseAdapter ma = new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int p = hov;
                        Point pt = e.getPoint();

                        if (isMusic && musicStarted && pauseBtn.width > 0 && pauseBtn.contains(pt)) {
                            hov = 4;
                        } else if (isMusic && musicStarted && nextBtn.width > 0 && nextBtn.contains(pt)) {
                            hov = 5;
                        } else if (backBtn.contains(pt)) {
                            hov = 0;
                        } else if (actBtn.contains(pt)) {
                            hov = 3;
                        } else if (noBtn.contains(pt)) {
                            hov = 2;
                        } else {
                            hov = -1;
                        }

                        setCursor(hov >= 0
                                ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                                : Cursor.getDefaultCursor());

                        if (p != hov) repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Point pt = e.getPoint();

                        if (backBtn.contains(pt)) {
                            app.navigate("home");
                            return;
                        }

                        if (noBtn.contains(pt)) {
                            app.navigate("supportChoice");
                            return;
                        }

                        if (isMusic && musicStarted && pauseBtn.width > 0 && pauseBtn.contains(pt)) {
                            MusicPlayer mp = app.getMusicPlayer();
                            if (mp.isPaused()) {
                                mp.resume();
                            } else {
                                mp.pause();
                            }
                            Timer t = new Timer(50, ev -> repaint());
                            t.setRepeats(false);
                            t.start();
                            return;
                        }

                        if (isMusic && musicStarted && nextBtn.width > 0 && nextBtn.contains(pt)) {
                            app.getMusicPlayer().nextSong();
                            app.nextMusic();
                            app.navigate("musicdetail");
                            return;
                        }

                        if (actBtn.contains(pt)) {
                            if (label.equals("TODAY'S RECIPE")) {
                                app.nextRecipe();
                                app.navigate("recipedetail");
                            } else if (isMusic) {
                                app.getMusicPlayer().replay();
                                musicStarted = true;
                                repaint();
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (hov != -1) {
                            hov = -1;
                            repaint();
                        }
                    }
                };

                addMouseListener(ma);
                addMouseMotionListener(ma);

                if (isMusic) {
                    MusicPlayer mp = app.getMusicPlayer();
                    if (mp.isPlaying() || mp.isPaused()) {
                        musicStarted = true;
                    }
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = ShelterWellnessApp.setup(g);

                int w = getWidth();
                int h = getHeight();
                int cx = w / 2;
                int cW = Math.min(380, w - 40);
                int cX = (w - cW) / 2;

                ShelterWellnessApp.drawBack(g2, 20, 20, backBtn);

                // ── Header section ──
                g2.setFont(ShelterWellnessApp.FONT_SMALL);
                g2.setColor(accent);
                ShelterWellnessApp.ctr(g2, label, cx, 72);

                g2.setFont(new Font("SansSerif", Font.PLAIN, 48));
                g2.setColor(accent);
                ShelterWellnessApp.ctr(g2, icon, cx, 120);

                // ── Title (auto-truncate if too long) ──
                g2.setFont(ShelterWellnessApp.FONT_DETAIL_TITLE);
                String displayTitle = title;
                FontMetrics titleFm = g2.getFontMetrics();
                int maxTitleW = cW - 20;
                if (titleFm.stringWidth(displayTitle) > maxTitleW) {
                    while (displayTitle.length() > 0 && titleFm.stringWidth(displayTitle + "...") > maxTitleW) {
                        displayTitle = displayTitle.substring(0, displayTitle.length() - 1);
                    }
                    displayTitle = displayTitle.trim() + "...";
                }
                g2.setColor(ShelterWellnessApp.TEXT_PRIMARY);
                ShelterWellnessApp.ctr(g2, displayTitle, cx, 160);

                g2.setFont(ShelterWellnessApp.FONT_SUBTITLE);
                g2.setColor(ShelterWellnessApp.TEXT_SECONDARY);
                ShelterWellnessApp.ctr(g2, subtitle, cx, 184);

                // ── Action button ──
                int abW = 280, abH = 44, abX = cx - abW / 2, abY = 215;
                actBtn.setBounds(abX, abY, abW, abH);
                g2.setColor(hov == 3
                        ? ShelterWellnessApp.alphaColor(accent, 35)
                        : ShelterWellnessApp.CARD_BG);
                g2.fillRoundRect(abX, abY, abW, abH, 20, 20);
                g2.setColor(ShelterWellnessApp.alphaColor(accent, 60));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(abX, abY, abW, abH, 20, 20);
                g2.setFont(ShelterWellnessApp.FONT_BUTTON);
                g2.setColor(accent);
                ShelterWellnessApp.ctr(g2, actionText, cx, abY + 29);

                // ── Music controls ──
                int controlY = abY + abH + 14;
                if (isMusic && musicStarted) {
                    int btnW = 130, btnH = 40, gap = 16;
                    int totalW = btnW * 2 + gap;
                    int startX = cx - totalW / 2;

                    boolean isPaused = app.getMusicPlayer().isPaused();
                    Color pauseColor = isPaused ? ShelterWellnessApp.ACCENT_CORAL : accent;

                    int pX = startX;
                    pauseBtn.setBounds(pX, controlY, btnW, btnH);
                    g2.setColor(hov == 4 ? ShelterWellnessApp.alphaColor(pauseColor, 35) : ShelterWellnessApp.CARD_BG);
                    g2.fillRoundRect(pX, controlY, btnW, btnH, 18, 18);
                    g2.setColor(ShelterWellnessApp.alphaColor(pauseColor, 70));
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(pX, controlY, btnW, btnH, 18, 18);
                    g2.setFont(ShelterWellnessApp.FONT_BUTTON);
                    g2.setColor(pauseColor);
                    ShelterWellnessApp.ctr(g2, isPaused ? "\u25B6  Resume" : "\u275A\u275A  Pause",
                            pX + btnW / 2, controlY + 26);

                    int nX = startX + btnW + gap;
                    nextBtn.setBounds(nX, controlY, btnW, btnH);
                    g2.setColor(hov == 5 ? ShelterWellnessApp.alphaColor(accent, 35) : ShelterWellnessApp.CARD_BG);
                    g2.fillRoundRect(nX, controlY, btnW, btnH, 18, 18);
                    g2.setColor(ShelterWellnessApp.alphaColor(accent, 70));
                    g2.drawRoundRect(nX, controlY, btnW, btnH, 18, 18);
                    g2.setFont(ShelterWellnessApp.FONT_BUTTON);
                    g2.setColor(accent);
                    ShelterWellnessApp.ctr(g2, "Next \u25B6\u25B6", nX + btnW / 2, controlY + 26);

                    controlY += btnH + 14;
                } else {
                    pauseBtn.setBounds(0, 0, 0, 0);
                    nextBtn.setBounds(0, 0, 0, 0);
                }

                // ── Body text box (auto-wrap + auto-height) ──
                int bY = controlY + 6;
                int textPadX = 20;
                int textPadTop = 20;
                int textPadBottom = 16;
                int lineHeight = 20;
                int textAreaW = cW - textPadX * 2;

                g2.setFont(ShelterWellnessApp.FONT_BODY);
                FontMetrics bodyFm = g2.getFontMetrics();

                // Calculate how many lines we can fit before the bottom of the panel
                // Leave room for the "Want something different?" button (38 + 20 margin) + bottom margin
                int reservedBottom = 38 + 20 + 30;
                int availableH = h - bY - reservedBottom;
                int maxBodyH = Math.max(80, availableH - textPadTop - textPadBottom);
                int maxLines = maxBodyH / lineHeight;

                List<String> wrappedLines = wrapBody(body, bodyFm, textAreaW, maxLines);
                int bH = textPadTop + wrappedLines.size() * lineHeight + textPadBottom;

                // Draw box
                g2.setColor(ShelterWellnessApp.CARD_BG);
                g2.fillRoundRect(cX, bY, cW, bH, 16, 16);
                g2.setColor(ShelterWellnessApp.CARD_BORDER);
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawRoundRect(cX, bY, cW, bH, 16, 16);

                // Draw wrapped text
                g2.setFont(ShelterWellnessApp.FONT_BODY);
                g2.setColor(ShelterWellnessApp.TEXT_SECONDARY);
                int tY = bY + textPadTop + bodyFm.getAscent();
                for (String line : wrappedLines) {
                    g2.drawString(line, cX + textPadX, tY);
                    tY += lineHeight;
                }

                // ── "Want something different?" button ──
                String hint = "Want something different?";
                int smallBtnW = 260, smallBtnH = 38;
                int smallBtnX = cx - smallBtnW / 2;
                int smallBtnY = bY + bH + 16;

                noBtn.setBounds(smallBtnX, smallBtnY, smallBtnW, smallBtnH);

                g2.setColor(hov == 2
                        ? ShelterWellnessApp.alphaColor(ShelterWellnessApp.ACCENT_PURPLE, 28)
                        : ShelterWellnessApp.CARD_BG);
                g2.fillRoundRect(smallBtnX, smallBtnY, smallBtnW, smallBtnH, 18, 18);
                g2.setColor(ShelterWellnessApp.alphaColor(
                        ShelterWellnessApp.ACCENT_PURPLE, hov == 2 ? 80 : 45));
                g2.drawRoundRect(smallBtnX, smallBtnY, smallBtnW, smallBtnH, 18, 18);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.setColor(hov == 2 ? ShelterWellnessApp.ACCENT_PURPLE : ShelterWellnessApp.TEXT_SECONDARY);
                ShelterWellnessApp.ctr(g2, hint, cx, smallBtnY + 24);

                g2.dispose();
            }
        };
    }
}