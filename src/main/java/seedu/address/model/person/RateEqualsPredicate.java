package seedu.address.model.person;

import java.util.function.Predicate;

/**
 * Tests whether a {@code Person}'s rate matches the specified rate.
 */
public class RateEqualsPredicate implements Predicate<Person> {

    private final Rate rate;

    public RateEqualsPredicate(Rate rate) {
        this.rate = rate;
    }

    @Override
    public boolean test(Person person) {
        return person.getRate().equals(rate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RateEqualsPredicate)) {
            return false;
        }
        RateEqualsPredicate otherPredicate = (RateEqualsPredicate) other;
        return rate.equals(otherPredicate.rate);
    }
}
