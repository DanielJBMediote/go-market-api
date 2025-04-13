package gomarketapi.enums;

public enum PaymentMethod {

  CASH("Cash"),
  CREDIT_CARD("Credit Card"),
  DEBIT_CARD("Debit Card");

  private final String value;

  PaymentMethod(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
