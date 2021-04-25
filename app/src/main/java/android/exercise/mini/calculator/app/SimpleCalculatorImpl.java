package android.exercise.mini.calculator.app;

import java.io.Serializable;
import java.util.ArrayList;


public class SimpleCalculatorImpl implements SimpleCalculator {

  private static final Character PLUS = '+';
  private static final Character MINUS = '-';

  private static final Character NO_INPUT = '0';

  private static final String NUMBER_PATTERN = "\\d+";
  private static final String LIST_TO_STRING_IRRELEVANT_CHARS = "[,\\s\\[\\]]";
  private static final String REMOVE = "";

  ArrayList<Character> rawInput;
  ArrayList<String> numbersInput;
  boolean noInputGiven;

  public SimpleCalculatorImpl(){
    clear();
  }

  @Override
  public String output() {

    return this.rawInput.toString()
            .replaceAll(LIST_TO_STRING_IRRELEVANT_CHARS, REMOVE);
  }

  @Override
  public void insertDigit(int digit) {

    char asChar = Character.forDigit(digit, 10);

      if (Character.isDigit(asChar)) {      // checks the digit is indeed digit
      if (noInputGiven) {
        this.rawInput.clear();      // clear the 0 ("No input") from the screen
      }
      this.rawInput.add(asChar);
      noInputGiven = false;
    }
    else{
      throw new RuntimeException("Calculator expect a digit 0-9");
    }
  }

  @Override
  public void insertPlus() {
    char lastInput = this.rawInput.get(this.rawInput.size() - 1);
    if (Character.isDigit(lastInput))   // Avoiding insert order after order
    {
      this.rawInput.add(PLUS);
    }
    noInputGiven = false;
  }

  @Override
  public void insertMinus() {
    char lastInput = this.rawInput.get(this.rawInput.size() - 1);
    if (Character.isDigit(lastInput))   // Avoiding insert order after order
    {
      this.rawInput.add(MINUS);
    }
    noInputGiven = false;
  }

  @Override
  public void insertEquals() {

    // Calculate result
    unifyDigits();
    String result = calculateInput();

    // Clear calculating history
    clearHelper(false);

    // Put result as the only item in history
    this.rawInput = toChrList(result);
  }

  @Override
  public void deleteLast() {

    if (noInputGiven){
      return;
    }

    if (this.rawInput.size() > 0){
      // Removing last input
      this.rawInput.remove(this.rawInput.size() - 1);

      // If this removal empties all input - clear calculator
      if (this.rawInput.size() == 0)
      {
        clear();
      }
    }
  }

  @Override
  public void clear() {

    clearHelper(true);

  }

  /**
   * Resets calculator attributes. if hardReset, the noInput flag is turned on and
   * raw input is updated accordingly.
   * @param hardReset
   */
  private void clearHelper(boolean hardReset)
  {
    this.rawInput = new ArrayList<>();
    this.numbersInput = new ArrayList<>();

    this.rawInput.clear();
    this.numbersInput.clear();

    if (hardReset){
      this.noInputGiven = true;
      this.rawInput.add(NO_INPUT);
    }
  }

  @Override
  public Serializable saveState() {
    CalculatorState state = new CalculatorState();
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

  /***
   * Converts rawInput from a list of chars to a list of strings.
   * For example ['3', '5', '-', '2'] -> ["35", "+", "2"]
   * This will result in a list of string that it's structure is numbers/orders intermittently.
   * Meaning number-order-number..... / order-number-order....
   * This structure will be used in CalculateInput method
   */
  private void unifyDigits()
  {
    this.numbersInput = new ArrayList<>();
    int singleInputIdx = 0;

    while (singleInputIdx < this.rawInput.size()){

      Character singleInput = this.rawInput.get(singleInputIdx);
      if (Character.isDigit(singleInput)){
        String multiDigit = singleInput.toString();
        singleInputIdx++;
        while(singleInputIdx < this.rawInput.size() &&
              Character.isDigit(this.rawInput.get(singleInputIdx)))
        {
          multiDigit = multiDigit.concat(rawInput.get(singleInputIdx).toString());
          singleInputIdx++;
        }
        this.numbersInput.add(multiDigit);
      }
      else{
        this.numbersInput.add(singleInput.toString());
        singleInputIdx++;
      }
    }
  }

  /***
   * Calculating input using numbersInput String List.
   * We assume that in this list, the numbers and orders are appearing intermittently.
   * Meaning number-order-number.... / order-number-order....
   *
   * @return String represent to result of the mathematical expression
   */
  private String calculateInput()
  {
    String result = NO_INPUT.toString();
    int startExpression = 0;

    // Aligning input to start with an order
    if (this.numbersInput.size() > 0) {
      String firstInput = this.numbersInput.get(0);
      if (isNumber(firstInput)) {
        result = firstInput;
        startExpression++;
      }

      // Going over input is pairs of (order, number), and applying the order on the number and
      // the accumulated result
      for (int inputIdx = startExpression; inputIdx < this.numbersInput.size() - 1; inputIdx += 2) {
        String operator = this.numbersInput.get(inputIdx);
        String rhs = this.numbersInput.get(inputIdx + 1);

        // This code should not be accessed if program flow is correct
        if (assertInputType(operator, false) ||
                assertInputType(rhs, true)) {
          System.err.println("Error in your code !");
          System.exit(1);
        }

        result = getOperator(operator).apply(result, rhs);
      }
    }
    return result;
  }

  /***
   * Given a operator descriptor (PLUS/MINUS) returns a integer addition / subtraction function
   * that are represented using strings
   * @param operatorDesc
   * @return
   */
  private Function2Args<String, String, String> getOperator (String operatorDesc){

    if (operatorDesc.charAt(0) == PLUS) {
      return (String i1, String i2) -> String.valueOf((Integer.parseInt(i1) + Integer.parseInt(i2)));
    }
    else{
      return (String i1, String i2) -> String.valueOf((Integer.parseInt(i1) - Integer.parseInt(i2)));
    }

  }

  /***
   * Validating input type. We assume that input is a string representing an operator (+/-) or
   * a number. Function return true if input as expected (referring to expectsNumber variable),
   * false, otherwise
   * @param input
   * @param expectsNumber
   * @return
   */
  private static boolean assertInputType(String input, boolean expectsNumber){
    return (expectsNumber) != isNumber(input);
  }

  /***
   * Returns True if given string represents a number
   * @param input - string
   * @return
   */
  private static boolean isNumber(String input){
    return input.matches(NUMBER_PATTERN);
  }

  /***
   * Converts a string to ArrayList of Characters
   * @param str - string to convert
   * @return array list of characters
   */
  private static ArrayList<Character> toChrList(String str){

    ArrayList<Character> chrList = new ArrayList<>();
    for (char chr : str.toCharArray())
    {
      chrList.add(chr);
    }
    return chrList;
  }


  private static class CalculatorState implements Serializable {

    ArrayList<Character> rawInput;
    boolean noInputGiven;

    public void setRawInput(ArrayList<Character> rawInput){
      this.rawInput = new ArrayList<>(rawInput);
    }

    public ArrayList<Character> getRawInput(){
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

    R apply(T1 t1, T2 t2);

  }


}
