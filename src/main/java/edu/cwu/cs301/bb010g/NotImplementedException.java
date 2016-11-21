package edu.cwu.cs301.bb010g;

public class NotImplementedException extends UnsupportedOperationException {
  private static final long serialVersionUID = 7174420439689583625L;
  final String message;
  final Throwable cause;
  final String code;

  public NotImplementedException(final String message) {
    this(message, null, null);
  }

  public NotImplementedException(final String message, final String code) {
    this(message, null, code);
  }

  public NotImplementedException(final String message, final Throwable cause) {
    this(message, cause, null);
  }

  public NotImplementedException(final String message, final Throwable cause, final String code) {
    this.message = message;
    this.cause = cause;
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
