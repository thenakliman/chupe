package org.thenakliman.chupe.services;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

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

@RunWith(MockitoJUnitRunner.class)
public class ActionItemServiceTest {
  @Mock
  private RetroActionItemService retroActionItemService;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @InjectMocks
  private ActionItemService actionItemService;

  @Test
  public void getActionItems_shouldReturnActionItems() {
    String username = "username";
    ActionItem actionItem = new ActionItem();
    actionItem.setId(1013);
    actionItem.setDescription("my description");
    when(retroActionItemService.getActiveActionItem(username))
        .thenReturn(Collections.singletonList(actionItem));

    List<ActionItem> actionItems = actionItemService.getActionItems(username);

    assertThat(actionItems, hasSize(1));
    assertThat(actionItems, hasItem(actionItem));
  }
}