package gomarketapi;

import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;

import gomarketapi.model.OrderBean;
import gomarketapi.repository.OrderRepository;

// @SpringBootApplication
public class MigrateRepository {

  public static void main(String[] args) {
    // SpringApplication.run(MigrateRepository.class, args);
  }

  // @Bean
  CommandLineRunner migrate(OrderRepository orderRepository) {
    return args -> {
      // Long userId = 2L;
      // WhereBuilder<OrderBean> wb = new WhereBuilder<>(OrderBean.class);
      // wb.addWhere(new Where("items.product.store.owner.id", userId,
      // Where.Operator.EQUALS));
      // Specification<OrderBean> spec = wb.build();
      List<OrderBean> orders = orderRepository.findAll();

      System.out.println(">> Found " + orders.size() + " orders(s)...");

      for (OrderBean order : orders) {
        if (order.getUUID() == null) {
          order.setUUID(UUID.randomUUID().toString());
        }
      }
      orderRepository.saveAll(orders);
      System.out.println(">> Migration complete.");

    };
  }
}
