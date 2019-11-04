package com.flamingo.comeon.spec;

import java.util.function.Predicate;

/**
 * Simple implementation based on Predicate.
 *
 * @param <T>
 * @author wyh
 */
public class PredicateSpecification<T> extends AbstractSpecification<T> {

    private Predicate<T> predicate;
    // private String desc;

    public PredicateSpecification(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean isSatisfiedBy(T target) {
        return predicate.test(target);
    }

    @Override
    public Specification<T> and(Specification<T> other) {
        if (other instanceof PredicateSpecification) {
            return new PredicateSpecification(predicate.and(((PredicateSpecification<T>) other).predicate));
        }
        return super.and(other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        if (other instanceof PredicateSpecification) {
            return new PredicateSpecification(predicate.or(((PredicateSpecification<T>) other).predicate));
        }
        return super.and(other);
    }

    @Override
    public Specification<T> not() {
        return new PredicateSpecification<>(predicate.negate());
    }
}
