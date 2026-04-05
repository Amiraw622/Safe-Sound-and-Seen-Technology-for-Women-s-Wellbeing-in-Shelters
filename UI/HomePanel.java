package UI;

import javax.swing.JPanel;



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
                ...
            }
        }