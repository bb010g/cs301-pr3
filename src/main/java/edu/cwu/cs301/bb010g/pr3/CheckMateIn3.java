package edu.cwu.cs301.bb010g.pr3;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.Spliterator;
import java8.util.stream.Collectors;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import edu.cwu.cs301.bb010g.Pair;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CheckMateIn3 {
  public void main(final String[] args) throws IOException {
    final int DIST_IMMUT = Spliterator.DISTINCT | Spliterator.IMMUTABLE;
    final String input = Files.readAllLines(Paths.get("position.fen")).get(0);
    final PrintStream output = System.out;
    final Board board = Notation.fenToBoard(input);
    final Set<Pair<Board, List<Move>>> checkmates = new HashSet<>();
    val firstPartitioning = Moves.step(board)
        .collect(Collectors.partitioningBy(p -> p.snd.checkmate(), Collectors.toSet()));
    StreamSupport.stream(firstPartitioning.get(true), DIST_IMMUT)
        .map(p -> p.mapSnd(Collections::singletonList)).forEach(checkmates::add);
    Stream<Pair<Board, List<Move>>> step =
        StreamSupport.stream(firstPartitioning.get(false), DIST_IMMUT).map(s -> {
          final List<Move> moves = new ArrayList<>();
          moves.add(s.snd);
          return Pair.of(s.fst, moves);
        });
    for (int i = 0; i < 3; i++) {
      val partitioning =
          step.flatMap(CheckMateIn3::stepHist).collect(Collectors.partitioningBy(p -> {
            final List<Move> hist = p.snd;
            return hist.get(hist.size() - 1).checkmate();
          }, Collectors.toSet()));
      for (val checkmate : partitioning.get(true)) {
        checkmates.add(checkmate);
      }
      step = StreamSupport.stream(partitioning.get(false), DIST_IMMUT, true);
      // That `true` means that this algorithm processes each new set of steps in parallel.
      // /me puts on sunglasses
    }
    StreamSupport.stream(checkmates, DIST_IMMUT, false).forEach(game -> {
      output.println(Notation.drawBoard(game.fst.board()));
      output.println(listMoves(game.snd, game.fst.fullmoveNum()));
    });
  }

  public Stream<Pair<Board, List<Move>>> stepHist(final Pair<Board, List<Move>> boardHist) {
    val hist = boardHist.snd;
    return Moves.step(boardHist.fst).map(p -> {
      final List<Move> moves = new ArrayList<>(hist);
      moves.add(p.snd);
      return Pair.of(p.fst, moves);
    });
  }

  public String listMoves(final List<Move> moves, int endingMove) {
    val listing = new StringBuilder();
    val movesSize = moves.size();
    int moveN = 0;
    if (endingMove != -1) {
      moveN = endingMove - movesSize;
    }
    for (int i = 0; i < movesSize; i++) {
      val move = moves.get(i);
      listing.append(moveN++);
      listing.append(". ");
      listing.append(Notation.moveToAlg(move));
      i++;
      if (!(i < movesSize)) {
        break;
      }
      listing.append(' ');
      listing.append(Notation.moveToAlg(move));
    }
    return listing.toString();
  }
}
