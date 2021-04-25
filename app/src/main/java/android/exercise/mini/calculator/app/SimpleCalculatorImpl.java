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
    if (Character.isDigit(asChar)) {
      if (noInputGiven) {
        this.rawInput.clear();
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
    if (Character.isDigit(lastInput))
    {
      this.rawInput.add(PLUS);
    }
    noInputGiven = false;
  }

  @Override
  public void insertMinus() {
    char lastInput = this.rawInput.get(this.rawInput.size() - 1);
    if (Character.isDigit(lastInput))
    {
      this.rawInput.add(MINUS);
    }
    noInputGiven = false;
  }

  @Override
  public void insertEquals() {

    UnifyDigits();
    String result = calculateInput();
    clearHelper(false);

    this.rawInput = toChrList(result);
  }

  @Override
  public void deleteLast() {

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

  }

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


  private void UnifyDigits()
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

  private String calculateInput()
  {
    String result = NO_INPUT.toString();
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

    if (operatorDesc.charAt(0) == PLUS) {
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
    return input.matches(NUMBER_PATTERN);
  }

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

    R run(T1 t1, T2 t2);

  }


}
