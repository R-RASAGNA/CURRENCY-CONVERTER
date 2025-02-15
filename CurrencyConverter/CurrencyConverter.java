import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/";

    public static double getExchangeRate(String baseCurrency, String targetCurrency) throws IOException {
        URL url = new URL(API_URL + baseCurrency);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Error: HTTP Response Code " + responseCode);
        }

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder jsonStr = new StringBuilder();
        while (scanner.hasNext()) {
            jsonStr.append(scanner.nextLine());
        }
        scanner.close();

        JSONObject json = new JSONObject(jsonStr.toString());
        return json.getJSONObject("rates").getDouble(targetCurrency);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.next().toUpperCase();

        System.out.print("Enter target currency (e.g., EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        try {
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
            double convertedAmount = amount * exchangeRate;
            System.out.printf("Converted Amount: %.2f %s%n", convertedAmount, targetCurrency);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}

