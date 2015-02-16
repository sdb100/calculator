package arm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link Calculator} class.
 *
 */
public class CalculatorImpl implements Calculator {

    /**
     * {@inheritDoc}
     */
    @Override
    public int add(String numbers) {
        String delimiterRegex = "[,\n]";
        String numbersToAdd = numbers;

        // Find out if there's a user-defined delimiter
        if (numbersToAdd.startsWith("//")) {
            Pattern p = Pattern.compile("//(.+)\n(.*)");
            Matcher m = p.matcher(numbersToAdd);

            if (m.matches()) {
                String delimiter = m.group(1);
                numbersToAdd = m.group(2);
                if (!delimiter.isEmpty()) {
                    // Try to match square brackets for groups
                    p = Pattern.compile("\\[(.+)\\]+");
                    m = p.matcher(delimiter);
                    if (m.matches()) {
                        StringBuffer ds = new StringBuffer();
                        // May be multiple groups
                        for (int i = 1; i < m.groupCount(); i++) {
                            if (i > 1) {
                                ds.append("|");
                            }
                            ds.append(Pattern.quote(m.group(i)));
                        }
                        delimiterRegex = ds.toString();

                    } else {
                        delimiterRegex = Pattern.quote(delimiter);
                    }
                }
            }
        }

        List<String> sNumbers = new ArrayList<String>();

        // Get the string representations
        Collections.addAll(sNumbers, numbersToAdd.trim().split(delimiterRegex));

        // Parse them to integers (ignore errors here, except for negatives)
        // Sum at the same time
        Integer total = new Integer(0);
        List<String> negatives = new ArrayList<String>();
        for (String sNumber : sNumbers) {
            try {
                Integer i = Integer.parseInt(sNumber.trim());
                if (i < 0) {
                    negatives.add(i.toString());
                }
                total = this.sum(total, i);
            } catch (NumberFormatException e) {
                // Catches non-numbers including the empty string case - just
                // ignore
            }
        }

        // Throw error if any negatives
        if (!negatives.isEmpty()) {
            StringBuffer negs = new StringBuffer();
            negatives.forEach(neg -> {
                negs.append(" ").append(neg);
            });
            throw new RuntimeException("negatives not allowed:" + negs);
        }

        return total;
    }

    /**
     * Sum two Integers, ignoring values over 1000.
     * 
     * @param a
     *            lhs integer
     * @param b
     *            rhs integer
     * @return the sum
     */
    private Integer sum(Integer a, Integer b) {
        if (b > 1000) {
            return a;
        } else {
            return a + b;
        }
    }

}
