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
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.dto.ActionItemQueryParams;
import org.thenakliman.chupe.dto.InsertActionItemDTO;
import org.thenakliman.chupe.dto.UpdateActionItemDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.RetroActionItem;
import org.thenakliman.chupe.repositories.RetroActionItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class RetroActionItemServiceTest {
  @Mock
  private RetroActionItemRepository retroActionItemRepository;
  @Mock
  private Converter converter;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @InjectMocks
  private RetroActionItemService retroActionItemService;

  @Test
  public void addActionItem_shouldCreateActionItem() {
    RetroActionItem retroActionItem = mock(RetroActionItem.class);
    when(converter.convertToObject(any(InsertActionItemDTO.class), eq(RetroActionItem.class))).thenReturn(retroActionItem);
    when(retroActionItemRepository.save(retroActionItem)).thenReturn(retroActionItem);
    ActionItemDTO savedActionItemDto = ActionItemDTO.builder().id(101L).build();
    when(converter.convertToObject(retroActionItem, ActionItemDTO.class)).thenReturn(savedActionItemDto);

    ActionItemDTO actionItemDTO = retroActionItemService.addActionItem(new InsertActionItemDTO(), "username");

    assertThat(actionItemDTO, is(savedActionItemDto));
  }

  @Test
  public void updateActionItem_shouldThrowNotFoundException_whenActionItemDoesNotExist() {
    when(retroActionItemRepository
        .findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(anyLong(), anyString(), anyLong(), anyString())).thenReturn(Optional.empty());
    expectedException.expect(NotFoundException.class);
    retroActionItemService.updateActionItem(10L, new UpdateActionItemDTO(), "username");
  }

  @Test
  public void updateActionItem_shouldUpdateActionItem() {
    RetroActionItem retroActionItem = mock(RetroActionItem.class);
    when(retroActionItemRepository
        .findByIdAndCreatedByUserNameOrIdAndAssignedToUserName(anyLong(), anyString(), anyLong(), anyString())).thenReturn(Optional.of(retroActionItem));
    when(retroActionItemRepository.save(retroActionItem)).thenReturn(retroActionItem);
    ActionItemDTO actionItemDTO = mock(ActionItemDTO.class);
    when(converter.convertToObject(retroActionItem, ActionItemDTO.class)).thenReturn(actionItemDTO);

    ActionItemDTO savedActionItem = retroActionItemService.updateActionItem(10L, new UpdateActionItemDTO(), "username");

    assertThat(actionItemDTO, is(savedActionItem));
  }

  @Test
  public void getActionItems_shouldGetActionItem() {
    List<RetroActionItem> retroActionItems = asList(
        RetroActionItem.builder().id(101L).build(),
        RetroActionItem.builder().id(102L).build());
    when(retroActionItemRepository.findAll(any(Specification.class))).thenReturn(retroActionItems);
    ActionItemDTO actionItemDTO1 = ActionItemDTO.builder().id(101L).build();
    ActionItemDTO actionItemDTO2 = ActionItemDTO.builder().id(102L).build();
    when(converter.convertToListOfObjects(retroActionItems, ActionItemDTO.class)).thenReturn(asList(
        actionItemDTO1,
        actionItemDTO2
    ));

    List<ActionItemDTO> fetchedActionItems = retroActionItemService.getActionItems(new ActionItemQueryParams());

    assertThat(fetchedActionItems, hasSize(2));
    assertThat(fetchedActionItems, hasItems(actionItemDTO1, actionItemDTO2));
  }

  @Test
  public void getActiveActionItems_shouldGetActionItemInStateOfCreatedAndInProgress() {
    List<RetroActionItem> retroActionItems = asList(
        RetroActionItem.builder().id(101L).build(),
        RetroActionItem.builder().id(102L).build());

    String username = "username";
    when(retroActionItemRepository.findByAssignedToUserNameAndStatusIn(
        username,
        asList(ActionItemStatus.IN_PROGRESS, ActionItemStatus.CREATED))
    ).thenReturn(retroActionItems);

    ActionItem actionItem1 = new ActionItem();
    actionItem1.setId(101L);
    ActionItem actionItem2 = new ActionItem();
    actionItem2.setId(102L);
    when(converter.convertToListOfObjects(retroActionItems, ActionItem.class)).thenReturn(asList(
        actionItem1,
        actionItem2
    ));

    List<ActionItem> fetchedActionItems = retroActionItemService.getActiveActionItem("username");

    assertThat(fetchedActionItems, hasSize(2));
    assertThat(fetchedActionItems, hasItems(actionItem1, actionItem2));
  }
}