package android.exercise.mini.calculator.app;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

  @VisibleForTesting
  public SimpleCalculator calculator;
  private static final String STATE_KEY  = "CalculatorState";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (calculator == null) {
      calculator = new SimpleCalculatorImpl();
    }


    // find and arrange views
    TextView plusButton = findViewById(R.id.buttonPlus);
    TextView minusButton = findViewById(R.id.buttonMinus);
    TextView equalsButton = findViewById(R.id.buttonEquals);
    TextView clearButton = findViewById(R.id.buttonClear);
    View backSpaceButton = findViewById(R.id.buttonBackSpace);
    TextView calculatorOutput = findViewById(R.id.textViewCalculatorOutput);

    TextView button0 = findViewById(R.id.button0);
    TextView button1 = findViewById(R.id.button1);
    TextView button2 = findViewById(R.id.button2);
    TextView button3 = findViewById(R.id.button3);
    TextView button4 = findViewById(R.id.button4);
    TextView button5 = findViewById(R.id.button5);
    TextView button6 = findViewById(R.id.button6);
    TextView button7 = findViewById(R.id.button7);
    TextView button8 = findViewById(R.id.button8);
    TextView button9 = findViewById(R.id.button9);

    TextView[] buttons = {button0, button1, button2, button3, button4, button5, button6,
                          button7, button8, button9};

    // Set initial view
    calculatorOutput.setText(calculator.output());


    // Set on click listeners
    for (int i = 0; i < 10; i++)
    {
      int finalI = i;
      buttons[i].setOnClickListener(v->{
        calculator.insertDigit(finalI);
        calculatorOutput.setText(calculator.output());
      });
    }


    plusButton.setOnClickListener(v->{
      calculator.insertPlus();
      calculatorOutput.setText(calculator.output());
    });

    minusButton.setOnClickListener(v->{
      calculator.insertMinus();
      calculatorOutput.setText(calculator.output());
    });

    equalsButton.setOnClickListener(v->{
      calculator.insertEquals();
      calculatorOutput.setText(calculator.output());
    });

    backSpaceButton.setOnClickListener(v->{
      calculator.deleteLast();
      calculatorOutput.setText(calculator.output());
    });

    clearButton.setOnClickListener(v->{
      calculator.clear();
      calculatorOutput.setText(calculator.output());
    });

  }


  @Override
  protected void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putSerializable(STATE_KEY, calculator.saveState());

  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    calculator.loadState(savedInstanceState.getSerializable(STATE_KEY));
  }
}