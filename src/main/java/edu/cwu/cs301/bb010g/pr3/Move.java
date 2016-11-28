package edu.cwu.cs301.bb010g.pr3;

import java8.util.Comparators;
import java8.util.Optional;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpts;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@Builder(builderClassName = "Builder", builderMethodName = "rawBuilder")
@AllArgsConstructor(staticName = "of")
public class Move implements Comparable<Move> {
  Piece piece;
  IntPair src;
  // Optional
  IntPair dest;
  boolean capture;
  boolean enPassant;
  // Optional
  Type promotion;
  // Optional
  CastlingOpts.Opt castling;
  boolean check;
  boolean checkmate;
  boolean drawOffer;

  public Optional<IntPair> dest() {
    return Optional.ofNullable(this.dest);
  }

  public Optional<Type> promotion() {
    return Optional.ofNullable(this.promotion);
  }

  public Optional<CastlingOpts.Opt> castling() {
    return Optional.ofNullable(this.castling);
  }

  public static Move.Builder builder() {
    return Move.rawBuilder().dest(null).capture(false).enPassant(false).promotion(null)
        .castling(null).check(false).checkmate(false).drawOffer(false);
  }

  public static Move.Builder builderMove(final Piece piece, final IntPair src, final IntPair dest) {
    return Move.builder().piece(piece).src(src).dest(dest);
  }

  public static Move.Builder builderCapture(final Piece piece, final IntPair src,
      final IntPair dest) {
    return Move.builderMove(piece, src, dest).capture(true);
  }

  public static Move.Builder builderEnPassant(final Piece piece, final IntPair src,
      final IntPair dest) {
    return Move.builderCapture(piece, src, dest).enPassant(true);
  }

  public static Move.Builder builderCastling(final Piece piece, final IntPair src,
      final CastlingOpts.Opt castling) {
    return Move.builder().piece(piece).src(src).castling(castling);
  }

  public String verify() {
    val type = this.piece.type();
    if (this.dest == null) {
      if (this.castling == null) {
        return "Must have a destination if not castling";
      } else if (type != Type.KING) {
        return "Only kings can castle";
      }
    }
    if (this.enPassant) {
      if (type != Type.PAWN) {
        return "Only pawns can capture en passant";
      }
      if (!this.capture) {
        return "En passant is a type of capturing";
      }
    }
    if (this.promotion != null) {
      if (type != Type.PAWN) {
        return "Only pawns can promote";
      }
    }
    if (this.check && !this.checkmate) {
      return "Must be in check to be in checkmate";
    }
    return null;
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
    val destCmp = Comparators.nullsFirst(IntPair::compareTo).compare(this.dest, that.dest);
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
    val promotionCmp =
        Comparators.nullsFirst(Type::compareTo).compare(this.promotion, that.promotion);
    if (promotionCmp != 0) {
      return promotionCmp;
    }
    val castlingCmp =
        Comparators.nullsFirst(CastlingOpts.Opt::compareTo).compare(this.castling, that.castling);
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
