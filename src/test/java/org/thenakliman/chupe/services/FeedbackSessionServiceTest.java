package org.thenakliman.chupe.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;
import org.thenakliman.chupe.repositories.FeedbackSessionRepository;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackSessionServiceTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();
  @Mock
  private FeedbackSessionRepository feedbackSessionRepository;
  @Mock
  private DateUtil dateUtil;
  @Mock
  private Converter converter;
  @InjectMocks
  private FeedbackSessionService feedbackSessionService;

  @Test
  public void shouldSaveFeedbackSession() {
    UpsertFeedbackSessionDTO upsertFeedbackSessionDTO = UpsertFeedbackSessionDTO
        .builder()
        .description("description")
        .build();

    FeedbackSession feedbackSession = FeedbackSession.builder().build();
    when(converter.convertToObject(upsertFeedbackSessionDTO, FeedbackSession.class)).thenReturn(feedbackSession);

    String createdByUsername = "created - by";
    feedbackSessionService.createSession(upsertFeedbackSessionDTO, createdByUsername);

    assertThat(feedbackSession.getCreatedBy().getUserName(), is(createdByUsername));
    verify(feedbackSessionRepository).save(feedbackSession);
  }

  @Test
  public void shouldGetFeedbackSession() {
    FeedbackSessionDTO feedbackSessionDTO = FeedbackSessionDTO
        .builder()
        .description("description")
        .build();

    FeedbackSession feedbackSession = FeedbackSession.builder().build();
    when(converter.convertToListOfObjects(
        Collections.singletonList(feedbackSession), FeedbackSessionDTO.class))
        .thenReturn(Collections.singletonList(feedbackSessionDTO));

    String username = "created-by";
    when(feedbackSessionRepository.findAllByCreatedByUserName(username))
        .thenReturn(Collections.singletonList(feedbackSession));
    List<FeedbackSessionDTO> feedbackSessions = feedbackSessionService.getFeedbackSessions(username);

    verify(feedbackSessionRepository).findAllByCreatedByUserName(username);
    assertThat(feedbackSessions, hasSize(1));
    assertThat(feedbackSessions, hasItem(feedbackSessionDTO));
  }

  @Test
  public void shouldUpdateFeedbackSession() throws NotFoundException {
    FeedbackSession feedbackSession = FeedbackSession.builder().build();
    String username = "created-by";
    long feedbackSessionId = 1L;
    when(feedbackSessionRepository.findByIdAndCreatedByUserName(feedbackSessionId, username))
        .thenReturn(Optional.of(feedbackSession));
    when(dateUtil.getTime()).thenReturn(new Date());
    UpsertFeedbackSessionDTO upsertFeedbackSessionDTO = UpsertFeedbackSessionDTO
        .builder()
        .description("description")
        .build();

    feedbackSessionService.updateSession(feedbackSessionId, upsertFeedbackSessionDTO, username);

    verify(feedbackSessionRepository).findByIdAndCreatedByUserName(feedbackSessionId, username);
    verify(feedbackSessionRepository).save(feedbackSession);
  }

  @Test
  public void shouldThrowExceptionWhenFeedbackSessionToBeUpdatedNotFound() throws NotFoundException {
    long feedbackSessionId = 1L;
    String username = "created-by";
    when(feedbackSessionRepository.findByIdAndCreatedByUserName(feedbackSessionId, username))
        .thenReturn(Optional.empty());

    exception.expect(NotFoundException.class);
    feedbackSessionService.updateSession(
        feedbackSessionId,
        UpsertFeedbackSessionDTO.builder().build(),
        username);
  }
}