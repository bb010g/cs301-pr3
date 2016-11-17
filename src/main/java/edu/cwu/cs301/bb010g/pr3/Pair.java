package edu.cwu.cs301.bb010g.pr3;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
final public class Pair<A, B> implements Map.Entry<A, B>, Comparable<Pair<A, B>> {
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

  public static <A extends Comparable<A>, B extends Comparable<B>> int compareTo(final Pair<A, B> p,
      final Pair<A, B> q) {
    val fstCmp = p.fst.compareTo(q.fst);
    if (fstCmp != 0) {
      return fstCmp;
    }
    return p.snd.compareTo(q.snd);
  }

  // evil, I know
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public int compareTo(final Pair<A, B> that) {
    final Pair p = this;
    final Pair q = that;
    return Pair.compareTo(p, q);
  }
}
