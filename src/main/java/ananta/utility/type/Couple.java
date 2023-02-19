package ananta.utility.type;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Couple<L, R> {

    private final L left;
    private final R right;

    private Couple(final @Nullable L left, final @Nullable R right) {
        this.left = left;
        this.right = right;
    }

    public static <L, R> Couple<L, R> of(final L left, final R right) {
        return new Couple<>(left, right);
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couple<?, ?> couple = (Couple<?, ?>) o;
        return Objects.equals(left, couple.left) && Objects.equals(right, couple.right);
    }
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.left, this.right);
    }
}
