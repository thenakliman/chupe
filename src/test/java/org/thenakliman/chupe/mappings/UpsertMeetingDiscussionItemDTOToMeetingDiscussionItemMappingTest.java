package org.thenakliman.chupe.mappings;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
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
import org.thenakliman.chupe.dto.UpsertMeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.DiscussionItemType;
import org.thenakliman.chupe.models.MeetingDiscussionItem;

@RunWith(MockitoJUnitRunner.class)
public class UpsertMeetingDiscussionItemDTOToMeetingDiscussionItemMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new UpsertMeetingDiscussionItemDTOToMeetingDiscussionItemMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapCreateMeetingDiscussionItemDtoToMeetingDiscussionItem_whenInformationType() {
    UpsertMeetingDiscussionItemDTO upsertMeetingDiscussionItemDTO = new UpsertMeetingDiscussionItemDTO();
    upsertMeetingDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.INFORMATION);
    upsertMeetingDiscussionItemDTO.setMeetingId(1011L);
    upsertMeetingDiscussionItemDTO.setAssignedTo("lal");
    upsertMeetingDiscussionItemDTO.setDiscussionItem("discussion - item");

    MeetingDiscussionItem discussionItem = modelMapper.map(upsertMeetingDiscussionItemDTO, MeetingDiscussionItem.class);

    assertThat(discussionItem.getAssignedTo().getUserName(), is("lal"));
    assertThat(discussionItem.getDiscussionItem(), is("discussion - item"));
    assertThat(discussionItem.getDiscussionItemType(), is(DiscussionItemType.INFORMATION));
    assertThat(discussionItem.getMeeting().getId(), is(1011L));
    assertThat(discussionItem.getCreatedAt(), is(now));
    assertThat(discussionItem.getUpdatedAt(), is(now));
    assertThat(discussionItem.getStatus(), is(ActionItemStatus.DONE));
    assertThat(discussionItem.getId(), nullValue());
    assertThat(discussionItem.getCreatedBy(), nullValue());
    assertThat(discussionItem.getDeadlineToAct(), is(now));
  }

  @Test
  public void shouldMapCreateMeetingDiscussionItemDtoToMeetingDiscussionItem_whenActionItemType() {
    UpsertMeetingDiscussionItemDTO upsertMeetingDiscussionItemDTO = new UpsertMeetingDiscussionItemDTO();
    upsertMeetingDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.ACTION_ITEM);
    upsertMeetingDiscussionItemDTO.setMeetingId(1011L);
    upsertMeetingDiscussionItemDTO.setAssignedTo("lal");
    upsertMeetingDiscussionItemDTO.setDiscussionItem("discussion - item");

    MeetingDiscussionItem discussionItem = modelMapper.map(upsertMeetingDiscussionItemDTO, MeetingDiscussionItem.class);

    assertThat(discussionItem.getAssignedTo().getUserName(), is("lal"));
    assertThat(discussionItem.getDiscussionItem(), is("discussion - item"));
    assertThat(discussionItem.getDiscussionItemType(), is(DiscussionItemType.ACTION_ITEM));
    assertThat(discussionItem.getMeeting().getId(), is(1011L));
    assertThat(discussionItem.getCreatedAt(), is(now));
    assertThat(discussionItem.getUpdatedAt(), is(now));
    assertThat(discussionItem.getStatus(), is(ActionItemStatus.CREATED));
    assertThat(discussionItem.getId(), nullValue());
    assertThat(discussionItem.getCreatedBy(), nullValue());
  }

  @Test
  public void shouldMapCreateMeetingDiscussionItemDtoToMeetingDiscussionItem_whenDeadlineToActIsGiven() {
    UpsertMeetingDiscussionItemDTO upsertMeetingDiscussionItemDTO = new UpsertMeetingDiscussionItemDTO();
    upsertMeetingDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.ACTION_ITEM);
    upsertMeetingDiscussionItemDTO.setMeetingId(1011L);
    upsertMeetingDiscussionItemDTO.setAssignedTo("lal");
    upsertMeetingDiscussionItemDTO.setDiscussionItem("discussion - item");
    Date deadlineToAct = new Date();
    upsertMeetingDiscussionItemDTO.setDeadlineToAct(deadlineToAct);

    MeetingDiscussionItem discussionItem = modelMapper.map(upsertMeetingDiscussionItemDTO, MeetingDiscussionItem.class);

    assertThat(discussionItem.getAssignedTo().getUserName(), is("lal"));
    assertThat(discussionItem.getDiscussionItem(), is("discussion - item"));
    assertThat(discussionItem.getDiscussionItemType(), is(DiscussionItemType.ACTION_ITEM));
    assertThat(discussionItem.getMeeting().getId(), is(1011L));
    assertThat(discussionItem.getCreatedAt(), is(now));
    assertThat(discussionItem.getUpdatedAt(), is(now));
    assertThat(discussionItem.getStatus(), is(ActionItemStatus.CREATED));
    assertThat(discussionItem.getId(), nullValue());
    assertThat(discussionItem.getCreatedBy(), nullValue());
    assertThat(discussionItem.getDeadlineToAct(), is(deadlineToAct));
  }
}