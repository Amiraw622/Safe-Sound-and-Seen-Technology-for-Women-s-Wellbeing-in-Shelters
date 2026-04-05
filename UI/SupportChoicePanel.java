package UI;

import java.awt.*;
import java.awt.event.*;

import static UI.ShelterWellnessApp.*;

public class SupportChoicePanel extends ShelterWellnessApp.GradientPanel {

    private final ShelterWellnessApp app;

    private final Rectangle calmBtn = new Rectangle();
    private final Rectangle comfortBtn = new Rectangle();
    private final Rectangle distractBtn = new Rectangle();
    private final Rectangle talkBtn = new Rectangle();

    private int hov = -1;

    public SupportChoicePanel(ShelterWellnessApp app) {
        this.app = app;

     MouseAdapter ma = new MouseAdapter() {
    @Override
    public void mouseMoved(MouseEvent e) {
        // ...
    }

    @Override
            public void mouseClicked(MouseEvent e) {}
    @Override
            public void mouseExited(MouseEvent e) {}
}   
    @Override
    protected void paintComponent(Graphics g) {
        // ...
    }
}