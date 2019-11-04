package com.flamingo.comeon.spec;

/**
 * Basic Specification interface, typically you should use {@link AbstractSpecification} to extend your application.
 *
 * @author wyh
 * @since 2019-10-10
 */
public interface Specification<T> {

    /**
     * if the target object is satisfied by this Specification.
     *
     * @param target
     * @return
     */
    boolean isSatisfiedBy(T target);

    /**
     * And operate
     *
     * @param other
     * @return
     */
    Specification<T> and(Specification<T> other);

    /**
     * Or operate
     *
     * @param other
     * @return
     */
    Specification<T> or(Specification<T> other);

    /**
     * not operate
     *
     * @return
     */
    Specification<T> not();

}
