package edu.cwu.cs301.bb010g.pr3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java8.util.Comparators;
import java8.util.Optional;
import java8.util.Spliterator;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.Pair;
import edu.cwu.cs301.bb010g.Util;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpts;
import edu.cwu.cs301.bb010g.pr3.Move.CastlingData;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AccessLevel;
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
    // pawns are special
    Moves.moveOffsets.put(Type.KNIGHT, Moves.leaper(1, 2));
    Moves.moveOffsets.put(Type.ROOK, Moves.rider(1, 0));
    Moves.moveOffsets.put(Type.BISHOP, Moves.rider(1, 1));
    {
      val offsets = new HashMap<IntPair, List<IntPair>>();
      Moves.moveOffsets.put(Type.QUEEN, offsets);
      offsets.putAll(Moves.rider(1, 0));
      offsets.putAll(Moves.rider(1, 1));
    }
    {
      val offsets = new HashMap<IntPair, List<IntPair>>();
      Moves.moveOffsets.put(Type.KING, offsets);
      offsets.putAll(Moves.leaper(1, 0));
      offsets.putAll(Moves.leaper(1, 1));
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
      if (!nZero) {
        offsets.put(IntPair.of(n, -m), lst6 = new ArrayList<>());
      }
      if (!mZero) {
        offsets.put(IntPair.of(-n, m), lst7 = new ArrayList<>());
      }
      if (!mZero && !nZero) {
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
        if (!nZero) {
          lst6.add(IntPair.of(-n, m).multMap(i));
        }
        if (!mZero) {
          lst7.add(IntPair.of(n, -m).multMap(i));
        }
        if (!mZero && !nZero) {
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
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public class MovePlus implements Comparable<MovePlus> {
    Move move;
    int castlingOpts;
    int opposingCastlingOpts;
    IntPair enPassantTarget;
    // (king, rook(src, dest))
    Pair<IntPair, IntPair> castlingDests;

    public static MovePlus of(final Move move) {
      return new MovePlus(move, -1, -1, null, null);
    }

    public static MovePlus of(final Move move, final int castlingOpts) {
      return new MovePlus(move, castlingOpts, -1, null, null);
    }

    public static MovePlus of(final Move move, final int castlingOpts,
        final int opposingCastlingOpts) {
      return new MovePlus(move, castlingOpts, opposingCastlingOpts, null, null);
    }

    public static MovePlus ofEnPassant(final Move move, final IntPair enPassantTarget) {
      return new MovePlus(move, -1, -1, enPassantTarget, null);
    }

    public static MovePlus ofCastling(final Move move, final IntPair kingDest,
        final IntPair rookDest) {
      return new MovePlus(move, CastlingOpts.USED, -1, null, Pair.of(kingDest, rookDest));
    }

    public Optional<Integer> castlingOpts() {
      return this.castlingOpts == -1 ? Optional.empty() : Optional.of(this.castlingOpts);
    }

    public Optional<IntPair> enPassantTarget() {
      return Optional.ofNullable(this.enPassantTarget);
    }

    public Optional<Pair<IntPair, IntPair>> castlingDests() {
      return Optional.ofNullable(this.castlingDests);
    }

    @Override
    public int compareTo(final MovePlus that) {
      val moveCmp = this.move.compareTo(that.move);
      if (moveCmp != 0) {
        return moveCmp;
      }
      val castlingOptsCmp = Integer.compare(this.castlingOpts, that.castlingOpts);
      if (castlingOptsCmp != 0) {
        return castlingOptsCmp;
      }
      val opposingCastlingOptsCmp =
          Integer.compare(this.opposingCastlingOpts, that.opposingCastlingOpts);
      if (opposingCastlingOptsCmp != 0) {
        return opposingCastlingOptsCmp;
      }
      val enPassantTargetCmp = Comparators.nullsFirst(IntPair::compareTo)
          .compare(this.enPassantTarget, that.enPassantTarget);
      if (enPassantTargetCmp != 0) {
        return enPassantTargetCmp;
      }
      val castlingDestsCmp = Comparators
          .nullsFirst((final Pair<IntPair, IntPair> p, final Pair<IntPair, IntPair> q) -> Pair
              .compareToWithComp(Comparators.nullsFirst(IntPair::compareTo),
                  Comparators.nullsFirst(IntPair::compareTo), p, q))
          .compare(this.castlingDests, that.castlingDests);
      return castlingDestsCmp;
    }
  }

  public Stream<Pair<Board, Move>> step(final Board board) {
    return Moves.allMoves(board).map(m -> Moves.applyMove(board, m));
  }

  public Stream<MovePlus> allMoves(final Board board) {
    return Moves.allMoves(board, board.active());
  }

  public Stream<MovePlus> allMoves(final Board board, final Color color) {
    val promotionRank = Moves.promotionRank(color);
    return board.stream().filter(p -> p.fst != null && p.fst.color().equals(color)).flatMap(p -> {
      final IntPair coord = p.snd;
      return Moves.moves(board, color, promotionRank, coord.fst, coord.snd, p.fst);
    });
  }

  public Stream<MovePlus> attacking(final Board board, final Color attacker, final IntPair coord) {
    val defender = attacker == Color.BLACK ? Color.WHITE : Color.BLACK;
    val pieceTest = board.piece(coord);
    val boardTest = (pieceTest != null && pieceTest.color() == defender) ? board
        : board.withPiece(Piece.of(Type.PAWN, defender), coord.fst, coord.snd);
    return Moves.attackingPresent(boardTest, attacker, coord);
  }

  public Stream<MovePlus> attacking(final Board board, final Color attacker, final int file,
      final int rank) {
    val defender = attacker == Color.BLACK ? Color.WHITE : Color.BLACK;
    val pieceTest = board.piece(file, rank);
    val boardTest = (pieceTest != null && pieceTest.color() == defender) ? board
        : board.withPiece(Piece.of(Type.PAWN, defender), file, rank);
    return Moves.attackingPresent(boardTest, attacker, file, rank);
  }

  public Stream<MovePlus> attackingPresent(final Board board, final Color attacker,
      final IntPair coord) {
    return Moves.allMoves(board, attacker)
        .filter(m -> m.move.capture() && m.move.dest().equals(coord));
  }

  public Stream<MovePlus> attackingPresent(final Board board, final Color attacker, final int file,
      final int rank) {
    return Moves.allMoves(board, attacker).filter(m -> {
      if (!m.move.capture()) {
        return false;
      }
      // safe because capture is set -> move is set -> move is present
      final IntPair dest = m.move.dest().get();
      return dest.fst == file && dest.snd == rank;
    });
  }

  public Stream<MovePlus> check(final Board board, final Color attacker) {
    val defender = attacker == Color.BLACK ? Color.WHITE : Color.BLACK;
    // guaranteed to be a king on the board
    val king = board.stream().filter(p -> {
      final Piece piece = p.fst;
      return piece.type() == Type.KING && piece.color() == defender;
    }).findAny().get();
    return Moves.attackingPresent(board, attacker, king.snd);
  }

  public Stream<MovePlus> moves(final Board board, final int file, final int rank) {
    final Color activeColor = board.active();
    return Moves.moves(board, activeColor, Moves.promotionRank(activeColor), file, rank,
        board.piece(file, rank));
  }

  public Stream<MovePlus> moves(final Board board, final IntPair coords) {
    return Moves.moves(board, coords.fst, coords.snd);
  }

  public Stream<MovePlus> moves(final Board board, final Color color, final int file,
      final int rank) {
    return Moves.moves(board, color, Moves.promotionRank(color), file, rank,
        board.piece(file, rank));
  }

  public Stream<MovePlus> moves(final Board board, final Color color, final IntPair coords) {
    return Moves.moves(board, color, coords.fst, coords.snd);
  }

  public Stream<MovePlus> moves(final Board board, final Color color, final int promotionRank,
      final int file, final int rank, final Piece piece) {
    val otherColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
    val coord = IntPair.of(file, rank);
    val castling = board.castling();
    val type = piece.type();
    val king = board.stream().filter(p -> {
      final Piece test = p.fst;
      return test != null && test.type() == Type.KING && test.color() == color;
    }).findAny().get();
    val kingPiece = king.fst;
    val kingCoord = king.snd;
    if (type != Type.PAWN) {
      return Moves.normalMoves(board, color, file, rank, piece, otherColor, type, coord, castling,
          king, kingPiece, kingCoord);
    } else {
      return Moves.pawnMoves(board, color, rank, promotionRank, piece, otherColor, coord,
          kingCoord);
    }
  }

  private Stream<MovePlus> normalMoves(final Board board, final Color color, final int file,
      final int rank, final Piece piece, final Color otherColor, final Type type,
      final IntPair coord, final CastlingOpts castling, final Pair<Piece, IntPair> king,
      final Piece kingPiece, final IntPair kingCoord) {
    final Map<IntPair, List<IntPair>> offsets = Moves.moveOffsets.get(type);
    val IMMUT_DISTINCT = Spliterator.IMMUTABLE | Spliterator.DISTINCT;
    final int castlingOpts;
    if (type == Type.ROOK) {
      castlingOpts = castling.castlingOpts(color) & ~piece.rookSideRaw();
    } else if (type == Type.KING) {
      castlingOpts = CastlingOpts.USED;
    } else {
      castlingOpts = castling.castlingOpts(color);
    }
    final Stream<MovePlus> normalMoves = StreamSupport.stream(offsets.keySet(), IMMUT_DISTINCT)
        .flatMap(path -> Util.takeWhileInclusive(
            StreamSupport.stream(offsets.get(path), IMMUT_DISTINCT)
                .map(offset -> coord.add(color == Color.WHITE ? offset : offset.neg()))
                .takeWhile(destCoord -> Moves.validCoord(destCoord) && Moves.attackingPresent(
                    board.withPieces(Pair.of(null, coord), Pair.of(piece, destCoord)), otherColor,
                    kingCoord).count() == 0)
                .map(destCoord -> Pair.of(destCoord, board.piece(destCoord) == null)),
            dest -> dest.snd).map(dest -> {
              final Move.Builder moveB = Move.builderMove(piece, coord, dest.fst);
              if (!dest.snd) {
                moveB.capture(true);
                final Piece captured = board.piece(dest.fst);
                if (captured.type() == Type.ROOK) {
                  final int rookSideRaw = captured.rookSideRaw();
                  final int otherCastlingOpts = castling.castlingOpts(otherColor);
                  return MovePlus.of(moveB.build(), castlingOpts,
                      rookSideRaw == -1 ? otherCastlingOpts : otherCastlingOpts & ~rookSideRaw);
                }
              }
              return MovePlus.of(moveB.build(), castlingOpts);
            }));
    final Stream<MovePlus> castlingMoves = Moves.castlingMoves(board, color, file, rank, piece,
        otherColor, type, king, kingPiece, kingCoord);
    return RefStreams.concat(normalMoves, castlingMoves);
  }

  private Stream<MovePlus> castlingMoves(final Board board, final Color color, final int file,
      final int rank, final Piece piece, final Color otherColor, final Type type,
      final Pair<Piece, IntPair> king, final Piece kingPiece, final IntPair kingCoord) {
    final Stream.Builder<MovePlus> moves = RefStreams.builder();
    castling: if (type == Type.KING) {
      if (!board.castling().canCastle(color)) {
        break castling;
      }
      val rankArr = board.rankRaw(rank);
      int castlingOpt = CastlingOpts.A_SIDE;
      while (true) {
        castlingInner: {
          final int startFile;
          final int endFile;
          final int fileInc;
          switch (castlingOpt) {
            case CastlingOpts.A_SIDE:
              startFile = 0;
              endFile = Board.FILES;
              fileInc = 1;
              break;
            case CastlingOpts.H_SIDE:
              startFile = Board.FILES - 1;
              endFile = -1;
              fileInc = -1;
              break;
            default:
              throw new IllegalStateException();
          }
          val rookDestF = (Board.FILES / 2) - fileInc;
          val kingDestF = rookDestF - fileInc;
          Pair<Piece, Integer> rook = null;
          boolean rookDestFFound = false;
          boolean kingFound = false;
          boolean kingDestFFound = false;
          // for each file from the side of castling to the other
          for (int testFile = startFile; Math.abs(testFile - endFile) > 0; testFile += fileInc) {
            /*
             * 1. The king and the castling rook must not have previously moved, including having
             * castled.
             *
             * 2. No square from the king's initial square to the king's final square may be under
             * attack by an enemy piece.
             *
             * 3. All the squares between the king's initial and final squares (including the final
             * square), and all of the squares between the rook's initial and final squares
             * (including the final square), must be vacant except for the king and castling rook.
             */
            val testPiece = rankArr[testFile];
            if (rook == null && !rookDestFFound) {
              // looking for the right rook
              if (testPiece != null && testPiece.type() == Type.ROOK
                  && testPiece.rookSideRaw() == castlingOpt) {
                rook = Pair.of(testPiece, testFile);
              }
            } else if ((rook != null && !rookDestFFound) || (rook == null && rookDestFFound)) {
              // the path needs to stay clear, save for the king
              // if we haven't found the rook, then finding one means we made it
              // if we've found the rook, then finding its dest means we made it
              if (testPiece != null && testPiece != kingPiece) {
                if (rook == null && testPiece.type() == Type.ROOK
                    && testPiece.rookSideRaw() == castlingOpt) {
                  rook = Pair.of(testPiece, testFile);
                } else {
                  // the path wasn't clear
                  break castlingInner;
                }
              }
            }
            // we'll only find them once each and we're always looking for them, so out here is fine
            if (testFile == rookDestF) {
              rookDestFFound = true;
            }
            if (!kingFound && !kingDestFFound) {
              // waiting on the king
              if (testPiece == kingPiece) {
                // let's go
                kingFound = true;
              }
            } else if ((kingFound && !kingDestFFound) || (!kingFound && kingDestFFound)) {
              // the path needs to be clear, save for the rook
              // the path needs to not put the king in check
              if (testPiece != rook.fst) {
                // the path wasn't clear
                break castlingInner;
              }
              if (Moves
                  .attackingPresent(board.withPieces(Pair.of(null, king.snd),
                      Pair.of(piece, IntPair.of(testFile, rank))), otherColor, kingCoord)
                  .count() != 0) {
                // the path would put the king in check
                break castlingInner;
              }
            }
            // we'll only find it once and we're always looking for it, so out of the ifs is fine
            if (testFile == kingDestF) {
              kingDestFFound = true;
            }
            if (rook != null && rookDestFFound && kingFound && kingDestFFound) {
              // we've satisfied the castling requirements already
              break;
            }
          }
          // check for validity
          if (!(rook != null && rookDestFFound && kingFound && kingDestFFound)) {
            // the castling requirements were never met
            break castlingInner;
          }
          moves.add(MovePlus.ofCastling(
              Move.builderCastling(kingPiece, kingCoord,
                  CastlingData.of(castlingOpt, rook.fst, IntPair.of(rook.snd, file))).build(),
              IntPair.of(kingDestF, rank), IntPair.of(rookDestF, rank)));
        }
        switch (castlingOpt) {
          case CastlingOpts.A_SIDE:
            castlingOpt = CastlingOpts.H_SIDE;
            break;
          case CastlingOpts.H_SIDE:
            break;
          default:
            throw new IllegalStateException();
        }
      }
    }
    return moves.build();
  }

  private Stream<MovePlus> pawnMoves(final Board board, final Color color, final int rank,
      final int promotionRank, final Piece piece, final Color otherColor, final IntPair coord,
      final IntPair kingCoord) {
    final Stream.Builder<MovePlus> moves = RefStreams.builder();
    normal: {
      val normalOffset = IntPair.of(color == Color.WHITE ? 1 : -1, 0);
      val normalMove = coord.add(normalOffset);
      if (Moves.validCoord(normalMove) && board.piece(normalMove) == null
          && Moves
              .attackingPresent(board.withPieces(Pair.of(null, coord), Pair.of(piece, normalMove)),
                  otherColor, kingCoord)
              .count() == 0) {
        val moveB = Move.builder().piece(piece).src(coord).dest(normalMove);
        if (normalMove.fst != promotionRank) {
          moves.add(MovePlus.of(moveB.build()));
        } else {
          // mutation of the builder (sharing members) is safe because the members are
          // immutable and can be shared, and the mutated entry is a new ref each time
          moves.add(MovePlus.of(moveB.promotion(Type.KNIGHT).build()))
              .add(MovePlus.of(moveB.promotion(Type.ROOK).build()))
              .add(MovePlus.of(moveB.promotion(Type.BISHOP).build()))
              .add(MovePlus.of(moveB.promotion(Type.QUEEN).build()));
        }
      } else {
        // ensures that normalMove is clear for two square advance
        break normal;
      }
      if (rank == Moves.pawnRank(color)) {
        val initOffset = IntPair.of(color == Color.WHITE ? 2 : -2, 0);
        val initMove = coord.add(initOffset);
        if (Moves.validCoord(initMove) && board.piece(initMove) == null
            && Moves
                .attackingPresent(board.withPieces(Pair.of(null, coord), Pair.of(piece, initMove)),
                    otherColor, kingCoord)
                .count() == 0) {
          val moveB = Move.builder().piece(piece).src(coord).dest(initMove);
          if (initMove.fst != promotionRank) {
            moves.add(MovePlus.ofEnPassant(moveB.build(), normalMove));
          } else {
            moves.add(MovePlus.ofEnPassant(moveB.promotion(Type.KNIGHT).build(), normalMove))
                .add(MovePlus.ofEnPassant(moveB.promotion(Type.ROOK).build(), normalMove))
                .add(MovePlus.ofEnPassant(moveB.promotion(Type.BISHOP).build(), normalMove))
                .add(MovePlus.ofEnPassant(moveB.promotion(Type.QUEEN).build(), normalMove));
          }
        }
      }
    }
    {
      IntPair captOffset = IntPair.of(1, 1);
      if (color == Color.BLACK) {
        captOffset = captOffset.neg();
      }
      for (int loop = 0;; loop++) {
        val captMove = coord.add(captOffset);
        if (Moves.validCoord(captMove) && Moves
            .attackingPresent(board.withPieces(Pair.of(null, coord), Pair.of(piece, captMove)),
                otherColor, kingCoord)
            .count() == 0) {
          val moveB = Move.builder().piece(piece).src(coord).dest(captMove).capture(true);
          if (board.piece(captMove) != null) {
            if (captMove.fst != promotionRank) {
              moves.add(MovePlus.of(moveB.build()));
            } else {
              moves.add(MovePlus.of(moveB.promotion(Type.KNIGHT).build()))
                  .add(MovePlus.of(moveB.promotion(Type.ROOK).build()))
                  .add(MovePlus.of(moveB.promotion(Type.BISHOP).build()))
                  .add(MovePlus.of(moveB.promotion(Type.QUEEN).build()));
            }
          } else if (captMove.equals(board.enPassantTarget())) {
            moves.add(MovePlus.of(moveB.enPassant(true).build()));
          }
        }
        if (loop == 0) {
          captOffset = IntPair.of(-1, 1);
        } else if (loop == 1) {
          break;
        }
      }
    }
    return moves.build();
  }

  public static Pair<Board, Move> applyMove(final Board board, final MovePlus movePlus) {
    val move = movePlus.move;
    val active = board.active();
    val newActive = active == Color.WHITE ? Color.BLACK : Color.WHITE;
    Piece piece = move.piece();
    val src = move.src();
    val dest_ = move.dest();
    val castlingOpts = movePlus.castlingOpts;
    val opposingCastlingOpts = movePlus.opposingCastlingOpts;
    final Piece[] newBoardArr;
    final CastlingOpts newCastlingOpts;
    final IntPair newEnPassantTarget;
    if (dest_.isPresent()) {
      {
        val dest = dest_.get();
        val promotion_ = move.promotion();
        if (promotion_.isPresent()) {
          piece = piece.withType(promotion_.get());
        }
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
      {
        CastlingOpts newCastlingOpts_ = board.castling();
        if (castlingOpts != -1) {
          newCastlingOpts_ = newCastlingOpts_.withOpts(active, castlingOpts);
        }
        if (opposingCastlingOpts != -1) {
          newCastlingOpts_ = newCastlingOpts_.withOpts(newActive, opposingCastlingOpts);
        }
        newCastlingOpts = newCastlingOpts_;
      }
    } else {
      // castling
      val castling = move.castling().get();
      val castlingDests = movePlus.castlingDests;
      val kingDest = castlingDests.fst;
      val rook = castling.rook();
      val rookSrc = castling.rookCoord();
      val rookDest = castlingDests.snd;
      newBoardArr = new Piece[Board.SIZE];
      System.arraycopy(board.board(), 0, newBoardArr, 0, Board.SIZE);
      newBoardArr[src.fst * Board.RANKS + src.snd] = null;
      newBoardArr[rookSrc.fst * Board.RANKS + rookSrc.snd] = null;
      newBoardArr[kingDest.fst * Board.RANKS + kingDest.snd] = piece;
      newBoardArr[rookDest.fst * Board.RANKS + rookDest.snd] = rook;
      newCastlingOpts = board.castling().withOpts(active, castlingOpts);
      newEnPassantTarget = movePlus.enPassantTarget;
    }
    val newHalfmoveClock = board.halfmoveClock() + 1;
    val newFullmoveNum = board.fullmoveNum() + (active == Color.BLACK ? 1 : 0);
    return Pair.of(Board.of(newBoardArr, newActive, newCastlingOpts, newEnPassantTarget,
        newHalfmoveClock, newFullmoveNum), move);
  }
}
