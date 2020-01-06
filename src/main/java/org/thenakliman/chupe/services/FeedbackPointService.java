package org.thenakliman.chupe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FeedbackPointRepository;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class FeedbackPointService {
  private FeedbackPointRepository feedbackPointRepository;
  private DateUtil dateUtil;
  private Converter converter;

  @Autowired
  public FeedbackPointService(FeedbackPointRepository feedbackPointRepository,
                              DateUtil dateUtil,
                              Converter converter) {

    this.feedbackPointRepository = feedbackPointRepository;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public List<FeedbackPointDTO> getFeedbackPointsGivenToUser(String username,
                                                             long feedbackSessionId) {
    List<FeedbackPoint> feedbackPoints = feedbackPointRepository
        .findByGivenToUserNameAndFeedbackSessionId(username, feedbackSessionId);

    return converter.convertToListOfObjects(feedbackPoints, FeedbackPointDTO.class);
  }

  public List<FeedbackPointDTO> getFeedbackGivenToUserByAUser(String givenBy,
                                                              String givenTo,
                                                              long feedbackSessionId) {

    List<FeedbackPoint> feedbackPoints = feedbackPointRepository
        .findByGivenToUserNameAndGivenByUserNameAndFeedbackSessionId(
            givenTo,
            givenBy,
            feedbackSessionId);

    return converter.convertToListOfObjects(feedbackPoints, FeedbackPointDTO.class);
  }

  public void saveFeedbackPoint(String givenBy, UpsertFeedbackPointDTO upsertFeedbackPointDTO) {
    FeedbackPoint feedbackPoint = converter.convertToObject(upsertFeedbackPointDTO, FeedbackPoint.class);
    feedbackPoint.setGivenBy(User.builder().userName(givenBy).build());
    feedbackPointRepository.save(feedbackPoint);
  }

  public void updateFeedbackPoint(String givenBy,
                                  long feedbackPointId,
                                  UpsertFeedbackPointDTO upsertFeedbackPointDTO) {

    Optional<FeedbackPoint> feedbackPointOptional = feedbackPointRepository
        .findByGivenByUserNameAndId(givenBy, feedbackPointId);

    FeedbackPoint feedbackPoint = feedbackPointOptional.orElseThrow(
        () -> new NotFoundException(format("%d Feedback point not found", feedbackPointId)));

    feedbackPoint.setDescription(upsertFeedbackPointDTO.getDescription());
    feedbackPoint.setUpdatedAt(dateUtil.getTime());
    feedbackPointRepository.save(feedbackPoint);
  }
}
