import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import com.github.prominence.openweathermap.api.model.weather.Weather;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main extends Container {

    OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient("");

    private static Main app;

    JFrame window;
    JPanel startPanel;
    JPanel GUIPanel;
    JPanel writePanel;
    JPanel textPanel;

    JButton menuButton;
    JLabel welcomeLabel;
    JTextField textField;
    JTextPane textPane;

    Color firstColor;
    Color secColor;

    String txt;


    public void run() {
        window = new JFrame();

        window.setSize(500, 500);
        window.setVisible(true);
        window.setLayout(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        app.menu();
    }

    public void menu() {

        JPanel menuLabelPanel = new JPanel();
        JPanel menuButtonPanel = new JPanel();

        Font labelFont = new Font(Font.SANS_SERIF, Font.BOLD, 65);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        firstColor = new Color(137, 121, 156);
        secColor = new Color(126, 106, 153);

        startPanel = new JPanel();

        menuButton = new JButton("HELLO");
        welcomeLabel = new JLabel("Welcome");

        //Menu Panel

        startPanel.setSize(500, 500);
        startPanel.setBackground(firstColor);
        startPanel.setLayout(null);
        startPanel.add(menuLabelPanel);
        startPanel.add(menuButtonPanel);

        //Menu Label

        menuLabelPanel.setBounds(100, 50, 300, 100);
        menuLabelPanel.add(welcomeLabel);
        menuLabelPanel.setBackground(firstColor);

        welcomeLabel.setFont(labelFont);
        welcomeLabel.setSize(300, 100);
        welcomeLabel.setForeground(Color.WHITE);

        //Menu Button

        menuButtonPanel.setBounds(160, 220, 150, 90);
        menuButtonPanel.add(menuButton);
        menuButtonPanel.setLayout(null);

        menuButton.setFont(font);
        menuButton.setSize(150, 90);
        menuButton.setBackground(secColor);
        menuButton.setBorder(null);
        menuButton.setFocusPainted(false);
        menuButton.addActionListener(ac);

        window.add(startPanel);

    }

    public void GUI() {
        GUIPanel = new JPanel();
        writePanel = new JPanel();
        textPanel = new JPanel();

        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 45);
        Font textFont = new Font(Font.SANS_SERIF, Font.BOLD, 20);

        textField = new JTextField();
        textPane = new JTextPane();
        JButton entryButton = new JButton();

        GUIPanel.setSize(500, 500);
        GUIPanel.add(writePanel);
        GUIPanel.add(textPanel);
        GUIPanel.setLayout(null);

        //Write Panel

        writePanel.setBounds(0, 400, 500, 70);
        writePanel.setBackground(secColor);
        writePanel.add(textField);
        writePanel.add(entryButton);
        writePanel.setLayout(null);

        textField.setBounds(0, 0,450, 70);
        textField.setFont(font);

        //Write Panel Button
        entryButton.setBounds(450, 0,50, 70);
        entryButton.addActionListener(ac2);

        //Text Panel

        textPanel.setBounds(0, 0, 500, 400);
        textPanel.setBackground(firstColor);
        textPanel.add(textPane);
        textPanel.setLayout(null);

        textPane.setBounds(0, 0, 500, 400);
        textPane.setBackground(firstColor);
        textPane.setFont(textFont);
        textPane.setEditable(false);

        window.add(GUIPanel);
    }

    final Weather weather = openWeatherClient
            .currentWeather()
            .single()
            .byCityName("Kra??nik")
            .language(Language.POLISH)
            .unitSystem(UnitSystem.METRIC)
            .retrieve()
            .asJava();

    public void controlSystem() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime now = LocalTime.now();

        DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate today = LocalDate.now();

        StyledDocument doc = textPane.getStyledDocument();

        SimpleAttributeSet right = new SimpleAttributeSet();
        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);

        try {
            if (txt.contains("godzina")) {
                doc.insertString(doc.getLength(), "\nJest Godzina " + time.format(now), right);
                doc.setParagraphAttributes(doc.getLength(), 5, right, false);

            } else if (txt.contains("dzien") || txt.contains("data")) {
                doc.insertString(doc.getLength(), "\nDzisiaj jest " + day.format(today), right);
                doc.setParagraphAttributes(doc.getLength(), 5, right, false);

            } else if (txt.contains("potrafisz")) {
                doc.insertString(doc.getLength(), "\nNa ten moment jestem w stanie poda?? ci godzine oraz date", right);
                doc.setParagraphAttributes(doc.getLength(), 5, right, false);

            } else if (txt.contains("pogoda")) {
                doc.insertString(doc.getLength(), "\nObecna pogoda: " + weather, right);
                doc.setParagraphAttributes(doc.getLength(), 5, right, false);

            }



        } catch (BadLocationException e) {
        e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        app = new Main();
        app.run();
    }

    ActionListener ac = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            window.remove(startPanel);
            window.revalidate();
            window.repaint();

            app.GUI();

            System.out.println("startPanel Has been removed");
        }
    };

    ActionListener ac2 = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            StyledDocument doc = textPane.getStyledDocument();

            SimpleAttributeSet left = new SimpleAttributeSet();
            StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);

            txt = textField.getText();
            textField.setText("");

            try {
                doc.insertString(doc.getLength(),"\n" + txt, left);
                doc.setParagraphAttributes(doc.getLength(), 5, left, false);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }

            System.out.println(txt);

            app.controlSystem();
        }
    };


}
