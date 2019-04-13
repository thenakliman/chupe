package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FeedbackSessionRepository;

@Service
public class FeedbackSessionService {
  private FeedbackSessionRepository feedbackSessionRepository;
  private ModelMapper modelMapper;
  private DateUtil dateUtil;

  @Autowired
  public FeedbackSessionService(FeedbackSessionRepository feedbackSessionRepository,
                                ModelMapper modelMapper,
                                DateUtil dateUtil) {
    this.feedbackSessionRepository = feedbackSessionRepository;
    this.modelMapper = modelMapper;
    this.dateUtil = dateUtil;
  }

  public void createSession(UpsertFeedbackSessionDTO upsertFeedbackSessionDTO, String createdBy) {
    FeedbackSession feedbackSession = modelMapper.map(
        upsertFeedbackSessionDTO,
        FeedbackSession.class);

    feedbackSession.setCreatedBy(User.builder().userName(createdBy).build());
    feedbackSessionRepository.save(feedbackSession);
  }

  public List<FeedbackSessionDTO> getFeedbackSessions(String username) {
    List<FeedbackSession> feedbackSessions = feedbackSessionRepository
        .findAllByCreatedByUserName(username);

    return feedbackSessions
        .stream()
        .map(feedbackSession -> modelMapper.map(feedbackSession, FeedbackSessionDTO.class))
        .collect(Collectors.toList());
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
