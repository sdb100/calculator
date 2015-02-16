package arm;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTests {

    Calculator c;

    @Before
    public void setUp() {
        this.c = new CalculatorImpl();
    }

    @Test
    public void testEmptyString() {
        Assert.assertEquals("Empty string did not return zero", 0, c.add(""));
    }

    @Test
    public void testOneNumber() {
        Assert.assertEquals("One number should return itself", 5, c.add("5"));
    }

    @Test
    public void testTwoNumbers() {
        Assert.assertEquals("Two numbers should sum", 10, c.add("5,5"));
    }

    @Test
    public void testArbitraryNumberSet() {
        // Add a random number of random numbers, a random number of times
        Random g = new Random();

        // Do this a random number (between 1 and 11) of times
        int iterations = g.nextInt(10) + 1;
        while (iterations > 0) {
            // The number of numbers to add - between 0 and 100
            int noOfNos = g.nextInt(100);

            // This will hold the string to pass to the function
            StringBuffer sb = new StringBuffer();

            // Create a string buffer with all the numbers in it
            Integer runningTotal = new Integer(0);
            while (noOfNos > 0) {
                // Get a number between 0 and 1000
                Integer n = g.nextInt(1000);
                runningTotal = runningTotal + n;
                sb.append(n.toString());
                sb.append(",");
                noOfNos--;
            }
            // System.out.println(sb);
            Assert.assertEquals("Sum was incorrect", runningTotal.intValue(), c.add(sb.toString()));
            iterations--;
        }
    }

    @Test
    public void testNLSeparator() {
        Assert.assertEquals("Newline sum incorrect", 10, c.add("7\n3"));
        Assert.assertEquals("Newline sum incorrect", 6, c.add("1\n2,3"));
    }

    @Test
    public void testDefinedDelimiter() {
        Assert.assertEquals("Defined delimiter incorrect", 6, c.add("//a\n1a2a3"));
        Assert.assertEquals("Defined delimiter incorrect", 6, c.add("//;\n1;2;3"));
        Assert.assertEquals("Defined delimiter incorrect", 10, c.add("//\n\n1\n2\n3\n4"));
    }

    @Test
    public void negativesDisallowed() {
        try {
            c.add("1,2,-3,4");
            Assert.fail("Negatives should throw exception");
        } catch (Exception e) {
            Assert.assertEquals("Exception message incorrect", "negatives not allowed: -3", e.getMessage());
        }
    }

    @Test
    public void multipleNegativesDisallowed() {
        try {
            c.add("1,2,-3,4,-5,-6,7,8");
            Assert.fail("Negatives should throw exception");
        } catch (Exception e) {
            Assert.assertEquals("Exception message incorrect", "negatives not allowed: -3 -5 -6", e.getMessage());
        }
    }

    @Test
    public void ignoreGreaterThanThousand() {
        Assert.assertEquals("Numbers greater that 1000 should not be added", 2, c.add("2,1001"));
        Assert.assertEquals("Numbers greater that 1000 should not be added", 1004, c.add("1000,1001,2,1001,2"));
    }

    @Test
    public void testArbitraryLengthDelimiter() {
        Assert.assertEquals("Arbitrary delimiter incorrect", 6, c.add("//[my delimiter]\n1my delimiter2my delimiter3"));
        Assert.assertEquals("Arbitrary delimiter incorrect", 6, c.add("//[**]\n1**2**3"));
    }

    @Test
    public void testMultipleArbitraryLengthDelimiters() {
        Assert.assertEquals("Multiple arbitrary delimiter incorrect", 10, c.add("//[my delimiter][**][;]\n1my delimiter2**3;4"));
    }
}
