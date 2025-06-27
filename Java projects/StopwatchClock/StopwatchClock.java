import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * StopwatchClock - A GUI application that combines stopwatch and clock
 * functionality
 * Features:
 * - Digital clock showing current time
 * - Stopwatch with start, stop, reset functionality
 * - Modern and clean interface
 */
public class StopwatchClock extends JFrame {

    // Clock components
    private JLabel clockLabel;
    private Timer clockTimer;
    private SimpleDateFormat clockFormat;

    // Stopwatch components
    private JLabel stopwatchLabel;
    private Timer stopwatchTimer;
    private long startTime;
    private long elapsedTime;
    private boolean isRunning;

    // Control buttons
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;

    // Colors for modern UI
    private final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private final Color PANEL_COLOR = new Color(60, 60, 60);
    private final Color TEXT_COLOR = new Color(255, 255, 255);
    private final Color ACCENT_COLOR = new Color(0, 150, 255);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color DANGER_COLOR = new Color(244, 67, 54);

    public StopwatchClock() {
        initializeComponents();
        setupLayout();
        startClock();
        setupEventListeners();
    }

    private void initializeComponents() {
        setTitle("Stopwatch & Clock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_COLOR); // Initialize clock
        clockFormat = new SimpleDateFormat("HH:mm:ss");
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        clockLabel.setForeground(ACCENT_COLOR);
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize stopwatch
        stopwatchLabel = new JLabel("00:00:00.000");
        stopwatchLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        stopwatchLabel.setForeground(TEXT_COLOR);
        stopwatchLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Initialize buttons
        startButton = createStyledButton("START", SUCCESS_COLOR);
        stopButton = createStyledButton("STOP", DANGER_COLOR);
        resetButton = createStyledButton("RESET", ACCENT_COLOR);
        // Disable stop and reset until stopwatch starts
        stopButton.setEnabled(false);
        resetButton.setEnabled(false);

        // Initialize timers
        clockTimer = new Timer(1000, e -> updateClock());
        stopwatchTimer = new Timer(10, e -> updateStopwatch());

        // Initialize stopwatch variables
        startTime = 0;
        elapsedTime = 0;
        isRunning = false;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        // Larger button dimensions for better UI
        button.setPreferredSize(new Dimension(120, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void setupLayout() {
        // Use BorderLayout for frame
        setLayout(new BorderLayout());

        // Main content container with vertical layout
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Clock panel
        JLabel clockTitle = new JLabel("Current Time", SwingConstants.CENTER);
        clockTitle.setFont(new Font("Arial", Font.BOLD, 16));
        clockTitle.setForeground(TEXT_COLOR);
        clockTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel clockPanel = createStyledPanel();
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clockPanel.add(clockTitle);
        clockPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        clockPanel.add(clockLabel);

        // Spacer between panels
        Dimension spacer = new Dimension(0, 20);

        // Stopwatch panel
        JLabel stopwatchTitle = new JLabel("Stopwatch", SwingConstants.CENTER);
        stopwatchTitle.setFont(new Font("Arial", Font.BOLD, 16));
        stopwatchTitle.setForeground(TEXT_COLOR);
        stopwatchTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        stopwatchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel stopwatchPanel = createStyledPanel();
        stopwatchPanel.setLayout(new BoxLayout(stopwatchPanel, BoxLayout.Y_AXIS));
        stopwatchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stopwatchPanel.add(stopwatchTitle);
        stopwatchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        stopwatchPanel.add(stopwatchLabel);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(stopButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalGlue());

        // Build content
        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(clockPanel);
        contentPanel.add(Box.createRigidArea(spacer));
        contentPanel.add(stopwatchPanel);
        contentPanel.add(Box.createRigidArea(spacer));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        // Add to frame
        add(contentPanel, BorderLayout.CENTER);
        // Layout and sizing
        pack();
        // Increase default window size
        setSize(600, 500);
        // Set minimum size to prevent too-small resizing
        setMinimumSize(new Dimension(600, 500));
        setLocationRelativeTo(null);
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        return panel;
    }

    private void setupEventListeners() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startStopwatch();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopStopwatch();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStopwatch();
            }
        });
    }

    private void startClock() {
        updateClock();
        clockTimer.start();
    }

    private void updateClock() {
        String currentTime = clockFormat.format(new Date());
        clockLabel.setText(currentTime);
    }

    private void startStopwatch() {
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            stopwatchTimer.start();
            isRunning = true;
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            // Disable reset button during running
            resetButton.setEnabled(false);
        }
    }

    private void stopStopwatch() {
        if (isRunning) {
            stopwatchTimer.stop();
            isRunning = false;
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            // Enable reset button when stopped
            resetButton.setEnabled(true);
        }
    }

    private void resetStopwatch() {
        stopwatchTimer.stop();
        isRunning = false;
        elapsedTime = 0;
        startTime = 0;
        stopwatchLabel.setText("00:00:00.000");
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        // Disable reset button after reset
        resetButton.setEnabled(false);
    }

    private void updateStopwatch() {
        elapsedTime = System.currentTimeMillis() - startTime;

        long hours = elapsedTime / 3600000;
        long minutes = (elapsedTime % 3600000) / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        long millis = elapsedTime % 1000;

        String timeString = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
        stopwatchLabel.setText(timeString);
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StopwatchClock().setVisible(true);
            }
        });
    }
}
