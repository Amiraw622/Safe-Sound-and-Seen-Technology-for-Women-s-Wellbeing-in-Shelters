package UI;

import UI.ShelterWellnessApp.GradientPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatPanel extends JPanel{
    private ShelterWellnessApp app;

    public ChatPanel(ShelterWellnessApp app, String label, Color accent, boolean sup, String backTo) {
        this.app = app;
        setLayout(new BorderLayout());
        add(buildChatScreen(label, accent, sup, backTo), BorderLayout.CENTER);
    }
    
    private JPanel buildChatScreen(String label, Color accent, boolean sup, String backTo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(ShelterWellnessApp.BG_PRIMARY);

        JPanel top = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(
                        new GradientPaint(0, 0, new Color(255, 245, 240), 0, getHeight(), new Color(255, 235, 225)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(230, 190, 175, 80));
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

                int cx = getWidth() / 2;
                int imWidth = 142, imHeight = 102;
                int totalW = imWidth + 12 + 120;
                int avatarX = cx - totalW / 2 + 30;
                int avatarY = 13;
                g2.drawImage(app.getAnimals()[8], avatarX, avatarY, imWidth, imHeight, null);

                int textX = avatarX + imWidth + 12;
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                g2.setColor(new Color(140, 76, 45));
                g2.drawString("Your friend", textX, 55);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.setColor(new Color(180, 140, 130));
                g2.drawString("always here for you \u2665", textX, 72);

                g2.dispose();
            }
        };
        top.setLayout(new BorderLayout());
        top.setPreferredSize(new Dimension(0, 128));

        JButton bk = new JButton("\u2190");
        bk.setFont(new Font("SansSerif", Font.BOLD, 18));
        bk.setForeground(new Color(180, 120, 100));
        bk.setOpaque(false);
        bk.setBorderPainted(false);
        bk.setContentAreaFilled(false);
        bk.setFocusPainted(false);
        bk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bk.setPreferredSize(new Dimension(50, 68));
        bk.addActionListener(e -> app.navigate(backTo));
        top.add(bk, BorderLayout.WEST);

        p.add(top, BorderLayout.NORTH);

        JPanel chatArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(
                        new GradientPaint(0, 0, new Color(255, 252, 248), 0, getHeight(), new Color(255, 243, 235)));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBorder(BorderFactory.createEmptyBorder(4, 16, 16, 16));

        JScrollPane cs = new JScrollPane(chatArea);
        cs.setBorder(null);
        cs.getViewport().setOpaque(false);
        cs.setOpaque(false);
        cs.getVerticalScrollBar().setUnitIncrement(16);
        cs.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
        p.add(cs, BorderLayout.CENTER);

        // ─── Bottom options panel ───
        JPanel bottomPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(
                        new GradientPaint(0, 0, new Color(255, 245, 238), 0, getHeight(), new Color(255, 240, 230)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(230, 200, 185, 60));
                g2.drawLine(0, 0, getWidth(), 0);
            }
        };
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        p.add(bottomPanel, BorderLayout.SOUTH);

        // Start conversation
        showGreeting(chatArea, bottomPanel, cs, sup);

        return p;
    }

    // ─── Show greeting + first options ───
    private void showGreeting(JPanel chatArea, JPanel bottomPanel, JScrollPane cs, boolean sup) {
        app.addWarmBubble(chatArea, "Hey, I'm glad you're here. How are you feeling right now?", false, ShelterWellnessApp.ACCENT_TEAL);

        String[] options = {
                "I'm doing okay \uD83D\uDE0A",
                "Not great today \uD83D\uDE14",
                "I feel lonely",
                "I'm scared",
                "I just want company"
        };

        showOptions(chatArea, bottomPanel, cs, options, choice -> {
            app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);

            String lower = choice.toLowerCase();
            if (lower.contains("okay") || lower.contains("company")) {
                showHappyPath(chatArea, bottomPanel, cs);
            } else if (lower.contains("not great") || lower.contains("lonely") || lower.contains("scared")) {
                showSupportPath(chatArea, bottomPanel, cs);
            }
        });
    }

    // ─── Happy path ───
    private void showHappyPath(JPanel chatArea, JPanel bottomPanel, JScrollPane cs) {
        delayBubble(chatArea, cs, "That's nice to hear! What would you like to do?", () -> {
            String[] options = {
                    "Tell me something positive \u2728",
                    "Share a fun fact \uD83D\uDCA1",
                    "Give me a compliment \uD83D\uDC9B",
                    "I changed my mind, I need support",
                    "\u2190 Go back home"
            };
            showOptions(chatArea, bottomPanel, cs, options, choice -> {
                app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);
                String lower = choice.toLowerCase();

                if (lower.contains("positive")) {
                    delayBubble(chatArea, cs,
                            "Every sunrise is a reminder that you can begin again. You're doing amazing just by being here.",
                            () -> {
                                showContinueOrHome(chatArea, bottomPanel, cs, true);
                            });
                } else if (lower.contains("fun fact")) {
                    delayBubble(chatArea, cs,
                            "Did you know? Otters hold hands while they sleep so they don't drift apart. You're never truly alone either.",
                            () -> {
                                showContinueOrHome(chatArea, bottomPanel, cs, true);
                            });
                } else if (lower.contains("compliment")) {
                    delayBubble(chatArea, cs,
                            "You are braver than you believe, stronger than you seem, and more loved than you know.",
                            () -> {
                                showContinueOrHome(chatArea, bottomPanel, cs, true);
                            });
                } else if (lower.contains("support")) {
                    showSupportPath(chatArea, bottomPanel, cs);
                } else if (lower.contains("home")) {
                    app.navigate("home");
                }
            });
        });
    }

    // ─── Support path ───
    private void showSupportPath(JPanel chatArea, JPanel bottomPanel, JScrollPane cs) {
        delayBubble(chatArea, cs, "I'm here for you. You're not alone in this. What would help most right now?", () -> {
            String[] options = {
                    "I need someone to talk to \uD83D\uDCAC",
                    "Show me calming music \uD83C\uDFB5",
                    "I want to cook something \uD83C\uDF75",
                    "Show me help resources \u260E",
                    "Just stay with me quietly \uD83E\uDD0D"
            };
            showOptions(chatArea, bottomPanel, cs, options, choice -> {
                app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);
                String lower = choice.toLowerCase();

                if (lower.contains("talk to")) {
                    showTalkPath(chatArea, bottomPanel, cs);
                } else if (lower.contains("music")) {
                    delayBubble(chatArea, cs, "Music can really help. Let me take you to something calming.", () -> {
                        app.navigate("musicdetail");
                    });
                } else if (lower.contains("cook")) {
                    delayBubble(chatArea, cs, "Making something warm can feel so comforting. Let's find a recipe.",
                            () -> {
                                app.navigate("recipedetail");
                            });
                } else if (lower.contains("help resources")) {
                    delayBubble(chatArea, cs, "There are people who care and want to help. Let me show you.", () -> {
                        app.navigate("help");
                    });
                } else if (lower.contains("stay with me")) {
                    showQuietCompany(chatArea, bottomPanel, cs);
                }
            });
        });
    }

    // ─── Talk deeper ───
    private void showTalkPath(JPanel chatArea, JPanel bottomPanel, JScrollPane cs) {
        delayBubble(chatArea, cs, "I'm listening. Can you tell me a little more about how you're feeling?", () -> {
            String[] options = {
                    "I feel sad and I don't know why",
                    "Someone hurt me",
                    "I'm worried about my safety",
                    "I feel overwhelmed",
                    "I don't want to say, just comfort me"
            };
            showOptions(chatArea, bottomPanel, cs, options, choice -> {
                app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);
                String lower = choice.toLowerCase();

                if (lower.contains("sad")) {
                    delayBubble(chatArea, cs,
                            "It's okay to feel sad without knowing why. Your feelings are valid. You don't have to figure it all out right now.",
                            () -> {
                                showAfterSupport(chatArea, bottomPanel, cs);
                            });
                } else if (lower.contains("hurt me")) {
                    delayBubble(chatArea, cs,
                            "I'm so sorry that happened to you. You didn't deserve that. You are worthy of safety and respect.",
                            () -> {
                                showSafetyOptions(chatArea, bottomPanel, cs);
                            });
                } else if (lower.contains("safety")) {
                    delayBubble(chatArea, cs,
                            "Your safety matters most. Here are some resources that can help right now.", () -> {
                                showSafetyOptions(chatArea, bottomPanel, cs);
                            });
                } else if (lower.contains("overwhelmed")) {
                    delayBubble(chatArea, cs,
                            "Take a deep breath with me. In... 2... 3... 4... Out... 2... 3... 4... 5... 6... You're doing so well.",
                            () -> {
                                showAfterSupport(chatArea, bottomPanel, cs);
                            });
                } else if (lower.contains("comfort")) {
                    delayBubble(chatArea, cs,
                            "You are safe here. You are enough. You are loved. Take all the time you need.", () -> {
                                showAfterSupport(chatArea, bottomPanel, cs);
                            });
                }
            });
        });
    }

    // ─── Safety options ───
    private void showSafetyOptions(JPanel chatArea, JPanel bottomPanel, JScrollPane cs) {
        String[] options = {
                "Show me emergency numbers \u260E",
                "I want to keep talking",
                "Take me home"
        };
        showOptions(chatArea, bottomPanel, cs, options, choice -> {
            app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);
            if (choice.contains("emergency")) {
                app.navigate("help");
            } else if (choice.contains("talking")) {
                showAfterSupport(chatArea, bottomPanel, cs);
            } else {
                app.navigate("home");
            }
        });
    }

    // ─── Quiet company ───
    private void showQuietCompany(JPanel chatArea, JPanel bottomPanel, JScrollPane cs) {
        delayBubble(chatArea, cs, "I'm right here with you. No need to say anything. \uD83E\uDD0D", () -> {
            delayBubble(chatArea, cs, "...", () -> {
                delayBubble(chatArea, cs, "You're not alone.", () -> {
                    showAfterSupport(chatArea, bottomPanel, cs);
                });
            });
        });
    }

    // ─── After support: what next ───
    private void showAfterSupport(JPanel chatArea, JPanel bottomPanel, JScrollPane cs) {
        String[] options = {
                "That helped, thank you \uD83D\uDC9B",
                "I want to talk more",
                "Show me something calming",
                "Take me home"
        };
        showOptions(chatArea, bottomPanel, cs, options, choice -> {
            app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);
            String lower = choice.toLowerCase();

            if (lower.contains("thank")) {
                delayBubble(chatArea, cs, "I'm always here whenever you need me. You matter so much. \u2764", () -> {
                    showContinueOrHome(chatArea, bottomPanel, cs, false);
                });
            } else if (lower.contains("talk more")) {
                showTalkPath(chatArea, bottomPanel, cs);
            } else if (lower.contains("calming")) {
                app.navigate("musicdetail");
            } else if (lower.contains("home")) {
                app.navigate("home");
            }
        });
    }

    // ─── Continue or go home ───
    private void showContinueOrHome(JPanel chatArea, JPanel bottomPanel, JScrollPane cs, boolean happy) {
        String[] options = happy
                ? new String[] { "Tell me more! \u2728", "I need support actually", "\u2190 Go home" }
                : new String[] { "I feel a bit better now", "I still need help", "\u2190 Go home" };

        showOptions(chatArea, bottomPanel, cs, options, choice -> {
            app.addWarmBubble(chatArea, choice, true, ShelterWellnessApp.ACCENT_TEAL);
            String lower = choice.toLowerCase();

            if (lower.contains("more") || lower.contains("better")) {
                showHappyPath(chatArea, bottomPanel, cs);
            } else if (lower.contains("support") || lower.contains("help")) {
                showSupportPath(chatArea, bottomPanel, cs);
            } else {
                app.navigate("home");
            }
        });
    }

    // show option buttons
    private void showOptions(JPanel chatArea, JPanel bottomPanel, JScrollPane cs,
            String[] options, java.util.function.Consumer<String> onChoice) {
        bottomPanel.removeAll();

        for (String opt : options) {
            JButton btn = new JButton(opt) {
                boolean hover = false;
                {
                    addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e) {
                            hover = true;
                            repaint();
                        }

                        public void mouseExited(MouseEvent e) {
                            hover = false;
                            repaint();
                        }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (hover) {
                        g2.setColor(new Color(240, 215, 205));
                    } else {
                        g2.setColor(new Color(255, 255, 255));
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                    g2.setColor(new Color(220, 185, 170));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
            btn.setForeground(new Color(120, 70, 55));
            btn.setOpaque(false);
            btn.setContentAreaFilled(false);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setPreferredSize(new Dimension(300, 40));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

            btn.addActionListener(e -> {
                onChoice.accept(opt);
                scrollToBottom(cs);
            });

            bottomPanel.add(btn);
            bottomPanel.add(Box.createVerticalStrut(6));
        }

        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    // delayed bubble with callback
    private void delayBubble(JPanel chatArea, JScrollPane cs, String text, Runnable after) {
        Timer tm = new Timer(600 + (int) (Math.random() * 600), ev -> {
            app.addWarmBubble(chatArea, text, false, ShelterWellnessApp.ACCENT_TEAL);
            chatArea.revalidate();
            scrollToBottom(cs);
            if (after != null) {
                Timer t2 = new Timer(300, ev2 -> after.run());
                t2.setRepeats(false);
                t2.start();
            }
        });
        tm.setRepeats(false);
        tm.start();
    }

    // scroll to the bottom available
    private void scrollToBottom(JScrollPane cs) {
        SwingUtilities.invokeLater(() -> {
            JScrollBar sb = cs.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }
}
