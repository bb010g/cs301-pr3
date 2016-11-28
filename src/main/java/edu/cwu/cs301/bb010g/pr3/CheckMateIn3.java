package edu.cwu.cs301.bb010g.pr3;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.Spliterator;
import java8.util.stream.Collectors;
import java8.util.stream.IntStreams;
import java8.util.stream.RefStreams;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import edu.cwu.cs301.bb010g.Pair;
import lombok.SneakyThrows;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CheckMateIn3 {
  @SneakyThrows
  public void main(final String[] args) {
    CheckMateIn3.main(Arrays.asList(args));
  }

  public void main(@SuppressWarnings("unused") final List<String> args) throws IOException {
    final int DIST_IMMUT = Spliterator.DISTINCT | Spliterator.IMMUTABLE;
    final String input = Files.readAllLines(Paths.get("position1.fen")).get(0);
    final PrintStream output = System.out;
    final Board board = Notation.fenToBoard(input);
    System.out.println("Input: " + Notation.boardToFen(board));
    System.out.println(Notation.drawBoard(board.board()));
    Stream<Pair<Pair<Board, List<Move>>, Boolean>> step = Moves.step(board).map(s -> {
      final List<Move> moves = new ArrayList<>();
      moves.add(s.snd);
      return Pair.of(Pair.of(s.fst, moves), true);
    });
    for (int i = 0; i < (3 - 1) * 2; i++) {
      step = step.flatMap(CheckMateIn3::stepHist);// .parallel();
      // That single call means that this algorithm processes each new set of steps in parallel.
      // /me puts on sunglasses
      // /me also needs to figure out why his Spliterator probably doesn't handle parallel well
    }
    final Set<Pair<Board, List<Move>>> checkmates = new HashSet<>();
    step/* .peek(CheckMateIn3::printFen_) */.map(Pair::fst).filter(boardHist -> {
      final List<Move> hist = boardHist.snd;
      final int histSize = hist.size();
      return IntStreams.range(0, histSize).filter(n -> n % 2 == 0).mapToObj(hist::get)
          .allMatch(Move::check) && hist.get(histSize - 1).checkmate();
    }).forEach(checkmates::add);
    System.out.println("Checkmates:");
    StreamSupport.stream(checkmates, DIST_IMMUT, false).forEach(game -> {
      output.println(Notation.drawBoard(game.fst.board()));
      output.println(CheckMateIn3.listMoves(game.snd, game.fst.fullmoveNum()));
    });
    System.out.println("Done");
  }

  public Stream<Pair<Pair<Board, List<Move>>, Boolean>> stepHist(
      final Pair<Pair<Board, List<Move>>, Boolean> boardHist_) {
    if (!boardHist_.snd) {
      return RefStreams.of(boardHist_);
    }
    val boardHist = boardHist_.fst;
    val hist = boardHist.snd;
    return RefStreams.concat(RefStreams.of(Pair.of(boardHist, false)),
        Moves.step(boardHist.fst).map(p -> {
          final List<Move> moves = new ArrayList<>(hist);
          moves.add(p.snd);
          return Pair.of(Pair.of(p.fst, moves), true);
        }));
  }

  public String listMoves(final List<Move> moves, final int endingMove) {
    val listing = new StringBuilder();
    val movesSize = moves.size();
    int moveN = 0;
    if (endingMove != -1) {
      moveN = endingMove - movesSize / 2;
    }
    for (int i = 0; i < movesSize; i++) {
      listing.append(moveN++);
      listing.append(". ");
      listing.append(Notation.moveToAlg(moves.get(i)));
      i++;
      if (!(i < movesSize)) {
        break;
      }
      listing.append(' ');
      listing.append(Notation.moveToAlg(moves.get(i)));
      listing.append(' ');
    }
    return listing.toString();
  }

  public void printFen_(final Pair<Pair<Board, List<Move>>, Boolean> board_) {
    val board = board_.fst;
    System.out.println(Notation.boardToFen(board.fst) + " | " + StreamSupport.stream(board.snd)
        .map(Notation::moveToAlg).collect(Collectors.joining(", ")));
  }

  public void printFen(final Pair<Board, Move> board) {
    System.out.println(Notation.boardToFen(board.fst) + " | " + Notation.moveToAlg(board.snd));
  }

  public void printFen(final Board board) {
    System.out.println(Notation.boardToFen(board));
  }
}
