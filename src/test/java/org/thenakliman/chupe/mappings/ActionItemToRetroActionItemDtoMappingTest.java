package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.ActionItemDTO;
import org.thenakliman.chupe.models.RetroActionItem;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class ActionItemToRetroActionItemDtoMappingTest {
  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() {
    modelMapper.addMappings(new ActionItemToActionItemDtoMapping().mapping());
  }

  @Test
  public void shouldMapActionItemToActionItemDto() {
    Date deadlineToAct = new Date();
    RetroActionItem retroActionItem = RetroActionItem.builder()
        .status(ActionItemStatus.CREATED)
        .retroPoint(RetroPoint.builder().id(102L).build())
        .retro(Retro.builder().id(101L).build())
        .description("description")
        .deadlineToAct(deadlineToAct)
        .id(103L)
        .createdBy(User.builder().userName("lal_singh").build())
        .assignedTo(User.builder().userName("assigned-to").build())
        .build();

    ActionItemDTO actionItemDTO = modelMapper.map(retroActionItem, ActionItemDTO.class);

    assertEquals(Long.valueOf(103), actionItemDTO.getId());
    assertEquals("description", actionItemDTO.getDescription());
    assertEquals(deadlineToAct, actionItemDTO.getDeadlineToAct());
    assertEquals("lal_singh", actionItemDTO.getCreatedBy());
    assertEquals("assigned-to", actionItemDTO.getAssignedTo());
    assertEquals(ActionItemStatus.CREATED, actionItemDTO.getStatus());
  }
}