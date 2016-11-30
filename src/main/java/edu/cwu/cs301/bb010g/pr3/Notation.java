package edu.cwu.cs301.bb010g.pr3;

import java.util.regex.Pattern;

import java8.util.stream.IntStreams;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpts;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Notation {
  public String coordToAlg(final IntPair coord) {
    return new StringBuilder().append((char) (97 + coord.fst)).append((char) (49 + coord.snd))
        .toString();
  }

  public IntPair algToCoord(final String alg) {
    return IntPair.of(alg.charAt(0) - 97, alg.charAt(1) - 49);
  }

  public String boardToFen(final Board board) {
    val fen = new StringBuilder();
    int emptyCount = 0;
    for (int rank = Board.RANKS - 1; rank >= 0; rank--) {
      for (int file = 0; file < Board.FILES; file++) {
        val piece = board.pieceRaw(file, rank);
        if (piece == null) {
          emptyCount++;
          if (file == Board.FILES - 1) {
            fen.append(emptyCount);
            emptyCount = 0;
          }
          continue;
        }
        if (emptyCount > 0) {
          fen.append(emptyCount);
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
        fen.append(c);
      }
      if (rank > 0) {
        fen.append('/');
      }
    }
    fen.append(' ');
    fen.append(board.active() == Color.WHITE ? 'w' : 'b');
    fen.append(' ');
    {
      val castling = board.castling();
      boolean used = false;
      if (castling.whiteASide() != -1) {
        used = true;
        fen.append('Q');
      }
      if (castling.whiteHSide() != -1) {
        used = true;
        fen.append('K');
      }
      if (castling.blackASide() != -1) {
        used = true;
        fen.append('q');
      }
      if (castling.blackHSide() != -1) {
        used = true;
        fen.append('k');
      }
      if (!used) {
        fen.append('-');
      }
    }
    fen.append(' ');
    fen.append(board.enPassantTarget().map(Notation::coordToAlg).orElse("-"));
    fen.append(' ');
    fen.append(board.halfmoveClock());
    fen.append(' ');
    fen.append(board.fullmoveNum());
    return fen.toString();
  }

  static Pattern FEN = Pattern.compile("(?<board>(?:[PpNnBbRrQqKk0-9]+/)*[PpNnBbRrQqKk0-9]+) "
      + "(?<active>[wb]) (?<castling>[KQkq]+|-) (?<enPassantTarget>[a-z]+[0-9]+|-) "
      + "(?<halfmoveClock>[0-9]+) (?<fullmoveNum>[0-9]+)");
  static Pattern FEN_BOARD_BS = Pattern.compile("/");

  public Board fenToBoard(final String fen) {
    val matcher = Notation.FEN.matcher(fen);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("FEN doesn't match");
    }

    final Piece[] board;
    final Color active;
    final CastlingOpts castling;
    final IntPair enPassantTarget;
    final int halfmoveClock;
    final int fullmoveNum;

    {
      val boardStr = matcher.group("board");
      val ranks = Notation.FEN_BOARD_BS.split(boardStr);
      board = new Piece[Board.SIZE];
      for (int rankOpp = Board.RANKS - 1; rankOpp >= 0; rankOpp--) {
        val rankArr = ranks[rankOpp];
        val rank = Board.RANKS - 1 - rankOpp;
        int file = 0;
        for (val ch : rankArr.toCharArray()) {
          if (Character.isDigit(ch)) {
            final int skip = ch - 48;
            file += skip;
          } else {
            val color = Character.isUpperCase(ch) ? Color.WHITE : Color.BLACK;
            final Type type;
            val p = Character.toLowerCase(ch);
            if (p == 'p') {
              type = Type.PAWN;
            } else if (p == 'n') {
              type = Type.KNIGHT;
            } else if (p == 'r') {
              type = Type.ROOK;
            } else if (p == 'b') {
              type = Type.BISHOP;
            } else if (p == 'q') {
              type = Type.QUEEN;
            } else if (p == 'k') {
              type = Type.KING;
            } else {
              throw new IllegalArgumentException();
            }
            board[file * Board.RANKS + rank] = Piece.of(type, color);
            file++;
          }
        }
      }
    }
    {
      val activeStr = matcher.group("active");
      val activeChar = activeStr.charAt(0);
      if (activeChar == 'w') {
        active = Color.WHITE;
      } else if (activeChar == 'b') {
        active = Color.BLACK;
      } else {
        throw new IllegalArgumentException("active");
      }
    }
    {
      val castlingStr = matcher.group("castling");
      if (castlingStr.charAt(0) == '-') {
        castling = CastlingOpts.BOTH_UNAVAILABLE;
      } else {
        CastlingOpts castling_ = CastlingOpts.BOTH_UNAVAILABLE;
        for (val ch : castlingStr.toCharArray()) {
          if (ch == 'Q') {
            castling_ = castling_.withWhiteASide(0);
          } else if (ch == 'K') {
            castling_ = castling_.withWhiteHSide(Board.FILES - 1);
          } else if (ch == 'q') {
            castling_ = castling_.withBlackASide(0);
          } else if (ch == 'k') {
            castling_ = castling_.withBlackHSide(Board.FILES - 1);
          }
        }
        castling = castling_;
      }
    }
    {
      val enPassantTargetStr = matcher.group("enPassantTarget");
      if (enPassantTargetStr.charAt(0) == '-') {
        enPassantTarget = null;
      } else {
        enPassantTarget = Notation.algToCoord(enPassantTargetStr);
      }
    }
    {
      val halfmoveClockStr = matcher.group("halfmoveClock");
      halfmoveClock = Integer.parseInt(halfmoveClockStr);
    }
    {
      val fullmoveNumStr = matcher.group("fullmoveNum");
      fullmoveNum = Integer.parseInt(fullmoveNumStr);
    }

    return Board.of(board, active, castling, enPassantTarget, halfmoveClock, fullmoveNum);
  }

  public String moveToAlg(final Move move) {
    if (move.castling().isPresent()) {
      val castling = move.castling().get();
      if (castling == CastlingOpts.Opt.A_SIDE) {
        return "0-0-0";
      } else if (castling == CastlingOpts.Opt.H_SIDE) {
        return "0-0";
      } else {
        return null;
      }
    }
    val alg = new StringBuilder();
    val piece = move.piece();
    val type = piece.type();
    alg.append(Notation.typeChar(type));
    // possibly unnecessary disambiguation, but screw trying to find out when it's actually needed
    alg.append(Notation.coordToAlg(move.src()));
    if (move.capture()) {
      alg.append('x');
    }
    alg.append(Notation.coordToAlg(move.dest().get()));
    if (move.enPassant()) {
      alg.append("e.p.");
    }
    move.promotion().ifPresent(pType -> {
      alg.append('=');
      alg.append(Notation.typeChar(pType));
    });
    if (move.checkmate()) {
      alg.append('#');
    } else if (move.check()) {
      alg.append('+');
    }
    if (move.drawOffer()) {
      alg.append(" (=)");
    }
    return alg.toString();
  }

  public char typeChar(final Type type) {
    final char typeCh;
    if (type == Type.PAWN) {
      typeCh = 'P';
    } else if (type == Type.KNIGHT) {
      typeCh = 'N';
    } else if (type == Type.ROOK) {
      typeCh = 'R';
    } else if (type == Type.BISHOP) {
      typeCh = 'B';
    } else if (type == Type.QUEEN) {
      typeCh = 'Q';
    } else if (type == Type.KING) {
      typeCh = 'K';
    } else {
      throw new IllegalArgumentException();
    }
    return typeCh;
  }

  public String drawBoard(final Piece[] board) {
    val art = new StringBuilder();
    art.append('{');
    IntStreams.range(1, Board.FILES * 2 - 1).forEach(n -> art.append('-'));
    art.append('\n');
    for (int rankI = Board.RANKS - 1; rankI >= 0; rankI--) {
      for (int fileI = 0; fileI < Board.FILES; fileI++) {
        val piece = board[fileI * Board.RANKS + rankI];
        final char typeChar;
        if (piece == null) {
          typeChar = '.';
        } else {
          val typeChar_ = Notation.typeChar(piece.type());
          typeChar = piece.color() == Color.WHITE ? typeChar_ : Character.toLowerCase(typeChar_);
        }
        art.append(typeChar);
        if (fileI < Board.FILES - 1) {
          art.append(' ');
        }
      }
      art.append('\n');
    }
    IntStreams.range(0, Board.FILES * 2 - 2).forEach(n -> art.append('-'));
    art.append('}');
    return art.toString();
  }
}
