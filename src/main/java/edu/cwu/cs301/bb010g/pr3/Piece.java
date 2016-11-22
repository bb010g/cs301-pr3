package edu.cwu.cs301.bb010g.pr3;

import java8.util.Optional;

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
  // Optional
  int rookSide;

  public static Piece of(final Type type, final Color color) {
    return new Piece(type, color, -1);
  }

  public Optional<Integer> rookSide() {
    return this.rookSide == -1 ? Optional.empty() : Optional.of(this.rookSide);
  }

  public int rookSideRaw() {
    return this.rookSide;
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
    return Integer.compare(this.rookSide, that.rookSide);
  }
}
