package edu.cwu.cs301.bb010g.pr3;

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
  public int compareTo(final IntPair that) {
    if (that == null) {
      return 1;
    }
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
