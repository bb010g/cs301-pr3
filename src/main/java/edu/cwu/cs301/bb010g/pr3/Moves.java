package edu.cwu.cs301.bb010g.pr3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import java8.util.Maps;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.Pair;
import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpt;
import edu.cwu.cs301.bb010g.pr3.Move.CastlingData;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import lombok.experimental.UtilityClass;
import lombok.experimental.Wither;

@UtilityClass
public class Moves {
  // offsets are from active player's POV
  public final Map<Piece.Type, Map<IntPair, List<IntPair>>> moveOffsets;
  static {
    moveOffsets = new EnumMap<>(Piece.Type.class);
    // pawns are special
    Moves.moveOffsets.put(Type.KNIGHT, Moves.leaper(1, 2));
    Moves.moveOffsets.put(Type.ROOK, Moves.rider(1, 0));
    Moves.moveOffsets.put(Type.BISHOP, Moves.rider(1, 1));
    {
      val offsets = new TreeMap<IntPair, List<IntPair>>();
      Moves.moveOffsets.put(Type.QUEEN, offsets);
      offsets.putAll(Moves.rider(1, 0));
      offsets.putAll(Moves.rider(1, 1));
    }
    {
      val offsets = new TreeMap<IntPair, List<IntPair>>();
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
    val offsets = new TreeMap<IntPair, List<IntPair>>();
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
    val offsets = new TreeMap<IntPair, List<IntPair>>();
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
    @NonNull
    Move move;
    IntPair enPassantTarget;
    // not required, but we're computing it anyways
    IntPair castlingKingDest;
    // not required, but we're computing it anyways
    IntPair castlingRookDest;

    public static MovePlus of(final Move move) {
      return new MovePlus(move, null, null, null);
    }

    public static MovePlus ofEnPassant(final Move move, final IntPair enPassantTarget) {
      return new MovePlus(move, enPassantTarget, null, null);
    }

    public static MovePlus ofCastling(final Move move, final IntPair kingDest,
        final IntPair rookDest) {
      return new MovePlus(move, null, kingDest, rookDest);
    }

    @Override
    public int compareTo(final MovePlus that) {
      val moveCmp = this.move.compareTo(that.move);
      if (moveCmp != 0) {
        return moveCmp;
      }
      val enPassantTargetCmp = Comparator.nullsFirst(IntPair::compareTo)
          .compare(this.enPassantTarget, that.enPassantTarget);
      if (enPassantTargetCmp != 0) {
        return enPassantTargetCmp;
      }
      val castlingKingDestCmp = Comparator.nullsFirst(IntPair::compareTo)
          .compare(this.castlingKingDest, that.castlingKingDest);
      if (castlingKingDestCmp != 0) {
        return castlingKingDestCmp;
      }
      return Comparator.nullsFirst(IntPair::compareTo).compare(this.castlingRookDest,
          that.castlingRookDest);
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
    return Moves.allMoves(board, attacker).filter(m -> m.move.capture())
        .filter(m -> m.move.dest().equals(coord));
  }

  public Stream<MovePlus> attacking(final Board board, final Color attacker, final int file,
      final int rank) {
    return Moves.allMoves(board, attacker).filter(m -> m.move.capture()).filter(m -> {
      final IntPair dest = m.move.dest();
      return dest.fst == file && dest.snd == rank;
    });
  }

  public Stream<MovePlus> check(final Board board, final Color attacker) {
    // guaranteed to be a king on the board
    val king = board.stream().filter(p -> {
      final Piece piece = p.fst;
      return piece.type() == Type.KING && piece.color() == attacker;
    }).findAny().get();
    return Moves.attacking(board, attacker, king.snd);
  }

  public Stream<MovePlus> moves(final Board board, final int file, final int rank) {
    val piece = board.piece(file, rank);
    val color = board.active();
    val promotionRank = Moves.promotionRank(color);
    return Moves.moves(board, color, promotionRank, file, rank, piece);
  }

  public Stream<MovePlus> moves(final Board board, final IntPair coords) {
    val file = coords.fst;
    val rank = coords.snd;
    return Moves.moves(board, file, rank);
  }

  public Stream<MovePlus> moves(final Board board, final Color color, final int file,
      final int rank) {
    val piece = board.piece(file, rank);
    val promotionRank = Moves.promotionRank(color);
    return Moves.moves(board, color, promotionRank, file, rank, piece);
  }

  public Stream<MovePlus> moves(final Board board, final Color color, final IntPair coords) {
    val file = coords.fst;
    val rank = coords.snd;
    return Moves.moves(board, color, file, rank);
  }

  public Stream<MovePlus> moves(final Board board, final Color color, final int promotionRank,
      final int file, final int rank, final Piece piece) {
    final Stream.Builder<MovePlus> moves = RefStreams.builder();
    val type = piece.type();
    val coord = IntPair.of(file, rank);
    if (type != Type.PAWN) {
      for (val pathE : Moves.moveOffsets.get(type).entrySet()) {
        for (val offset : pathE.getValue()) {
          val possible = coord.add(color == Color.WHITE ? offset : offset.neg());
          if (!Moves.validCoord(possible)) {
            break;
          }
          if (board.piece(possible) != null) {
            moves.add(MovePlus.of(Move.builderCapture(piece.move(), coord, possible).build()));
            break;
          }
          moves.add(MovePlus.of(Move.builderMove(piece.move(), coord, possible).build()));
        }
      }
      castling: if (type == Type.KING) {
        val castlingOpts = board.castling().castlingOpts(color);
        if (castlingOpts.isEmpty()) {
          break castling;
        }
        val rankArr = board.rankRaw(rank);
        Pair<Piece, Integer> rook = null;
        Pair<Piece, Integer> king = null;
        CastlingOpt castlingOpt = CastlingOpt.A_SIDE;
        inf: while (true) {
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
                startFile = 0;
                endFile = Board.FILES;
                fileInc = 1;
                break;
              default:
                throw new IllegalStateException();
            }
            for (int ixFile = startFile; Math.abs(ixFile - endFile) > 0; ixFile += fileInc) {
              val p = rankArr[ixFile];
              if (p == null) {
                continue;
              }
              val pType = p.type();
              if (rook != null) {
                break castlingInner;
              }
              if (pType == Type.ROOK && !p.moved()) {
                rook = Pair.of(p, ixFile);
              } else if (pType == Type.KING) {
                king = Pair.of(p, ixFile);
                break castlingInner;
              }
            }
            val sideMod = (fileInc + 1) / 2;
            val rookDestF = (Board.FILES / 2) - 2 * sideMod;
            val rookDest = rankArr[rookDestF];
            val kingDestF = (Board.FILES / 2) - 1 * sideMod;
            val kingDest = rankArr[kingDestF];
            val rookP = rook.fst;
            val kingP = king.fst;
            if (!(rookDest == rookP || rookDest == kingP || rookDest == null)
                || !(kingDest == rookP || kingDest == kingP || kingDest == null)) {
              continue inf;
            }
            moves.add(MovePlus.ofCastling(
                Move.builderCastling(kingP.move(), IntPair.of(king.snd, rank),
                    CastlingData.of(castlingOpt, rookP, IntPair.of(rook.snd, file))).build(),
                IntPair.of(kingDestF, rank), IntPair.of(rookDestF, rank)));
          }
          switch (castlingOpt) {
            case A_SIDE:
              castlingOpt = CastlingOpt.H_SIDE;
              break;
            case H_SIDE:
              break inf;
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
        if (Moves.validCoord(normalMove) && board.piece(normalMove) == null) {
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
          if (Moves.validCoord(initMove) && board.piece(initMove) == null) {
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
          final IntPair captMove = coord.add(captOffset);
          if (Moves.validCoord(captMove)) {
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
            captMove.mult(IntPair.of(1, -1));
          } else if (loop == 1) {
            break;
          }
        }
      }
    }
    return moves.build();
  }

  public static Pair<Board, Move> applyMove(final Board board, final MovePlus move) {
    return null;
  }
}
