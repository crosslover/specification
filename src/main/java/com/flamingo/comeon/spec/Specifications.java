package com.flamingo.comeon.spec;

import com.flamingo.comeon.spec.util.AssertUtil;
import com.flamingo.comeon.spec.util.CollectionUtil;

import java.util.function.Predicate;

/**
 * Util for Specifications
 *
 * @author wyh
 */
public final class Specifications {
    private Specifications() {
    }

    /**
     * create a {@link Specification} by a Predicate
     *
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> Specification<T> s(Predicate<T> predicate) {
        return new PredicateSpecification<>(predicate);
    }

    /**
     * create a {@link Specification} aggregating specifications by AND relation.
     *
     * @param specifications
     * @param <T>
     * @return
     */
    public static <T> Specification<T> and(Specification<T>... specifications) {
        assertSpecifications(specifications);
        if (specifications.length == 1) {
            return specifications[0];
        }
        if (specifications.length == 2) {
            return specifications[0].and(specifications[1]);
        }
        return new CompositeSpecification<>(CollectionUtil.newArrayList(specifications));
    }

    /**
     * create a {@link Specification} aggregating specifications by OR relation.
     *
     * @param specifications
     * @param <T>
     * @return
     */
    public static <T> Specification<T> or(Specification<T>... specifications) {
        assertSpecifications(specifications);
        if (specifications.length == 1) {
            return specifications[0];
        }
        if (specifications.length == 2) {
            return specifications[0].or(specifications[1]);
        }
        return new CompositeSpecification<>(CollectionUtil.newArrayList(specifications), CompositeSpecification.Operator.OR);
    }

    /**
     * create a negate {@link Specification}.
     *
     * @param specification
     * @param <T>
     * @return
     */
    public static <T> Specification<T> not(Specification<T> specification) {
        AssertUtil.notNull(specification, "specification cannot be null");
        return specification.not();
    }

    /**
     * {@link Specification}'s version for {@code s}
     *
     * @param specification
     * @param success
     * @param fail
     * @param <M>
     * @param <T>
     * @return
     */
    public static <M, T> MessageSpecification<M, T> ms(Specification<T> specification, M success, M fail) {
        return new GenericMessageSpecification<>(specification, success, fail);
    }

    /**
     * {@link Specification}'s version for {@code and}
     *
     * @param specifications
     * @param <M>
     * @param <T>
     * @return
     */
    public static <M, T> MessageSpecification<M, T> and(MessageSpecification<M, T>... specifications) {
        assertSpecifications(specifications);
        if (specifications.length == 1) {
            return specifications[0];
        }
        if (specifications.length == 2) {
            return specifications[0].and(specifications[1]);
        }
        return new CompositeMessageSpecification<>(CollectionUtil.newArrayList(specifications));
    }

    /**
     * {@link Specification}'s version for {@code or}
     *
     * @param specifications
     * @param <M>
     * @param <T>
     * @return
     */
    public static <M, T> MessageSpecification<M, T> or(MessageSpecification<M, T>... specifications) {
        assertSpecifications(specifications);
        if (specifications.length == 1) {
            return specifications[0];
        }
        if (specifications.length == 2) {
            return specifications[0].or(specifications[1]);
        }
        return new CompositeMessageSpecification<>(CollectionUtil.newArrayList(specifications));
    }

    /**
     * {@link Specification}'s version for {@code not}
     *
     * @param specification
     * @param <M>
     * @param <T>
     * @return
     */
    public static <M, T> MessageSpecification<M, T> or(MessageSpecification<M, T> specification) {
        AssertUtil.notNull(specification, "specification cannot be null");
        return specification.not();
    }

    public static <T> void assertSpecifications(Specification<T>[] specifications) {
        AssertUtil.notNull(specifications, "specifications cannot be null");
        AssertUtil.noNullElements(specifications, "specifications cannot have null element");
        AssertUtil.notEmpty(specifications, "specifications cannot be empty");
    }
}
