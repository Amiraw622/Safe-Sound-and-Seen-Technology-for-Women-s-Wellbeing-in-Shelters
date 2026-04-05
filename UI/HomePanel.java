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
        ...

            @Override
            protected void paintComponent(Graphics g) {
                ...
            }
        }