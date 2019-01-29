package org.thenakliman.chupe.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.RetroPointType.NEED_IMPROVEMENT;

import java.util.Date;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroPointType;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroRepository;

@RunWith(MockitoJUnitRunner.class)
public class RetroPointServiceTest {
  @Mock
  private ModelMapper modelMapper;

  @Mock
  private RetroPointRepository retroPointRepository;

  @Mock
  private RetroRepository retroRepository;

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private RetroPointService retroPointService;

  final private Date now = new Date();
  final private long retroId = 101L;

  @Test
  public void shouldSaveRetroPoint() {
    String username = "james my name";
    String description = "my description";
    RetroPointType retroPointType = NEED_IMPROVEMENT;
    RetroPointDTO retroPointDTO = getRetroPointDTO(username, description, retroPointType);

    RetroPoint retroPoint = getRetroPoint(username, retroId, retroPointType, description, null);
    when(modelMapper.map(retroPointDTO, RetroPoint.class)).thenReturn(retroPoint);
    when(retroPointRepository.save(retroPoint)).thenReturn(retroPoint);
    when(modelMapper.map(retroPoint, RetroPointDTO.class)).thenReturn(retroPointDTO);

    RetroPointDTO savedRetroPoint = retroPointService.saveRetroPoint(retroPointDTO);

    assertEquals(username, savedRetroPoint.getAddedBy());
    assertEquals(description, savedRetroPoint.getDescription());
    assertEquals(retroId, savedRetroPoint.getRetroId());
    assertEquals(retroPointType, savedRetroPoint.getType());
  }

  private RetroPointDTO getRetroPointDTO(String username, String description, RetroPointType retroPointType) {
    return RetroPointDTO
        .builder()
        .addedBy(username)
        .description(description)
        .retroId(retroId)
        .type(retroPointType)
        .build();
  }

  private RetroPoint getRetroPoint(String username, Long retroId, RetroPointType retroPointType, String description, Long id) {
    return RetroPoint
        .builder()
        .addedBy(User.builder().userName(username).build())
        .retro(Retro.builder().id(retroId).build())
        .type(retroPointType)
        .description(description)
        .createdAt(now)
        .updatedAt(now)
        .id(id)
        .build();
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaisedNotFoundExceptionWhenRetroDoesNotExistForFetchingAllPoints() throws NotFoundException {
    retroPointService.getRetroPoints(retroId);
  }

  @Test(expected = NotFoundException.class)
  public void shouldRaisedNotFoundExceptionWhenRetroDoesNotExistForUpdate() throws NotFoundException {
    when(retroPointRepository.findById(retroId)).thenReturn(Optional.empty());
    retroPointService.updateRetroPoint(retroId, RetroPointDTO.builder().build());
  }

  @Test
  public void shouldUpdateRetroPointWhenRetroDoesNotExistForUpdate() throws NotFoundException {
    long retroPointId = 101L;
    RetroPoint retroPoint = getRetroPoint("my username", retroId, NEED_IMPROVEMENT, "my description", retroPointId);
    when(retroPointRepository.findById(retroPointId)).thenReturn(Optional.of(retroPoint));
    RetroPointDTO retroPointDTO = RetroPointDTO.builder().id(retroPointId).build();
    when(modelMapper.map(any(), eq(RetroPointDTO.class))).thenReturn(retroPointDTO);
    when(retroPointRepository.save(any())).thenReturn(retroPoint);
    when(dateUtil.getTime()).thenReturn(now);

    RetroPointDTO updateRetroPoint = retroPointService.updateRetroPoint(retroId, retroPointDTO);

    assertThat(updateRetroPoint, samePropertyValuesAs(retroPointDTO));
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundExceptionWhenRetroPointDoesNotExist() throws NotFoundException {
    Long retroId = 1939L;
    when(retroPointRepository.findById(retroId)).thenReturn(Optional.empty());

    retroPointService.getRetroPoint(retroId);
  }

  @Test
  public void shouldReturnRetroPoint() throws NotFoundException {
    Long retroId = 1939L;
    RetroPoint retroPoint = RetroPoint.builder().build();
    when(retroPointRepository.findById(retroId)).thenReturn(Optional.of(retroPoint));

    RetroPoint actualRetroPoint = retroPointService.getRetroPoint(retroId);

    assertEquals(retroPoint, actualRetroPoint);
  }
}