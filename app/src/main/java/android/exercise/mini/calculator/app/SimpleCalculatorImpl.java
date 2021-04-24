package android.exercise.mini.calculator.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class SimpleCalculatorImpl implements SimpleCalculator {

  // todo: add fields as needed
  private static final String PLUS = "+";
  private static final String MINUS = "-";


  ArrayList<String> rawInput;
  ArrayList<String> numbersInput;
  boolean noInputGiven;

  public SimpleCalculatorImpl(){
    clear();
  }

  @Override
  public String output() {

    String out = "";
    for (String input : this.rawInput)
    {
      out = out.concat(input);
    }

    return out;
  }

  @Override
  public void insertDigit(int digit) {

    if (isDigit(String.valueOf(digit))) {
      if (noInputGiven) {
        this.rawInput.clear();
      }
      this.rawInput.add(String.valueOf(digit));
      noInputGiven = false;

    }
    else{
      throw new RuntimeException("Calculator expect a digit 0-9");
    }
  }

  @Override
  public void insertPlus() {
    String lastInput = this.rawInput.get(this.rawInput.size() - 1);
    if (isDigit(lastInput))
    {
      this.rawInput.add(PLUS);
    }
    noInputGiven = false;
  }

  @Override
  public void insertMinus() {
    String lastInput = this.rawInput.get(this.rawInput.size() - 1);
    if (isDigit(lastInput))
    {
      this.rawInput.add(MINUS);
    }
    noInputGiven = false;
  }

  @Override
  public void insertEquals() {
    // todo: calculate the equation. after calling `insertEquals()`, the output should be the result
    //  e.g. given input "14+3", calling `insertEquals()`, and calling `output()`, output should be "17"

    UnifyDigits();
    String result = calculateInput();
    clearHelper(false);

    this.rawInput = new ArrayList<>(Arrays.asList(result.split("")));
  }

  @Override
  public void deleteLast() {
    // todo: delete the last input (digit, plus or minus)
    //  e.g.
    //  if input was "12+3" and called `deleteLast()`, then delete the "3"
    //  if input was "12+" and called `deleteLast()`, then delete the "+"
    //  if no input was given, then there is nothing to do here

    if (noInputGiven){
      return;
    }

    if (this.rawInput.size() > 0){

      this.rawInput.remove(this.rawInput.size() - 1);
      if (this.rawInput.size() == 0)
      {
        clear();
      }
    }
  }

  @Override
  public void clear() {

    clearHelper(true);

    // todo: clear everything (same as no-input was never given)
  }

  private void clearHelper(boolean hardReset)
  {
    this.rawInput = new ArrayList<>();
    this.numbersInput = new ArrayList<>();

    this.rawInput.clear();
    this.numbersInput.clear();

    if (hardReset){
      this.noInputGiven = true;
      this.rawInput.add("0");
    }
  }

  @Override
  public Serializable saveState() {
    CalculatorState state = new CalculatorState();
    // todo: insert all data to the state, so in the future we can load from this state
    state.setRawInput(this.rawInput);
    state.setNoInputGiven(this.noInputGiven);

    return state;
  }

  @Override
  public void loadState(Serializable prevState) {
    if (!(prevState instanceof CalculatorState)) {
      return; // ignore
    }
    CalculatorState casted = (CalculatorState) prevState;
    this.rawInput = casted.getRawInput();
    this.noInputGiven = casted.getNoInputGiven();

  }


  private void UnifyDigits()
  {
    this.numbersInput = new ArrayList<>();
    int singleInputIdx = 0;

    while (singleInputIdx < this.rawInput.size()){

      String singleInput = this.rawInput.get(singleInputIdx);
      if (isDigit(singleInput)){
        String multiDigit = singleInput;
        singleInputIdx++;
        while(singleInputIdx < this.rawInput.size() && isDigit(this.rawInput.get(singleInputIdx)))
        {
          multiDigit = multiDigit.concat(rawInput.get(singleInputIdx));
          singleInputIdx++;
        }
        this.numbersInput.add(multiDigit);
      }
      else{
        this.numbersInput.add(singleInput);
        singleInputIdx++;
      }
    }
  }

  private String calculateInput()
  {
    String result = "0";
    int startExpression = 0;

    if (this.numbersInput.size() > 0) {
      String firstInput = this.numbersInput.get(0);
      if (isNumber(firstInput)) {
        result = firstInput;
        startExpression++;
      }

      for (int inputIdx = startExpression; inputIdx < this.numbersInput.size() - 1; inputIdx += 2) {
        String operator = this.numbersInput.get(inputIdx);
        String rhs = this.numbersInput.get(inputIdx + 1);

        if (assertInputType(operator, false) ||
                assertInputType(rhs, true)) {
          System.err.println("Error in your code !");
          System.exit(1);
        }
        result = getOperator(operator).run(result, rhs);

      }
    }

    return result;

  }

  private Function2Args<String, String, String> getOperator (String operatorDesc){
    if (operatorDesc.equals(PLUS)) {
      return (String i1, String i2) -> String.valueOf((Integer.parseInt(i1) + Integer.parseInt(i2)));
    }
    else{
      return (String i1, String i2) -> String.valueOf((Integer.parseInt(i1) - Integer.parseInt(i2)));
    }

  }

  private static boolean assertInputType(String input, boolean expectsDigit){
    return (expectsDigit) != isNumber(input);
  }

  private static boolean isNumber(String input){
    return input.matches("\\d+");
  }

  private static boolean isDigit(String input){
    return input.matches("\\b\\d\\b");
  }


  private static class CalculatorState implements Serializable {
    /*
    TODO: add fields to this class that will store the calculator state
    all fields must only be from the types:
    - primitives (e.g. int, boolean, etc)
    - String
    - ArrayList<> where the type is a primitive or a String
    - HashMap<> where the types are primitives or a String
     */

    ArrayList<String> rawInput;
    boolean noInputGiven;

    public void setRawInput(ArrayList<String> rawInput){
      this.rawInput = new ArrayList<>(rawInput);
    }

    public ArrayList<String> getRawInput(){
      return this.rawInput;
    }

    public void setNoInputGiven(boolean noInputGiven) {
      this.noInputGiven = noInputGiven;
    }

    public boolean getNoInputGiven() {
      return noInputGiven;
    }
  }


  @FunctionalInterface
  public interface Function2Args<T1, T2, R> {

    R run(T1 t1, T2 t2);

  }


}
