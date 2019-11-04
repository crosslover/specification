package com.flamingo.comeon.spec;

/**
 * Basic implementation for {@link Specification<T>}, use this for extension.
 *
 * @author wyh
 */
public abstract class AbstractSpecification<T> implements Specification<T> {
    @Override
    public Specification<T> and(Specification<T> other) {
        return new AndSpecification<>(this, other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return new OrSpecification<>(this, other);
    }

    @Override
    public Specification<T> not() {
        return new NotSpecification<>(this);
    }

    private static class AndSpecification<T> extends AbstractSpecification<T> {
        Specification<T> one;
        Specification<T> other;

        protected AndSpecification(AbstractSpecification<T> one, Specification<T> other) {
            this.one = one;
            this.other = other;
        }

        @Override
        public boolean isSatisfiedBy(T target) {
            return one.isSatisfiedBy(target) && other.isSatisfiedBy(target);
        }

        @Override
        public String toString() {
            return "(" + one.toString() + " AND " + other.toString() +  ")";
        }
    }

    private static class OrSpecification<T> extends AbstractSpecification<T> {
        Specification<T> one;
        Specification<T> other;

        protected OrSpecification(AbstractSpecification<T> one, Specification<T> other) {
            this.one = one;
            this.other = other;
        }

        @Override
        public boolean isSatisfiedBy(T target) {
            return one.isSatisfiedBy(target) || other.isSatisfiedBy(target);
        }

        @Override
        public String toString() {
            return "(" + one.toString() + " OR " + other.toString() +  ")";
        }
    }

    private static class NotSpecification<T> extends AbstractSpecification<T> {
        Specification<T> specification;

        protected NotSpecification(AbstractSpecification<T> specification) {
            this.specification = specification;
        }

        @Override
        public boolean isSatisfiedBy(T target) {
            return !specification.isSatisfiedBy(target);
        }

        @Override
        public String toString() {
            return "(NOT " + specification.toString() + ")";
        }
    }
}
