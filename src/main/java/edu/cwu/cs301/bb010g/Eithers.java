package edu.cwu.cs301.bb010g;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.function.Predicate;
import java8.util.function.Supplier;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Eithers {
  /**
   * Constructs an Either.Right.
   */
  public <L, R> Either<L, R> right(final R right) {
    return new Either.Right<>(right);
  }

  /**
   * Constructs an Either.Left.
   */
  public <L, R> Either<L, R> left(final L left) {
    return new Either.Left<>(left);
  }

  @SuppressWarnings("unchecked")
  public <L, R> Either<L, R> narrow(final Either<? extends L, ? extends R> either) {
    return (Either<L, R>) either;
  }

  public <L, R> Either<L, R> fromOptional(final Optional<R> optional, final L empty) {
    if (!optional.isPresent()) {
      return Eithers.left(empty);
    }
    return Eithers.right(optional.get());
  }

  public <L, R> Either<L, R> fromOptionalGet(final Optional<R> optional, final Supplier<? extends L> emptySupplier) {
    if (!optional.isPresent()) {
      return Eithers.left(emptySupplier.get());
    }
    return Eithers.right(optional.get());
  }

  /**
   * Maps either the left or the right side of this disjunction.
   */
  public <L, R, X, Y> Either<X, Y> bimap(final Either<L, R> that,
      final Function<? super L, ? extends X> leftMapper, final Function<? super R, ? extends Y> rightMapper) {
    if (that.isLeft()) {
      return Eithers.left(leftMapper.apply(that.getLeft()));
    }
    return Eithers.right(rightMapper.apply(that.get()));
  }

  /**
   * Filters this right-based Either by testing a predicate.
   */
  public <L, R> Optional<Either<L, R>> filter(final Either<L, R> that,
      @NonNull final Predicate<? super R> predicate) {
    if (that.isLeft()) {
      return Optional.of(that);
    }
    if (predicate.test(that.get())) {
      return Optional.of(that);
    } else {
      return Optional.empty();
    }
  }

  public <L, R, U> Either<L, U> flatMap(final Either<L, R> that,
      final Function<? super R, ? extends Either<L, ? extends U>> mapper) {
    if (that.isLeft()) {
      // safe because right is absent
      @SuppressWarnings("unchecked")
      val out = (Either<L, U>) that;
      return out;
    }
    // safe because U is a supertype
    @SuppressWarnings("unchecked")
    final Either<L, U> out = (Either<L, U>) mapper.apply(that.get());
    return out;
  }

  public <L, R, U> U fold(final Either<L, R> that, final Function<? super L, ? extends U> leftMapper,
      final Function<? super R, ? extends U> rightMapper) {
    if (that.isLeft()) {
      return leftMapper.apply(that.getLeft());
    }
    return rightMapper.apply(that.get());
  }

  public <L, R> R orElse(final Either<L, R> that, final R other) {
    if (that.isLeft()) {
      return other;
    }
    return that.get();
  }

  public <L, R> R getOrElseGet(final Either<L, R> that, final Function<? super L, ? extends R> other) {
    if (that.isLeft()) {
      return other.apply(that.getLeft());
    }
    return that.get();
  }

  @SneakyThrows
  public <L, R, X extends Throwable> R getOrElseThrow(final Either<L, R> that,
      final Function<? super L, X> exceptionFunction) {
    if (that.isLeft()) {
      throw exceptionFunction.apply(that.getLeft());
    }
    return that.get();
  }

  public <L, R> Iterator<R> iterator(final Either<L, R> that) {
    return new Iterator<R>() {
      private boolean done = that.isRight();

      @Override
      public boolean hasNext() {
        return this.done;
      }

      @Override
      public R next() {
        if (this.done) {
          throw new NoSuchElementException();
        }
        this.done = false;
        return that.get();
      }
    };
  }

  public <L, R, U> Either<L, U> map(final Either<L, R> that, final Function<? super R, ? extends U> mapper) {
    if (that.isLeft()) {
      // safe because right is absent
      @SuppressWarnings("unchecked")
      val out = (Either<L, U>) that;
      return out;
    }
    return Eithers.right(mapper.apply(that.get()));
  }

  public <L, R, U> Either<U, R> mapLeft(final Either<L, R> that,
      final Function<? super L, ? extends U> mapper) {
    if (that.isRight()) {
      // safe because right is absent
      @SuppressWarnings("unchecked")
      val out = (Either<U, R>) that;
      return out;
    }
    return Eithers.left(mapper.apply(that.getLeft()));
  }

  public <L, R> void orElseRun(final Either<L, R> that, final Consumer<? super L> action) {
    if (that.isLeft()) {
      action.accept(that.getLeft());
    }
  }

  public <L, R> Either<R, L> swap(final Either<L, R> that) {
    if (that.isLeft()) {
      return Eithers.right(that.getLeft());
    }
    return Eithers.left(that.get());
  }
}
