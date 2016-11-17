package edu.cwu.cs301.bb010g.pr3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.cwu.cs301.bb010g.pr3.Board.CastlingOpts;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
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

  // static methods inside static methods would be nice
  private Pair<List<IntPair>, Boolean> mapInit_(final Map<IntPair, List<IntPair>> offsets,
      final IntPair type) {
    List<IntPair> lst = new ArrayList<>();
    val lstPrev = offsets.put(type, lst);
    boolean dup = false;
    if (lstPrev != null) {
      dup = true;
      lst = lstPrev;
      offsets.put(type, lst);
    }
    return Pair.of(lst, dup);
  }

  // Java 8 has some nice methods for Maps, like putIfAbsent.
  public Map<IntPair, List<IntPair>> rider(int m_, int n_) {
    if (m_ > n_) {
      val swp = m_;
      m_ = n_;
      n_ = swp;
    }
    val m = m_;
    val n = n_;
    val offsets = new TreeMap<IntPair, List<IntPair>>();
    final List<IntPair> lst1, lst2, lst3, lst4, lst5, lst6, lst7, lst8;
    final boolean dup1, dup2, dup3, dup4, dup5, dup6, dup7, dup8;
    {
      val lst1_ = Moves.mapInit_(offsets, IntPair.of(m, n));
      lst1 = lst1_.fst;
      dup1 = lst1_.snd;
    }
    {
      val lst2_ = Moves.mapInit_(offsets, IntPair.of(m, -n));
      lst2 = lst2_.fst;
      dup2 = lst2_.snd;
    }
    {
      val lst3_ = Moves.mapInit_(offsets, IntPair.of(-m, n));
      lst3 = lst3_.fst;
      dup3 = lst3_.snd;
    }
    {
      val lst4_ = Moves.mapInit_(offsets, IntPair.of(-m, -n));
      lst4 = lst4_.fst;
      dup4 = lst4_.snd;
    }
    {
      val lst5_ = Moves.mapInit_(offsets, IntPair.of(n, m));
      lst5 = lst5_.fst;
      dup5 = lst5_.snd;
    }
    {
      val lst6_ = Moves.mapInit_(offsets, IntPair.of(n, -m));
      lst6 = lst6_.fst;
      dup6 = lst6_.snd;
    }
    {
      val lst7_ = Moves.mapInit_(offsets, IntPair.of(-n, m));
      lst7 = lst7_.fst;
      dup7 = lst7_.snd;
    }
    {
      val lst8_ = Moves.mapInit_(offsets, IntPair.of(-n, -m));
      lst8 = lst8_.fst;
      dup8 = lst8_.snd;
    }
    for (int i = 1; i < Math.max(Board.RANKS, Board.FILES) / m; i++) {
      if (!dup1) {
        lst1.add(IntPair.of(m, n).multMap(i));
      }
      if (!dup2) {
        lst2.add(IntPair.of(m, -n).multMap(i));
      }
      if (!dup3) {
        lst3.add(IntPair.of(-m, n).multMap(i));
      }
      if (!dup4) {
        lst4.add(IntPair.of(-m, -n).multMap(i));
      }
      if (!dup5) {
        lst5.add(IntPair.of(n, m).multMap(i));
      }
      if (!dup6) {
        lst6.add(IntPair.of(-n, m).multMap(i));
      }
      if (!dup7) {
        lst7.add(IntPair.of(n, -m).multMap(i));
      }
      if (!dup8) {
        lst8.add(IntPair.of(-n, -m).multMap(i));
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
    val offsets = new TreeMap<IntPair, List<IntPair>>();
    final List<IntPair> lst1, lst2, lst3, lst4, lst5, lst6, lst7, lst8;
    final boolean dup1, dup2, dup3, dup4, dup5, dup6, dup7, dup8;
    {
      val lst1_ = Moves.mapInit_(offsets, IntPair.of(m, n));
      lst1 = lst1_.fst;
      dup1 = lst1_.snd;
    }
    {
      val lst2_ = Moves.mapInit_(offsets, IntPair.of(m, -n));
      lst2 = lst2_.fst;
      dup2 = lst2_.snd;
    }
    {
      val lst3_ = Moves.mapInit_(offsets, IntPair.of(-m, n));
      lst3 = lst3_.fst;
      dup3 = lst3_.snd;
    }
    {
      val lst4_ = Moves.mapInit_(offsets, IntPair.of(-m, -n));
      lst4 = lst4_.fst;
      dup4 = lst4_.snd;
    }
    {
      val lst5_ = Moves.mapInit_(offsets, IntPair.of(n, m));
      lst5 = lst5_.fst;
      dup5 = lst5_.snd;
    }
    {
      val lst6_ = Moves.mapInit_(offsets, IntPair.of(n, -m));
      lst6 = lst6_.fst;
      dup6 = lst6_.snd;
    }
    {
      val lst7_ = Moves.mapInit_(offsets, IntPair.of(-n, m));
      lst7 = lst7_.fst;
      dup7 = lst7_.snd;
    }
    {
      val lst8_ = Moves.mapInit_(offsets, IntPair.of(-n, -m));
      lst8 = lst8_.fst;
      dup8 = lst8_.snd;
    }
    if (!dup1) {
      lst1.add(IntPair.of(m, n));
    }
    if (!dup2) {
      lst2.add(IntPair.of(m, -n));
    }
    if (!dup3) {
      lst3.add(IntPair.of(-m, n));
    }
    if (!dup4) {
      lst4.add(IntPair.of(-m, -n));
    }
    if (!dup5) {
      lst5.add(IntPair.of(n, m));
    }
    if (!dup6) {
      lst6.add(IntPair.of(n, -m));
    }
    if (!dup7) {
      lst7.add(IntPair.of(-n, m));
    }
    if (!dup8) {
      lst8.add(IntPair.of(-n, -m));
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
  @AllArgsConstructor(staticName = "of")
  public class MovePlus implements Comparable<MovePlus> {
    @NonNull
    Move move;
    IntPair enPassantTarget;
    @NonNull
    CastlingOpts castling;

    public static MovePlus of(final Move move, final CastlingOpts castling) {
      return MovePlus.of(move, null, castling);
    }

    @Override
    public int compareTo(final MovePlus that) {
      val moveCmp = this.move.compareTo(that.move);
      if (moveCmp != 0) {
        return moveCmp;
      }
      val enPassantTargetCmp =
          (this.enPassantTarget == null) ? (that.enPassantTarget == null ? 0 : -1)
              : this.enPassantTarget.compareTo(that.enPassantTarget);
      if (enPassantTargetCmp != 0) {
        return enPassantTargetCmp;
      }
      return this.castling.compareTo(that.castling);
    }
  }

  public Set<Pair<Board, Move>> step(final Board board) {
    val boards = new HashSet<Pair<Board, Move>>();
    val moves = Moves.allMoves(board);
    for (val move : moves) {
      boards.add(Moves.applyMove(board, move));
    }
    return boards;
  }

  public Deque<MovePlus> allMoves(final Board board) {
    return Moves.allMoves(board, board.active());
  }

  public Deque<MovePlus> allMoves(final Board board, final Color color) {
    val promotionRank = Moves.promotionRank(color);
    // (move, enPassantTarget)
    val moves = new ArrayDeque<MovePlus>();
    for (int file = 0; file < Board.FILES; file++) {
      for (int rank = 0; rank < Board.RANKS; rank++) {
        val piece = board.board()[file][rank];
        if (piece == null) {
          continue;
        }
        if (!piece.color().equals(color)) {
          continue;
        }
        moves.addAll(Moves.moves(board, color, board.castling(), promotionRank, file, rank, piece));
      }
    }
    return moves;
  }

  public Deque<MovePlus> attacking(final Board board, final Color attacker, final int file,
      final int rank) {
    final Deque<MovePlus> moves = Moves.allMoves(board, attacker);
    {
      val iter = moves.iterator();
      while (iter.hasNext()) {
        val moveP = iter.next();
        val move = moveP.move;
        if (!move.capture()) {
          iter.remove();
          continue;
        }
        val dest = move.dest();
        val dFile = dest.fst;
        val dRank = dest.snd;
        if (!(dFile == file) || !(dRank == rank)) {
          iter.remove();
          continue;
        }
      }
    }
    return moves;
  }

  public Deque<MovePlus> check(final Board that, final Color attacker) {
    val board = that.board();
    int kFile = -1;
    int kRank = -1;
    loop: for (int file = 0; file < Board.FILES; file++) {
      for (int rank = 0; rank < Board.RANKS; rank++) {
        val piece = board[file][rank];
        if (piece.type() != Type.KING || piece.color() == attacker) {
          continue;
        }
        kFile = file;
        kRank = rank;
        break loop;
      }
    }
    val moves = Moves.attacking(that, attacker, kFile, kRank);
    return moves;
  }

  public Deque<MovePlus> moves(final Board board, final int file, final int rank) {
    val piece = board.board()[file][rank];
    val color = board.active();
    val castling = board.castling();
    val promotionRank = Moves.promotionRank(color);
    return Moves.moves(board, color, castling, promotionRank, file, rank, piece);
  }

  public Deque<MovePlus> moves(final Board board, final IntPair coords) {
    val file = coords.fst;
    val rank = coords.snd;
    return Moves.moves(board, file, rank);
  }

  public Deque<MovePlus> moves(final Board board, final Color color, final int file,
      final int rank) {
    val piece = board.board()[file][rank];
    val castling = board.castling();
    val promotionRank = Moves.promotionRank(color);
    return Moves.moves(board, color, castling, promotionRank, file, rank, piece);
  }

  public Deque<MovePlus> moves(final Board board, final Color color, final IntPair coords) {
    val file = coords.fst;
    val rank = coords.snd;
    return Moves.moves(board, color, file, rank);
  }

  public Deque<MovePlus> moves(final Board board, final Color color, final CastlingOpts castling,
      final int promotionRank, final int file, final int rank, final Piece piece) {
    final Deque<MovePlus> moves = new ArrayDeque<>();
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
            moves.push(MovePlus.of(
                Move.builder().piece(piece.move()).src(coord).dest(possible).capture(true).build(),
                castling));
            break;
          }
          moves.push(MovePlus
              .of(Move.builder().piece(piece.move()).src(coord).dest(possible).build(), castling));
        }
      }
      if (type == Type.KING & !piece.moved()) {
        // castling
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
            moves.push(MovePlus.of(moveB.build(), castling));
          } else {
            // mutation of the builder (sharing members) is safe because the members are
            // immutable and can be shared, and the mutated entry is a new ref each time
            moves.push(MovePlus.of(moveB.promotion(Type.KNIGHT).build(), castling));
            moves.push(MovePlus.of(moveB.promotion(Type.ROOK).build(), castling));
            moves.push(MovePlus.of(moveB.promotion(Type.BISHOP).build(), castling));
            moves.push(MovePlus.of(moveB.promotion(Type.QUEEN).build(), castling));
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
              moves.push(MovePlus.of(moveB.build(), normalMove, castling));
            } else {
              moves.push(MovePlus.of(moveB.promotion(Type.KNIGHT).build(), normalMove, castling));
              moves.push(MovePlus.of(moveB.promotion(Type.ROOK).build(), normalMove, castling));
              moves.push(MovePlus.of(moveB.promotion(Type.BISHOP).build(), normalMove, castling));
              moves.push(MovePlus.of(moveB.promotion(Type.QUEEN).build(), normalMove, castling));
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
                moves.push(MovePlus.of(moveB.build(), castling));
              } else {
                moves.push(MovePlus.of(moveB.promotion(Type.KNIGHT).build(), castling));
                moves.push(MovePlus.of(moveB.promotion(Type.ROOK).build(), castling));
                moves.push(MovePlus.of(moveB.promotion(Type.BISHOP).build(), castling));
                moves.push(MovePlus.of(moveB.promotion(Type.QUEEN).build(), castling));
              }
            } else if (captMove.equals(board.enPassantTarget())) {
              moves.push(MovePlus.of(moveB.enPassant(true).build(), castling));
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
    return moves;
  }

  public static Pair<Board, Move> applyMove(final Board board, final MovePlus move) {

    return null;
  }
}
