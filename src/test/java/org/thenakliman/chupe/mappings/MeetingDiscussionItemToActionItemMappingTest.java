package org.thenakliman.chupe.mappings;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
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
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.ActionItemType;
import org.thenakliman.chupe.models.MeetingDiscussionItem;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class MeetingDiscussionItemToActionItemMappingTest {
  @InjectMocks
  private ModelMapper modelMapper;

  @Mock
  private DateUtil dateUtil;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    modelMapper.addConverter(new MeetingDiscussionItemToActionItemMapping(dateUtil).converter());
  }

  @Test
  public void shouldMeetingDiscussionItemToRetroPointDto() {
    long id = 103;
    String discussionItem = "some discussion item";
    MeetingDiscussionItem meetingDiscussionItem = MeetingDiscussionItem.builder()
        .id(id)
        .discussionItem(discussionItem)
        .createdBy(User.builder().userName("some Name").build())
        .build();

    ActionItem mappedActionItem = modelMapper.map(meetingDiscussionItem, ActionItem.class);

    ActionItem actionItem = new ActionItem();
    actionItem.setDescription(discussionItem);
    actionItem.setId(103);
    actionItem.setDeadlineToAct(now);
    actionItem.setType(ActionItemType.MEETING);

    assertThat(mappedActionItem, is(actionItem));
  }
}