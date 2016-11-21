package edu.cwu.cs301.bb010g;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java8.util.Optional;
import java8.util.function.Consumer;
import java8.util.function.Function;
import java8.util.function.Predicate;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;

public interface Either<L, R> extends Iterable<R> {
  R get();

  L getLeft();

  boolean isRight();

  boolean isLeft();

  @Override
  int hashCode();

  @Override
  boolean equals(Object o);

  @Override
  String toString();

  // "default" methods (default impls defined in Eithers)

  <X, Y> Either<X, Y> bimap(Function<? super L, ? extends X> leftMapper,
      Function<? super R, ? extends Y> rightMapper);

  Optional<Either<L, R>> filter(@NonNull Predicate<? super R> predicate);

  <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, ? extends U>> mapper);

  <U> U fold(Function<? super L, ? extends U> leftMapper,
      Function<? super R, ? extends U> rightMapper);

  R orElse(R other);

  R getOrElseGet(Function<? super L, ? extends R> other);

  <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionFunction);

  <U> Either<L, U> map(Function<? super R, ? extends U> mapper);

  <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper);

  void orElseRun(Consumer<? super L> action);

  Either<R, L> swap();

  @EqualsAndHashCode
  @ToString
  public static class Right<L, R> implements Either<L, R> {
    private final R right;

    // would be private if we had static instance methods
    public Right(final R right) {
      this.right = right;
    }

    @Override
    public R get() {
      return this.right;
    }

    @Override
    public L getLeft() {
      throw new NoSuchElementException("left");
    }

    @Override
    public boolean isRight() {
      return true;
    }

    @Override
    public boolean isLeft() {
      return false;
    }

    @Override
    public Iterator<R> iterator() {
      return Eithers.iterator(this);
    }

    @Override
    public <X, Y> Either<X, Y> bimap(final Function<? super L, ? extends X> leftMapper,
        final Function<? super R, ? extends Y> rightMapper) {
      return Eithers.right(rightMapper.apply(this.right));
    }

    @Override
    public Optional<Either<L, R>> filter(final Predicate<? super R> predicate) {
      if (predicate.test(this.right)) {
        return Optional.of(this);
      } else {
        return Optional.empty();
      }
    }

    @Override
    public <U> Either<L, U> flatMap(final Function<? super R, ? extends Either<L, ? extends U>> mapper) {
      // safe because U is a supertype
      @SuppressWarnings("unchecked")
      final Either<L, U> out = (Either<L, U>) mapper.apply(this.right);
      return out;
    }

    @Override
    public <U> U fold(final Function<? super L, ? extends U> leftMapper,
        final Function<? super R, ? extends U> rightMapper) {
      return rightMapper.apply(this.right);
    }

    @Override
    public R orElse(final R other) {
      return this.right;
    }

    @Override
    public R getOrElseGet(final Function<? super L, ? extends R> other) {
      return this.right;
    }

    @Override
    public <X extends Throwable> R getOrElseThrow(final Function<? super L, X> exceptionFunction) {
      return this.right;
    }

    @Override
    public <U> Either<L, U> map(final Function<? super R, ? extends U> mapper) {
      return Eithers.right(mapper.apply(this.right));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Either<U, R> mapLeft(final Function<? super L, ? extends U> mapper) {
      return (Either<U, R>) this;
    }

    @Override
    public void orElseRun(final Consumer<? super L> action) {
      return;
    }

    @Override
    public Either<R, L> swap() {
      return Eithers.left(this.right);
    }
  }

  @EqualsAndHashCode
  @ToString
  public static class Left<L, R> implements Either<L, R> {
    private final L left;

    // would be private if we had static instance methods
    public Left(final L left) {
      this.left = left;
    }

    @Override
    public R get() {
      throw new NoSuchElementException("right");
    }

    @Override
    public L getLeft() {
      return this.left;
    }

    @Override
    public boolean isRight() {
      return false;
    }

    @Override
    public boolean isLeft() {
      return true;
    }

    @Override
    public Iterator<R> iterator() {
      return Eithers.iterator(this);
    }

    @Override
    public <X, Y> Either<X, Y> bimap(final Function<? super L, ? extends X> leftMapper,
        final Function<? super R, ? extends Y> rightMapper) {
      return Eithers.left(leftMapper.apply(this.left));
    }

    @Override
    public Optional<Either<L, R>> filter(final Predicate<? super R> predicate) {
      return Optional.of(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Either<L, U> flatMap(final Function<? super R, ? extends Either<L, ? extends U>> mapper) {
      return (Either<L, U>) this;
    }

    @Override
    public <U> U fold(final Function<? super L, ? extends U> leftMapper,
        final Function<? super R, ? extends U> rightMapper) {
      return leftMapper.apply(this.left);
    }

    @Override
    public R orElse(final R other) {
      return other;
    }

    @Override
    public R getOrElseGet(final Function<? super L, ? extends R> other) {
      return other.apply(this.left);
    }

    @SneakyThrows
    @Override
    public <X extends Throwable> R getOrElseThrow(final Function<? super L, X> exceptionFunction) {
      throw exceptionFunction.apply(this.left);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> Either<L, U> map(final Function<? super R, ? extends U> mapper) {
      return (Either<L, U>) this;
    }

    @Override
    public <U> Either<U, R> mapLeft(final Function<? super L, ? extends U> mapper) {
      return Eithers.left(mapper.apply(this.left));
    }

    @Override
    public void orElseRun(final Consumer<? super L> action) {
      action.accept(this.left);
      return;
    }

    @Override
    public Either<R, L> swap() {
      return Eithers.right(this.left);
    }
  }
}
