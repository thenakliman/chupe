package org.thenakliman.chupe.services;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.dto.ActionItemQueryParams;
import org.thenakliman.chupe.dto.InsertActionItemDTO;
import org.thenakliman.chupe.dto.UpdateActionItemDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.ActionItem;
import org.thenakliman.chupe.repositories.ActionItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ActionItemServiceTest {
  @Mock
  private ActionItemRepository actionItemRepository;
  @Mock
  private Converter converter;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @InjectMocks
  private ActionItemService actionItemService;

  @Test
  public void addActionItem_shouldCreateActionItem() {
    ActionItem actionItem = mock(ActionItem.class);
    when(converter.convertToObject(any(InsertActionItemDTO.class), eq(ActionItem.class))).thenReturn(actionItem);
    when(actionItemRepository.save(actionItem)).thenReturn(actionItem);
    ActionItemDTO savedActionItemDto = ActionItemDTO.builder().id(101L).build();
    when(converter.convertToObject(actionItem, ActionItemDTO.class)).thenReturn(savedActionItemDto);

    ActionItemDTO actionItemDTO = actionItemService.addActionItem(new InsertActionItemDTO(), "username");

    assertThat(actionItemDTO, is(savedActionItemDto));
  }

  @Test
  public void updateActionItem_shouldThrowNotFoundException_whenActionItemDoesNotExist() {
    when(actionItemRepository
        .findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(anyLong(), anyString(), anyLong(), anyString())).thenReturn(Optional.empty());
    expectedException.expect(NotFoundException.class);
    actionItemService.updateActionItem(10L, new UpdateActionItemDTO(), "username");
  }

  @Test
  public void updateActionItem_shouldUpdateActionItem() {
    ActionItem actionItem = mock(ActionItem.class);
    when(actionItemRepository
        .findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(anyLong(), anyString(), anyLong(), anyString())).thenReturn(Optional.of(actionItem));
    when(actionItemRepository.save(actionItem)).thenReturn(actionItem);
    ActionItemDTO actionItemDTO = mock(ActionItemDTO.class);
    when(converter.convertToObject(actionItem, ActionItemDTO.class)).thenReturn(actionItemDTO);

    ActionItemDTO savedActionItem = actionItemService.updateActionItem(10L, new UpdateActionItemDTO(), "username");

    assertThat(actionItemDTO, is(savedActionItem));
  }

  @Test
  public void getActionItems_shouldGetActionItem() {
    List<ActionItem> actionItems = asList(
        ActionItem.builder().id(101L).build(),
        ActionItem.builder().id(102L).build());
    when(actionItemRepository.findAll(any(Specification.class))).thenReturn(actionItems);
    ActionItemDTO actionItemDTO1 = ActionItemDTO.builder().id(101L).build();
    ActionItemDTO actionItemDTO2 = ActionItemDTO.builder().id(102L).build();
    when(converter.convertToListOfObjects(actionItems, ActionItemDTO.class)).thenReturn(asList(
        actionItemDTO1,
        actionItemDTO2
    ));

    List<ActionItemDTO> fetchedActionItems = actionItemService.getActionItems(new ActionItemQueryParams());

    assertThat(fetchedActionItems, hasSize(2));
    assertThat(fetchedActionItems, hasItems(actionItemDTO1, actionItemDTO2));
  }
}