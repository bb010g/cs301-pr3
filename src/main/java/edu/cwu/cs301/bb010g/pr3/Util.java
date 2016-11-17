package edu.cwu.cs301.bb010g.pr3;

import java.util.Comparator;
import java.util.EnumSet;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
  @RequiredArgsConstructor
  public class EnumSetComparator<E extends Enum<E>> implements Comparator<EnumSet<E>> {
    @Override
    public int compare(final EnumSet<E> s, final EnumSet<E> t) {
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
  }

  @RequiredArgsConstructor
  public class CompObjArrComparator<E extends Comparable<E>> implements Comparator<E[]> {
    @Override
    public int compare(final E[] arr, final E[] brr) {
      val length = arr.length;
      val lengthCmp = Integer.compare(length, brr.length);
      if (lengthCmp != 0) {
        return lengthCmp;
      }
      for (int i = 0; i < length; i++) {
        val cmp = arr[i].compareTo(brr[i]);
        if (cmp != 0) {
          return cmp;
        }
      }
      return 0;
    }

  }

  @RequiredArgsConstructor
  public class ObjArrComparator<E> implements Comparator<E[]> {
    final private Comparator<E> comp;

    @Override
    public int compare(final E[] arr, final E[] brr) {
      val length = arr.length;
      val lengthCmp = Integer.compare(length, brr.length);
      if (lengthCmp != 0) {
        return lengthCmp;
      }
      for (int i = 0; i < length; i++) {
        val cmp = this.comp.compare(arr[i], brr[i]);
        if (cmp != 0) {
          return cmp;
        }
      }
      return 0;
    }
  }
}
