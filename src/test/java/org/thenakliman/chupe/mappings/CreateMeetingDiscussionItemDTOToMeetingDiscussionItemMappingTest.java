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
import org.thenakliman.chupe.dto.CreateMeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.DiscussionItemType;
import org.thenakliman.chupe.models.MeetingDiscussionItem;

@RunWith(MockitoJUnitRunner.class)
public class CreateMeetingDiscussionItemDTOToMeetingDiscussionItemMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    modelMapper.addConverter(new CreateMeetingDiscussionItemDTOToMeetingDiscussionItemMapping(dateUtil).converter());
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldMapCreateMeetingDiscussionItemDtoToMeetingDiscussionItem() {
    CreateMeetingDiscussionItemDTO createMeetingDiscussionItemDTO = new CreateMeetingDiscussionItemDTO();
    createMeetingDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.INFORMATION);
    createMeetingDiscussionItemDTO.setMeetingId(1011L);
    createMeetingDiscussionItemDTO.setAssignedTo("lal");
    createMeetingDiscussionItemDTO.setDiscussionItem("discussion - item");

    MeetingDiscussionItem discussionItem = modelMapper.map(createMeetingDiscussionItemDTO, MeetingDiscussionItem.class);

    assertThat(discussionItem.getAssignedTo().getUserName(), is("lal"));
    assertThat(discussionItem.getDiscussionItem(), is("discussion - item"));
    assertThat(discussionItem.getDiscussionItemType(), is(DiscussionItemType.INFORMATION));
    assertThat(discussionItem.getMeeting().getId(), is(1011L));
    assertThat(discussionItem.getCreatedAt(), is(now));
    assertThat(discussionItem.getUpdatedAt(), is(now));
    assertThat(discussionItem.getId(), nullValue());
    assertThat(discussionItem.getCreatedBy(), nullValue());
  }
}