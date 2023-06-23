package ananta.utility.lambdas;

import java.util.function.Function;

public class FluentStatement<T, P extends MorePredicates<T>> {

    public final P is;
    public final P isNot;

    FluentStatement(final P is, final P isNot) {
        this.is = is;
        this.isNot = isNot;
    }

    public FluentStatement<T, P> map(final Function<T, T> function) {
        return new FluentStatement<>(is.cloneWithMapper(function), isNot.cloneWithMapper(function));
    }

}
