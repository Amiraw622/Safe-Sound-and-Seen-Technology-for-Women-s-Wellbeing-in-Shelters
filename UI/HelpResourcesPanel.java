package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HelpResourcesPanel extends JPanel {

    private final ShelterWellnessApp app;

    public HelpResourcesPanel(ShelterWellnessApp app) {
        this.app = app;
        setLayout(new BorderLayout());
        setBackground(new Color(255, 252, 248));

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildScrollableContent(), BorderLayout.CENTER);
    }

    // ─── Warm top bar ───
    private JPanel buildTopBar() {
        JPanel top = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(
                        new GradientPaint(0, 0, new Color(255, 245, 240), 0, getHeight(), new Color(255, 235, 228)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(230, 190, 175, 80));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(0, 60));

        JButton bk = new JButton("\u2190");
        bk.setFont(new Font("SansSerif", Font.BOLD, 18));
        bk.setForeground(new Color(180, 120, 100));
        bk.setOpaque(false);
        bk.setBorderPainted(false);
        bk.setContentAreaFilled(false);
        bk.setFocusPainted(false);
        bk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bk.setPreferredSize(new Dimension(50, 60));
        bk.addActionListener(e -> app.navigate("home"));
        bk.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                bk.setForeground(new Color(140, 76, 45));
            }

            public void mouseExited(MouseEvent e) {
                bk.setForeground(new Color(180, 120, 100));
            }
        });
        top.add(bk, BorderLayout.WEST);

        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                /* transparent */ }
        };
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 16));
        JLabel tt = new JLabel("Help & Support");
        tt.setFont(new Font("SansSerif", Font.BOLD, 20));
        tt.setForeground(new Color(140, 76, 45));
        titlePanel.add(tt);
        top.add(titlePanel, BorderLayout.CENTER);

        return top;
    }

    // ─── Scrollable content area ───
    private JScrollPane buildScrollableContent() {
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(
                        new GradientPaint(0, 0, new Color(255, 252, 248), 0, getHeight(), new Color(255, 243, 235)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));

        // Privacy notice
        content.add(createWarmCard(
                "\uD83D\uDD12  Your safety matters",
                "If you are in immediate danger, call emergency services.",
                new Color(180, 140, 120),
                new Color(255, 248, 242),
                new Color(240, 220, 210)));
        content.add(Box.createVerticalStrut(20));

        // Section: Emergency numbers
        content.add(createSectionTitle("Emergency numbers by region"));

        String[][] emergencyNumbers = {
                { "\uD83C\uDDE8\uD83C\uDDF3", "China", "110", "Police" },
                { "\uD83C\uDDFA\uD83C\uDDF8", "United States", "911", "All emergencies" },
                { "\uD83C\uDDE8\uD83C\uDDE6", "Canada", "911", "All emergencies" },
                { "\uD83C\uDDE6\uD83C\uDDFA", "Australia", "000 / 112", "Emergency / mobile" },
                { "\uD83C\uDDF3\uD83C\uDDFF", "New Zealand", "111", "All emergencies" },
                { "\uD83C\uDDEE\uD83C\uDDF3", "India", "112 / 14490", "Emergency / women helpline" },
                { "\uD83C\uDDEC\uD83C\uDDE7", "United Kingdom", "999 / 112", "All emergencies" },
                { "\uD83C\uDDEA\uD83C\uDDFA", "Europe", "112", "Universal emergency" },
                { "\uD83C\uDDF8\uD83C\uDDEC", "Singapore", "999", "Police" },
                { "\uD83C\uDDEF\uD83C\uDDF5", "Japan", "110", "Police" },
                { "\uD83C\uDDF0\uD83C\uDDF7", "Korea", "112", "Police / emergency" },
                { "\uD83C\uDDF9\uD83C\uDDED", "Thailand", "191", "Police" },
                { "\uD83C\uDDF2\uD83C\uDDFE", "Malaysia", "999", "All emergencies" },
        };

        for (String[] entry : emergencyNumbers) {
            content.add(createEmergencyCard(entry[0], entry[1], entry[2], entry[3]));
            content.add(Box.createVerticalStrut(8));
        }

        content.add(Box.createVerticalStrut(16));

        // Section: Support organizations
        content.add(createSectionTitle("Support organizations"));

        String[][] orgs = {
                { "Assaulted Women's Helpline", "https://www.awhl.org/",
                        "24/7 crisis counselling and emotional support" },
                { "Crisis Centre BC", "https://www.crisiscentre.bc.ca/",
                        "Barrier-free, non-judgemental support" },
                { "RAINN (US)", "800.656.HOPE",
                        "National Sexual Assault Hotline" },
                { "Rise", "https://www.womenslegalcentre.ca/",
                        "Women's legal centre" },
                { "ASHIYANAA (South Asia)", "1-888-417-2742",
                        "Culturally-competent support services (violence)" },
                { "WAVE (EUROPE)", "https://wave-network.org/list-of-helplines-in-46-countries/",
                        "A table of the national women's helplines in the 46 European Countries" },
        };

        for (String[] org : orgs) {
            content.add(createOrgCard(org[0], org[1], org[2]));
            content.add(Box.createVerticalStrut(8));
        }

        content.add(Box.createVerticalStrut(12));

        // Footer
        JLabel footer = new JLabel("<html><div style='text-align:center;color:#B08878;font-size:11px;'>"
                + "You are not alone. Reaching out is a sign of strength.</div></html>");
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        content.add(footer);

        JScrollPane sc = new JScrollPane(content);
        sc.setBorder(null);
        sc.getViewport().setOpaque(false);
        sc.setOpaque(false);
        sc.getVerticalScrollBar().setUnitIncrement(16);
        sc.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        return sc;
    }

    // ─── Helper: section title ───
    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(new Color(140, 76, 45));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 4, 12, 0));
        return label;
    }

    // ─── Helper: emergency number card ───
    private JPanel createEmergencyCard(String flag, String country, String number, String desc) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(235, 215, 205, 100));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(12, 0));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JLabel countryLabel = new JLabel(country);
        countryLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        countryLabel.setForeground(new Color(100, 60, 45));
        left.add(countryLabel);
        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descLabel.setForeground(new Color(170, 135, 125));
        left.add(descLabel);

        JLabel numLabel = new JLabel(number);
        numLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        numLabel.setForeground(new Color(200, 80, 60));
        numLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(left, BorderLayout.CENTER);
        card.add(numLabel, BorderLayout.EAST);
        return card;
    }

    // ─── Helper: support organization card ───
    private JPanel createOrgCard(String name, String contact, String desc) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(225, 200, 210, 100));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(new Color(100, 60, 45));
        card.add(nameLabel);

        JLabel contactLabel = new JLabel("\u260E  " + contact);
        contactLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        contactLabel.setForeground(new Color(180, 90, 70));
        contactLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(contactLabel);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        descLabel.setForeground(new Color(170, 135, 125));
        card.add(descLabel);

        return card;
    }

    // ─── Helper: warm info card ───
    private JPanel createWarmCard(String title, String body, Color textColor, Color bgColor, Color borderColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setForeground(textColor);
        card.add(titleLabel);

        JLabel bodyLabel = new JLabel(body);
        bodyLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        bodyLabel.setForeground(new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 180));
        card.add(bodyLabel);

        return card;
    }
}