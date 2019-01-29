package org.thenakliman.chupe.services;

import javassist.NotFoundException;
import javassist.tools.web.BadHttpRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.RetroPointDTO;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroVote;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.RetroVoteRepository;

import java.util.Date;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetroVoteServiceTest {
  @Mock
  private RetroVoteRepository retroVoteRepository;

  @Mock
  private RetroPointService retroPointService;

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private RetroVoteService retroVoteService;

  final private Long retroPointId = 1357L;
  final private String username = "my username";

  @Test(expected = NotFoundException.class)
  public void castVoteToInvalidPointShouldRaiseNotFoundException() throws NotFoundException, BadHttpRequest {
    when(retroPointService.getRetroPoint(retroPointId)).thenThrow(new NotFoundException("not Found"));
    retroVoteService.castVote(retroPointId, username);
  }

  @Test
  public void castVoteToShouldDeleteVoteWhenAlreadyCasted() throws NotFoundException, BadHttpRequest {
    RetroPoint retroPoint = RetroPoint.builder().build();
    User user = User.builder().userName(username).build();
    when(retroPointService.getRetroPoint(retroPointId)).thenReturn(retroPoint);
    RetroVote retroVote = RetroVote.builder().build();
    when(retroVoteRepository.findByRetroPointAndVotedBy(retroPoint, user)).thenReturn(retroVote);

    retroVoteService.castVote(retroPointId, username);

    verify(retroVoteRepository).delete(retroVote);
  }

  @Test(expected = BadHttpRequest.class)
  public void shouldRaiseBadRequestWhenAlreadyCastedVoteIsGreaterThanMaximumVoteForRetro() throws NotFoundException, BadHttpRequest {
    long retroId = 1654L;
    Retro retro = Retro.builder().id(retroId).maximumVote(3L).build();
    RetroPoint retroPoint = RetroPoint.builder().retro(retro).build();
    User user = User.builder().userName(username).build();
    when(retroPointService.getRetroPoint(retroPointId)).thenReturn(retroPoint);
    when(retroVoteRepository.findByRetroPointAndVotedBy(retroPoint, user)).thenReturn(null);
    when(retroVoteRepository.findByVotedBy(user)).thenReturn(asList(
        RetroVote.builder().retroPoint(RetroPoint.builder().id(1L).build()).build(),
        RetroVote.builder().retroPoint(RetroPoint.builder().id(2L).build()).build(),
        RetroVote.builder().retroPoint(RetroPoint.builder().id(3L).build()).build()
    ));

    when(retroPointService.getRetroPoints(retroId)).thenReturn(asList(
        RetroPointDTO.builder().id(1L).build(),
        RetroPointDTO.builder().id(2L).build(),
        RetroPointDTO.builder().id(3L).build()
    ));

    retroVoteService.castVote(retroPointId, username);
  }

  @Test
  public void shouldCasteVoteWhenAlreadyCastedVoteIsLessThanMaximumVoteForRetro() throws NotFoundException, BadHttpRequest {
    long retroId = 1654L;
    Retro retro = Retro.builder().id(retroId).maximumVote(3L).build();
    RetroPoint retroPoint = RetroPoint.builder().retro(retro).build();
    User user = User.builder().userName(username).build();
    when(retroPointService.getRetroPoint(retroPointId)).thenReturn(retroPoint);
    when(retroVoteRepository.findByRetroPointAndVotedBy(retroPoint, user)).thenReturn(null);
    when(retroPointService.getRetroPoints(retroId)).thenReturn(asList(
        RetroPointDTO.builder().build(),
        RetroPointDTO.builder().build()
    ));
    Date now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    retroVoteService.castVote(retroPointId, username);
    RetroVote retroVote =  RetroVote
        .builder()
        .votedBy(user)
        .retroPoint(retroPoint)
        .createdAt(dateUtil.getTime())
        .updatedAt(dateUtil.getTime())
        .build();

    verify(retroVoteRepository).save(retroVote);
  }
}