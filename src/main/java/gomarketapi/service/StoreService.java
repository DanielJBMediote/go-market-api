package gomarketapi.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import gomarketapi.contexts.UserContext;
import gomarketapi.dto.ChartDataDTO;
import gomarketapi.dto.StoreDashboardMetricsDTO;
import gomarketapi.dto.store.StoreCreateDTO;
import gomarketapi.dto.store.StoreReadDTO;
import gomarketapi.exceptions.ForbiddenException;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.mapper.StoreMapper;
import gomarketapi.model.FileBean;
import gomarketapi.model.StoreBean;
import gomarketapi.model.UserBean;
import gomarketapi.repository.FileRepository;
import gomarketapi.repository.OrderRepository;
import gomarketapi.repository.StoreRepository;
import jakarta.transaction.Transactional;

@Service
public class StoreService {

  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;
  private final FileRepository fileRepository;
  private final FileService fileService;
  private final UserContext userContext;

  // private JdbcTemplate jdbcTemplate;
  // private EntityManager entityManager;

  public StoreService(StoreRepository storeRepository, OrderRepository orderRepository,
      UserContext userContext, FileService fileService, FileRepository fileRepository) {
    this.storeRepository = storeRepository;
    this.orderRepository = orderRepository;
    this.userContext = userContext;
    this.fileService = fileService;
    this.fileRepository = fileRepository;
  }

  @Transactional
  public StoreReadDTO create(StoreCreateDTO storeBody, MultipartFile file) {

    StoreBean storeBean = new StoreBean();
    storeBean.setName(storeBody.getName());
    storeBean.setDescription(storeBody.getDescription());
    storeBean.setSlug(storeBody.getSlug());
    if (file != null) {
      try {
        FileBean fileBean = fileService.createFileBean(file);
        FileBean newLogo = fileRepository.save(fileBean);

        storeBean.setLogo(newLogo);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    Long ownerId = userContext.getUserId();
    storeBean.setOwner(new UserBean(ownerId));
    StoreBean createdStore = this.storeRepository.save(storeBean);
    StoreReadDTO storeDTO = StoreMapper.toDTO(createdStore);

    return storeDTO;
  }

  public StoreReadDTO getById(Long id) {

    StoreBean store = this.storeRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Store not found with id " + id + "."));
    StoreReadDTO storeDTO = StoreMapper.toDTO(store);

    return storeDTO;
  }

  public List<StoreReadDTO> getAll(String whereEncodedURI) {

    WhereBuilder<StoreBean> wb = new WhereBuilder<>(StoreBean.class);
    wb.parseWhereEncodedURIParams(whereEncodedURI);
    Specification<StoreBean> spec = wb.build();

    List<StoreBean> storeList = this.storeRepository.findAll(spec);
    List<StoreReadDTO> storeReadList = storeList.stream().map(StoreMapper::toDTO).toList();

    return storeReadList;
  }

  @Transactional
  public StoreReadDTO update(Long id, StoreCreateDTO storeBody, MultipartFile file) {

    StoreBean storeBean = this.storeRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Store not found with id: " + id + "!"));

    if (storeBean.getOwner().getId() != userContext.getUserId()) {
      throw new ForbiddenException("You don't have permission to update this store!");
    }

    boolean hasLogoToUpload = (file != null);

    if (storeBean.getLogo() != null) {
      this.fileRepository.delete(storeBean.getLogo());
      storeBean.setLogo(null);
    }

    if (hasLogoToUpload && storeBean.getLogo() == null) {
      try {
        FileBean fileBean = fileService.createFileBean(file);
        FileBean newLogo = fileRepository.save(fileBean);
        storeBean.setLogo(newLogo);
      } catch (Exception e) {
        throw new RuntimeException("Failed to upload new logo.", e);
      }
    }

    storeBean.setName(storeBody.getName());
    storeBean.setDescription(storeBody.getDescription());
    storeBean.setSlug(storeBody.getSlug());
    storeBean.setIsActive(storeBody.getIsActive());

    StoreBean updatedStore = this.storeRepository.save(storeBean);
    StoreReadDTO storeReadDTO = StoreMapper.toDTO(updatedStore);

    return storeReadDTO;
  }

  public void delete(Long id) {
    StoreBean storeBean = this.storeRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Store not found with id: " + id + "!"));

    this.storeRepository.delete(storeBean);

  }

  public StoreDashboardMetricsDTO getDashboardMetricsByStoreId(Long storeId) {

    StoreBean storeBean = this.storeRepository.findById(storeId)
        .orElseThrow(() -> new NotFoundException("Store not found with id " + storeId));

    if (storeBean.getOwner().getId() != userContext.getUserId()) {
      throw new ForbiddenException("Owner don't have access to this store informations.");
    }

    LocalDate today = LocalDate.now();

    LocalDateTime currentMonthStart = today.withDayOfMonth(1).atStartOfDay();
    LocalDateTime currentMonthEnd = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);

    LocalDateTime previousMonthStart = currentMonthStart.minusMonths(1);
    LocalDateTime previousMonthEnd = currentMonthEnd.minusMonths(1);

    LocalDateTime currentDayStart = today.atStartOfDay();
    LocalDateTime currentDayEnd = today.atTime(23, 59, 59);

    LocalDateTime previousDayStart = today.minusDays(1).atStartOfDay();
    LocalDateTime previousDayEnd = today.minusDays(1).atTime(23, 59, 59);

    // 1. Total de vendas no mês (unidades)
    Integer monthlySales = orderRepository.sumSalesByPeriod(
        storeId,
        currentMonthStart,
        currentMonthEnd);
    Integer previousMonthlySales = orderRepository.sumSalesByPeriod(
        storeId,
        previousMonthStart,
        previousMonthEnd);
    BigDecimal diffMonthlySalesPercent = calculatePercentChange(monthlySales, previousMonthlySales);

    // 2. Total de vendas hoje (unidades)
    Integer dailySales = orderRepository.sumSalesByPeriod(
        storeId,
        currentDayStart,
        currentDayEnd);
    Integer previousDaySales = orderRepository.sumSalesByPeriod(
        storeId,
        previousDayStart,
        previousDayEnd);
    BigDecimal diffDailySalesPercent = calculatePercentChange(dailySales, previousDaySales);

    // 3. Performance (valor total em Moeda)
    BigDecimal monthlyRevenue = orderRepository.sumRevenueByPeriod(
        storeId,
        currentMonthStart,
        currentMonthEnd);
    BigDecimal lastMonthlyRevenue = orderRepository.sumRevenueByPeriod(
        storeId,
        previousMonthStart,
        previousMonthEnd);
    BigDecimal diffMonthlyRevenuePercent = calculatePercentChange(monthlyRevenue, lastMonthlyRevenue);

    // 4. Média diária (R$)
    BigDecimal averageDaily = monthlyRevenue.divide(
        BigDecimal.valueOf(today.getDayOfMonth()),
        2, RoundingMode.HALF_UP);
    BigDecimal averageYestarday = lastMonthlyRevenue.divide(
        BigDecimal.valueOf(today.getDayOfMonth()),
        2, RoundingMode.HALF_UP);
    BigDecimal diffDailyAvarageSalesPercent = calculatePercentChange(averageDaily, averageYestarday);

    List<Map<String, Object>> rawData = orderRepository.getDailySalesChartData(storeId);

    // Crie um mapa para todos os dias do mês
    Map<Integer, ChartDataDTO> resultMap = new TreeMap<>();
    LocalDate now = LocalDate.now();
    int daysInMonth = now.lengthOfMonth();

    // Inicialize todos os dias com valores zerados
    for (int day = 1; day <= daysInMonth; day++) {
      ChartDataDTO dto = new ChartDataDTO();
      dto.setDay(day);
      dto.setRevenue(BigDecimal.ZERO);
      dto.setUnits(0);
      resultMap.put(day, dto);
    }

    // Preencha com os dados existentes
    for (Map<String, Object> row : rawData) {
      Integer day = ((Number) row.get("day")).intValue();
      ChartDataDTO dto = resultMap.get(day);
      dto.setUnits(((Number) row.get("units")).intValue());
      dto.setRevenue(new BigDecimal(row.get("revenue").toString()));
    }

    // Converta para lista ordenada
    List<ChartDataDTO> dataChart = new ArrayList<>(resultMap.values());

    StoreDashboardMetricsDTO result = new StoreDashboardMetricsDTO();
    result.setDailySales(dailySales);
    result.setMonthlySales(monthlySales);
    result.setMonthlyRevenue(monthlyRevenue);
    result.setAverageDaily(averageDaily);
    result.setDataChart(dataChart);
    result.setDiffMonthlyRevenuePercent(diffMonthlyRevenuePercent);
    result.setDiffDailySalesPercent(diffDailySalesPercent);
    result.setDiffMonthlySalesPercent(diffMonthlySalesPercent);
    result.setDiffDailyAvarageSalesPercent(diffDailyAvarageSalesPercent);
    result.setStore(StoreMapper.toDTO(storeBean));

    return result;
  }

  public StoreDashboardMetricsDTO getDashboardMetrics() {

    Long userId = userContext.getUserId();

    LocalDate today = LocalDate.now();

    LocalDateTime currentMonthStart = today.withDayOfMonth(1).atStartOfDay();
    LocalDateTime currentMonthEnd = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59);

    LocalDateTime previousMonthStart = currentMonthStart.minusMonths(1);
    LocalDateTime previousMonthEnd = currentMonthEnd.minusMonths(1);

    LocalDateTime currentDayStart = today.atStartOfDay();
    LocalDateTime currentDayEnd = today.atTime(23, 59, 59);

    LocalDateTime previousDayStart = today.minusDays(1).atStartOfDay();
    LocalDateTime previousDayEnd = today.minusDays(1).atTime(23, 59, 59);

    // 1. Total de vendas no mês (unidades)
    Integer monthlySales = orderRepository.sumAllStoresSalesByPeriod(
        userId,
        currentMonthStart,
        currentMonthEnd);
    Integer previousMonthlySales = orderRepository.sumAllStoresSalesByPeriod(
        userId,
        previousMonthStart,
        previousMonthEnd);
    BigDecimal diffMonthlySalesPercent = calculatePercentChange(monthlySales, previousMonthlySales);

    // 2. Total de vendas hoje (unidades)
    Integer dailySales = orderRepository.sumAllStoresSalesByPeriod(
        userId,
        currentDayStart,
        currentDayEnd);
    Integer previousDaySales = orderRepository.sumAllStoresSalesByPeriod(
        userId,
        previousDayStart,
        previousDayEnd);
    BigDecimal diffDailySalesPercent = calculatePercentChange(dailySales, previousDaySales);

    // 3. Performance (valor total em Moeda)
    BigDecimal monthlyRevenue = orderRepository.sumAllStoresRevenueByPeriod(
        userId,
        currentMonthStart,
        currentMonthEnd);
    BigDecimal lastMonthlyRevenue = orderRepository.sumAllStoresRevenueByPeriod(
        userId,
        previousMonthStart,
        previousMonthEnd);
    BigDecimal diffMonthlyRevenuePercent = calculatePercentChange(monthlyRevenue, lastMonthlyRevenue);

    // 4. Média diária (R$)
    BigDecimal averageDaily = monthlyRevenue.divide(
        BigDecimal.valueOf(today.getDayOfMonth()),
        2, RoundingMode.HALF_UP);
    BigDecimal averageYestarday = lastMonthlyRevenue.divide(
        BigDecimal.valueOf(today.getDayOfMonth()),
        2, RoundingMode.HALF_UP);
    BigDecimal diffDailyAvarageSalesPercent = calculatePercentChange(averageDaily, averageYestarday);

    List<Map<String, Object>> rawData = orderRepository.getAllStoresDailySalesChartData(userId);

    // Crie um mapa para todos os dias do mês
    Map<Integer, ChartDataDTO> resultMap = new TreeMap<>();
    LocalDate now = LocalDate.now();
    int daysInMonth = now.lengthOfMonth();

    // Inicialize todos os dias com valores zerados
    for (int day = 1; day <= daysInMonth; day++) {
      ChartDataDTO dto = new ChartDataDTO();
      dto.setDay(day);
      dto.setRevenue(BigDecimal.ZERO);
      dto.setUnits(0);
      resultMap.put(day, dto);
    }

    // Preencha com os dados existentes
    for (Map<String, Object> row : rawData) {
      Integer day = ((Number) row.get("day")).intValue();
      ChartDataDTO dto = resultMap.get(day);
      dto.setUnits(((Number) row.get("units")).intValue());
      dto.setRevenue(new BigDecimal(row.get("revenue").toString()));
    }

    // Converta para lista ordenada
    List<ChartDataDTO> dataChart = new ArrayList<>(resultMap.values());

    StoreDashboardMetricsDTO result = new StoreDashboardMetricsDTO();
    result.setDailySales(dailySales);
    result.setMonthlySales(monthlySales);
    result.setMonthlyRevenue(monthlyRevenue);
    result.setAverageDaily(averageDaily);
    result.setDataChart(dataChart);
    result.setDiffMonthlyRevenuePercent(diffMonthlyRevenuePercent);
    result.setDiffDailySalesPercent(diffDailySalesPercent);
    result.setDiffMonthlySalesPercent(diffMonthlySalesPercent);
    result.setDiffDailyAvarageSalesPercent(diffDailyAvarageSalesPercent);

    return result;
  }

  /**
   * Calcula a variação percentual entre um valor atual e um valor anterior
   * 
   * @param currentValue  Valor atual (período mais recente)
   * @param previousValue Valor anterior (período de comparação)
   * @return Variação percentual (positiva = aumento, negativa = redução)
   */
  private BigDecimal calculatePercentChange(Number currentValue, Number previousValue) {
    BigDecimal current = new BigDecimal(currentValue.toString());
    BigDecimal previous = new BigDecimal(previousValue.toString());

    // Caso especial: se o valor anterior for zero
    if (previous.compareTo(BigDecimal.ZERO) == 0) {
      return current.compareTo(BigDecimal.ZERO) == 0
          ? BigDecimal.ZERO
          : new BigDecimal("100");
    }

    // Fórmula correta: ((current - previous) / previous) * 100
    return current.subtract(previous)
        .divide(previous, 4, RoundingMode.HALF_UP)
        .multiply(new BigDecimal("100"));
  }

}
