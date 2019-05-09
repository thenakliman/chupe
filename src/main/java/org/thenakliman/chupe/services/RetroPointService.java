package org.thenakliman.chupe.services;

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.ConverterUtil;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.dto.UpsertRetroPointDTO;
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
  private ConverterUtil converterUtil;

  @Autowired
  public RetroPointService(RetroPointRepository retroPointRepository,
                           DateUtil dateUtil,
                           RetroVoteRepository retroVoteRepository,
                           ConverterUtil converterUtil) {

    this.retroPointRepository = retroPointRepository;
    this.dateUtil = dateUtil;
    this.retroVoteRepository = retroVoteRepository;
    this.converterUtil = converterUtil;
  }

  public RetroPointDTO saveRetroPoint(UpsertRetroPointDTO upsertRetroPointDTO, String username) {
    RetroPoint retroPoint = converterUtil.convertToObject(upsertRetroPointDTO, RetroPoint.class);
    retroPoint.setAddedBy(User.builder().userName(username).build());
    RetroPoint savedRetroPoint = retroPointRepository.save(retroPoint);
    return converterUtil.convertToObject(savedRetroPoint, RetroPointDTO.class);
  }

  public List<RetroPointDTO> getRetroPoints(Long retroId) {
    List<RetroPoint> retroPoints = retroPointRepository.findAllByRetroId(retroId);
    return retroPoints
        .stream()
        .map(retroPoint -> converterUtil.convertToObject(retroPoint, RetroPointDTO.class))
        .peek(retroPointDTO -> retroPointDTO.setVotes(getVotes(retroPointDTO.getId())))
        .collect(toList());
  }

  private long getVotes(Long retroPointId) {
    return retroVoteRepository.countByRetroPointId(retroPointId);
  }

  public RetroPointDTO updateRetroPoint(Long retroPointId,
                                        UpsertRetroPointDTO upsertRetroPointDTO
                                        ) throws NotFoundException {

    Optional<RetroPoint> savedRetroPointOptional = retroPointRepository.findById(retroPointId);
    RetroPoint retroPoint = savedRetroPointOptional.orElseThrow(
        () -> new NotFoundException(format("Retro point not found for %d id", retroPointId)));

    retroPoint.setDescription(upsertRetroPointDTO.getDescription());
    retroPoint.setType(upsertRetroPointDTO.getType());
    retroPoint.setUpdatedAt(dateUtil.getTime());
    RetroPoint updatedRetroPoint = retroPointRepository.save(retroPoint);
    return converterUtil.convertToObject(updatedRetroPoint, RetroPointDTO.class);
  }

  RetroPoint getRetroPoint(Long id) throws NotFoundException {
    Optional<RetroPoint> retroPoint = retroPointRepository.findById(id);
    return retroPoint.orElseThrow(
        () -> new NotFoundException("Retro point not found for id " + id));
  }

  public void castVote(Long retroPointId, String username)
      throws BadHttpRequest, NotFoundException {

    RetroVote alreadyCastedVote = retroVoteRepository.findByRetroPointIdAndVotedByUserName(
        retroPointId,
        username);

    if (isNull(alreadyCastedVote)) {
      saveVote(retroPointId, username);
    } else {
      retroVoteRepository.delete(alreadyCastedVote);
    }
  }

  private void saveVote(Long retroPointId, String username)
      throws BadHttpRequest, NotFoundException {

    Retro retro = getRetro(retroPointId);
    long votesByUser = retroVoteRepository.countByVotedByUserNameAndRetroPointRetroId(
        username,
        retro.getId());

    if (votesByUser >= retro.getMaximumVote()) {
      throw new BadHttpRequest();
    }

    retroVoteRepository.save(getRetroVote(username, retroPointId));
  }

  private Retro getRetro(Long retroPointId) throws NotFoundException {
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
