package org.thenakliman.chupe.services;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.repositories.FeedbackPointRepository;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackPointServiceTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();
  @Mock
  private FeedbackPointRepository feedbackPointRepository;
  @Mock
  private DateUtil dateUtil;
  @Mock
  private Converter converter;
  @InjectMocks
  private FeedbackPointService feedbackPointService;

  @Test
  public void shouldReturnFeedbackPointsGivenToUser() {
    Long sessionId = 1029L;
    String givenTo = "givenTo-username";
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    when(feedbackPointRepository.findByGivenToUserNameAndFeedbackSessionId(givenTo, sessionId))
        .thenReturn(Collections.singletonList(feedbackPoint));
    FeedbackPointDTO feedbackPointDTO = FeedbackPointDTO.builder().build();
    when(converter.convertToListOfObjects(Collections.singletonList(feedbackPoint), FeedbackPointDTO.class))
        .thenReturn(Collections.singletonList(feedbackPointDTO));

    List<FeedbackPointDTO> feedbackPoints = feedbackPointService.getFeedbackPointsGivenToUser(givenTo, sessionId);

    assertThat(feedbackPoints, hasSize(1));
    assertThat(feedbackPoints, hasItem(feedbackPointDTO));
  }

  @Test
  public void shouldReturnFeedbackGivenToUserByAUser() {
    Long sessionId = 1029L;
    String givenTo = "givenTo-username";
    String givenBy = "createdBy-username";
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    when(feedbackPointRepository.findByGivenToUserNameAndGivenByUserNameAndFeedbackSessionId(givenTo, givenBy, sessionId))
        .thenReturn(Collections.singletonList(feedbackPoint));
    FeedbackPointDTO feedbackPointDTO = FeedbackPointDTO.builder().build();
    when(converter.convertToListOfObjects(Collections.singletonList(feedbackPoint), FeedbackPointDTO.class))
        .thenReturn(Collections.singletonList(feedbackPointDTO));

    List<FeedbackPointDTO> feedbackPoints = feedbackPointService.getFeedbackGivenToUserByAUser(givenBy, givenTo, sessionId);

    assertThat(feedbackPoints, hasSize(1));
    assertThat(feedbackPoints, hasItem(feedbackPointDTO));
  }

  @Test
  public void saveFeedbackPoint() {
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO.builder().build();
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    when(converter.convertToObject(upsertFeedbackPointDTO, FeedbackPoint.class)).thenReturn(feedbackPoint);
    String givenBy = "given-by";

    feedbackPointService.saveFeedbackPoint(givenBy, upsertFeedbackPointDTO);

    verify(feedbackPointRepository).save(feedbackPoint);
  }

  @Test
  public void shouldUpdateFeedbackPoint() {
    FeedbackPoint feedbackPoint = FeedbackPoint.builder().build();
    String givenBy = "given-by";
    Long feedbackPointId = 10L;
    when(feedbackPointRepository.findByGivenByUserNameAndId(givenBy, feedbackPointId)).thenReturn(Optional.of(feedbackPoint));
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO.builder().build();

    feedbackPointService.updateFeedbackPoint(givenBy, feedbackPointId, upsertFeedbackPointDTO);

    verify(feedbackPointRepository).save(feedbackPoint);
  }

  @Test
  public void shouldThrowNotFoundExceptionWhenFeedbackPointToUpdateIsNotFound() {
    String givenBy = "given-by";
    Long feedbackPointId = 10L;
    when(feedbackPointRepository.findByGivenByUserNameAndId(givenBy, feedbackPointId)).thenReturn(Optional.empty());
    UpsertFeedbackPointDTO upsertFeedbackPointDTO = UpsertFeedbackPointDTO.builder().build();

    exception.expect(NotFoundException.class);
    feedbackPointService.updateFeedbackPoint(givenBy, feedbackPointId, upsertFeedbackPointDTO);
  }
}