package edu.cwu.cs301.bb010g.pr3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java8.util.stream.Collectors;
import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import edu.cwu.cs301.bb010g.NotImplementedException;
import edu.cwu.cs301.bb010g.Pair;
import lombok.val;

public class CheckMateIn3 {
  public static void main(final String[] args) {
    System.out.println("Hello World!");
    if (true) {
      // avoid unreachable statements compiler error
      throw new NotImplementedException("TODO: Needs a parser.");
    }
    final Board board = null; // parse input
    final Set<List<Move>> checkmates = new HashSet<>();
    val firstPartitioning = Moves.step(board)
        .collect(Collectors.partitioningBy(p -> p.snd.checkmate(), Collectors.toSet()));
    StreamSupport.stream(firstPartitioning.get(true)).map(p -> Collections.singletonList(p.snd))
        .forEach(checkmates::add);
    // Java is overwhelmed or something
    Stream<Pair<Board, List<Move>>> step =
        StreamSupport.stream(firstPartitioning.get(false)).map(s -> {
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
        checkmates.add(checkmate.snd);
      }
      step = StreamSupport.stream(partitioning.get(false));
    }
  }

  public static Stream<Pair<Board, List<Move>>> stepHist(final Pair<Board, List<Move>> boardHist) {
    val hist = boardHist.snd;
    return Moves.step(boardHist.fst).map(p -> {
      final List<Move> moves = new ArrayList<>(hist);
      moves.add(p.snd);
      return Pair.of(p.fst, moves);
    });
  }
}
