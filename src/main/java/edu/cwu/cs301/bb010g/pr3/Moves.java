package edu.cwu.cs301.bb010g.pr3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java8.util.Comparators;
import java8.util.Lists2;
import java8.util.Maps2;
import java8.util.Optional;
import java8.util.Spliterator;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.Pair;
import edu.cwu.cs301.bb010g.Util;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpts;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.UtilityClass;
import lombok.experimental.Wither;

@UtilityClass
public class Moves {
  // offsets are from active player's POV
  public final Map<Piece.Type, Map<IntPair, List<IntPair>>> moveOffsets;
  static {
    moveOffsets = new HashMap<>();
    // pawns are special!
    // Maps2.of is a backport of Java 9 stuff, but I'm past the point of caring now
    Moves.moveOffsets.put(Type.PAWN,
        Maps2.of(IntPair.P0_1, Lists2.of(), IntPair.P1_1, Lists2.of(), IntPair.PN1_1, Lists2.of()));
    Moves.moveOffsets.put(Type.KNIGHT, Moves.leaper(1, 2));
    Moves.moveOffsets.put(Type.ROOK, Moves.rider(1, 0));
    Moves.moveOffsets.put(Type.BISHOP, Moves.rider(1, 1));
    {
      val offsets = new HashMap<IntPair, List<IntPair>>();
      offsets.putAll(Moves.rider(1, 0));
      offsets.putAll(Moves.rider(1, 1));
      Moves.moveOffsets.put(Type.QUEEN, offsets);
    }
    // so are kings (everyone loves castling)
    {
      val offsets = new HashMap<IntPair, List<IntPair>>();
      offsets.putAll(Moves.leaper(1, 0));
      offsets.putAll(Moves.leaper(1, 1));
      offsets.put(IntPair.P0_0, new ArrayList<>());
      Moves.moveOffsets.put(Type.KING, offsets);
    }
  }

  public Map<IntPair, List<IntPair>> rider(int m_, int n_) {
    if (m_ > n_) {
      val swp = m_;
      m_ = n_;
      n_ = swp;
    }
    val m = m_;
    val n = n_;
    val offsets = new HashMap<IntPair, List<IntPair>>();
    val mZero = m == 0;
    val nZero = n == 0;
    val same = n == m;
    List<IntPair> lst1 = null, lst2 = null, lst3 = null, lst4 = null, lst5 = null, lst6 = null,
        lst7 = null, lst8 = null;
    offsets.put(IntPair.of(m, n), lst1 = new ArrayList<>());
    if (!nZero) {
      offsets.put(IntPair.of(m, -n), lst2 = new ArrayList<>());
    }
    if (!mZero) {
      offsets.put(IntPair.of(-m, n), lst3 = new ArrayList<>());
    }
    if (!mZero && !nZero) {
      offsets.put(IntPair.of(-m, -n), lst4 = new ArrayList<>());
    }
    if (!same) {
      offsets.put(IntPair.of(n, m), lst5 = new ArrayList<>());
      if (!mZero) {
        offsets.put(IntPair.of(n, -m), lst6 = new ArrayList<>());
      }
      if (!nZero) {
        offsets.put(IntPair.of(-n, m), lst7 = new ArrayList<>());
      }
      if (!nZero && !mZero) {
        offsets.put(IntPair.of(-n, -m), lst8 = new ArrayList<>());
      }
    }
    for (int i = 1; i < Math.max(Board.RANKS, Board.FILES) / Math.max(Math.min(n, m), 1); i++) {
      lst1.add(IntPair.of(m, n).multMap(i));
      if (!nZero) {
        lst2.add(IntPair.of(m, -n).multMap(i));
      }
      if (!mZero) {
        lst3.add(IntPair.of(-m, n).multMap(i));
      }
      if (!mZero && !nZero) {
        lst4.add(IntPair.of(-m, -n).multMap(i));
      }
      if (!same) {
        lst5.add(IntPair.of(n, m).multMap(i));
        if (!mZero) {
          lst6.add(IntPair.of(n, -m).multMap(i));
        }
        if (!nZero) {
          lst7.add(IntPair.of(-n, m).multMap(i));
        }
        if (!nZero && !mZero) {
          lst8.add(IntPair.of(-n, -m).multMap(i));
        }
      }
    }
    return offsets;
  }

  public Map<IntPair, List<IntPair>> rider(final IntPair type) {
    return Moves.rider(type.fst, type.snd);
  }

  public Map<IntPair, List<IntPair>> leaper(int m_, int n_) {
    if (m_ > n_) {
      val swp = m_;
      m_ = n_;
      n_ = swp;
    }
    val m = m_;
    val n = n_;
    val offsets = new HashMap<IntPair, List<IntPair>>();
    val mZero = m == 0;
    val nZero = n == 0;
    val same = n == m;
    List<IntPair> lst1 = null, lst2 = null, lst3 = null, lst4 = null, lst5 = null, lst6 = null,
        lst7 = null, lst8 = null;
    offsets.put(IntPair.of(m, n), lst1 = new ArrayList<>());
    if (!nZero) {
      offsets.put(IntPair.of(m, -n), lst2 = new ArrayList<>());
    }
    if (!mZero) {
      offsets.put(IntPair.of(-m, n), lst3 = new ArrayList<>());
    }
    if (!mZero && !nZero) {
      offsets.put(IntPair.of(-m, -n), lst4 = new ArrayList<>());
    }
    if (!same) {
      offsets.put(IntPair.of(n, m), lst5 = new ArrayList<>());
      if (!mZero) {
        offsets.put(IntPair.of(n, -m), lst6 = new ArrayList<>());
      }
      if (!nZero) {
        offsets.put(IntPair.of(-n, m), lst7 = new ArrayList<>());
      }
      if (!mZero && !nZero) {
        offsets.put(IntPair.of(-n, -m), lst8 = new ArrayList<>());
      }
    }
    lst1.add(IntPair.of(m, n));
    if (!nZero) {
      lst2.add(IntPair.of(m, -n));
    }
    if (!mZero) {
      lst3.add(IntPair.of(-m, n));
    }
    if (!mZero && !nZero) {
      lst4.add(IntPair.of(-m, -n));
    }
    if (!same) {
      lst5.add(IntPair.of(n, m));
      if (!mZero) {
        lst6.add(IntPair.of(n, -m));
      }
      if (!nZero) {
        lst7.add(IntPair.of(-n, m));
      }
      if (!nZero && !mZero) {
        lst8.add(IntPair.of(-n, -m));
      }
    }
    return offsets;
  }

  public Map<IntPair, List<IntPair>> leaper(final IntPair type) {
    return Moves.leaper(type.fst, type.snd);
  }

  public boolean validCoord(final IntPair coord) {
    {
      val fst = coord.fst;
      if (fst < 0 || fst >= Board.FILES) {
        return false;
      }
    }
    {
      val snd = coord.snd;
      if (snd < 0 || snd >= Board.RANKS) {
        return false;
      }
    }
    return true;
  }

  public int promotionRank(final Color color) {
    if (color == Color.WHITE) {
      return Board.RANKS - 1;
    } else {
      return 0;
    }
  }

  public int pawnRank(final Color color) {
    if (color == Color.WHITE) {
      return 1;
    } else {
      return Board.RANKS - 2;
    }
  }

  public int mainRank(final Color color) {
    if (color == Color.WHITE) {
      return 0;
    } else {
      return Board.RANKS - 1;
    }
  }

  // extra data to keep the board's application happy (not actually required, but useful)
  @Value
  @Wither
  @AllArgsConstructor(staticName = "of")
  public class MovePlus implements Comparable<MovePlus> {
    Move move;
    CastlingOpts castlingOpts;
    IntPair enPassantTarget;
    // rook
    Piece castlingRook;

    public static MovePlus of(final Move move) {
      return new MovePlus(move, null, null, null);
    }

    public static MovePlus of(final Move move, final CastlingOpts castlingOpts) {
      return new MovePlus(move, castlingOpts, null, null);
    }

    public static MovePlus ofEnPassant(final Move move, final IntPair enPassantTarget) {
      return new MovePlus(move, null, enPassantTarget, null);
    }

    public static MovePlus ofCastling(final Move move, final CastlingOpts castlingOpts,
        final Piece castlingRook) {
      return new MovePlus(move, castlingOpts, null, castlingRook);
    }

    public Optional<CastlingOpts> castlingOpts() {
      return this.castlingOpts == null ? Optional.empty() : Optional.of(this.castlingOpts);
    }

    public Optional<IntPair> enPassantTarget() {
      return Optional.ofNullable(this.enPassantTarget);
    }

    public Optional<Piece> castlingRook() {
      return Optional.ofNullable(this.castlingRook);
    }

    @Override
    public int compareTo(final MovePlus that) {
      val moveCmp = this.move.compareTo(that.move);
      if (moveCmp != 0) {
        return moveCmp;
      }
      val castlingOptsCmp = this.castlingOpts.compareTo(that.castlingOpts);
      if (castlingOptsCmp != 0) {
        return castlingOptsCmp;
      }
      val enPassantTargetCmp = Comparators.nullsFirst(IntPair::compareTo)
          .compare(this.enPassantTarget, that.enPassantTarget);
      if (enPassantTargetCmp != 0) {
        return enPassantTargetCmp;
      }
      val castlingRookCmp =
          Comparators.nullsFirst(Piece::compareTo).compare(this.castlingRook, that.castlingRook);
      return castlingRookCmp;
    }
  }

  public IntPair orientOffset(final IntPair offset, final Color color) {
    return color == Color.WHITE ? offset : IntPair.of(offset.fst, -offset.snd);
  }

  /**
   * @return Stream[(direction, Stream[dest coords])]
   */
  public Stream<Pair<IntPair, Stream<IntPair>>> uninhibitedDirectedPaths(final Board board,
      final Piece piece, final IntPair pieceCoord, final int castlingRec) {
    val DIST_IMMUT = Spliterator.DISTINCT & Spliterator.IMMUTABLE;
    val pieceType = piece.type();
    val pieceColor = piece.color();
    val typeOffsets = Moves.moveOffsets.get(pieceType);
    return StreamSupport.stream(typeOffsets.keySet(), DIST_IMMUT).map(direction -> {
      return Pair.of(direction, RefStreams
          .concat(StreamSupport.stream(typeOffsets.get(direction), DIST_IMMUT).map(offset -> {
            return pieceCoord.add(Moves.orientOffset(offset, pieceColor));
          }).takeWhile(Moves::validCoord),
              Moves.enhancePath(direction, board, piece, pieceCoord, castlingRec)));
    });
  }

  /**
   * Add in the missing coords that moveOffsets can't produce for uninhibited.
   */
  private Stream<IntPair> enhancePath(final IntPair directionUnoriented, final Board board,
      final Piece piece, final IntPair pieceCoord, final int castlingRec) {
    final Stream.Builder<IntPair> enhancedCoords = RefStreams.builder();
    // missing special cases for pawn and king
    val pieceType = piece.type();
    val pieceRank = pieceCoord.snd;
    val pieceColor = piece.color();
    val direction = Moves.orientOffset(directionUnoriented, pieceColor);
    if (pieceType == Type.PAWN) {
      // pawns have straight forward move-only, two-rank rush variation at start, diagonal
      // capture-only, and en passant captures
      if (direction.equals(IntPair.P0_1)) {
        // move if not capturing
        final IntPair singleMoveCoord =
            pieceCoord.add(Moves.orientOffset(IntPair.P0_1, pieceColor));
        if (Moves.validCoord(singleMoveCoord) && board.pieceRaw(singleMoveCoord) == null) {
          enhancedCoords.add(singleMoveCoord);
          if (pieceRank == Moves.pawnRank(pieceColor)) {
            // rush if also not capturing
            final IntPair rushMoveCoord =
                pieceCoord.add(Moves.orientOffset(IntPair.P0_2, pieceColor));
            if (Moves.validCoord(rushMoveCoord) && board.pieceRaw(rushMoveCoord) == null) {
              enhancedCoords.add(rushMoveCoord);
            }
          }
        }
      } else {
        val rightDirected = direction.equals(IntPair.P1_1);
        val leftDirected = direction.equals(IntPair.PN1_1);
        if (rightDirected || leftDirected) {
          board.enPassantTarget().ifPresent(enPassantTarget -> {
            // capture if nearby
            final IntPair targetOffset =
                Moves.orientOffset(enPassantTarget.subt(pieceCoord), pieceColor);
            if ((rightDirected && targetOffset.equals(IntPair.P1_1))
                || (leftDirected && targetOffset.equals(IntPair.PN1_1))) {
              // en passant target is in the proper direction
              enhancedCoords.add(enPassantTarget);
            }
          });
          if (rightDirected) {
            val rightCoord = pieceCoord.add(IntPair.P1_1);
            if (Moves.validCoord(rightCoord)) {
              board.piece(rightCoord).ifPresent(t -> enhancedCoords.add(rightCoord));
            }
          } else {
            val leftCoord = pieceCoord.add(IntPair.PN1_1);
            if (Moves.validCoord(leftCoord)) {
              board.piece(leftCoord).ifPresent(t -> enhancedCoords.add(leftCoord));
            }
          }
        }
      }
    } else if (castlingRec > 0 && pieceType == Type.KING && direction.equals(IntPair.P0_0)) {
      // basic castling check
      val castling = board.castling();
      if (castling.canCastle(pieceColor)) {
        if (castling.whiteASide() != 0 || castling.blackASide() != 0) {
          val kingDest = IntPair.of(Moves.CASTLE_DEST_A_KING, pieceCoord.snd);
          if (Moves.validCastlePieces(piece, pieceCoord, kingDest, CastlingOpts.Opt.A_SIDE, board,
              castlingRec - 1)) {
            enhancedCoords.add(kingDest);
          }
        } else if (castling.whiteHSide() != 0 || castling.blackHSide() != 0) {
          val kingDest = IntPair.of(Moves.CASTLE_DEST_H_KING, pieceCoord.snd);
          if (Moves.validCastlePieces(piece, pieceCoord, kingDest, CastlingOpts.Opt.H_SIDE, board,
              castlingRec - 1)) {
            enhancedCoords.add(kingDest);
          }
        }
      }
    }
    return enhancedCoords.build();
  }

  public final int CASTLE_ITER_DIR_A = 1;
  public final int CASTLE_DEST_A_ROOK = (Board.FILES / 2) - Moves.CASTLE_ITER_DIR_A;
  public final int CASTLE_DEST_A_KING = Moves.CASTLE_DEST_A_ROOK - Moves.CASTLE_ITER_DIR_A;
  public final int CASTLE_ITER_DIR_H = -1;
  public final int CASTLE_DEST_H_ROOK = (Board.FILES / 2) - Moves.CASTLE_ITER_DIR_H;
  public final int CASTLE_DEST_H_KING = Moves.CASTLE_DEST_H_ROOK - Moves.CASTLE_ITER_DIR_H;

  public boolean validCastlePieces(final Piece kingPiece, final IntPair kingCoord,
      final IntPair kingDest, final CastlingOpts.Opt opt, final Board board,
      final int castlingRec) {
    val rookFile = board.castling().opt(kingPiece.color(), opt);
    val kingFile = kingCoord.fst;
    val kingRank = kingCoord.snd;
    val kingColor = kingPiece.color();
    val rookPiece = board.pieceRaw(rookFile, kingRank);
    val kingDestFile = kingDest.fst;
    // not checking for castling with inCheck because castling on one side can't put the other side
    // in check
    val clearPathKing = Util.rangeIncClosed(kingFile, kingDestFile).allMatch(
        f -> board.piece(f, kingRank).map(p -> p == kingPiece || p == rookPiece).orElse(true)
            && !Moves.inCheck(board, kingColor, castlingRec).findAny().isPresent());
    val rookDestFile =
        opt == CastlingOpts.Opt.A_SIDE ? Moves.CASTLE_DEST_A_ROOK : Moves.CASTLE_DEST_H_ROOK;
    val clearPathRook = Util.rangeIncClosed(rookFile, rookDestFile).anyMatch(
        f -> board.piece(f, kingRank).filter(p -> !(p == kingPiece || p == rookPiece)).isPresent());
    return clearPathKing && clearPathRook;
  }

  public Stream<IntPair> inhibitPathPieces(final Stream<IntPair> path, final Board board,
      final Color color) {
    return Util.takeWhileInclusive(
        path.takeWhile(
            coord -> !board.piece(coord).filter(test -> test.color() == color).isPresent()),
        coord -> !board.piece(coord).filter(test -> test.color() != color).isPresent());
  }

  /**
   * @return Stream[(direction, Stream[dest coords])]
   */
  public Stream<Pair<IntPair, Stream<IntPair>>> inhibitedDirectedPaths(final Board board,
      final Piece piece, final IntPair pieceCoord, final int castlingRec,
      final boolean checkPrevention) {
    val pieceColor = piece.color();
    if (checkPrevention) {
      return Moves.uninhibitedDirectedPaths(board, piece, pieceCoord, castlingRec).map(
          directedPath -> directedPath.mapSnd(path -> directedPath.fst.equals(IntPair.P0_0) ? path
              : Moves.inhibitPathPieces(path, board, pieceColor)
                  .filter(destCoord -> !Moves.inCheck(
                      board.withPieces(Pair.of(null, pieceCoord), Pair.of(piece, destCoord)),
                      pieceColor, castlingRec).findAny().isPresent())));
    } else {
      return Moves.uninhibitedDirectedPaths(board, piece, pieceCoord, castlingRec)
          .map(directedPath -> directedPath
              .mapSnd(path -> Moves.inhibitPathPieces(path, board, pieceColor)));
    }
  }

  /**
   * @return Stream[((piece, coord), Stream[(dest coords)])]
   */
  public Stream<Pair<Pair<Piece, IntPair>, Stream<IntPair>>> inhibitedDestsColor(final Board board,
      final Color color, final int castlingRec, final boolean checkPrevention) {
    return board.stream().filter(p -> p.fst != null && p.fst.color() == color)
        .map(p -> Pair.of(p,
            Moves.inhibitedDirectedPaths(board, p.fst, p.snd, castlingRec, checkPrevention)
                .flatMap(directedPaths -> directedPaths.snd)));
  }

  /**
   * @return Optional[((piece, coord), target)]
   */
  public Stream<Pair<Pair<Piece, IntPair>, IntPair>> inCheck(final Board board, final Color color,
      final int castlingRec) {
    val king = board.stream().filter(p -> {
      return p.fst != null && p.fst.type() == Type.KING && p.fst.color() == color;
    }).findAny().orElseThrow(() -> {
      System.err.println(Notation.drawBoard(board.board()));
      return new NoSuchElementException();
    });
    val kingCoord = king.snd;
    val otherColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
    return Moves.inhibitedDestsColor(board, otherColor, castlingRec, false)
        .map(pDests -> Pair.of(pDests.fst, pDests.snd.filter(coord -> coord.equals(kingCoord))))
        .flatMap(pTargetDests -> pTargetDests.snd.map(coord -> Pair.of(pTargetDests.fst, coord)));
  }

  public Stream<MovePlus> moves(final Board board) {
    return Moves.inhibitedDestsColor(board, board.active(), 1, true)
        .flatMap(pDests -> Moves.pCoordsToMovePlus(board, pDests));
  }

  public Stream<MovePlus> pCoordsToMovePlus(final Board board,
      final Pair<Pair<Piece, IntPair>, Stream<IntPair>> pCoords) {
    return Moves.pCoordsToMovePlus(board, pCoords.fst, pCoords.snd);
  }

  public Stream<MovePlus> pCoordsToMovePlus(final Board board, final Pair<Piece, IntPair> p,
      final Stream<IntPair> coords) {
    return Moves.pCoordsToMovePlus(board, p.fst, p.snd, coords);
  }

  public Stream<MovePlus> pCoordsToMovePlus(final Board board, final Piece piece,
      final IntPair pieceCoord, final Stream<IntPair> coords) {
    return coords.flatMap(coord -> Moves.pCoordToMovePlus(board, piece, pieceCoord, coord));
  }

  public Stream<MovePlus> pCoordToMovePlus(final Board board, final Piece piece,
      final IntPair pieceCoord, final IntPair destCoord) {
    final Stream.Builder<MovePlus> out = RefStreams.builder();
    final Move.Builder move;
    final IntPair enPassantTarget;
    final Piece castlingRook;
    val pieceType = piece.type();
    val pieceColor = piece.color();
    val pieceFile = pieceCoord.fst;
    val pieceRank = pieceCoord.snd;
    val otherColor = pieceColor == Color.WHITE ? Color.BLACK : Color.WHITE;
    val destFile = destCoord.fst;
    val destRank = destCoord.snd;
    val destPiece = board.pieceRaw(destCoord);
    val offset = Moves.orientOffset(destCoord.subt(pieceCoord), pieceColor);
    val castlingOpts = board.castling();
    final int colorASide, colorHSide, otherASide, otherHSide;
    final int newColorASide, newColorHSide, newOtherASide, newOtherHSide;
    {
      val colorSide = castlingOpts.opts(pieceColor);
      colorASide = colorSide.fst;
      colorHSide = colorSide.snd;
      val otherSide = castlingOpts.opts(otherColor);
      otherASide = otherSide.fst;
      otherHSide = otherSide.snd;
    }
    if (!(pieceType == Type.KING && offset.fst == 0 && offset.snd == 0)) {
      // normal move or capture
      if (colorASide != -1 || colorHSide != -1) {
        if (pieceType == Type.KING) {
          newColorASide = -1;
          newColorHSide = -1;
        } else if (pieceType == Type.ROOK && pieceFile == Moves.mainRank(pieceColor)) {
          if (pieceFile == colorASide) {
            newColorASide = -1;
            newColorHSide = colorHSide;
          } else if (pieceFile == colorHSide) {
            newColorASide = colorASide;
            newColorHSide = colorHSide;
          } else {
            newColorASide = colorASide;
            newColorHSide = colorHSide;
          }
        } else {
          newColorASide = colorASide;
          newColorHSide = colorHSide;
        }
      } else {
        newColorASide = colorASide;
        newColorHSide = colorHSide;
      }
      if (destPiece == null) {
        // move
        newOtherASide = otherASide;
        newOtherHSide = otherHSide;
        if (offset.snd == 2 && pieceType == Type.PAWN) {
          enPassantTarget = IntPair.of(pieceFile, pieceRank + (pieceColor == Color.WHITE ? -1 : 1));
        } else {
          enPassantTarget = null;
        }
        move = Move.builderMove(piece, pieceCoord, destCoord);
      } else {
        // capture
        val destType = destPiece.type();
        if (destType == Type.ROOK) {
          // capture changes in other's castling options
          if (destRank == otherASide) {
            newOtherASide = -1;
            newOtherHSide = otherHSide;
          } else if (destRank == otherHSide) {
            newOtherASide = otherASide;
            newOtherHSide = -1;
          } else {
            newOtherASide = otherASide;
            newOtherHSide = otherHSide;
          }
        } else {
          newOtherASide = otherASide;
          newOtherHSide = otherHSide;
        }
        if (board.enPassantTarget().map(t -> t.equals(destCoord)).orElse(false)) {
          move = Move.builderEnPassant(piece, pieceCoord, destCoord);
        } else {
          move = Move.builderCapture(piece, pieceCoord, destCoord);
        }
        enPassantTarget = null;
      }
      castlingRook = null;
    } else {
      // castling
      val castlingOpt =
          destFile == Moves.CASTLE_DEST_A_KING ? CastlingOpts.Opt.A_SIDE : CastlingOpts.Opt.H_SIDE;
      final int rookFile;
      if (castlingOpt == CastlingOpts.Opt.A_SIDE) {
        rookFile = colorASide;
        newColorASide = -1;
        newColorHSide = colorHSide;
      } else {
        rookFile = colorHSide;
        newColorASide = colorASide;
        newColorHSide = -1;
      }
      castlingRook = board.pieceRaw(rookFile, pieceRank);
      newOtherASide = otherASide;
      newOtherHSide = otherHSide;
      enPassantTarget = null;
      move = Move.builderCastling(piece, pieceCoord, castlingOpt);
    }

    val newCastlingOpts =
        CastlingOpts.of(newColorASide, newColorHSide, newOtherASide, newOtherHSide);
    if (pieceType == Type.PAWN && destRank == Moves.promotionRank(pieceColor)) {
      out.add(MovePlus.of(move.promotion(Type.KNIGHT).build(), newCastlingOpts, enPassantTarget,
          castlingRook));
      out.add(MovePlus.of(move.promotion(Type.ROOK).build(), newCastlingOpts, enPassantTarget,
          castlingRook));
      out.add(MovePlus.of(move.promotion(Type.BISHOP).build(), newCastlingOpts, enPassantTarget,
          castlingRook));
      out.add(MovePlus.of(move.promotion(Type.QUEEN).build(), newCastlingOpts, enPassantTarget,
          castlingRook));
    } else {
      out.add(MovePlus.of(move.build(), newCastlingOpts, enPassantTarget, castlingRook));
    }
    return out.build();
  }

  public Stream<Pair<Board, Move>> step(final Board board) {
    return Moves.moves(board).map(moveP -> Moves.applyMove(board, moveP));
  }

  public Pair<Board, Move> applyMove(final Board board, final MovePlus movePlus) {
    val move = movePlus.move;
    val active = board.active();
    val newActive = active == Color.WHITE ? Color.BLACK : Color.WHITE;
    val promotion = move.promotion();
    val piece = promotion.map(t -> move.piece().withType(t)).orElse(move.piece());
    val src = move.src();
    val dest_ = move.dest();
    final Piece[] newBoardArr;
    final CastlingOpts castlingOpts = board.castling();
    final CastlingOpts newCastlingOpts = movePlus.castlingOpts().orElse(castlingOpts);
    final IntPair newEnPassantTarget;
    if (dest_.isPresent()) {
      {
        val dest = dest_.get();
        if (move.enPassant()) {
          // en passant
          newEnPassantTarget = movePlus.enPassantTarget;
        } else {
          newEnPassantTarget = board.enPassantTarget().orElse(null);
        }
        newBoardArr = new Piece[Board.SIZE];
        System.arraycopy(board.board(), 0, newBoardArr, 0, Board.SIZE);
        newBoardArr[src.fst * Board.RANKS + src.snd] = null;
        newBoardArr[dest.fst * Board.RANKS + dest.snd] = piece;
      }
    } else {
      // castling
      val castling = move.castling().get();
      val rook = movePlus.castlingRook;
      val kingDest =
          castling == CastlingOpts.Opt.A_SIDE ? Moves.CASTLE_DEST_A_KING : Moves.CASTLE_DEST_H_KING;
      val rookDest =
          castling == CastlingOpts.Opt.A_SIDE ? Moves.CASTLE_DEST_A_ROOK : Moves.CASTLE_DEST_H_ROOK;
      val rookSrc = castlingOpts.opt(active, castling);
      newBoardArr = new Piece[Board.SIZE];
      System.arraycopy(board.board(), 0, newBoardArr, 0, Board.SIZE);
      val srcRank = src.snd;
      newBoardArr[src.fst * Board.RANKS + srcRank] = null;
      newBoardArr[rookSrc * Board.RANKS + srcRank] = null;
      newBoardArr[kingDest * Board.RANKS + srcRank] = piece;
      newBoardArr[rookDest * Board.RANKS + srcRank] = rook;
      newEnPassantTarget = movePlus.enPassantTarget;
    }
    val newHalfmoveClock = board.halfmoveClock() + 1;
    val newFullmoveNum = board.fullmoveNum() + (active == Color.BLACK ? 1 : 0);

    val newBoard = Board.of(newBoardArr, newActive, newCastlingOpts, newEnPassantTarget,
        newHalfmoveClock, newFullmoveNum);

    val check = Moves.inCheck(newBoard, newActive, 1).findAny();
    val moveCheck = move.withCheck(check.isPresent());
    final Move moveCheckmate;
    if (check.isPresent()) {
      moveCheckmate =
          moveCheck.withCheckmate(!Moves.inhibitedDestsColor(newBoard, newActive, 1, true)
              .flatMap(Pair::snd).findAny().isPresent());
    } else {
      moveCheckmate = moveCheck;
    }

    return Pair.of(newBoard, moveCheckmate);
  }
}
