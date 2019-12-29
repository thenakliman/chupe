package org.thenakliman.chupe.services;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.BestPracticeDTO;
import org.thenakliman.chupe.dto.UpsertBestPracticeDTO;
import org.thenakliman.chupe.models.BestPractice;
import org.thenakliman.chupe.repositories.BestPracticeRepository;

@RunWith(MockitoJUnitRunner.class)
public class PracticeServiceTest {
  @Mock
  private Converter converter;

  @Mock
  private BestPracticeRepository bestPracticeRepository;

  @InjectMocks
  private PracticeService practiceService;

  @Test
  public void getActiveBestPractices_shouldReturnActivePractices() {
    BestPractice bestPractice = BestPractice.builder().id(123L).build();
    when(bestPracticeRepository.findByApplicable(true)).thenReturn(
        Collections.singletonList(bestPractice));
    BestPracticeDTO bestPracticeDTO = BestPracticeDTO.builder().id(123L).build();
    when(converter.convertToListOfObjects(Collections.singletonList(bestPractice), BestPracticeDTO.class))
        .thenReturn(Collections.singletonList(bestPracticeDTO));
    List<BestPracticeDTO> activeBestPractices = practiceService.getActiveBestPractices();
    assertThat(activeBestPractices, hasSize(1));
    assertThat(activeBestPractices, hasItems(bestPracticeDTO));
  }

  @Test
  public void saveBestPractice_shouldSaveBestPractices() {
    String description = "description";
    UpsertBestPracticeDTO upsertBestPracticeDTO = UpsertBestPracticeDTO.builder().description(description).build();
    BestPractice bestPractice = BestPractice.builder().id(123L).description(description).build();
    when(converter.convertToObject(upsertBestPracticeDTO, BestPractice.class)).thenReturn(bestPractice);
    when(bestPracticeRepository.save(bestPractice)).thenReturn(bestPractice);
    BestPracticeDTO savedBestPracticeDTO = BestPracticeDTO.builder().id(123L).description(description).build();
    when(converter.convertToObject(bestPractice, BestPracticeDTO.class)).thenReturn(savedBestPracticeDTO);

    BestPracticeDTO savedBestPracticedDto = practiceService.saveBestPractice(
        upsertBestPracticeDTO, "username");

    assertThat(savedBestPracticedDto,
        samePropertyValuesAs(BestPracticeDTO.builder().id(123L).description(description).build()));
  }
}
