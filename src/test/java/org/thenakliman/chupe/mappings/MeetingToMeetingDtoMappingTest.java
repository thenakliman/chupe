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
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.models.Meeting;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class MeetingToMeetingDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    modelMapper.addMappings(new MeetingToMeetingDtoMapping().mapping());
    now = new Date();
  }

  @Test
  public void shouldMapMeetingToMeetingDto() {
    Meeting meeting = Meeting
        .builder()
        .id(101L)
        .createdBy(User.builder().userName("user-name").build())
        .subject("new subject")
        .createdAt(now)
        .updatedAt(now)
        .build();

    MeetingDTO meetingDTO = modelMapper.map(meeting, MeetingDTO.class);

    assertThat(meetingDTO.getId(), is(101L));
    assertThat(meetingDTO.getSubject(), is("new subject"));
    assertThat(meetingDTO.getCreatedBy(), is("user-name"));
  }
}