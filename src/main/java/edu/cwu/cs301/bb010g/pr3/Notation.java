package edu.cwu.cs301.bb010g.pr3;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpt;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Notation {
  public String coordToAlg(final IntPair coord) {
    return new StringBuilder().append((char) (97 + coord.fst)).append((char) (49 + coord.snd))
        .toString();
  }

  public String boardToFen(final Board board) {
    val sb = new StringBuilder();
    int emptyCount = 0;
    for (int rank = Board.RANKS - 1; rank >= 0; rank--) {
      for (int file = 0; file < Board.FILES; file++) {
        val piece = board.piece(file, rank);
        if (piece == null) {
          emptyCount++;
          if (file == Board.FILES - 1) {
            sb.append(emptyCount);
            emptyCount = 0;
          }
          continue;
        }
        if (emptyCount > 0) {
          sb.append(emptyCount);
          emptyCount = 0;
        }
        char c = 0;
        switch (piece.type()) {
          case PAWN:
            c = 'P';
            break;
          case KNIGHT:
            c = 'N';
            break;
          case ROOK:
            c = 'R';
            break;
          case BISHOP:
            c = 'B';
            break;
          case QUEEN:
            c = 'Q';
            break;
          case KING:
            c = 'K';
            break;
          default:
        }
        assert (c != 0);
        if (piece.color() == Color.BLACK) {
          c += 32;
        }
        sb.append(c);
      }
      if (rank == Board.RANKS - 1) {
        break;
      }
    }
    sb.append(' ');
    sb.append(board.active() == Color.WHITE ? 'w' : 'b');
    sb.append(' ');
    {
      val castling = board.castling();
      boolean empty = true;
      white: {
        val white = castling.white();
        if (white.isEmpty()) {
          break white;
        }
        empty = false;
        if (white.contains(CastlingOpt.H_SIDE)) {
          sb.append('K');
        }
        if (white.contains(CastlingOpt.A_SIDE)) {
          sb.append('Q');
        }
      }
      black: {
        val black = castling.black();
        if (black.isEmpty()) {
          break black;
        }
        empty = false;
        if (black.contains(CastlingOpt.H_SIDE)) {
          sb.append('k');
        }
        if (black.contains(CastlingOpt.A_SIDE)) {
          sb.append('q');
        }
      }
      if (empty) {
        sb.append('-');
      }
    }
    sb.append(' ');
    enPassant: {
      val enPassantTarget = board.enPassantTarget();
      if (enPassantTarget == null) {
        sb.append('-');
        break enPassant;
      }
      sb.append(Notation.coordToAlg(enPassantTarget));
    }
    sb.append(' ');
    sb.append(board.halfmoveClock());
    sb.append(' ');
    sb.append(board.fullmoveNum());
    return sb.toString();
  }
}
