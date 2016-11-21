package edu.cwu.cs301.bb010g.pr3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java8.util.Comparators;
import java8.util.Maps;
import java8.util.Optional;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.NotImplementedException;
import edu.cwu.cs301.bb010g.Pair;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpt;
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
    val lst1 = Maps.putIfAbsent(offsets, IntPair.of(m, n), new ArrayList<>());
    val lst2 = Maps.putIfAbsent(offsets, IntPair.of(m, -n), new ArrayList<>());
    val lst3 = Maps.putIfAbsent(offsets, IntPair.of(-m, n), new ArrayList<>());
    val lst4 = Maps.putIfAbsent(offsets, IntPair.of(-m, -n), new ArrayList<>());
    val lst5 = Maps.putIfAbsent(offsets, IntPair.of(n, m), new ArrayList<>());
    val lst6 = Maps.putIfAbsent(offsets, IntPair.of(n, -m), new ArrayList<>());
    val lst7 = Maps.putIfAbsent(offsets, IntPair.of(-n, m), new ArrayList<>());
    val lst8 = Maps.putIfAbsent(offsets, IntPair.of(-n, -m), new ArrayList<>());
    for (int i = 1; i < Math.max(Board.RANKS, Board.FILES) / m; i++) {
      lst1.add(IntPair.of(m, n).multMap(i));
      lst2.add(IntPair.of(m, -n).multMap(i));
      lst3.add(IntPair.of(-m, n).multMap(i));
      lst4.add(IntPair.of(-m, -n).multMap(i));
      lst5.add(IntPair.of(n, m).multMap(i));
      lst6.add(IntPair.of(-n, m).multMap(i));
      lst7.add(IntPair.of(n, -m).multMap(i));
      lst8.add(IntPair.of(-n, -m).multMap(i));
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
    val lst1 = Maps.putIfAbsent(offsets, IntPair.of(m, n), new ArrayList<>());
    val lst2 = Maps.putIfAbsent(offsets, IntPair.of(m, -n), new ArrayList<>());
    val lst3 = Maps.putIfAbsent(offsets, IntPair.of(-m, n), new ArrayList<>());
    val lst4 = Maps.putIfAbsent(offsets, IntPair.of(-m, -n), new ArrayList<>());
    val lst5 = Maps.putIfAbsent(offsets, IntPair.of(n, m), new ArrayList<>());
    val lst6 = Maps.putIfAbsent(offsets, IntPair.of(n, -m), new ArrayList<>());
    val lst7 = Maps.putIfAbsent(offsets, IntPair.of(-n, m), new ArrayList<>());
    val lst8 = Maps.putIfAbsent(offsets, IntPair.of(-n, -m), new ArrayList<>());
    lst1.add(IntPair.of(m, n));
    lst2.add(IntPair.of(m, -n));
    lst3.add(IntPair.of(-m, n));
    lst4.add(IntPair.of(-m, -n));
    lst5.add(IntPair.of(n, m));
    lst6.add(IntPair.of(-n, m));
    lst7.add(IntPair.of(n, -m));
    lst8.add(IntPair.of(-n, -m));
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
    switch (color) {
      case WHITE:
        return Board.RANKS - 1;
      case BLACK:
        return 0;
      default:
        return -1;
    }
  }

  public int WHITE_PROMOTION_RANK = Moves.promotionRank(Color.WHITE);
  public int BLACK_PROMOTION_RANK = Moves.promotionRank(Color.BLACK);

  // extra data to keep the board happy
  @Value
  @Wither
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public class MovePlus implements Comparable<MovePlus> {
    Move move;
    IntPair enPassantTarget;
    // not required, but we're computing it anyways
    // (king, rook)
    Pair<IntPair, IntPair> castlingDests;

    public static MovePlus of(final Move move) {
      return new MovePlus(move, null, null);
    }

    public static MovePlus ofEnPassant(final Move move, final IntPair enPassantTarget) {
      return new MovePlus(move, enPassantTarget, null);
    }

    public static MovePlus ofCastling(final Move move, final IntPair kingDest,
        final IntPair rookDest) {
      return new MovePlus(move, null, Pair.of(kingDest, rookDest));
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
      val enPassantTargetCmp = Comparators.nullsFirst(IntPair::compareTo)
          .compare(this.enPassantTarget, that.enPassantTarget);
      if (enPassantTargetCmp != 0) {
        return enPassantTargetCmp;
      }
      val castlingDestsCmp = Comparators
          .nullsFirst((Pair<IntPair, IntPair> p, Pair<IntPair, IntPair> q) -> Pair
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
    return board.stream().filter(p -> p != null && p.fst.color().equals(color)).flatMap(p -> {
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
    final Stream.Builder<MovePlus> moves = RefStreams.builder();
    val otherColor = color == Color.WHITE ? Color.BLACK : Color.WHITE;
    val type = piece.type();
    val coord = IntPair.of(file, rank);
    val king = board.stream().filter(p -> {
      final Piece test = p.fst;
      return test.type() == Type.KING && test.color() == color;
    }).findAny().get();
    val kingPiece = king.fst;
    val kingCoord = king.snd;
    if (type != Type.PAWN) {
      for (val pathE : Moves.moveOffsets.get(type).entrySet()) {
        for (val offset : pathE.getValue()) {
          val destCoord = coord.add(color == Color.WHITE ? offset : offset.neg());
          if (!Moves.validCoord(destCoord) || Moves
              .attackingPresent(board.withPieces(Pair.of(null, coord), Pair.of(piece, destCoord)),
                  otherColor, kingCoord)
              .count() != 0) {
            break;
          }
          val moveB = Move.builderMove(piece.move(), coord, destCoord);
          if (board.piece(destCoord) != null) {
            moves.add(MovePlus.of(moveB.capture(true).build()));
            break;
          }
          moves.add(MovePlus.of(moveB.build()));
        }
      }
      castling: if (type == Type.KING) {
        if (board.castling().castlingOpt(color).isPresent()) {
          break castling;
        }
        val rankArr = board.rankRaw(rank);
        CastlingOpt castlingOpt = CastlingOpt.A_SIDE;
        while (true) {
          castlingInner: {
            final int startFile;
            final int endFile;
            final int fileInc;
            switch (castlingOpt) {
              case A_SIDE:
                startFile = 0;
                endFile = Board.FILES;
                fileInc = 1;
                break;
              case H_SIDE:
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
            boolean kingRookEncounter = false;
            // for each file from the side of castling to the other
            for (int testFile = startFile; Math.abs(testFile - endFile) > 0; testFile += fileInc) {
              /*
               * 1. The king and the castling rook must not have previously moved, including having
               * castled.
               *
               * 2. No square from the king's initial square to the king's final square may be under
               * attack by an enemy piece.
               *
               * 3. All the squares between the king's initial and final squares (including the
               * final square), and all of the squares between the rook's initial and final squares
               * (including the final square), must be vacant except for the king and castling rook.
               */
              val testPiece = rankArr[testFile];
              if (rook == null && !rookDestFFound) {
                // looking for the right rook
                if (testPiece != null && testPiece.type() == Type.ROOK && !testPiece.moved()) {
                  // found a good one
                  rook = Pair.of(testPiece, testFile);
                }
              } else if ((rook != null && !rookDestFFound) || (rook == null && rookDestFFound)) {
                // if this is the right rook, the path needs to stay clear, save for the king
                // if this is the wrong rook and we found the right one, use it
                // if we haven't found the rook, then finding one means we made it
                // if we have the right rook, then finding its dest means we made it
                if (testPiece != null && testPiece != kingPiece) {
                  if (testPiece.type() == Type.ROOK && !testPiece.moved()) {
                    rook = Pair.of(testPiece, testFile);
                  } else {
                    rook = null;
                  }
                }
              }
              // we'll only find it once and we're always looking for it, so out of the ifs is fine
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
                // the path needs to be clear, save for the right rook
                // the path needs to not put the king in check
                if (testPiece != null && testPiece == rook.fst && !kingRookEncounter) {
                  // we can only encounter the right rook on this path once
                  // more than one right rook means one we found was bad
                  kingRookEncounter = true;
                } else {
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
                Move.builderCastling(kingPiece.move(), kingCoord,
                    CastlingData.of(castlingOpt, rook.fst, IntPair.of(rook.snd, file))).build(),
                IntPair.of(kingDestF, rank), IntPair.of(rookDestF, rank)));
          }
          switch (castlingOpt) {
            case A_SIDE:
              castlingOpt = CastlingOpt.H_SIDE;
              break;
            case H_SIDE:
              break;
            default:
              throw new IllegalStateException();
          }
        }
      }
    } else {
      normal: {
        IntPair normalOffset = IntPair.of(1, 0);
        if (color == Color.BLACK) {
          normalOffset = normalOffset.neg();
        }
        val normalMove = coord.add(normalOffset);
        if (Moves.validCoord(normalMove) && board.piece(normalMove) == null
            && Moves.attackingPresent(
                board.withPieces(Pair.of(null, coord), Pair.of(piece, normalMove)), otherColor,
                kingCoord).count() == 0) {
          val moveB = Move.builder().piece(piece.move()).src(coord).dest(normalMove);
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
        if (!piece.moved()) {
          IntPair initOffset = IntPair.of(2, 0);
          if (color == Color.BLACK) {
            initOffset = initOffset.neg();
          }
          val initMove = coord.add(initOffset);
          if (Moves.validCoord(initMove) && board.piece(initMove) == null
              && Moves.attackingPresent(
                  board.withPieces(Pair.of(null, coord), Pair.of(piece, initMove)), otherColor,
                  kingCoord).count() == 0) {
            val moveB = Move.builder().piece(piece.move()).src(coord).dest(initMove);
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
            val moveB = Move.builder().piece(piece.move()).src(coord).dest(captMove).capture(true);
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
    }
    return moves.build();
  }

  public static Pair<Board, Move> applyMove(final Board board, final MovePlus move) {
    throw new NotImplementedException("TODO");
  }
}
