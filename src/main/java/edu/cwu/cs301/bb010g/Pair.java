package edu.cwu.cs301.bb010g;

import java.util.Comparator;
import java.util.Map;

import java8.util.Comparators;
import java8.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
final public class Pair<A, B> implements Map.Entry<A, B> {
  public A fst;
  public B snd;

  public static <A, B> Pair<A, B> ofFst(final A fst) {
    return Pair.of(fst, null);
  }

  public static <A, B> Pair<A, B> ofSnd(final B snd) {
    return Pair.of(null, snd);
  }

  public IntPair asInt() {
    return IntPair.of((Integer) this.fst, (Integer) this.snd);
  }

  public <U> Pair<U, B> mapFst(final Function<? super A, ? extends U> mapping) {
    return Pair.of(mapping.apply(this.fst), this.snd);
  }

  public <U> Pair<A, U> mapSnd(final Function<? super B, ? extends U> mapping) {
    return Pair.of(this.fst, mapping.apply(this.snd));
  }

  public <U, V> Pair<U, V> bimap(final Function<? super A, ? extends U> fstMapping,
      final Function<? super B, ? extends V> sndMapping) {
    return Pair.of(fstMapping.apply(this.fst), sndMapping.apply(this.snd));
  }

  @Override
  public A getKey() {
    return this.fst;
  }

  @Override
  public B getValue() {
    return this.snd;
  }

  @Override
  public B setValue(final B value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    val sb = new StringBuilder("Pair(");
    sb.append(this.fst);
    sb.append(", ");
    sb.append(this.snd);
    sb.append(')');
    return sb.toString();
  }

  public static <A extends Comparable<A>, B extends Comparable<B>> int compareTo(final Pair<A, B> p,
      final Pair<A, B> q) {
    return Pair.compareToWithComp(A::compareTo, B::compareTo, p, q);
  }

  public static <A, B> int compareToWithComp(final Comparator<A> compA, final Comparator<B> compB,
      final Pair<A, B> p, final Pair<A, B> q) {
    val fstCmp = Comparators.nullsFirst(compA).compare(p.fst, q.fst);
    if (fstCmp != 0) {
      return fstCmp;
    }
    return Comparators.nullsFirst(compB).compare(p.snd, q.snd);
  }
}
