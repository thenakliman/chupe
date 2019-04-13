package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.dto.UpsertFeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.FeedbackPointRepository;

@Service
public class FeedbackPointService {
  private FeedbackPointRepository feedbackPointRepository;
  private ModelMapper modelMapper;
  private DateUtil dateUtil;

  @Autowired
  public FeedbackPointService(FeedbackPointRepository feedbackPointRepository,
                              ModelMapper modelMapper,
                              DateUtil dateUtil) {

    this.feedbackPointRepository = feedbackPointRepository;
    this.modelMapper = modelMapper;
    this.dateUtil = dateUtil;
  }

  public List<FeedbackPointDTO> getFeedbackPointsGivenToUser(String username,
                                                             long feedbackSessionId) {
    List<FeedbackPoint> feedbackPoints = feedbackPointRepository
        .findByGivenToUserNameAndFeedbackSessionId(username, feedbackSessionId);

    return feedbackPoints
        .stream()
        .map(feedbackPoint -> modelMapper.map(feedbackPoint, FeedbackPointDTO.class))
        .collect(toList());
  }

  public List<FeedbackPointDTO> getFeedbackGivenToUserByAUser(String givenBy,
                                                              String givenTo,
                                                              long feedbackSessionId) {

    List<FeedbackPoint> feedbackPoints = feedbackPointRepository
        .findByGivenToUserNameAndGivenByUserNameAndFeedbackSessionId(
            givenTo,
            givenBy,
            feedbackSessionId);

    return feedbackPoints
        .stream()
        .map(feedbackPoint -> modelMapper.map(feedbackPoint, FeedbackPointDTO.class))
        .collect(toList());
  }

  public void saveFeedbackPoint(String givenBy, UpsertFeedbackPointDTO upsertFeedbackPointDTO) {
    FeedbackPoint feedbackPoint = modelMapper.map(upsertFeedbackPointDTO, FeedbackPoint.class);
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
