package org.thenakliman.chupe.mappings;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.ActionItemType;
import org.thenakliman.chupe.models.RetroActionItem;

@RunWith(MockitoJUnitRunner.class)
public class RetroActionItemToActionItemMappingTest {
  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() {
    modelMapper.addMappings(new RetroActionItemToActionItemMapping().mapping());
  }

  @Test
  public void shouldMapRetroPointToRetroPointDto() {
    RetroActionItem retroActionItem = new RetroActionItem();
    String description1 = "my-description";
    retroActionItem.setDescription(description1);
    long actionItemId = 1039L;
    retroActionItem.setId(actionItemId);
    Date deadlineToAct = new Date();
    retroActionItem.setDeadlineToAct(deadlineToAct);

    ActionItem mappedActionItem = modelMapper.map(retroActionItem, ActionItem.class);

    ActionItem actionItem = new ActionItem();
    actionItem.setDescription(description1);
    actionItem.setId(actionItemId);
    actionItem.setDeadlineToAct(deadlineToAct);
    actionItem.setType(ActionItemType.RETRO_ACTION_ITEM);

    assertThat(mappedActionItem, is(actionItem));
  }
}