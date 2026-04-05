package UI;

import javax.swing.JPanel;


import UI.ShelterWellnessApp.GradientPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class DetailPanel extends JPanel {
    private ShelterWellnessApp app;

    public DetailPanel(ShelterWellnessApp app, String title, String subtitle, String body,
            String icon, Color accent, String label, String actionText) {
        this.app = app;
        setLayout(new BorderLayout());
        add(createDetailScreen(title, subtitle, body, icon, accent, label, actionText), BorderLayout.CENTER);
    }

    private JPanel createDetailScreen(String title, String subtitle, String body,
            String icon, Color accent, String label, String actionText) {
        return new GradientPanel() {
            int hov = -1;

            final Rectangle backBtn = new Rectangle();
            final Rectangle yesBtn = new Rectangle();
            final Rectangle noBtn = new Rectangle();
            final Rectangle actBtn = new Rectangle();

            {
                MouseAdapter ma = new MouseAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int p = hov;
                        hov = backBtn.contains(e.getPoint()) ? 0
                                : yesBtn.contains(e.getPoint()) ? 1
                                : noBtn.contains(e.getPoint()) ? 2
                                : actBtn.contains(e.getPoint()) ? 3
                                : -1;

                        setCursor(hov >= 0
                                ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                                : Cursor.getDefaultCursor());

                        if (p != hov) repaint();
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (backBtn.contains(e.getPoint())) {
                            app.navigate("home");
                        } else if (yesBtn.contains(e.getPoint())) {
                            System.out.println("This works for the user.");
                        } else if (noBtn.contains(e.getPoint())) {
                            app.navigate("supportChoice");
                        } else if (actBtn.contains(e.getPoint())) {
                            if (label.equals("TODAY'S RECIPE")) {
                                app.nextRecipe();
                                app.navigate("recipedetail");
                            } else if (label.equals("TODAY'S MUSIC")) {
                                app.getMusicPlayer().replay();
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
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = ShelterWellnessApp.setup(g);

                int w = getWidth();
                int cx = w / 2;
                int cW = Math.min(380, w - 40);
                int cX = (w - cW) / 2;

                ShelterWellnessApp.drawBack(g2, 20, 20, backBtn);

                g2.setFont(ShelterWellnessApp.FONT_SMALL);
                g2.setColor(accent);
                ShelterWellnessApp.ctr(g2, label, cx, 72);

                g2.setFont(new Font("SansSerif", Font.PLAIN, 48));
                g2.setColor(accent);
                ShelterWellnessApp.ctr(g2, icon, cx, 120);

                g2.setFont(ShelterWellnessApp.FONT_DETAIL_TITLE);
                g2.setColor(ShelterWellnessApp.TEXT_PRIMARY);
                ShelterWellnessApp.ctr(g2, title, cx, 160);

                g2.setFont(ShelterWellnessApp.FONT_SUBTITLE);
                g2.setColor(ShelterWellnessApp.TEXT_SECONDARY);
                ShelterWellnessApp.ctr(g2, subtitle, cx, 184);

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

                int bY = 285, bH = 210;
                g2.setColor(ShelterWellnessApp.CARD_BG);
                g2.fillRoundRect(cX, bY, cW, bH, 16, 16);
                g2.setColor(ShelterWellnessApp.CARD_BORDER);
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawRoundRect(cX, bY, cW, bH, 16, 16);

                g2.setFont(ShelterWellnessApp.FONT_BODY);
                g2.setColor(ShelterWellnessApp.TEXT_SECONDARY);
                int tY = bY + 28;
                for (String line : body.split("\n")) {
                    g2.drawString(line, cX + 20, tY);
                    tY += 22;
                }

                String hint = "Want something different?";
                int smallBtnW = 260;
                int smallBtnH = 38;
                int smallBtnX = cx - smallBtnW / 2;
                int smallBtnY = bY + bH + 20;

                noBtn.setBounds(smallBtnX, smallBtnY, smallBtnW, smallBtnH);

                g2.setColor(hov == 2
                        ? ShelterWellnessApp.alphaColor(ShelterWellnessApp.ACCENT_PURPLE, 28)
                        : ShelterWellnessApp.CARD_BG);
                g2.fillRoundRect(smallBtnX, smallBtnY, smallBtnW, smallBtnH, 18, 18);

                g2.setColor(ShelterWellnessApp.alphaColor(
                        ShelterWellnessApp.ACCENT_PURPLE, hov == 2 ? 80 : 45));
                g2.drawRoundRect(smallBtnX, smallBtnY, smallBtnW, smallBtnH, 18, 18);

                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.setColor(hov == 2
                        ? ShelterWellnessApp.ACCENT_PURPLE
                        : ShelterWellnessApp.TEXT_SECONDARY);
                ShelterWellnessApp.ctr(g2, hint, cx, smallBtnY + 24);
                g2.dispose();
            }
        };
    }
}