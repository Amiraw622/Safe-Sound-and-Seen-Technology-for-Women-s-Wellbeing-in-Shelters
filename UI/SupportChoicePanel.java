package UI;

import java.awt.*;
import java.awt.event.*;
import static UI.ShelterWellnessApp.*;

import static UI.ShelterWellnessApp.*;

public class SupportChoicePanel extends ShelterWellnessApp.GradientPanel {

    private final ShelterWellnessApp app;

    private final Rectangle calmBtn = new Rectangle();
    private final Rectangle comfortBtn = new Rectangle();
    private final Rectangle distractBtn = new Rectangle();
    private final Rectangle talkBtn = new Rectangle();
    private final Rectangle backBtn = new Rectangle();

    private int hov = -1;

    public SupportChoicePanel(ShelterWellnessApp app) {
        this.app = app;

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hov = calmBtn.contains(e.getPoint()) ? 0
                        : comfortBtn.contains(e.getPoint()) ? 1
                                : distractBtn.contains(e.getPoint()) ? 2
                                        : talkBtn.contains(e.getPoint()) ? 3 : -1;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (backBtn.contains(e.getPoint())) {
                    app.navigate("home");
                }
                else if (calmBtn.contains(e.getPoint()))
                    app.navigate("stretch");
                else if (comfortBtn.contains(e.getPoint()))
                    app.navigate("breath");
                else if (distractBtn.contains(e.getPoint()))
                    app.navigate("water");
                else if (talkBtn.contains(e.getPoint()))
                    app.navigate("move");
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = setup(g);
        drawBack(g2, 20, 20, backBtn);

        int w = getWidth();
        int cx = w / 2;

        g2.setFont(FONT_TITLE);
        ctr(g2, "What do you feel like doing right now?", cx, 120);

        int btnH = 64;
        int startY = 165;
        int gapY = 28;
        int leftX = 200;
        int rightX = 360;
        int bubbleW = 330;

        String[] texts = {
                "Want to stretch with me for a minute?",
                "Can we take a slow breath together?",
                "Maybe a sip of water first?",
                "Want to change your spot with me?"
        };

        Rectangle[] btns = { calmBtn, comfortBtn, distractBtn, talkBtn };

        Image[] animals = app.getAnimals();
        Image[] chosen = {
                animals[7], // stretch
                animals[9], // think
                animals[0], // drink
                animals[3] // move
        };

        for (int i = 0; i < 4; i++) {
            int y = startY + i * (btnH + gapY);
            boolean left = (i % 2 == 0);

            int curX = left ? leftX : rightX;
            btns[i].setBounds(curX, y, bubbleW, btnH);

            Image img = chosen[i];

            g2.setColor(hov == i ? alphaColor(ACCENT_PURPLE, 24) : CARD_BG);
            g2.fillRoundRect(curX, y, bubbleW, btnH, 24, 24);

            g2.setColor(alphaColor(ACCENT_PURPLE, hov == i ? 90 : 55));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(curX, y, bubbleW, btnH, 24, 24);

            Polygon tail = new Polygon();

            if (left) {
                tail.addPoint(curX, y + 22);
                tail.addPoint(curX - 16, y + 30);
                tail.addPoint(curX, y + 38);
            } else {
                tail.addPoint(curX + bubbleW, y + 22);
                tail.addPoint(curX + bubbleW + 16, y + 30);
                tail.addPoint(curX + bubbleW, y + 38);
            }

            g2.setColor(hov == i ? alphaColor(ACCENT_PURPLE, 24) : CARD_BG);
            g2.fillPolygon(tail);

            g2.setColor(alphaColor(ACCENT_PURPLE, hov == i ? 90 : 55));
            g2.drawPolygon(tail);

            g2.setColor(TEXT_PRIMARY);
            g2.setFont(FONT_BUTTON);
            ctr(g2, texts[i], curX + bubbleW / 2, y + 39);

            int targetH;
            if (i == 0)
                targetH = 120;
            else if (i == 2)
                targetH = 115;
            else if (i == 3)
                targetH = 125;
            else
                targetH = 100;

            int imgW = img.getWidth(this);
            int imgH = img.getHeight(this);
            int targetW = (int) (imgW * (targetH / (double) imgH));

            int animalY = y + btnH / 2 - targetH / 2;

            int gap;
            if (i == 0)
                gap = 0;
            else if (i == 3)
                gap = 0;
            else
                gap = 18;

            if (left) {
                int animalX = curX - gap - targetW;
                g2.drawImage(img, animalX, animalY, targetW, targetH, this);
            } else {
                int animalX = curX + bubbleW + gap + targetW;
                g2.drawImage(img, animalX, animalY, -targetW, targetH, this);
            }
        }

        g2.dispose();
    }
}