package edu.cwu.cs301.bb010g;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
final public class IntPair implements Map.Entry<Integer, Integer>, Comparable<IntPair> {
  public int fst;
  public int snd;

  public static final IntPair P1_1 = new IntPair(1, 1);
  public static final IntPair P0_0 = new IntPair(0, 0);
  public static final IntPair P1_0 = new IntPair(1, 0);
  public static final IntPair P0_1 = new IntPair(0, 1);
  public static final IntPair P0_2 = new IntPair(0, 2);
  public static final IntPair PN1_1 = new IntPair(-1, 1);
  public static final IntPair PN1_0 = new IntPair(-1, 0);

  public Pair<Integer, Integer> asGeneric() {
    return Pair.of(this.fst, this.snd);
  }

  @Override
  public Integer getKey() {
    return this.fst;
  }

  @Override
  public Integer getValue() {
    return this.snd;
  }

  @Override
  public Integer setValue(final Integer value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    val sb = new StringBuilder("IntPair(");
    sb.append(this.fst);
    sb.append(", ");
    sb.append(this.snd);
    sb.append(')');
    return sb.toString();
  }

  @Override
  public int compareTo(final IntPair that) {
    val fstCmp = Integer.compare(this.fst, that.fst);
    if (fstCmp != 0) {
      return fstCmp;
    }
    return Integer.compare(this.snd, that.snd);
  }

  public IntPair add(final IntPair that) {
    return IntPair.of(this.fst + that.fst, this.snd + that.snd);
  }

  public IntPair addMap(final int that) {
    return IntPair.of(this.fst + that, this.snd + that);
  }

  public IntPair subt(final IntPair that) {
    return IntPair.of(this.fst - that.fst, this.snd - that.snd);
  }

  public IntPair subtMap(final int that) {
    return IntPair.of(this.fst - that, this.snd - that);
  }

  public IntPair mult(final IntPair that) {
    return IntPair.of(this.fst * that.fst, this.snd * that.snd);
  }

  public IntPair multMap(final int that) {
    return IntPair.of(this.fst * that, this.snd * that);
  }

  public IntPair div(final IntPair that) {
    return IntPair.of(this.fst / that.fst, this.snd / that.snd);
  }

  public IntPair divMap(final int that) {
    return IntPair.of(this.fst / that, this.snd / that);
  }

  public Pair<Double, Double> divDouble(final IntPair that) {
    return Pair.of((double) this.fst / (double) that.fst, (double) this.snd / (double) that.snd);
  }

  public Pair<Double, Double> divDoubleMap(final int that) {
    return Pair.of((double) this.fst / (double) that, (double) this.snd / (double) that);
  }

  public IntPair abs() {
    return IntPair.of(Math.abs(this.fst), Math.abs(this.snd));
  }

  public IntPair neg() {
    return IntPair.of(-this.fst, -this.snd);
  }
}
