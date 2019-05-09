package org.thenakliman.chupe.services;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FeedbackPointRepository;

@Service
public class FeedbackPointService {
  private FeedbackPointRepository feedbackPointRepository;
  private DateUtil dateUtil;
  private ConverterUtil converterUtil;

  @Autowired
  public FeedbackPointService(FeedbackPointRepository feedbackPointRepository,
                              DateUtil dateUtil,
                              ConverterUtil converterUtil) {

    this.feedbackPointRepository = feedbackPointRepository;
    this.dateUtil = dateUtil;
    this.converterUtil = converterUtil;
  }

  public List<FeedbackPointDTO> getFeedbackPointsGivenToUser(String username,
                                                             long feedbackSessionId) {
    List<FeedbackPoint> feedbackPoints = feedbackPointRepository
        .findByGivenToUserNameAndFeedbackSessionId(username, feedbackSessionId);

    return converterUtil.convertToListOfObjects(feedbackPoints, FeedbackPointDTO.class);
  }

  public List<FeedbackPointDTO> getFeedbackGivenToUserByAUser(String givenBy,
                                                              String givenTo,
                                                              long feedbackSessionId) {

    List<FeedbackPoint> feedbackPoints = feedbackPointRepository
        .findByGivenToUserNameAndGivenByUserNameAndFeedbackSessionId(
            givenTo,
            givenBy,
            feedbackSessionId);

    return converterUtil.convertToListOfObjects(feedbackPoints, FeedbackPointDTO.class);
  }

  public void saveFeedbackPoint(String givenBy, UpsertFeedbackPointDTO upsertFeedbackPointDTO) {
    FeedbackPoint feedbackPoint = converterUtil.convertToObject(upsertFeedbackPointDTO, FeedbackPoint.class);
    feedbackPoint.setGivenBy(User.builder().userName(givenBy).build());
    feedbackPointRepository.save(feedbackPoint);
  }

  public void updateFeedbackPoint(
      String givenBy,
      long feedbackPointId,
      UpsertFeedbackPointDTO upsertFeedbackPointDTO) throws NotFoundException {

    Optional<FeedbackPoint> feedbackPointOptional = feedbackPointRepository
        .findByGivenByUserNameAndId(givenBy, feedbackPointId);

    FeedbackPoint feedbackPoint = feedbackPointOptional.orElseThrow(
        () -> new NotFoundException(format("%d Feedback point not found", feedbackPointId)));

    feedbackPoint.setDescription(upsertFeedbackPointDTO.getDescription());
    feedbackPoint.setUpdatedAt(dateUtil.getTime());
    feedbackPointRepository.save(feedbackPoint);
  }
}
