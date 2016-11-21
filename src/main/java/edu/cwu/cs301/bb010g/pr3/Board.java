package edu.cwu.cs301.bb010g.pr3;

import java.util.Comparator;
import java.util.Iterator;

import java8.util.Comparators;
import java8.util.Objects;
import java8.util.Optional;
import java8.util.Spliterator;
import java8.util.Spliterators;
import java8.util.function.Consumer;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import edu.cwu.cs301.bb010g.IntPair;
import edu.cwu.cs301.bb010g.Pair;
import edu.cwu.cs301.bb010g.Util;
import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
public class Board implements Comparable<Board>, Iterable<Pair<Piece, IntPair>> {
  public static int FILES = 8;
  public static int RANKS = 8;
  public static int SIZE = Board.FILES * Board.RANKS;
  Piece[] board;
  Color active;
  CastlingOpts castling;
  // Optional
  IntPair enPassantTarget;
  int halfmoveClock;
  int fullmoveNum;

  public Optional<IntPair> enPassantTarget() {
    return Optional.ofNullable(this.enPassantTarget);
  }

  public enum CastlingOpt {
    A_SIDE, H_SIDE
  }

  @Value
  @Wither
  @AllArgsConstructor(staticName = "of")
  public static class CastlingOpts implements Comparable<CastlingOpts> {
    Optional<CastlingOpt> white;
    Optional<CastlingOpt> black;

    public static CastlingOpts starting() {
      return new CastlingOpts(Optional.empty(), Optional.empty());
    }

    public boolean castled(final Color color) {
      return color == Color.WHITE ? this.white.isPresent() : this.black.isPresent();
    }

    public Optional<CastlingOpt> castlingOpt(final Color color) {
      return color == Color.WHITE ? this.white : this.black;
    }

    public CastlingOpts withCastle(final Color color, final CastlingOpt opt) {
      return color == Color.WHITE ? this.withWhite(Optional.of(opt))
          : this.withBlack(Optional.of(opt));
    }

    @Override
    public int compareTo(final CastlingOpts that) {
      final int whiteCmp = Util.optionalEmptyFirstCompare(this.white, that.white);
      if (whiteCmp != 0) {
        return whiteCmp;
      }
      return Util.optionalEmptyFirstCompare(this.black, that.black);
    }
  }

  public static Board starting() {
    val board = new Piece[Board.SIZE];
    val bRk = Board.RANKS - 1;
    board[0 * Board.RANKS + bRk] = Piece.of(Type.ROOK, Color.BLACK, CastlingOpt.A_SIDE);
    board[1 * Board.RANKS + bRk] = Piece.of(Type.KNIGHT, Color.BLACK);
    board[2 * Board.RANKS + bRk] = Piece.of(Type.BISHOP, Color.BLACK);
    board[3 * Board.RANKS + bRk] = Piece.of(Type.QUEEN, Color.BLACK);
    board[4 * Board.RANKS + bRk] = Piece.of(Type.KING, Color.BLACK);
    board[5 * Board.RANKS + bRk] = Piece.of(Type.BISHOP, Color.BLACK);
    board[6 * Board.RANKS + bRk] = Piece.of(Type.KNIGHT, Color.BLACK);
    board[7 * Board.RANKS + bRk] = Piece.of(Type.ROOK, Color.BLACK, CastlingOpt.H_SIDE);
    for (int i = 0; i < 8; i++) {
      board[i * Board.RANKS + bRk - 1] = Piece.of(Type.PAWN, Color.BLACK);
      board[i * Board.RANKS + 1] = Piece.of(Type.PAWN, Color.WHITE);
    }
    board[0 * Board.RANKS + 0] = Piece.of(Type.ROOK, Color.WHITE, CastlingOpt.A_SIDE);
    board[1 * Board.RANKS + 0] = Piece.of(Type.KNIGHT, Color.WHITE);
    board[2 * Board.RANKS + 0] = Piece.of(Type.BISHOP, Color.WHITE);
    board[3 * Board.RANKS + 0] = Piece.of(Type.QUEEN, Color.WHITE);
    board[4 * Board.RANKS + 0] = Piece.of(Type.KING, Color.WHITE, CastlingOpt.H_SIDE);
    board[5 * Board.RANKS + 0] = Piece.of(Type.BISHOP, Color.WHITE);
    board[6 * Board.RANKS + 0] = Piece.of(Type.KNIGHT, Color.WHITE);
    board[7 * Board.RANKS + 0] = Piece.of(Type.ROOK, Color.WHITE);

    return Board.of(board, Color.WHITE, CastlingOpts.starting(), null, 0, 1);
  }

  public Piece[] file(final int file) {
    val out = new Piece[Board.RANKS];
    val from = file * Board.RANKS;
    System.arraycopy(this.board, file, from, 0, Board.RANKS);
    return out;
  }

  public Piece piece(final int file, final int rank) {
    return this.board[file * Board.RANKS + rank];
  }

  public Board withPiece(final Piece piece, final int file, final int rank) {
    val board = new Piece[Board.SIZE];
    System.arraycopy(this.board, 0, Board.SIZE, 0, Board.SIZE);
    board[file * Board.RANKS + rank] = piece;
    return this.withBoard(board);
  }

  public Board withPiece(final Piece piece, final IntPair coord) {
    return this.withPiece(piece, coord.fst, coord.snd);
  }

  public Board withoutPiece(final int file, final int rank) {
    return this.withPiece(null, file, rank);
  }

  public Board withoutPiece(final IntPair coord) {
    return this.withPiece(null, coord);
  }

  @SafeVarargs
  public final Board withPieces(final Pair<Piece, IntPair>... pieces) {
    val board = new Piece[Board.SIZE];
    System.arraycopy(this.board, 0, Board.SIZE, 0, Board.SIZE);
    for (val pieceP : pieces) {
      val piece = pieceP.fst;
      val coord = pieceP.snd;
      board[coord.fst * Board.RANKS + coord.snd] = piece;
    }
    return this.withBoard(board);
  }

  public Board withoutPieces(final IntPair... coords) {
    val board = new Piece[Board.SIZE];
    System.arraycopy(this.board, 0, Board.SIZE, 0, Board.SIZE);
    for (val coord : coords) {
      board[coord.fst * Board.RANKS + coord.snd] = null;
    }
    return this.withBoard(board);
  }

  public Piece piece(final IntPair coord) {
    return this.board[coord.fst * Board.RANKS + coord.snd];
  }

  public Piece[] rankRaw(final int rank) {
    val out = new Piece[Board.FILES];
    for (int file = 0; file < Board.FILES; file++) {
      out[file] = this.board[file * Board.RANKS + rank];
    }
    return out;
  }

  public Pair<Piece, Integer>[] rank(final int rank) {
    @SuppressWarnings("unchecked")
    final Pair<Piece, Integer>[] out = new Pair[Board.FILES];
    for (int file = 0; file < Board.FILES; file++) {
      out[file] = Pair.of(this.board[file * Board.RANKS + rank], file);
    }
    return out;
  }

  public Board withEnPassantTarget(final int file, final int rank) {
    return this.withEnPassantTarget(IntPair.of(file, rank));
  }

  @Override
  public int compareTo(final Board that) {
    final int boardCmp = Util.objArrCompare(this.board, that.board);
    if (boardCmp != 0) {
      return boardCmp;
    }
    val activeCmp = this.active.compareTo(that.active);
    if (activeCmp != 0) {
      return activeCmp;
    }
    val castlingCmp = this.castling.compareTo(that.castling);
    if (castlingCmp != 0) {
      return castlingCmp;
    }
    val enPassantTargetCmp = Comparators.nullsFirst(IntPair::compareTo)
        .compare(this.enPassantTarget, that.enPassantTarget);
    if (enPassantTargetCmp != 0) {
      return enPassantTargetCmp;
    }
    val halfmoveClockCmp = Integer.compare(this.halfmoveClock, that.halfmoveClock);
    if (halfmoveClockCmp != 0) {
      return halfmoveClockCmp;
    }
    return Integer.compare(this.fullmoveNum, that.fullmoveNum);
  }

  @Override
  public Iterator<Pair<Piece, IntPair>> iterator() {
    return new BoardIterator(this.board);
  }

  private static final class BoardIterator implements Iterator<Pair<Piece, IntPair>> {
    private final Piece[] board;
    private int index;
    private int file;
    private int rank;

    public BoardIterator(final Piece[] board) {
      this(board, 0, 0, 0);
    }

    public BoardIterator(final Piece[] board, final int origin, final int originFile,
        final int originRank) {
      this.board = board;
      this.index = origin;
      this.file = originFile;
      this.rank = originRank;
    }

    @Override
    public boolean hasNext() {
      return this.index < Board.SIZE;
    }

    @Override
    public Pair<Piece, IntPair> next() {
      val out = Pair.of(this.board[this.index], IntPair.of(this.file, this.rank));
      this.index++;
      if (++this.rank == Board.RANKS) {
        this.rank = 0;
        this.file++;
      }
      return out;
    }
  }

  public Stream<Piece> streamRaw() {
    return StreamSupport.stream(this.spliteratorRaw(), false);
  }

  public Stream<Piece> streamRawParallel() {
    return StreamSupport.stream(this.spliteratorRaw(), true);
  }

  public Stream<Pair<Piece, IntPair>> stream() {
    return StreamSupport.stream(this.spliterator8(), false);
  }

  public Stream<Pair<Piece, IntPair>> streamParallel() {
    return StreamSupport.stream(this.spliterator8(), true);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public Spliterator<Piece> spliteratorRaw() {
    return (Spliterator) Spliterators.spliterator(this.board, Spliterator.IMMUTABLE);
  }

  public Spliterator<Pair<Piece, IntPair>> spliterator8() {
    return new BoardSpliterator(this.board);
  }

  private static final class BoardSpliterator implements Spliterator<Pair<Piece, IntPair>> {
    private final Piece[] board;
    private int index;
    private int file;
    private int rank;
    private final int fence;
    private static final int CHARACTERISTICS =
        Spliterator.DISTINCT | Spliterator.IMMUTABLE | Spliterator.SIZED | Spliterator.SUBSIZED;

    public BoardSpliterator(final Piece[] board) {
      this(board, 0, 0, 0, Board.SIZE);
    }

    public BoardSpliterator(final Piece[] board, final int origin, final int fence) {
      this(board, origin, origin / Board.RANKS, origin % Board.RANKS, fence);
    }

    public BoardSpliterator(final Piece[] board, final int origin, final int originFile,
        final int originRank, final int fence) {
      this.board = board;
      this.index = origin;
      this.file = originFile;
      this.rank = originRank;
      this.fence = fence;
    }


    @Override
    public long getExactSizeIfKnown() {
      return this.estimateSize();
    }

    @Override
    public boolean hasCharacteristics(final int characteristics) {
      return (BoardSpliterator.CHARACTERISTICS & characteristics) == characteristics;
    }

    @Override
    public Spliterator<Pair<Piece, IntPair>> trySplit() {
      final int lo = this.index, mid = (lo + this.fence) >>> 1;
      return (lo >= mid) ? null : new BoardSpliterator(this.board, lo, this.index = mid);
    }

    @Override
    public void forEachRemaining(final Consumer<? super Pair<Piece, IntPair>> action) {
      Objects.requireNonNull(action);
      Piece[] b;
      int i, hi; // hoist accesses and checks from loop
      if ((b = this.board).length >= (hi = this.fence) && (i = this.index) >= 0
          && i < (this.index = hi)) {
        do {
          if (++this.rank == Board.RANKS) {
            this.rank = 0;
            this.file++;
          }
          action.accept(Pair.of(b[i], IntPair.of(this.file, this.rank)));
        } while (++i < hi);
      }
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Pair<Piece, IntPair>> action) {
      Objects.requireNonNull(action);
      if (this.index >= 0 && this.index < this.fence) {
        final Piece p = this.board[this.index++];
        if (++this.rank == Board.RANKS) {
          this.rank = 0;
          this.file++;
        }
        action.accept(Pair.of(p, IntPair.of(this.file, this.rank)));
        return true;
      }
      return false;
    }

    @Override
    public long estimateSize() {
      return this.fence - this.index;
    }

    @Override
    public int characteristics() {
      return BoardSpliterator.CHARACTERISTICS;
    }

    @Override
    public Comparator<? super Pair<Piece, IntPair>> getComparator() {
      throw new IllegalStateException();
    }
  }
}
