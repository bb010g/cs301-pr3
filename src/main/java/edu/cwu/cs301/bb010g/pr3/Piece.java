package edu.cwu.cs301.bb010g.pr3;

import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpt;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
public class Piece implements Comparable<Piece> {
  @NonNull
  Type type;
  @NonNull
  Color color;
  boolean moved;
  CastlingOpt rookSide;

  public static Piece of(final Type type, final Color color) {
    return new Piece(type, color, false, null);
  }

  public static Piece of(final Type type, final Color color, final CastlingOpt rookSide) {
    return new Piece(type, color, false, rookSide);
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
    val moveCmp = Boolean.compare(this.moved, that.moved);
    if (moveCmp != 0) {
      return moveCmp;
    }
    return this.rookSide.compareTo(that.rookSide);
  }
}
