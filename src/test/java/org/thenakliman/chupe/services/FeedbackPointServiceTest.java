package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.repositories.FeedbackPointRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackPointServiceTest {
  @Mock
  private FeedbackPointRepository feedbackPointRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private FeedbackPointService feedbackPointService;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void shouldReturnFeedbackPointsGivenToUser() {
    Long sessionId = 1029L;
    String givenTo = "givenTo-username";
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    when(feedbackPointRepository.findByGivenToUserNameAndFeedbackSessionId(givenTo, sessionId))
        .thenReturn(Collections.singletonList(feedbackPoint));
    FeedbackPointDTO feedbackPointDTO = FeedbackPointDTO.builder().build();
    when(modelMapper.map(feedbackPoint, FeedbackPointDTO.class)).thenReturn(feedbackPointDTO);

    List<FeedbackPointDTO> feedbackPoints = feedbackPointService.getFeedbackPointsGivenToUser(givenTo, sessionId);

    assertThat(feedbackPoints, hasSize(1));
    assertThat(feedbackPoints, hasItem(feedbackPointDTO));
  }

  @Test
  public void shouldReturnFeedbackGivenToUserByAUser() {
    Long sessionId = 1029L;
    String givenTo = "givenTo-username";
    String givenBy = "givenBy-username";
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    when(feedbackPointRepository.findByGivenToUserNameAndGivenByUserNameAndFeedbackSessionId(givenTo, givenBy, sessionId))
        .thenReturn(Collections.singletonList(feedbackPoint));
    FeedbackPointDTO feedbackPointDTO = FeedbackPointDTO.builder().build();
    when(modelMapper.map(feedbackPoint, FeedbackPointDTO.class)).thenReturn(feedbackPointDTO);

    List<FeedbackPointDTO> feedbackPoints = feedbackPointService.getFeedbackGivenToUserByAUser(givenBy, givenTo, sessionId);

    assertThat(feedbackPoints, hasSize(1));
    assertThat(feedbackPoints, hasItem(feedbackPointDTO));
  }

  @Test
  public void saveFeedbackPoint() {
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO.builder().build();
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    when(modelMapper.map(upsertFeedbackPointDTO, FeedbackPoint.class)).thenReturn(feedbackPoint);
    String givenBy = "given-by";

    feedbackPointService.saveFeedbackPoint(givenBy, upsertFeedbackPointDTO);

    verify(feedbackPointRepository).save(feedbackPoint);
  }

  @Test
  public void shouldUpdateFeedbackPoint() throws NotFoundException {
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    String givenBy = "given-by";
    Long feedbackPointId = 10L;
    when(feedbackPointRepository.findByGivenByUserNameAndId(givenBy, feedbackPointId)).thenReturn(Optional.of(feedbackPoint));
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO.builder().build();

    feedbackPointService.updateFeedbackPoint(givenBy, feedbackPointId, upsertFeedbackPointDTO);

    verify(feedbackPointRepository).save(feedbackPoint);
  }

  @Test
  public void shouldThrowNotFoundExceptionWhenFeedbackPointToUpdateIsNotFound() throws NotFoundException {
    String givenBy = "given-by";
    Long feedbackPointId = 10L;
    when(feedbackPointRepository.findByGivenByUserNameAndId(givenBy, feedbackPointId)).thenReturn(Optional.empty());
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO.builder().build();

    exception.expect(NotFoundException.class);
    feedbackPointService.updateFeedbackPoint(givenBy, feedbackPointId, upsertFeedbackPointDTO);
  }
}