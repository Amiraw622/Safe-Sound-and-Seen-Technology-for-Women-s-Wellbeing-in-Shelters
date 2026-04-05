package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ShelterWellnessApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private Image[] animals;

    public static final Color BG_PRIMARY = new Color(255, 255, 255);
    public static final Color BG_SECONDARY = new Color(35, 30, 48);
    public static final Color TEXT_PRIMARY = new Color(140, 66, 45);
    public static final Color TEXT_SECONDARY = new Color(170, 120, 135);
    public static final Color TEXT_MUTED = new Color(180, 122, 130);
    public static final Color ACCENT_TEAL = new Color(103, 20, 65);
    public static final Color ACCENT_WARM = new Color(39, 0, 100);
    public static final Color ACCENT_ROSE = new Color(220, 20, 40);
    public static final Color ACCENT_PINK = new Color(232, 63, 86);
    public static final Color ACCENT_PURPLE = new Color(140, 80, 180);
    public static final Color ACCENT_CORAL = new Color(216, 90, 48);
    public static final Color CARD_BG = new Color(255, 255, 255, 10);
    public static final Color CARD_HOVER = new Color(255, 255, 255, 20);
    public static final Color CARD_BORDER = new Color(255, 255, 255, 20);

    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_CARD_TITLE = new Font("SansSerif", Font.BOLD, 17);
    public static final Font FONT_CARD_DESC = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_CHAT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_DETAIL_TITLE = new Font("SansSerif", Font.BOLD, 22);

    static final String[][] DAILY_MUSIC = {
            // ...;
    };

    static final String[][] DAILY_RECIPES = {
            // ...;
    };


    public ShelterWellnessApp() {
            super("A small space for you");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(900, 720);
            setMinimumSize(new Dimension(820, 650));
            setLocationRelativeTo(null);
    }
}