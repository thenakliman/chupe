package org.thenakliman.chupe.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.BestPracticeRepository;

@Service
public class PracticeService {
  private BestPracticeRepository bestPracticeRepository;
  private Converter converter;

  @Autowired
  public PracticeService(BestPracticeRepository bestPracticeRepository,
                         Converter converter) {
    this.bestPracticeRepository = bestPracticeRepository;
    this.converter = converter;
  }

  public List<BestPracticeDTO> getActiveBestPractices() {
    List<BestPractice> bestPractices = bestPracticeRepository.findByApplicable(true);
    return converter.convertToListOfObjects(bestPractices, BestPracticeDTO.class);
  }

  public BestPracticeDTO saveBestPractice(UpsertBestPracticeDTO upsertBestPracticeDTO, String username) {
    BestPractice bestPractice = converter.convertToObject(upsertBestPracticeDTO, BestPractice.class);
    User createdBy = User.builder()
        .userName(username)
        .build();

    bestPractice.setCreatedBy(createdBy);
    bestPractice.setApplicable(true);
    BestPractice savedBestPractice = bestPracticeRepository.save(bestPractice);
    return converter.convertToObject(savedBestPractice, BestPracticeDTO.class);
  }
}
