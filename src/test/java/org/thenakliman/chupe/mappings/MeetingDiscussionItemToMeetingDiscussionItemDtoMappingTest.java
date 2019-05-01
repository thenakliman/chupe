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
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.DiscussionItemType;
import org.thenakliman.chupe.models.Meeting;
import org.thenakliman.chupe.models.MeetingDiscussionItem;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class MeetingDiscussionItemToMeetingDiscussionItemDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    modelMapper.addMappings(new MeetingDiscussionItemToMeetingDiscussionItemDtoMapping().mapping());
    now = new Date();
  }

  @Test
  public void shouldMapMeetingDiscussionItemToMeetingDiscussionItemDto() {
    MeetingDiscussionItem meetingDiscussionItem = MeetingDiscussionItem
        .builder()
        .discussionItemType(DiscussionItemType.ACTION_ITEM)
        .discussionItem("discussion - item")
        .meeting(Meeting.builder().id(101L).build())
        .assignedTo(User.builder().userName("user - name").build())
        .createdAt(now)
        .updatedAt(now)
        .createdBy(User.builder().userName("created - by").build())
        .id(10123L)
        .build();

    MeetingDiscussionItemDTO discussionItemDTO = modelMapper.map(meetingDiscussionItem, MeetingDiscussionItemDTO.class);

    assertThat(discussionItemDTO.getAssignedTo(), is("user - name"));
    assertThat(discussionItemDTO.getCreatedBy(), is("created - by"));
    assertThat(discussionItemDTO.getDiscussionItem(), is("discussion - item"));
    assertThat(discussionItemDTO.getDiscussionItemType(), is(DiscussionItemType.ACTION_ITEM));
    assertThat(discussionItemDTO.getId(), is(10123L));
    assertThat(discussionItemDTO.getMeetingId(), is(101L));
  }
}