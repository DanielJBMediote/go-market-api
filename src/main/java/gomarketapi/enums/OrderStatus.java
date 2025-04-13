package gomarketapi.enums;

public enum OrderStatus {

  PENDING("Pending"), // Pedido criado, aguardando pagamento
  PROCESSING("Processing"), // Pagamento confirmado, preparando para envio
  SHIPPED("Shipped"), // Pedido enviado/em transporte
  DELIVERED("Delivered"), // Pedido entregue ao cliente
  CANCELLED("Canceled"), // Pedido cancelado (pelo cliente ou sistema)
  REFUNDED("Refunded"), // Reembolsado (para casos de devolução)
  FAILED("Failed"), // Falha no processamento (ex: pagamento recusado)
  COMPLETED("Completed"); // Operação finalizada

  private final String value;

  OrderStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
