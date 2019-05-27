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
import org.thenakliman.chupe.models.RetroActionItem;
import org.thenakliman.chupe.models.ActionItemStatus;

@RunWith(MockitoJUnitRunner.class)
public class InsertActionItemDtoToRetroActionItemMappingTest {

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

    RetroActionItem retroActionItem = modelMapper.map(actionItemDTO, RetroActionItem.class);

    assertEquals("assigned-to", retroActionItem.getAssignedTo().getUserName());
    assertEquals(deadlineToAct, retroActionItem.getDeadlineToAct());
    assertEquals("description", retroActionItem.getDescription());
    assertEquals(ActionItemStatus.CREATED, retroActionItem.getStatus());
    assertEquals(now, retroActionItem.getCreatedAt());
    assertEquals(now, retroActionItem.getUpdatedAt());
    assertEquals(Long.valueOf(101L), retroActionItem.getRetro().getId());
    assertEquals(Long.valueOf(102L), retroActionItem.getRetroPoint().getId());
    assertNull(retroActionItem.getCreatedBy());
    assertNull(retroActionItem.getId());
  }

  @Test
  public void shouldMapActionItemDtoToActionItemForRetroPointNull() {
    Date deadlineToAct = new Date();
    InsertActionItemDTO actionItemDTO = getInsertActionItemDTO(deadlineToAct);
    actionItemDTO.setRetroPointId(null);

    RetroActionItem retroActionItem = modelMapper.map(actionItemDTO, RetroActionItem.class);

    assertEquals("assigned-to", retroActionItem.getAssignedTo().getUserName());
    assertEquals(deadlineToAct, retroActionItem.getDeadlineToAct());
    assertEquals("description", retroActionItem.getDescription());
    assertEquals(ActionItemStatus.CREATED, retroActionItem.getStatus());
    assertEquals(now, retroActionItem.getCreatedAt());
    assertEquals(now, retroActionItem.getUpdatedAt());
    assertEquals(Long.valueOf(101L), retroActionItem.getRetro().getId());
    assertNull(retroActionItem.getRetroPoint());
    assertNull(retroActionItem.getCreatedBy());
    assertNull(retroActionItem.getId());
  }

  private InsertActionItemDTO getInsertActionItemDTO(Date deadlineToAct) {
    return InsertActionItemDTO
        .builder()
        .assignedTo("assigned-to")
        .deadlineToAct(deadlineToAct)
        .description("description")
        .retroId(101L)
        .retroPointId(102L)
        .build();
  }
}