package org.thenakliman.chupe.services;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.ActionItemType;

@RunWith(MockitoJUnitRunner.class)
public class ActionItemServiceTest {
  @Mock
  private MeetingService meetingService;
  @Mock
  private RetroActionItemService retroActionItemService;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @InjectMocks
  private ActionItemService actionItemService;

  @Test
  public void getActionItems_shouldReturnActionItems() {
    String username = "username";
    List<ActionItem> retroActionItems = new ArrayList<>();
    ActionItem retroActionItem = new ActionItem();
    retroActionItem.setId(1013);
    retroActionItem.setType(ActionItemType.RETROSPECTION);
    retroActionItem.setDescription("my description");
    retroActionItems.add(retroActionItem);

    ActionItem meetingActionItem = new ActionItem();
    meetingActionItem.setId(4313);
    meetingActionItem.setType(ActionItemType.MEETING);
    meetingActionItem.setDescription("ohh my description");
    when(retroActionItemService.getActiveActionItem(username))
        .thenReturn(retroActionItems);

    when(meetingService.getMeetingActionItem(username))
        .thenReturn(Collections.singletonList(meetingActionItem));

    List<ActionItem> actionItems = actionItemService.getActionItems(username);

    assertThat(actionItems, hasSize(2));
    assertThat(actionItems, hasItem(retroActionItem));
    assertThat(actionItems, hasItem(meetingActionItem));
  }
}