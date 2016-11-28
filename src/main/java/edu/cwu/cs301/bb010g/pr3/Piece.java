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
    return this.color.compareTo(that.color);
  }
}
