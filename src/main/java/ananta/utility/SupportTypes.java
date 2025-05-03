package ananta.utility;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

class SupportTypes {
    static class FlatZipIterator<L, R> {

        private final Iterator<L> leftIterator;

        private final Function<L, Collection<R>> iteratorProvider;

        private Iterator<R> rightIterator;

        private L currentLeft;


        FlatZipIterator(final Iterator<L> leftIterator, final Function<L, Collection<R>> iteratorProvider) {
            this.leftIterator = leftIterator;
            this.iteratorProvider = iteratorProvider;
            goNextOneInLeftIterator();
        }

        private void goNextOneInLeftIterator() {
            this.currentLeft = this.leftIterator.next();
            this.rightIterator = this.iteratorProvider.apply(this.currentLeft).iterator();
        }

        boolean hasNext() {
            if (this.rightIterator.hasNext()) {
                return true;
            }
            if (this.leftIterator.hasNext()) {
                goNextOneInLeftIterator();
            }
            return this.rightIterator.hasNext();
        }

        L getLeft() {
            return this.currentLeft;
        }

        @Nullable
        R getRight() {
            try {
                return this.rightIterator.next();
            } catch (final NoSuchElementException e) {
                return null;
            }
        }

    }
}
