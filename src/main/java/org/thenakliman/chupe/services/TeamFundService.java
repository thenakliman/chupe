package org.thenakliman.chupe.services;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.models.FundType;
import org.thenakliman.chupe.repositories.FundTypeRepository;

@Service
public class TeamFundService {
  @Autowired
  private FundTypeRepository fundTypeRepository;

  /** Get all fund types.
   *
   * @returns list of fund types.
   */
  public List<FundType> getAllFundTypes() throws NotFoundException {
    List<FundType> fundTypes = fundTypeRepository.findAll();
    if (fundTypes.isEmpty()) {
      throw new NotFoundException("Fund types could not found");
    }
    return fundTypes;
  }
}
