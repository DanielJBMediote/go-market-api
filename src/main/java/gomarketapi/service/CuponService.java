package gomarketapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gomarketapi.dto.cupons.CuponCreateDTO;
import gomarketapi.dto.cupons.CuponReadDTO;
import gomarketapi.exceptions.ForbiddenException;
import gomarketapi.exceptions.NotFoundException;
import gomarketapi.helpers.Where;
import gomarketapi.helpers.WhereBuilder;
import gomarketapi.model.CuponBean;
import gomarketapi.repository.CuponRepository;

@Service
public class CuponService {

  private final CuponRepository cuponRepository;

  CuponService(CuponRepository cuponRepository) {
    this.cuponRepository = cuponRepository;
  }

  public CuponReadDTO create(CuponCreateDTO cuponBody) {

    CuponBean cuponBean = new CuponBean();

    cuponBean.setCuponCode(cuponBody.getCuponCode());
    cuponBean.setExpiredDate(cuponBody.getExpiredDate());
    cuponBean.setMaxUsed(cuponBody.getMaxUsed());
    cuponBean.setPercentDiscount(cuponBody.getPercentDiscount());
    cuponBean.setExpired(false);

    CuponBean createdCupon = this.cuponRepository.save(cuponBean);
    return new CuponReadDTO(createdCupon);
  }

  public CuponReadDTO update(Long id, CuponCreateDTO cuponBody) {

    CuponBean foundCupon = this.cuponRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cupon not found with id " + id));

    foundCupon.setExpiredDate(cuponBody.getExpiredDate());
    foundCupon.setMaxUsed(cuponBody.getMaxUsed());

    CuponBean updatedCupon = this.cuponRepository.save(foundCupon);
    return new CuponReadDTO(updatedCupon);
  }

  public CuponReadDTO useCuponByCode(String cuponCode) {
    WhereBuilder<CuponBean> wb = new WhereBuilder<>(CuponBean.class);
    wb.addWhere(new Where("cuponCode", cuponCode, Where.Operator.EQUALS));
    Specification<CuponBean> spec = wb.build();

    Optional<CuponBean> cuponOpt = this.cuponRepository.findAll(spec).stream().findFirst();

    if (cuponOpt.isEmpty()) {
      throw new NotFoundException("Cupon not found with code: " + cuponCode);
    }

    CuponBean cuponBean = cuponOpt.get();

    if (this.validateCupon(cuponBean) == false) {
      throw new ForbiddenException("Cupon is already expired or used the maximun times.");
    }

    return new CuponReadDTO(cuponBean);
  }

  private boolean validateCupon(CuponBean cuponBean) {
    boolean isNotExpired = cuponBean.getExpiredDate().isAfter(LocalDateTime.now());
    boolean isWithinUsageLimit = cuponBean.getUsed() < cuponBean.getMaxUsed();

    boolean isValid = isNotExpired && isWithinUsageLimit;

    cuponBean.setExpired(!isNotExpired);

    if (isValid) {
      cuponBean.setUsed(cuponBean.getUsed() + 1);
      this.cuponRepository.save(cuponBean);
    }

    return isValid;
  }

  public List<CuponReadDTO> getAll(String where) {
    WhereBuilder<CuponBean> wb = new WhereBuilder<>(CuponBean.class);
    wb.parseWhereEncodedURIParams(where);
    Specification<CuponBean> spec = wb.build();

    List<CuponBean> cuponList = this.cuponRepository.findAll(spec);
    return cuponList.stream().map(cupon -> new CuponReadDTO(cupon)).toList();
  }

  public CuponReadDTO getById(Long id) {
    CuponBean cuponBean = this.cuponRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cupon not found!"));

    return new CuponReadDTO(cuponBean);
  }
}
