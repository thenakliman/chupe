package org.thenakliman.chupe.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.exceptions.BadRequestException;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroActionItem;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroStatus;
import org.thenakliman.chupe.repositories.RetroActionItemRepository;
import org.thenakliman.chupe.repositories.RetroPointRepository;
import org.thenakliman.chupe.repositories.RetroRepository;

import java.util.Optional;

@Service
public class RetroValidationService {
  private RetroRepository retroRepository;
  private RetroPointRepository retroPointRepository;
  private RetroActionItemRepository retroActionItemRepository;

  @Autowired
  public RetroValidationService(RetroRepository retroRepository,
                                RetroPointRepository retroPointRepository,
                                RetroActionItemRepository retroActionItemRepository) {

    this.retroRepository = retroRepository;
    this.retroPointRepository = retroPointRepository;
    this.retroActionItemRepository = retroActionItemRepository;
  }

  private String getRequestUserName() {
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return user.getUsername();
  }

  public boolean canRetroBeUpdated(long retroId) {
    Optional<Retro> retroOptional = retroRepository.findById(retroId);
    Retro retro = retroOptional.orElseThrow(
            () -> new NotFoundException(String.format("Retro %s not found", retroId)));

    if (RetroStatus.CLOSED.equals(retro.getStatus())) {
      throw new BadRequestException(String.format("Retro %s not found", retroId));
    }
    return getRequestUserName().equals(retro.getCreatedBy().getUserName());
  }

  public boolean isRetroOpen(long retroId) {
    Optional<Retro> retroOptional = retroRepository.findById(retroId);
    Retro retro = retroOptional.orElseThrow(
            () -> new NotFoundException(String.format("Retro %s not found", retroId)));

    if (!RetroStatus.CREATED.equals(retro.getStatus())) {
      throw new BadRequestException(
              String.format("Retro %s is not in open status. status = %s", retroId, retro.getStatus()));
    }

    return true;
  }

  public boolean canBeVoted(long retroPointId) {
    Optional<RetroPoint> retroPointOptional = retroPointRepository.findById(retroPointId);
    RetroPoint retroPoint = retroPointOptional.orElseThrow(
            () -> new NotFoundException(String.format("Retro point %s not found", retroPointId)));

    if (!RetroStatus.IN_PROGRESS.equals(retroPoint.getRetro().getStatus())) {
      throw new BadRequestException(
              String.format("Retro %s is not in progress. status = %s",
                      retroPoint.getRetro().getId(), retroPoint.getRetro().getStatus()));
    }
    return true;
  }

  public boolean isRetroInProgress(long retroId) {
    Optional<Retro> retroOptional = retroRepository.findById(retroId);
    Retro retro = retroOptional.orElseThrow(
            () -> new NotFoundException(String.format("Retro %s not found", retroId)));

    if (!RetroStatus.IN_PROGRESS.equals(retro.getStatus())) {
      throw new BadRequestException(
              String.format("Retro %s is not in progress. status = %s",
                      retro.getId(), retro.getStatus()));
    }
    return true;
  }

  public boolean canActionItemBeUpdated(long actionItemId) {
    Optional<RetroActionItem> retroOptional = retroActionItemRepository.findById(actionItemId);
    RetroActionItem retroActionItem = retroOptional.orElseThrow(
            () -> new NotFoundException(String.format("Action item %s not found", actionItemId)));

    if (RetroStatus.CLOSED.equals(retroActionItem.getRetro().getStatus())) {
      throw new BadRequestException(
              String.format("Retro %s is in closed status.", retroActionItem.getRetro().getId()));
    }

    return getRequestUserName().equals(retroActionItem.getCreatedBy().getUserName());
  }

  public boolean canBeUpdated(long retroPointId) {
    Optional<RetroPoint> retroPointOptional = retroPointRepository.findById(retroPointId);
    RetroPoint retroPoint = retroPointOptional.orElseThrow(
            () -> new NotFoundException(String.format("Retro point %s not found", retroPointId)));

    if (RetroStatus.CLOSED.equals(retroPoint.getRetro().getStatus())) {
      throw new BadRequestException(
              String.format("Retro point %s can not be updated in closed status", retroPointId));
    }

    return getRequestUserName().equals(retroPoint.getAddedBy().getUserName());
  }
}
