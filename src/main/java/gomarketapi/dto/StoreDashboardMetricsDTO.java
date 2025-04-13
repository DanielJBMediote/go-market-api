package gomarketapi.dto;

import java.math.BigDecimal;
import java.util.List;

public class StoreDashboardMetricsDTO {

  private StoreDTO store;

  private Integer monthlySales;
  private Integer dailySales;
  private BigDecimal averageDaily;
  private BigDecimal monthlyRevenue;

  private BigDecimal diffDailySalesPercent;
  private BigDecimal diffMonthlySalesPercent;
  private BigDecimal diffMonthlyRevenuePercent;
  private BigDecimal diffDailyAvarageSalesPercent;

  private List<ChartDataDTO> dataChart;

  public Integer getMonthlySales() {
    return monthlySales;
  }

  public void setMonthlySales(Integer monthlySales) {
    this.monthlySales = monthlySales;
  }

  public Integer getDailySales() {
    return dailySales;
  }

  public void setDailySales(Integer dailySales) {
    this.dailySales = dailySales;
  }

  public BigDecimal getAverageDaily() {
    return averageDaily;
  }

  public void setAverageDaily(BigDecimal averageDaily) {
    this.averageDaily = averageDaily;
  }

  public BigDecimal getMonthlyRevenue() {
    return monthlyRevenue;
  }

  public void setMonthlyRevenue(BigDecimal monthlyRevenue) {
    this.monthlyRevenue = monthlyRevenue;
  }

  public List<ChartDataDTO> getDataChart() {
    return dataChart;
  }

  public void setDataChart(List<ChartDataDTO> dataChart) {
    this.dataChart = dataChart;
  }

  public BigDecimal getDiffDailySalesPercent() {
    return diffDailySalesPercent;
  }

  public void setDiffDailySalesPercent(BigDecimal diffDailySalesPercent) {
    this.diffDailySalesPercent = diffDailySalesPercent;
  }

  public BigDecimal getDiffMonthlySalesPercent() {
    return diffMonthlySalesPercent;
  }

  public void setDiffMonthlySalesPercent(BigDecimal diffMonthlySalesPercent) {
    this.diffMonthlySalesPercent = diffMonthlySalesPercent;
  }

  public BigDecimal getDiffMonthlyRevenuePercent() {
    return diffMonthlyRevenuePercent;
  }

  public void setDiffMonthlyRevenuePercent(BigDecimal diffMonthlyRevenuePercent) {
    this.diffMonthlyRevenuePercent = diffMonthlyRevenuePercent;
  }

  public BigDecimal getDiffDailyAvarageSalesPercent() {
    return diffDailyAvarageSalesPercent;
  }

  public void setDiffDailyAvarageSalesPercent(BigDecimal diffDailyAvarageSalesPercent) {
    this.diffDailyAvarageSalesPercent = diffDailyAvarageSalesPercent;
  }

  public StoreDTO getStore() {
    return store;
  }

  public void setStore(StoreDTO store) {
    this.store = store;
  }

}
