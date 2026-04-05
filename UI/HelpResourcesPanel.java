package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelpResourcesPanel extends JPanel {

    private final ShelterWellnessApp app;

    public HelpResourcesPanel(ShelterWellnessApp app) {
        ...
    }

    // ─── Warm top bar ───
    private JPanel buildTopBar() {
        ...
    }

    // ─── Scrollable content area ───
    private JScrollPane buildScrollableContent() {
        ...
    }

        
    private JLabel createSectionTitle(String text) {
        ...
    }

    // ─── Helper: emergency number card ───
    private JPanel createEmergencyCard(String flag, String country, String number, String desc) {
        ...
    }

    // ─── Helper: support organization card ───
    private JPanel createOrgCard(String name, String contact, String desc) {
        ...
    }

    // ─── Helper: warm info card ───
    private JPanel createWarmCard(String title, String body, Color textColor, Color bgColor, Color borderColor) {
      ...
    }
}

