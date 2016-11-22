package edu.cwu.cs301.bb010g;

import java.util.Comparator;
import java.util.EnumSet;

import java8.util.Optional;
import java8.util.Spliterator;
import java8.util.Spliterators;
import java8.util.function.Consumer;
import java8.util.function.Predicate;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
  public <E extends Enum<E>> int enumSetCompare(final EnumSet<E> s, final EnumSet<E> t) {
    final int size = s.size();
    val sizeCmp = Integer.compare(size, t.size());
    if (sizeCmp != 0) {
      return sizeCmp;
    }
    val sIter = s.iterator();
    val tIter = t.iterator();
    for (int i = 0; i < size; i++) {
      val cmp = Integer.compare(sIter.next().ordinal(), tIter.next().ordinal());
      if (cmp != 0) {
        return cmp;
      }
    }
    return 0;
  }

  public <T extends Comparable<T>> int optionalEmptyFirstCompare(final Optional<T> o,
      final Optional<T> p) {
    return Util.optionalEmptyFirstCompareWithComp(T::compareTo, o, p);
  }

  public <T> int optionalEmptyFirstCompareWithComp(final Comparator<T> comp, final Optional<T> o,
      final Optional<T> p) {
    return o.isPresent() ? p.isPresent() ? comp.compare(o.get(), p.get()) : 1
        : p.isPresent() ? -1 : 0;
  }

  public <E extends Comparable<E>> int objArrCompare(final E[] arr, final E[] brr) {
    return Util.objArrCompareWithComp(E::compareTo, arr, brr);
  }

  public <E> int objArrCompareWithComp(final Comparator<E> comp, final E[] arr, final E[] brr) {
    val length = arr.length;
    val lengthCmp = Integer.compare(length, brr.length);
    if (lengthCmp != 0) {
      return lengthCmp;
    }
    for (int i = 0; i < length; i++) {
      val cmp = comp.compare(arr[i], brr[i]);
      if (cmp != 0) {
        return cmp;
      }
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  public <E> Stream<E> streamArr(final E[] arr, final int additionalCharacteristics,
      final boolean parallel) {
    return StreamSupport.stream(
        (Spliterator<E>) Spliterators.spliterator(arr, additionalCharacteristics), parallel);
  }

  public <E> Stream<E> streamImmutArr(final E[] arr, final boolean parallel) {
    return Util.streamArr(arr, Spliterator.IMMUTABLE, parallel);
  }

  public <E> Stream<E> streamDistinctArr(final E[] arr, final boolean parallel) {
    return Util.streamArr(arr, Spliterator.DISTINCT, parallel);
  }

  public <E> Stream<E> streamDistinctImmutArr(final E[] arr, final boolean parallel) {
    return Util.streamArr(arr, Spliterator.DISTINCT | Spliterator.IMMUTABLE, parallel);
  }

  public <E> Stream<E> takeWhileInclusive(final Stream<E> source, final Predicate<E> predicate) {
    return StreamSupport
        .stream(new TakeWhileInclusiveSpliterator<>(source.spliterator(), predicate), false);
  }

  @RequiredArgsConstructor
  public class TakeWhileInclusiveSpliterator<T> implements Spliterator<T> {
    private final Spliterator<T> source;
    private final Predicate<T> predicate;
    private boolean predicateHolds;

    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
      return this.predicateHolds && this.source.tryAdvance(e -> {
        if (this.predicate.test(e)) {
          action.accept(e);
        }
      });
    }

    @Override
    public Spliterator<T> trySplit() {
      return null;
    }

    @Override
    public long estimateSize() {
      return this.predicateHolds ? this.source.estimateSize() : 0;
    }

    @Override
    public int characteristics() {
      return this.source.characteristics() & ~Spliterator.SIZED;
    }

    @Override
    public void forEachRemaining(final Consumer<? super T> action) {
      do {
        /* whee */ } while (this.tryAdvance(action));
    }

    @Override
    public long getExactSizeIfKnown() {
      return Spliterators.getExactSizeIfKnown(this);
    }

    @Override
    public boolean hasCharacteristics(final int characteristics) {
      return Spliterators.hasCharacteristics(this, this.characteristics());
    }

    @Override
    public Comparator<? super T> getComparator() {
      return this.source.getComparator();
    }
  }
}
