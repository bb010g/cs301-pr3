package edu.cwu.cs301.bb010g.pr3;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
public class Piece implements Comparable<Piece> {
  Type type;
  Color color;
  boolean moved;

  public static Piece of(final Type type, final Color color) {
    return Piece.of(type, color, false);
  }

  public Piece move() {
    return this.withMoved(true);
  }

  public enum Color {
    WHITE, BLACK;
    final static int WHITE_ORD = WHITE.ordinal();
    final static int BLACK_ORD = BLACK.ordinal();
  }

  public enum Type {
    PAWN, KNIGHT, ROOK, BISHOP, QUEEN, KING;
  }

  @Override
  public int compareTo(final Piece that) {
    val typeCmp = this.type.compareTo(that.type);
    if (typeCmp != 0) {
      return typeCmp;
    }
    val colorCmp = this.color.compareTo(that.color);
    if (colorCmp != 0) {
      return colorCmp;
    }
    return Boolean.compare(this.moved, that.moved);
  }
}
