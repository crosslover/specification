package com.flamingo.comeon.spec;

import com.flamingo.comeon.spec.util.AssertUtil;
import com.flamingo.comeon.spec.util.StringUtil;

import java.util.List;

/**
 * {@link Specification<T>}'s composite implementation.
 *
 * @author wyh
 */
public class CompositeSpecification<S extends Specification<T>, T> extends AbstractSpecification<T> {
    protected List<S> leafSpecifications;
    protected Operator operator = Operator.AND;
    private boolean shortCircuit = true;

    public CompositeSpecification(List<S> leafSpecifications) {
        AssertUtil.notNull(leafSpecifications);
        this.leafSpecifications = leafSpecifications;
    }

    public CompositeSpecification(List<S> leafSpecifications, Operator operator) {
        this(leafSpecifications);
        AssertUtil.notNull(operator);
        this.operator = operator;
    }

    @Override
    public boolean isSatisfiedBy(T target) {
        boolean result = true;
        if (operator == Operator.AND) {
            for (S specification : leafSpecifications) {
                result = (result && specification.isSatisfiedBy(target));
                postLeafExecutedHook(result, specification);
                if (!result && shortCircuit) {
                    beforeShortCircuitHook(result, specification);
                    return false;
                }
            }
            return result;
        }

        result = false;
        if (operator == Operator.OR) {
            for (S specification : leafSpecifications) {
                result = (result || specification.isSatisfiedBy(target));
                postLeafExecutedHook(result, specification);
                if (result && shortCircuit) {
                    beforeShortCircuitHook(result, specification);
                    return true;
                }
            }
            return result;
        }
        return false;
    }

    @Override
    public Specification<T> and(Specification<T> other) {
        if (operator == Operator.AND) {
            leafSpecifications.add((S) other);
            return this;
        }
        return super.and(other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        if (operator == Operator.OR) {
            leafSpecifications.add((S) other);
            return this;
        }
        return super.or(other);
    }

    @Override
    public String toString() {
        return "(" + StringUtil.join(leafSpecifications, operator == Operator.AND ? " AND " : " OR ") + ")";
    }

    public void setShortCircuit(boolean shortCircuit) {
        this.shortCircuit = shortCircuit;
    }

    protected void postLeafExecutedHook(boolean result, S specification) {
    }

    protected void beforeShortCircuitHook(boolean result, S leaf) {
    }


}
