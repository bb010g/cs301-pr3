package edu.cwu.cs301.bb010g.pr3;

import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpt;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@Builder(builderClassName = "Builder", builderMethodName = "rawBuilder")
@AllArgsConstructor(staticName = "of")
public class Move implements Comparable<Move> {
  @NonNull
  Piece piece;
  @NonNull
  IntPair src;
  @NonNull
  IntPair dest;
  boolean capture;
  boolean enPassant;
  Type promotion;
  CastlingOpt castling;
  boolean check;
  boolean checkmate;
  boolean drawOffer;

  public static Move.Builder builder() {
    return Move.defaults(Move.rawBuilder());
  }

  public static Move.Builder defaults(final Move.Builder that) {
    return that.capture(false).enPassant(false).promotion(null).castling(null).check(false)
        .checkmate(false).drawOffer(false);
  }

  @Override
  public int compareTo(final Move that) {
    val pieceCmp = this.piece.compareTo(that.piece);
    if (pieceCmp != 0) {
      return pieceCmp;
    }
    val srcCmp = this.src.compareTo(that.src);
    if (srcCmp != 0) {
      return srcCmp;
    }
    val destCmp = this.dest.compareTo(that.dest);
    if (destCmp != 0) {
      return destCmp;
    }
    val captureCmp = Boolean.compare(this.capture, that.capture);
    if (captureCmp != 0) {
      return captureCmp;
    }
    val enPassantCmp = Boolean.compare(this.enPassant, that.enPassant);
    if (enPassantCmp != 0) {
      return enPassantCmp;
    }
    val promotionCmp = this.promotion.compareTo(that.promotion);
    if (promotionCmp != 0) {
      return promotionCmp;
    }
    val castlingCmp = this.castling.compareTo(that.castling);
    if (castlingCmp != 0) {
      return castlingCmp;
    }
    val checkCmp = Boolean.compare(this.check, that.check);
    if (checkCmp != 0) {
      return checkCmp;
    }
    val checkmateCmp = Boolean.compare(this.checkmate, that.checkmate);
    if (checkCmp != 0) {
      return checkmateCmp;
    }
    return Boolean.compare(this.drawOffer, that.drawOffer);
  }
}
