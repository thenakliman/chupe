package org.thenakliman.chupe.mappings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.InsertActionItemDTO;
import org.thenakliman.chupe.models.ActionItem;
import org.thenakliman.chupe.models.ActionItemStatus;

@RunWith(MockitoJUnitRunner.class)
public class InsertActionItemDtoToActionItemMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new InsertActionItemDtoToActionItemMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapActionItemDtoToActionItem() {
    Date deadlineToAct = new Date();
    InsertActionItemDTO actionItemDTO = getInsertActionItemDTO(deadlineToAct);

    ActionItem actionItem = modelMapper.map(actionItemDTO, ActionItem.class);

    assertEquals("assigned-to", actionItem.getAssignedTo().getUserName());
    assertEquals(deadlineToAct, actionItem.getDeadlineToAct());
    assertEquals("description", actionItem.getDescription());
    assertEquals(ActionItemStatus.CREATED, actionItem.getStatus());
    assertEquals(now, actionItem.getCreatedAt());
    assertEquals(now, actionItem.getUpdatedAt());
    assertEquals(Long.valueOf(101L), actionItem.getRetro().getId());
    assertEquals(Long.valueOf(102L), actionItem.getRetroPoint().getId());
    assertNull(actionItem.getCreatedBy());
    assertNull(actionItem.getId());
  }

  @Test
  public void shouldMapActionItemDtoToActionItemForRetroPointNull() {
    Date deadlineToAct = new Date();
    InsertActionItemDTO actionItemDTO = getInsertActionItemDTO(deadlineToAct);
    actionItemDTO.setRetroPointId(null);

    ActionItem actionItem = modelMapper.map(actionItemDTO, ActionItem.class);

    assertEquals("assigned-to", actionItem.getAssignedTo().getUserName());
    assertEquals(deadlineToAct, actionItem.getDeadlineToAct());
    assertEquals("description", actionItem.getDescription());
    assertEquals(ActionItemStatus.CREATED, actionItem.getStatus());
    assertEquals(now, actionItem.getCreatedAt());
    assertEquals(now, actionItem.getUpdatedAt());
    assertEquals(Long.valueOf(101L), actionItem.getRetro().getId());
    assertNull(actionItem.getRetroPoint());
    assertNull(actionItem.getCreatedBy());
    assertNull(actionItem.getId());
  }

  private InsertActionItemDTO getInsertActionItemDTO(Date deadlineToAct) {
    return InsertActionItemDTO
        .builder()
        .assignedTo("assigned-to")
        .deadlineToAct(deadlineToAct)
        .description("description")
        .status(ActionItemStatus.CREATED)
        .retroId(101L)
        .retroPointId(102L)
        .build();
  }
}