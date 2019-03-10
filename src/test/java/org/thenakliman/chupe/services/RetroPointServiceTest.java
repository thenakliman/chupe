package org.thenakliman.chupe.services;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.thenakliman.chupe.models.RetroPointType.NEED_IMPROVEMENT;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.dto.UpsertRetroPointDTO;
import org.thenakliman.chupe.models.*;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroVoteRepository;

@RunWith(MockitoJUnitRunner.class)
public class RetroPointServiceTest {
  @Mock
  private ModelMapper modelMapper;

  @Mock
  private RetroPointRepository retroPointRepository;

  @Mock
  private RetroVoteRepository retroVoteRepository;

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private RetroPointService retroPointService;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  final private Date now = new Date();
  final private long retroId = 101L;

  @Test
  public void shouldSaveRetroPoint() {
    String username = "james my name";
    String description = "my description";
    RetroPointType retroPointType = NEED_IMPROVEMENT;
    RetroPointDTO retroPointDTO = getRetroPointDTO(username, description, retroPointType);
    UpsertRetroPointDTO upsertRetroPointDTO = UpsertRetroPointDTO
        .builder()
        .description(description)
        .type(retroPointType)
        .build();

    RetroPoint retroPoint = getRetroPoint(username, retroId, retroPointType, description, null);
    when(modelMapper.map(upsertRetroPointDTO, RetroPoint.class)).thenReturn(retroPoint);
    when(retroPointRepository.save(retroPoint)).thenReturn(retroPoint);
    when(modelMapper.map(retroPoint, RetroPointDTO.class)).thenReturn(retroPointDTO);

    RetroPointDTO savedRetroPoint = retroPointService.saveRetroPoint(upsertRetroPointDTO, username);

    assertEquals(username, savedRetroPoint.getAddedBy());
    assertEquals(description, savedRetroPoint.getDescription());
    assertEquals(retroId, savedRetroPoint.getRetroId());
    assertEquals(retroPointType, savedRetroPoint.getType());
  }

  @Test
  public void shouldReturnAllRetroPointsForARetro() {
    long retroId = 1928L;
    long retroPointId = 101L;
    RetroPoint retroPoint1 = RetroPoint.builder().id(retroPointId).build();
    when(retroPointRepository.findAllByRetroId(retroId)).thenReturn(
        Collections.singletonList(retroPoint1));

    RetroPointDTO retroPointDTO = RetroPointDTO.builder().id(retroPointId).build();
    when(modelMapper.map(any(RetroPoint.class), eq(RetroPointDTO.class)))
        .thenReturn(retroPointDTO);
    when(retroVoteRepository.countByRetroPointId(retroPointId)).thenReturn(2L);

    List<RetroPointDTO> retroPoints = retroPointService.getRetroPoints(retroId);

    assertEquals(retroPointId, retroPoints.get(0).getId());
    assertEquals(2, retroPoints.get(0).getVotes());
  }

  private RetroPointDTO getRetroPointDTO(String username,
                                         String description,
                                         RetroPointType retroPointType) {

    return RetroPointDTO
        .builder()
        .addedBy(username)
        .description(description)
        .retroId(retroId)
        .type(retroPointType)
        .build();
  }

  private RetroPoint getRetroPoint(String username,
                                   Long retroId,
                                   RetroPointType retroPointType,
                                   String description,
                                   Long id) {

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

  @Test
  public void shouldRaisedNotFoundExceptionWhenRetroDoesNotExistForUpdate()
      throws NotFoundException {

    when(retroPointRepository.findById(retroId)).thenReturn(Optional.empty());
    exception.expect(NotFoundException.class);
    retroPointService.updateRetroPoint(retroId, UpsertRetroPointDTO.builder().build());
  }

  @Test
  public void shouldUpdateRetroPointWhenRetroDoesNotExistForUpdate()
      throws NotFoundException {

    long retroPointId = 101L;
    String description = "my description";
    RetroPoint retroPoint = getRetroPoint("my username",
        retroId,
        NEED_IMPROVEMENT,
        description,
        retroPointId);

    when(retroPointRepository.findById(retroPointId)).thenReturn(Optional.of(retroPoint));
    RetroPointDTO retroPointDTO = RetroPointDTO.builder().id(retroPointId).build();
    when(modelMapper.map(any(), eq(RetroPointDTO.class))).thenReturn(retroPointDTO);
    when(retroPointRepository.save(any())).thenReturn(retroPoint);
    when(dateUtil.getTime()).thenReturn(now);

    UpsertRetroPointDTO upsertRetroPointDTO = UpsertRetroPointDTO
        .builder()
        .type(NEED_IMPROVEMENT)
        .description(description)
        .build();
    RetroPointDTO updateRetroPoint = retroPointService.updateRetroPoint(retroId, upsertRetroPointDTO);

    assertThat(updateRetroPoint, samePropertyValuesAs(retroPointDTO));
  }

  @Test
  public void shouldThrowNotFoundExceptionWhenRetroPointDoesNotExist() throws NotFoundException {
    Long retroId = 1939L;
    when(retroPointRepository.findById(retroId)).thenReturn(Optional.empty());

    exception.expect(NotFoundException.class);
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

  @Test
  public void shouldDeleteVoteIfAlreadyCasted() throws NotFoundException, BadHttpRequest {
    Long retroPointId = 1939L;
    String username = "voted by";
    RetroVote retroVote = RetroVote.builder().build();
    when(retroVoteRepository.findByRetroPointIdAndVotedByUserName(retroPointId, username))
        .thenReturn(retroVote);

    retroPointService.castVote(retroPointId, username);

    verify(retroVoteRepository).delete(retroVote);
    verify(retroVoteRepository).findByRetroPointIdAndVotedByUserName(retroPointId, username);
  }

  @Test
  public void shouldRaiseNotFoundWhenRetroPointDoesNotExistForTheVote()
      throws NotFoundException, BadHttpRequest {

    Long retroPointId = 1939L;
    String username = "voted by";
    when(retroVoteRepository.findByRetroPointIdAndVotedByUserName(retroPointId, username)).thenReturn(null);
    when(retroPointRepository.findById(retroPointId)).thenReturn(Optional.empty());

    exception.expect(NotFoundException.class);
    retroPointService.castVote(retroPointId, username);
  }

  @Test
  public void shouldRaiseBadRequestExceptionWhenVotesForARetroIsMoreThanMaximumVotes()
      throws NotFoundException, BadHttpRequest {

    Long retroPointId = 1939L;
    Long retroId = 10L;
    String username = "voted by";
    when(retroVoteRepository.findByRetroPointIdAndVotedByUserName(retroPointId, username))
        .thenReturn(null);

    when(retroVoteRepository.countByVotedByUserNameAndRetroPointRetroId(username, retroId))
        .thenReturn(2L);

    RetroPoint retroPoint = RetroPoint.builder().retro(
        Retro.builder().id(retroId).maximumVote(2L).build()
    ).build();

    when(retroPointRepository.findById(retroPointId)).thenReturn(Optional.of(retroPoint));

    exception.expect(BadHttpRequest.class);
    retroPointService.castVote(retroPointId, username);
  }

  @Test
  public void shouldCastVote() throws NotFoundException, BadHttpRequest {
    Long retroPointId = 1939L;
    Long retroId = 10L;
    String username = "voted by";
    when(retroVoteRepository.findByRetroPointIdAndVotedByUserName(retroPointId, username))
        .thenReturn(null);

    when(retroVoteRepository.countByVotedByUserNameAndRetroPointRetroId(username, retroId))
        .thenReturn(2L);

    RetroPoint retroPoint = RetroPoint.builder().retro(
        Retro.builder().id(retroId).maximumVote(3L).build()
    ).build();

    when(retroPointRepository.findById(retroPointId)).thenReturn(Optional.of(retroPoint));

    retroPointService.castVote(retroPointId, username);

    verify(retroVoteRepository).save(any(RetroVote.class));
  }
}