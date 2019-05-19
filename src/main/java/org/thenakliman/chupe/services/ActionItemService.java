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
import org.thenakliman.chupe.models.ActionItem;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.ActionItemRepository;
import org.thenakliman.chupe.repositories.specifications.retroactionitem.ActionItemAssignedToSpecification;
import org.thenakliman.chupe.repositories.specifications.retroactionitem.ActionItemRetroSpecification;
import org.thenakliman.chupe.repositories.specifications.retroactionitem.ActionItemStatusSpecification;

@Service
public class ActionItemService {
  private final ActionItemRepository actionItemRepository;
  private final Converter converter;

  @Autowired
  public ActionItemService(ActionItemRepository actionItemRepository,
                           Converter converter) {

    this.actionItemRepository = actionItemRepository;
    this.converter = converter;
  }

  public ActionItemDTO addActionItem(InsertActionItemDTO insertActionItemDTO, String username) {
    ActionItem actionItem = converter.convertToObject(insertActionItemDTO, ActionItem.class);
    actionItem.setCreatedBy(User.builder().userName(username).build());
    ActionItem savedActionItem = actionItemRepository.save(actionItem);
    return converter.convertToObject(savedActionItem, ActionItemDTO.class);
  }

  public List<ActionItemDTO> getActionItems(ActionItemQueryParams actionItemQueryParams) {
    Specification<ActionItem> specification = Specification
        .<ActionItem>where(new ActionItemStatusSpecification(actionItemQueryParams.getStatuses()))
        .and(new ActionItemRetroSpecification(actionItemQueryParams.getRetro()))
        .and(new ActionItemAssignedToSpecification(actionItemQueryParams.getAssignedTo()));

    List<ActionItem> actionItems = actionItemRepository.findAll(specification);
    return converter.convertToListOfObjects(actionItems, ActionItemDTO.class);
  }

  public ActionItemDTO updateActionItem(Long actionItemId, UpdateActionItemDTO updateActionItemDTO, String username) {
    Optional<ActionItem> actionItemOptional = actionItemRepository
        .findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(actionItemId,
                                                               username,
                                                               actionItemId,
                                                               username);

    ActionItem actionItem = actionItemOptional.orElseThrow(
        () -> new NotFoundException(
            String.format("Action item %s is not found or you don't have permission.", actionItemId)));

    actionItem.setAssignedTo(User.builder().userName(updateActionItemDTO.getAssignedTo()).build());
    actionItem.setDeadlineToAct(updateActionItemDTO.getDeadlineToAct());
    actionItem.setDescription(updateActionItemDTO.getDescription());
    actionItem.setStatus(updateActionItemDTO.getStatus());
    ActionItem savedActionItem = actionItemRepository.save(actionItem);
    return converter.convertToObject(savedActionItem, ActionItemDTO.class);
  }
}
