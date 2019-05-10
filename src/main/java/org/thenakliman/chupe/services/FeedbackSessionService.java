package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FeedbackSessionRepository;

@Service
public class FeedbackSessionService {
  private FeedbackSessionRepository feedbackSessionRepository;
  private DateUtil dateUtil;
  private Converter converter;

  @Autowired
  public FeedbackSessionService(FeedbackSessionRepository feedbackSessionRepository,
                                DateUtil dateUtil,
                                Converter converter) {
    this.feedbackSessionRepository = feedbackSessionRepository;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public void createSession(UpsertFeedbackSessionDTO upsertFeedbackSessionDTO, String createdBy) {
    FeedbackSession feedbackSession = converter.convertToObject(
        upsertFeedbackSessionDTO,
        FeedbackSession.class);

    feedbackSession.setCreatedBy(User.builder().userName(createdBy).build());
    feedbackSessionRepository.save(feedbackSession);
  }

  public List<FeedbackSessionDTO> getFeedbackSessions(String username) {
    List<FeedbackSession> feedbackSessions = feedbackSessionRepository
        .findAllByCreatedByUserName(username);

    return converter.convertToListOfObjects(feedbackSessions, FeedbackSessionDTO.class);
  }

  public void updateSession(long feedbackSessionId,
                            UpsertFeedbackSessionDTO upsertFeedbackSessionDTO,
                            String createdBy) throws NotFoundException {

    Optional<FeedbackSession> feedbackSessionOptional = feedbackSessionRepository
        .findByIdAndCreatedByUserName(feedbackSessionId, createdBy);

    FeedbackSession feedbackSession = feedbackSessionOptional.orElseThrow(() ->
        new NotFoundException(String.format("Feedback session %d not found", feedbackSessionId)));

    feedbackSession.setDescription(upsertFeedbackSessionDTO.getDescription());
    feedbackSession.setUpdatedAt(dateUtil.getTime());
    feedbackSessionRepository.save(feedbackSession);
  }
}
