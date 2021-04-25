package test.android.exercise.mini.calculator.app;

import android.exercise.mini.calculator.app.SimpleCalculatorImpl;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class SimpleCalculatorImplTest {

  @Test
  public void when_noInputGiven_then_outputShouldBe0(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    assertEquals("0", calculatorUnderTest.output());
  }

  @Test
  public void when_inputIsPlus_then_outputShouldBe0Plus(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    calculatorUnderTest.insertPlus();
    assertEquals("0+", calculatorUnderTest.output());
  }


  @Test
  public void when_inputIsMinus_then_outputShouldBeCorrect(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    calculatorUnderTest.insertMinus();
    String expected = "0-"; // TODO: decide the expected output when having a single minus
    assertEquals(expected, calculatorUnderTest.output());
  }

  @Test
  public void when_callingInsertDigitWithIllegalNumber_then_exceptionShouldBeThrown(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    try {
      calculatorUnderTest.insertDigit(357);
      fail("should throw an exception and not reach this line");
    } catch (RuntimeException e) {
      // good :)
    }
  }


  @Test
  public void when_callingDeleteLast_then_lastOutputShouldBeDeleted(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    // give some input
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(6);
    calculatorUnderTest.deleteLast();

    assertEquals("5+73", calculatorUnderTest.output());

  }

  @Test
  public void when_callingDeleteLastOnEmptyList_then_NothingShouldHappen(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    assertEquals("0", calculatorUnderTest.output());

  }

  @Test
  public void when_callingDeleteLastOneInputList_then_Show0(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.deleteLast();

    assertEquals("0", calculatorUnderTest.output());

  }

  @Test
  public void when_callingClear_then_outputShouldBeCleared(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    // give some input
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(6);

    calculatorUnderTest.clear();
    assertEquals("0", calculatorUnderTest.output());

  }

  @Test
  public void when_savingState_should_loadThatStateCorrectly(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();
    // give some input
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);

    // save current state
    Serializable savedState = calculatorUnderTest.saveState();
    assertNotNull(savedState);

    // call `clear` and make sure calculator cleared
    calculatorUnderTest.clear();
    assertEquals("0", calculatorUnderTest.output());

    // load the saved state and make sure state was loaded correctly
    calculatorUnderTest.loadState(savedState);
    assertEquals("5+7", calculatorUnderTest.output());
  }

  @Test
  public void when_savingStateFromFirstCalculator_should_loadStateCorrectlyFromSecondCalculator(){
    SimpleCalculatorImpl firstCalculator = new SimpleCalculatorImpl();
    SimpleCalculatorImpl secondCalculator = new SimpleCalculatorImpl();

    // first calc output is 5+7
    firstCalculator.insertDigit(5);
    firstCalculator.insertPlus();
    firstCalculator.insertDigit(7);

    // second calc output is 5+7
    secondCalculator.insertDigit(2);
    secondCalculator.insertPlus();
    secondCalculator.insertDigit(3);

    // save current state
    Serializable savedState = firstCalculator.saveState();
    assertNotNull(savedState);

    // load state to second calculator (out should be 5+7)
    secondCalculator.loadState(savedState);

    assertEquals("5+7", secondCalculator.output());

  }

  @Test
  public void when_pressingDeleteLastInMiddleOfNumber_ShouldContinueThisNumber(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(1);
    calculatorUnderTest.insertDigit(3);
    // At this point output is 5+7-13

    // Now deleting 3 so it's 5+7+1
    calculatorUnderTest.deleteLast();

    // Now inserting 25
    calculatorUnderTest.insertDigit(2);
    calculatorUnderTest.insertDigit(5);

    assertEquals("5+7-125", calculatorUnderTest.output());

  }

  @Test
  public void when_enteringOrderAfterOrder_should_Ignore(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertMinus();
    // order after order - should ignore plus and use minus instead
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(8);

    assertEquals("5+7-8", calculatorUnderTest.output());

  }

  @Test
  public void basic_equality(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    // "5+7="
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertEquals();

    assertEquals("12", calculatorUnderTest.output());

  }

  @Test
  public void multiDigit_equality(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    // "33+55="

    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertEquals();

    assertEquals("88", calculatorUnderTest.output());

  }

  @Test
  public void whenLastInputBeforeEqualsIsOrder_should_Ignore(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    // "5+7-="
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertEquals();

    assertEquals("12", calculatorUnderTest.output());

  }

  @Test
  public void whenFirstInputIsMinus_should_calculateCorrectly(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    // "-5+7="
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertEquals();

    assertEquals("2", calculatorUnderTest.output());

  }

  @Test
  public void whenFirstInputIsPlus_should_calculateCorrectly(){
    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    // "+5+7="
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(5);
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertEquals();

    assertEquals("12", calculatorUnderTest.output());

  }

  @Test
  public void whenEqualsInMiddleOfInput_should_calculateCorrectly(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    // "8-7=+4=-1="
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertEquals();
    calculatorUnderTest.insertPlus();
    calculatorUnderTest.insertDigit(4);
    calculatorUnderTest.insertEquals();
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(1);
    calculatorUnderTest.insertEquals();

    assertEquals("4", calculatorUnderTest.output());

  }

  @Test
  public void whenEqualsInMiddleOfInputMultiDigit_should_calculateAndOutputCorrectly(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    //"999-888-222=-333"
    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(2);
    calculatorUnderTest.insertDigit(2);
    calculatorUnderTest.insertDigit(2);

    calculatorUnderTest.insertEquals();
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(3);

    assertEquals("-111-333", calculatorUnderTest.output());

  }
  @Test
  public void whenEqualsInMiddleOfInputMultiDigit_should_calculateCorrectly(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    //"999-888-222=-333="
    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(2);
    calculatorUnderTest.insertDigit(2);
    calculatorUnderTest.insertDigit(2);

    calculatorUnderTest.insertEquals();
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertDigit(3);
    calculatorUnderTest.insertEquals();

    assertEquals("-444", calculatorUnderTest.output());

  }

  @Test
  public void whenClearAndThenEquals_should_IgnoreBeforeClear(){

    SimpleCalculatorImpl calculatorUnderTest = new SimpleCalculatorImpl();

    calculatorUnderTest.insertDigit(9);
    calculatorUnderTest.deleteLast();
    calculatorUnderTest.insertDigit(1);
    calculatorUnderTest.insertDigit(2);
    calculatorUnderTest.clear();
    calculatorUnderTest.insertDigit(8);
    calculatorUnderTest.insertMinus();
    calculatorUnderTest.insertDigit(7);
    calculatorUnderTest.insertEquals();

    assertEquals("1", calculatorUnderTest.output());

  }
}