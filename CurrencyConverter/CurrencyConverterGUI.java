import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONObject;

public class CurrencyConverterGUI extends JFrame {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    private JComboBox<String> fromCurrency, toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;

    public CurrencyConverterGUI() {
        setTitle("Currency Converter");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Currency options
        String[] currencies = {"USD", "EUR", "INR", "GBP", "CAD", "AUD", "JPY"};

        // UI Components
        add(new JLabel("From:"));
        fromCurrency = new JComboBox<>(currencies);
        add(fromCurrency);

        add(new JLabel("To:"));
        toCurrency = new JComboBox<>(currencies);
        add(toCurrency);

        add(new JLabel("Amount:"));
        amountField = new JTextField();
        add(amountField);

        JButton convertButton = new JButton("Convert");
        add(convertButton);

        resultLabel = new JLabel("Converted Amount: ");
        add(resultLabel);

        // Action Listener for Button
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        setVisible(true);
    }

    private void convertCurrency() {
        try {
            String base = (String) fromCurrency.getSelectedItem();
            String target = (String) toCurrency.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            double exchangeRate = getExchangeRate(base, target);
            double convertedAmount = amount * exchangeRate;

            resultLabel.setText("Converted: " + String.format("%.2f", convertedAmount) + " " + target);
        } catch (Exception e) {
            resultLabel.setText("Error: " + e.getMessage());
        }
    }

    private double getExchangeRate(String base, String target) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + base))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        return json.getJSONObject("rates").getDouble(target);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CurrencyConverterGUI());
    }
}

