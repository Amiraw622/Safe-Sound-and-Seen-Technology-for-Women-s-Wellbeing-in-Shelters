package UI;

import UI.ShelterWellnessApp.GradientPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class HomePanel extends JPanel {
    private ShelterWellnessApp app;

    public HomePanel(ShelterWellnessApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        add(createHomeScreen(), BorderLayout.CENTER);
    }

    private JPanel createHomeScreen() {
        return new GradientPanel() {
            int hov = -1;
            final Rectangle[] cards = { new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle() };
            final Rectangle helpLink = new Rectangle(); // separate from cards!
            {
                addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        int prev = hov;
                        hov = -1;
                        for (int i = 0; i < 4; i++)
                            if (cards[i].contains(e.getPoint())) {
                                hov = i;
                                break;
                            }
                        if (helpLink.contains(e.getPoint()))
                            hov = 99;
                        setCursor(
                                hov >= 0 ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
                        if (prev != hov)
                            repaint();
                    }
                });
                addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (cards[0].contains(e.getPoint()))
                            app.navigate("freechat");
                        else if (cards[1].contains(e.getPoint()))
                            app.navigate("musicdetail");
                        else if (cards[2].contains(e.getPoint()))
                            app.navigate("recipedetail");
                        else if (cards[3].contains(e.getPoint()))
                            app.navigate("supportChoice");
                        else if (helpLink.contains(e.getPoint()))
                            app.navigate("help");
                    }

                    public void mouseExited(MouseEvent e) {
                        if (hov != -1) {
                            hov = -1;
                            repaint();
                        }
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                 super.paintComponent(g);
                Graphics2D g2 = ShelterWellnessApp.setup(g);
                int w = getWidth(), cx = w / 2;
                app.drawAnimal(g2, cx, 50, 0.95f, true);
                g2.setFont(ShelterWellnessApp.FONT_TITLE);
                g2.setColor(new Color(140, 76, 45));
                ShelterWellnessApp.ctr(g2, "Here's something gentle for you today \uD83D\uDC9B", cx, 218);
                g2.setFont(ShelterWellnessApp.FONT_SUBTITLE);
                g2.setColor(ShelterWellnessApp.TEXT_MUTED);

                int cW = Math.min(340, w - 48), cX = (w - cW) / 2, cH = 70, gap = 18, y = 240;

                ShelterWellnessApp.drawCard(g2, cX, y, cW, cH, hov == 0, ShelterWellnessApp.ACCENT_TEAL);
                cards[0].setBounds(cX, y, cW, cH);
                ShelterWellnessApp.cardIcon(g2, cX, y, "\u2661", ShelterWellnessApp.ACCENT_TEAL, hov == 0);
                ShelterWellnessApp.cardText(g2, cX, y, "Talk to me",
                        "I'm always here for you", ShelterWellnessApp.ACCENT_TEAL, hov == 0);

                y += cH + gap;
                ShelterWellnessApp.drawCard(g2, cX, y, cW, cH, hov == 1, ShelterWellnessApp.ACCENT_WARM);
                cards[1].setBounds(cX, y, cW, cH);
                ShelterWellnessApp.cardIcon(g2, cX, y, "\u266B", ShelterWellnessApp.ACCENT_WARM, hov == 1);
                ShelterWellnessApp.cardText(g2, cX, y, "Listen", "Wanna listen to some music?",
                        ShelterWellnessApp.ACCENT_WARM, hov == 1);

                y += cH + gap;
                ShelterWellnessApp.drawCard(g2, cX, y, cW, cH, hov == 2, ShelterWellnessApp.ACCENT_ROSE);
                cards[2].setBounds(cX, y, cW, cH);
                ShelterWellnessApp.cardIcon(g2, cX, y, "\u2615", ShelterWellnessApp.ACCENT_ROSE, hov == 2);
                ShelterWellnessApp.cardText(g2, cX, y, "Cook", "Find something to cook?", ShelterWellnessApp.ACCENT_ROSE, hov == 2);

                y += cH + gap;
                ShelterWellnessApp.drawCard(g2, cX, y, cW, cH, hov == 3, ShelterWellnessApp.ACCENT_PURPLE);
                cards[3].setBounds(cX, y, cW, cH);
                ShelterWellnessApp.cardIcon(g2, cX, y, "\u2728", ShelterWellnessApp.ACCENT_PURPLE, hov == 3);
                ShelterWellnessApp.cardText(g2, cX, y, "Do something", "Find what feels right", ShelterWellnessApp.ACCENT_PURPLE, hov == 3);

                // Help & Support link — uses helpLink, NOT cards[3]
                int linkY = getHeight() - 50;
                String helpText = "Help & Support";
                g2.setFont(ShelterWellnessApp.FONT_BODY);
                g2.setColor(hov == 99 ? new Color(250, 50, 70) : ShelterWellnessApp.ACCENT_PINK);

                FontMetrics fm = g2.getFontMetrics();
                int textW = fm.stringWidth(helpText);
                int linkX = cx - textW / 2;

                g2.drawString(helpText, linkX, linkY);
                g2.drawLine(linkX, linkY + 2, linkX + textW, linkY + 2);

                helpLink.setBounds(linkX, linkY - 16, textW, 24);
                g2.dispose();
            }
        };
    }
}