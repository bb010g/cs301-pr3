package edu.cwu.cs301.bb010g.pr3;

import java.util.EnumSet;
import java.util.Iterator;

import edu.cwu.cs301.bb010g.pr3.Piece.Color;
import edu.cwu.cs301.bb010g.pr3.Piece.Type;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

@Value
@Wither
@AllArgsConstructor(staticName = "of")
public class Board implements Comparable<Board>, Iterable<Pair<Piece, IntPair>> {
  public static int FILES = 8;
  public static int RANKS = 8;
  @NonNull
  Piece[][] board;
  @NonNull
  Color active;
  @NonNull
  CastlingOpts castling;
  IntPair enPassantTarget;
  int halfmoveClock;
  int fullmoveNum;

  public enum CastlingOpt {
    KINGSIDE, QUEENSIDE
  }

  @Value
  @Wither
  @AllArgsConstructor(staticName = "of")
  public static class CastlingOpts implements Comparable<CastlingOpts> {
    @NonNull
    EnumSet<CastlingOpt> white;
    @NonNull
    EnumSet<CastlingOpt> black;

    public static CastlingOpts starting() {
      return CastlingOpts.of(EnumSet.allOf(CastlingOpt.class), EnumSet.allOf(CastlingOpt.class));
    }

    public static CastlingOpts unavailable() {
      return CastlingOpts.of(EnumSet.noneOf(CastlingOpt.class), EnumSet.noneOf(CastlingOpt.class));
    }

    public EnumSet<CastlingOpt> castlingOpts(final Color color) {
      return color == Color.WHITE ? this.white : this.black;
    }

    public CastlingOpts withCastlingOpts(final Color color, final EnumSet<CastlingOpt> opts) {
      return color == Color.WHITE ? this.withWhite(opts) : this.withBlack(opts);
    }

    public boolean castlingOpt(final Color color, final CastlingOpt opt) {
      return this.castlingOpts(color).contains(opt);
    }

    public CastlingOpts withCastlingOpt(final Color color, final CastlingOpt opt) {
      val opts = EnumSet.copyOf(this.castlingOpts(color));
      opts.add(opt);
      return this.withCastlingOpts(color, opts);
    }

    public CastlingOpts withoutCastlingOpt(final Color color, final CastlingOpt opt) {
      val opts = EnumSet.copyOf(this.castlingOpts(color));
      opts.remove(opt);
      return this.withCastlingOpts(color, opts);
    }

    @Override
    public int compareTo(final CastlingOpts that) {
      final int whiteCmp =
          new Util.EnumSetComparator<CastlingOpt>().compare(this.white, that.white);
      if (whiteCmp != 0) {
        return whiteCmp;
      }
      return new Util.EnumSetComparator<CastlingOpt>().compare(this.black, that.black);
    }
  }

  public static Board starting() {
    val board = new Piece[Board.FILES][Board.RANKS];
    val bRk = Board.RANKS - 1;
    board[0][bRk] = Piece.of(Type.ROOK, Color.BLACK);
    board[1][bRk] = Piece.of(Type.KNIGHT, Color.BLACK);
    board[2][bRk] = Piece.of(Type.BISHOP, Color.BLACK);
    board[3][bRk] = Piece.of(Type.QUEEN, Color.BLACK);
    board[4][bRk] = Piece.of(Type.KING, Color.BLACK);
    board[5][bRk] = Piece.of(Type.BISHOP, Color.BLACK);
    board[6][bRk] = Piece.of(Type.KNIGHT, Color.BLACK);
    board[7][bRk] = Piece.of(Type.ROOK, Color.BLACK);
    for (int i = 0; i < 8; i++) {
      board[i][bRk - 1] = Piece.of(Type.PAWN, Color.BLACK);
      board[i][1] = Piece.of(Type.PAWN, Color.WHITE);
    }
    board[0][0] = Piece.of(Type.ROOK, Color.WHITE);
    board[1][0] = Piece.of(Type.KNIGHT, Color.WHITE);
    board[2][0] = Piece.of(Type.BISHOP, Color.WHITE);
    board[3][0] = Piece.of(Type.QUEEN, Color.WHITE);
    board[4][0] = Piece.of(Type.KING, Color.WHITE);
    board[5][0] = Piece.of(Type.BISHOP, Color.WHITE);
    board[6][0] = Piece.of(Type.KNIGHT, Color.WHITE);
    board[7][0] = Piece.of(Type.ROOK, Color.WHITE);

    return Board.of(board, Color.WHITE, CastlingOpts.starting(), null, 0, 1);
  }

  public Piece[] file(final int file) {
    return this.board[file];
  }

  public Board withFile(final Piece[] file, final int f) {
    val board = new Piece[Board.FILES][];
    System.arraycopy(this.board, 0, board, 0, Board.FILES);
    board[f] = file;
    return this.withBoard(board);
  }

  public Piece piece(final int file, final int rank) {
    return this.board[file][rank];
  }

  public Board withPiece(final Piece piece, final int file, final int rank) {
    val fileNew = new Piece[Board.RANKS];
    System.arraycopy(this.board[file], 0, fileNew, 0, Board.RANKS);
    fileNew[rank] = piece;
    return this.withFile(fileNew, rank);
  }

  public Board withPiece(final Piece piece, final IntPair coord) {
    return this.withPiece(piece, coord.fst, coord.snd);
  }

  public Piece piece(final IntPair coord) {
    return this.board[coord.fst][coord.snd];
  }

  public Piece[] rank(final int rank) {
    val out = new Piece[Board.FILES];
    for (int i = 0; i < Board.FILES; i++) {
      out[i] = this.board[i][rank];
    }
    return out;
  }

  public Board withEnPassantTarget(final int file, final int rank) {
    return this.withEnPassantTarget(IntPair.of(file, rank));
  }

  @Override
  public int compareTo(final Board that) {
    final int boardCmp = new Util.ObjArrComparator<>(new Util.CompObjArrComparator<Piece>())
        .compare(this.board, that.board);
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
    val enPassantTargetCmp = this.enPassantTarget.compareTo(that.enPassantTarget);
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
    return new Iterator<Pair<Piece, IntPair>>() {
      private int file = 0;
      private int rank = 0;

      @Override
      public boolean hasNext() {
        return this.file != Board.FILES - 1 || this.rank != Board.RANKS - 1;
      }

      @Override
      public Pair<Piece, IntPair> next() {
        val out = Pair.of(Board.this.board[this.file][this.rank], IntPair.of(this.file, this.rank));
        this.file++;
        this.rank++;
        return out;
      }
    };
  }
}
