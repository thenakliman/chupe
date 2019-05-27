package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.dto.ActionItemQueryParams;
import org.thenakliman.chupe.dto.InsertActionItemDTO;
import org.thenakliman.chupe.dto.UpdateActionItemDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.RetroActionItem;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.ActionItemRepository;
import org.thenakliman.chupe.repositories.specifications.retroactionitem.ActionItemAssignedToSpecification;
import org.thenakliman.chupe.repositories.specifications.retroactionitem.ActionItemRetroSpecification;
import org.thenakliman.chupe.repositories.specifications.retroactionitem.ActionItemStatusSpecification;

@Service
public class RetroActionItemService {
  private final ActionItemRepository actionItemRepository;
  private final Converter converter;

  @Autowired
  public RetroActionItemService(ActionItemRepository actionItemRepository,
                                Converter converter) {

    this.actionItemRepository = actionItemRepository;
    this.converter = converter;
  }

  public ActionItemDTO addActionItem(InsertActionItemDTO insertActionItemDTO, String username) {
    RetroActionItem retroActionItem = converter.convertToObject(insertActionItemDTO, RetroActionItem.class);
    retroActionItem.setCreatedBy(User.builder().userName(username).build());
    RetroActionItem savedRetroActionItem = actionItemRepository.save(retroActionItem);
    return converter.convertToObject(savedRetroActionItem, ActionItemDTO.class);
  }

  public List<ActionItemDTO> getActionItems(ActionItemQueryParams actionItemQueryParams) {
    Specification<RetroActionItem> specification = Specification
        .<RetroActionItem>where(new ActionItemStatusSpecification(actionItemQueryParams.getStatuses()))
        .and(new ActionItemRetroSpecification(actionItemQueryParams.getRetro()))
        .and(new ActionItemAssignedToSpecification(actionItemQueryParams.getAssignedTo()));

    List<RetroActionItem> retroActionItems = actionItemRepository.findAll(specification);
    return converter.convertToListOfObjects(retroActionItems, ActionItemDTO.class);
  }

  public ActionItemDTO updateActionItem(Long actionItemId, UpdateActionItemDTO updateActionItemDTO, String username) {
    Optional<RetroActionItem> actionItemOptional = actionItemRepository
        .findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(actionItemId,
                                                               username,
                                                               actionItemId,
                                                               username);

    RetroActionItem retroActionItem = actionItemOptional.orElseThrow(
        () -> new NotFoundException(
            String.format("Action item %s is not found or you don't have permission.", actionItemId)));

    retroActionItem.setAssignedTo(User.builder().userName(updateActionItemDTO.getAssignedTo()).build());
    retroActionItem.setDeadlineToAct(updateActionItemDTO.getDeadlineToAct());
    retroActionItem.setDescription(updateActionItemDTO.getDescription());
    retroActionItem.setStatus(updateActionItemDTO.getStatus());
    RetroActionItem savedRetroActionItem = actionItemRepository.save(retroActionItem);
    return converter.convertToObject(savedRetroActionItem, ActionItemDTO.class);
  }
}
