# AndroidCalculator - Calculator exercise for Android developers

## Pledge:
I pledge the highest level of ethical principles in support of academic excellence.
I ensure that all of my work reflects my own abilities and not those of someone else.

## Theoretical question:

Q: Saying we want to add a cool feature - button "x" to run multiplication.
   What code do we need to change/add/remove to support this feature?
   Which tests can we run on the calculator? On the activity? On the app?
   
A:  First, the backend logic of calculating the result out of the string  input, meaning the insertEquals() method, 
    should be changed.
    Right now, the calculation is based on order (since there is no operator priority), and having
    the "x" calculator added we would need to parse the input differently before calculating.
    Furthermore, we would add an insertMultiplication method that can be linked to OnClickListeners
    inside the app UI.
    As for the front-end, the changes are more minor.
    We would need to change the XML file of the main activity and the main activity itself
    to support this button.
    As for the tests : we would check that the calculator (backend) respects the operator priority
    for different kinds of inputs. As for the main activity, the tests would be trivial, checking
    that clicking of the "x" button does what we expected.
    In the flow tests we would combine tests for multiplication that will ensure operator priority
    in the calculation.  