package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.dto.UpsertRetroPointDTO;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroVote;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroVoteRepository;

@Service
public class RetroPointService {
  private final RetroPointRepository retroPointRepository;
  private final DateUtil dateUtil;
  private final RetroVoteRepository retroVoteRepository;
  private Converter converter;

  @Autowired
  public RetroPointService(RetroPointRepository retroPointRepository,
                           DateUtil dateUtil,
                           RetroVoteRepository retroVoteRepository,
                           Converter converter) {

    this.retroPointRepository = retroPointRepository;
    this.dateUtil = dateUtil;
    this.retroVoteRepository = retroVoteRepository;
    this.converter = converter;
  }

  public RetroPointDTO saveRetroPoint(UpsertRetroPointDTO upsertRetroPointDTO, String username) {
    RetroPoint retroPoint = converter.convertToObject(upsertRetroPointDTO, RetroPoint.class);
    retroPoint.setAddedBy(User.builder().userName(username).build());
    RetroPoint savedRetroPoint = retroPointRepository.save(retroPoint);
    return converter.convertToObject(savedRetroPoint, RetroPointDTO.class);
  }

  public List<RetroPointDTO> getRetroPoints(Long retroId) {
    List<RetroPoint> retroPoints = retroPointRepository.findAllByRetroId(retroId);
    return retroPoints
        .stream()
        .map(retroPoint -> converter.convertToObject(retroPoint, RetroPointDTO.class))
        .peek(retroPointDTO -> retroPointDTO.setVotes(getVotes(retroPointDTO.getId())))
        .collect(toList());
  }

  private long getVotes(Long retroPointId) {
    return retroVoteRepository.countByRetroPointId(retroPointId);
  }

  public RetroPointDTO updateRetroPoint(Long retroPointId,
                                        UpsertRetroPointDTO upsertRetroPointDTO) {

    Optional<RetroPoint> savedRetroPointOptional = retroPointRepository.findById(retroPointId);
    RetroPoint retroPoint = savedRetroPointOptional.orElseThrow(
        () -> new NotFoundException(format("Retro point not found for %d id", retroPointId)));

    retroPoint.setDescription(upsertRetroPointDTO.getDescription());
    retroPoint.setType(upsertRetroPointDTO.getType());
    retroPoint.setUpdatedAt(dateUtil.getTime());
    RetroPoint updatedRetroPoint = retroPointRepository.save(retroPoint);
    return converter.convertToObject(updatedRetroPoint, RetroPointDTO.class);
  }

  RetroPoint getRetroPoint(Long id) {
    Optional<RetroPoint> retroPoint = retroPointRepository.findById(id);
    return retroPoint.orElseThrow(
        () -> new NotFoundException("Retro point not found for id " + id));
  }

  public void castVote(Long retroPointId, String username) {

    RetroVote alreadyCastedVote = retroVoteRepository.findByRetroPointIdAndVotedByUserName(
        retroPointId,
        username);

    if (isNull(alreadyCastedVote)) {
      saveVote(retroPointId, username);
    } else {
      retroVoteRepository.delete(alreadyCastedVote);
    }
  }

  private void saveVote(Long retroPointId, String username) {
    Retro retro = getRetro(retroPointId);
    long votesByUser = retroVoteRepository.countByVotedByUserNameAndRetroPointRetroId(
        username,
        retro.getId());

    if (votesByUser >= retro.getMaximumVote()) {
      throw new BadRequestException(
          String.format("Maximum vote by a user for this retro is %s", retro.getMaximumVote()));
    }

    retroVoteRepository.save(getRetroVote(username, retroPointId));
  }

  private Retro getRetro(Long retroPointId) {
    Optional<RetroPoint> retroPointOptional = retroPointRepository.findById(retroPointId);
    RetroPoint retroPoint = retroPointOptional.orElseThrow(
        () -> new NotFoundException("Retro poing not found for id " + retroPointId));

    return retroPoint.getRetro();
  }

  private RetroVote getRetroVote(String user, Long retroPointId) {
    RetroPoint retroPoint = RetroPoint
        .builder()
        .id(retroPointId)
        .build();

    return RetroVote
        .builder()
        .votedBy(User.builder().userName(user).build())
        .retroPoint(retroPoint)
        .createdAt(dateUtil.getTime())
        .updatedAt(dateUtil.getTime())
        .build();
  }
}
