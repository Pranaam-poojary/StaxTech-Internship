import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PasswordGenerator extends JFrame {
    private JCheckBox uppercaseCb, lowercaseCb, numbersCb, specialCb;
    private JSpinner lengthSpinner;
    private JButton generateButton, copyButton, showHideButton;
    private JTextField passwordField;
    private JLabel strengthLabel;
    private JProgressBar strengthBar;
    private boolean isPasswordVisible = false;

    // Modern color scheme
    private static final Color DARK_BG = new Color(32, 33, 36);
    private static final Color CARD_BG = new Color(48, 49, 52);
    private static final Color PRIMARY_COLOR = new Color(66, 133, 244);
    private static final Color SUCCESS_COLOR = new Color(52, 168, 83);
    private static final Color WARNING_COLOR = new Color(251, 188, 4);
    private static final Color DANGER_COLOR = new Color(234, 67, 53);
    private static final Color TEXT_PRIMARY = new Color(232, 234, 237);
    private static final Color TEXT_SECONDARY = new Color(154, 160, 166);

    public PasswordGenerator() {
        super("Password Generator Pro");
        initializeUI();
        setupEventHandlers();
        setLocationRelativeTo(null);
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(DARK_BG);
        setLayout(new BorderLayout(10, 10));

        // Create main container
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Password Generator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Options panel
        JPanel optionsPanel = createOptionsPanel();

        // Password output panel
        JPanel outputPanel = createOutputPanel();

        // Button panel
        JPanel buttonPanel = createButtonPanel();

        // Strength panel
        JPanel strengthPanel = createStrengthPanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(DARK_BG);
        bottomPanel.add(outputPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(strengthPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(450, 380));
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 10));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(TEXT_SECONDARY, 1),
                        "Options",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 12),
                        TEXT_PRIMARY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Character set options
        JPanel checkboxPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        checkboxPanel.setBackground(CARD_BG);

        uppercaseCb = createStyledCheckBox("Uppercase (A-Z)", false);
        lowercaseCb = createStyledCheckBox("Lowercase (a-z)", true);
        numbersCb = createStyledCheckBox("Numbers (0-9)", false);
        specialCb = createStyledCheckBox("Special (!@#$%)", false);

        checkboxPanel.add(uppercaseCb);
        checkboxPanel.add(lowercaseCb);
        checkboxPanel.add(numbersCb);
        checkboxPanel.add(specialCb);

        // Length control
        JPanel lengthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lengthPanel.setBackground(CARD_BG);

        JLabel lengthLabel = new JLabel("Password Length:");
        lengthLabel.setForeground(TEXT_PRIMARY);
        lengthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lengthSpinner = new JSpinner(new SpinnerNumberModel(12, 4, 128, 1));
        lengthSpinner.setPreferredSize(new Dimension(80, 30));
        styleSpinner(lengthSpinner);

        lengthPanel.add(lengthLabel);
        lengthPanel.add(Box.createHorizontalStrut(10));
        lengthPanel.add(lengthSpinner);

        panel.add(checkboxPanel);
        panel.add(lengthPanel);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        passwordField = new JTextField();
        passwordField.setFont(new Font("Consolas", Font.PLAIN, 14));
        passwordField.setBackground(CARD_BG);
        passwordField.setForeground(TEXT_PRIMARY);
        passwordField.setCaretColor(PRIMARY_COLOR);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TEXT_SECONDARY, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        passwordField.setEditable(false);

        showHideButton = new JButton("441");
        showHideButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        showHideButton.setPreferredSize(new Dimension(40, 34));
        styleButton(showHideButton, TEXT_SECONDARY);
        showHideButton.setToolTipText("Show/Hide password");

        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(showHideButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(DARK_BG);

        generateButton = new JButton("Generate Password");
        generateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        generateButton.setPreferredSize(new Dimension(150, 40));
        styleButton(generateButton, PRIMARY_COLOR);

        copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyButton.setPreferredSize(new Dimension(150, 40));
        styleButton(copyButton, SUCCESS_COLOR);
        copyButton.setEnabled(false);

        panel.add(generateButton);
        panel.add(copyButton);

        return panel;
    }

    private JPanel createStrengthPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        strengthLabel = new JLabel("Password Strength: -");
        strengthLabel.setForeground(TEXT_SECONDARY);
        strengthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        strengthBar = new JProgressBar(0, 100);
        strengthBar.setStringPainted(false);
        strengthBar.setBackground(CARD_BG);
        strengthBar.setPreferredSize(new Dimension(0, 8));

        panel.add(strengthLabel, BorderLayout.NORTH);
        panel.add(strengthBar, BorderLayout.CENTER);

        return panel;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox cb = new JCheckBox(text, selected);
        cb.setBackground(CARD_BG);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        cb.setFocusPainted(false);
        return cb;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private void styleSpinner(JSpinner spinner) {
        spinner.setBorder(BorderFactory.createLineBorder(TEXT_SECONDARY, 1));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(CARD_BG);
        editor.getTextField().setForeground(TEXT_PRIMARY);
        editor.getTextField().setCaretColor(PRIMARY_COLOR);
        editor.getTextField().setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
    }

    @SuppressWarnings("unused")
    private void setupEventHandlers() {
        generateButton.addActionListener(e -> generatePassword());

        copyButton.addActionListener(e -> copyToClipboard());

        showHideButton.addActionListener(e -> togglePasswordVisibility());

        // Add change listeners for real-time strength calculation
        lengthSpinner.addChangeListener(e -> updatePasswordStrength());
        uppercaseCb.addActionListener(e -> updatePasswordStrength());
        lowercaseCb.addActionListener(e -> updatePasswordStrength());
        numbersCb.addActionListener(e -> updatePasswordStrength());
        specialCb.addActionListener(e -> updatePasswordStrength());
    }

    private void generatePassword() {
        int length = (Integer) lengthSpinner.getValue();
        boolean upper = uppercaseCb.isSelected();
        boolean lower = lowercaseCb.isSelected();
        boolean numbers = numbersCb.isSelected();
        boolean special = specialCb.isSelected();

        if (!upper && !lower && !numbers && !special) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one character type.",
                    "No Character Types Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String password = generatePassword(length, upper, lower, numbers, special);
        passwordField.setText(password);
        copyButton.setEnabled(true);
        updatePasswordStrength(); // Show success feedback
        generateButton.setText("Generated!");
        Timer timer = new Timer(1500, evt -> {
            evt.getActionCommand(); // suppress unused warning
            generateButton.setText("Generate Password");
        });
        timer.setRepeats(false);
        timer.start();
        ;
    }

    private void copyToClipboard() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            return;
        }

        StringSelection selection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null); // Show copy feedback
        copyButton.setText("Copied!");
        Timer timer = new Timer(1500, evt -> {
            evt.getActionCommand(); // suppress unused warning
            copyButton.setText("Copy to Clipboard");
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            showHideButton.setText("648");
            showHideButton.setToolTipText("Hide password");
        } else {
            showHideButton.setText("441");
            showHideButton.setToolTipText("Show password");
        }
        // Toggle password field font (could implement actual masking if needed)
    }

    private void updatePasswordStrength() {
        int length = (Integer) lengthSpinner.getValue();
        boolean upper = uppercaseCb.isSelected();
        boolean lower = lowercaseCb.isSelected();
        boolean numbers = numbersCb.isSelected();
        boolean special = specialCb.isSelected();

        int score = calculatePasswordStrength(length, upper, lower, numbers, special);
        strengthBar.setValue(score);

        String strengthText;
        Color barColor;

        if (score < 30) {
            strengthText = "Weak";
            barColor = DANGER_COLOR;
        } else if (score < 60) {
            strengthText = "Moderate";
            barColor = WARNING_COLOR;
        } else if (score < 80) {
            strengthText = "Strong";
            barColor = SUCCESS_COLOR;
        } else {
            strengthText = "Very Strong";
            barColor = SUCCESS_COLOR;
        }

        strengthLabel.setText("Password Strength: " + strengthText);
        strengthBar.setForeground(barColor);
    }

    private int calculatePasswordStrength(int length, boolean upper, boolean lower, boolean numbers, boolean special) {
        int score = 0;

        // Length scoring
        if (length >= 8)
            score += 20;
        if (length >= 12)
            score += 10;
        if (length >= 16)
            score += 10;
        if (length >= 20)
            score += 10;

        // Character type scoring
        if (upper)
            score += 15;
        if (lower)
            score += 15;
        if (numbers)
            score += 15;
        if (special)
            score += 15;

        return Math.min(100, score);
    }

    @SuppressWarnings("unused")
    private String generatePassword(int length, boolean upper, boolean lower, boolean numbers, boolean special) {
        StringBuilder pool = new StringBuilder();
        if (upper)
            pool.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        if (lower)
            pool.append("abcdefghijklmnopqrstuvwxyz");
        if (numbers)
            pool.append("0123456789");
        if (special)
            pool.append("!@#$%^&*()-_=+[]{};:,.<>?");

        if (pool.length() == 0) {
            return "";
        }

        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int idx = rand.nextInt(pool.length());
            password.append(pool.charAt(idx));
        }
        return password.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasswordGenerator().setVisible(true));
    }
}
