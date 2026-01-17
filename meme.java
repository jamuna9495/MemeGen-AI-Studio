import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GrandMemeAI extends JFrame {
    private JTextField topText, bottomText;
    private JLabel imageDisplay;
    private BufferedImage originalImage, processedImage;
    private JButton uploadBtn, saveBtn;

    public GrandMemeAI() {
        setTitle("AI Meme Studio Pro - Java Edition");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Grand Sidebar Panel ---
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, 700));
        sidebar.setBackground(new Color(15, 23, 42)); // Dark Navy
        sidebar.setLayout(new GridLayout(10, 1, 10, 10));

        JLabel title = new JLabel(" MEME STUDIO", SwingConstants.CENTER);
        title.setForeground(Color.CYAN);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        topText = new JTextField("Top Text Here");
        bottomText = new JTextField("Bottom Text Here");
        uploadBtn = new JButton("Upload Image");
        saveBtn = new JButton("Save Grand Meme");

        // Styling Buttons
        styleButton(uploadBtn);
        styleButton(saveBtn);

        sidebar.add(title);
        sidebar.add(new JLabel("  Top Caption:", SwingConstants.LEFT)).setForeground(Color.WHITE);
        sidebar.add(topText);
        sidebar.add(new JLabel("  Bottom Caption:", SwingConstants.LEFT)).setForeground(Color.WHITE);
        sidebar.add(bottomText);
        sidebar.add(uploadBtn);
        sidebar.add(saveBtn);

        // --- Main Preview Panel ---
        imageDisplay = new JLabel("Upload an image to start...", SwingConstants.CENTER);
        imageDisplay.setForeground(Color.GRAY);
        imageDisplay.setOpaque(true);
        imageDisplay.setBackground(new Color(30, 41, 59));

        add(sidebar, BorderLayout.WEST);
        add(imageDisplay, BorderLayout.CENTER);

        // Events
        uploadBtn.addActionListener(e -> chooseImage());
        saveBtn.addActionListener(e -> saveMeme());
        
        // Live typing update
        KeyAdapter updateAction = new KeyAdapter() {
            public void keyReleased(KeyEvent e) { applyText(); }
        };
        topText.addKeyListener(updateAction);
        bottomText.addKeyListener(updateAction);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(138, 43, 226));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                originalImage = ImageIO.read(chooser.getSelectedFile());
                applyText();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading image.");
            }
        }
    }

    private void applyText() {
        if (originalImage == null) return;

        // Copy original image
        processedImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = processedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, null);

        // Font Settings (Impact Style)
        int fontSize = originalImage.getWidth() / 12;
        g.setFont(new Font("Impact", Font.BOLD, fontSize));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw Top Text
        drawCenteredText(g, topText.getText().toUpperCase(), fontSize, true);
        // Draw Bottom Text
        drawCenteredText(g, bottomText.getText().toUpperCase(), originalImage.getHeight() - 40, false);

        g.dispose();
        imageDisplay.setIcon(new ImageIcon(processedImage.getScaledInstance(600, -1, Image.SCALE_SMOOTH)));
        imageDisplay.setText("");
    }

    private void drawCenteredText(Graphics2D g, String text, int y, boolean isTop) {
        FontMetrics fm = g.getFontMetrics();
        int x = (originalImage.getWidth() - fm.stringWidth(text)) / 2;
        
        // Text Outline (Black)
        g.setColor(Color.BLACK);
        int offset = originalImage.getWidth() / 200;
        g.drawString(text, x-offset, y);
        g.drawString(text, x+offset, y);
        g.drawString(text, x, y-offset);
        g.drawString(text, x, y+offset);
        
        // Main Text (White)
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }

    private void saveMeme() {
        if (processedImage == null) return;
        try {
            ImageIO.write(processedImage, "PNG", new File("AI_Meme_Java.png"));
            JOptionPane.showMessageDialog(this, "Meme saved successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GrandMemeAI().setVisible(true));
    }
}
