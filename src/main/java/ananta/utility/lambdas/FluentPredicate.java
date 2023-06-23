package ananta.utility.lambdas;

import java.util.Objects;
import java.util.function.Predicate;

public class FluentPredicate<T, P extends MorePredicates<T>> {

    public final P is;
    public final P isNot;

    FluentPredicate(final P is, final P isNot) {
        this.is = is;
        this.isNot = isNot;
    }

    public Predicate<T> isNonNull() {
        return Objects::nonNull;
    }

    public Predicate<T> isNull() {
        return Objects::isNull;
    }

}
