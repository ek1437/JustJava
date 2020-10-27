package android.example.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    int price = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        displayQuantity(quantity);

        //For Whipped Cream
        CheckBox checkBox = findViewById(R.id.checkBox);
        boolean whippedCream = checkBox.isChecked();

        //For Chocolate
        CheckBox chocolateCheckBox = findViewById(R.id.checkBoxChocolate);
        boolean chocolate = chocolateCheckBox.isChecked();

        EditText name = findViewById(R.id.textInputEditTextName);
        String customerName = name.getText().toString();


        String orderSummary = createOrderSummary(
                calculatePrice(quantity, price, whippedCream, chocolate),
                whippedCream,
                chocolate,
                customerName
        );

        composeEmail("JustJava order for " + customerName, orderSummary);
    }

    /**
     * Calculates the price of the order.
     *
     * @param quantity        number of coffees
     * @param price           price of each coffee
     * @param addWhippedCream if the user want whipped.
     * @param addChocolate    if the user want to add chocolate
     * @return the total.
     */
    private int calculatePrice(int quantity,
                               int price,
                               boolean addWhippedCream,
                               boolean addChocolate) {

        int basePrice = 0;
        if (addWhippedCream) basePrice += 1;
        if (addChocolate) basePrice += 2;
        if (quantity > 0) basePrice += price;

        return quantity * basePrice;
    }

    /**
     * This method increments the quantity variable and calls the display
     * method with new value
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot have more than 100 coffees!", Toast.LENGTH_LONG).show();
            return;
        }
        displayQuantity(++quantity);

    }

    /**
     * This method decrements the quantity variable and calls the display
     * method with new value
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot have less than 1 coffee!", Toast.LENGTH_LONG).show();
            return;
        }
        displayQuantity(--quantity);
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Creates the order summary to include name
     * quantity and total.
     *
     * @param total           price to include in the summary.
     * @param hasWhippedCream if whippedCream is needed.
     * @param hasChocolate    for Chocolate.
     * @param name            of the person making the order.
     */
    private String createOrderSummary(int total,
                                      boolean hasWhippedCream,
                                      boolean hasChocolate,
                                      String name) {
        return "Name: " + name +
                "\nAdd whipped Cream? " + hasWhippedCream +
                "\nAdd Chocolate? " + hasChocolate +
                "\nQuantity: " + quantity +
                "\nTotal: " + NumberFormat.getCurrencyInstance().format(total) +
                "\nThank You!";

    }

    public void composeEmail(String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}