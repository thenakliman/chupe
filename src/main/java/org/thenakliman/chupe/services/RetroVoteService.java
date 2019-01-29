package org.thenakliman.chupe.services;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroVote;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroVoteRepository;


@Service
public class RetroVoteService {
  private RetroVoteRepository retroVoteRepository;

  private RetroPointService retroPointService;

  private DateUtil dateUtil;

  @Autowired
  public RetroVoteService(RetroVoteRepository retroVoteRepository,
                          RetroPointService retroPointService,
                          DateUtil dateUtil) {

    this.retroVoteRepository = retroVoteRepository;
    this.retroPointService = retroPointService;
    this.dateUtil = dateUtil;
  }

  public void castVote(Long retroPointId, String username)
      throws NotFoundException, BadHttpRequest {

    RetroPoint retroPoint = retroPointService.getRetroPoint(retroPointId);
    User user = getUser(username);
    RetroVote alreadyCastedVote = retroVoteRepository.findByRetroPointAndVotedBy(retroPoint, user);

    if (isNull(alreadyCastedVote)) {
      saveVote(retroPoint, user);
    } else {
      retroVoteRepository.delete(alreadyCastedVote);
    }
  }

  private void saveVote(RetroPoint retroPoint, User user) throws NotFoundException, BadHttpRequest {
    // todo(thenakliman): solve problem with table join
    List<RetroPointDTO> retroPoints = retroPointService.getRetroPoints(
        retroPoint.getRetro().getId());

    List<RetroVote> votesByUser = retroVoteRepository.findByVotedBy(user);
    Set<Long> retroPointIds = retroPoints
        .stream()
        .map(RetroPointDTO::getId)
        .collect(Collectors.toSet());

    Set<Long> votesId = votesByUser
        .stream()
        .map(vote -> vote.getRetroPoint().getId())
        .collect(Collectors.toSet());

    votesId.retainAll(retroPointIds);
    if (votesId.size() >= retroPoint.getRetro().getMaximumVote()) {
      throw new BadHttpRequest();
    }

    retroVoteRepository.save(getRetroVote(user, retroPoint));
  }

  private RetroVote getRetroVote(User user, RetroPoint retroPoint) {
    return RetroVote
        .builder()
        .votedBy(user)
        .retroPoint(retroPoint)
        .createdAt(dateUtil.getTime())
        .updatedAt(dateUtil.getTime())
        .build();
  }

  private User getUser(String username) {
    return User
        .builder()
        .userName(username)
        .build();
  }
}
