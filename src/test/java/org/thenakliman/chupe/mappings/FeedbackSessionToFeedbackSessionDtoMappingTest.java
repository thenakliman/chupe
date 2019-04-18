package org.thenakliman.chupe.mappings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;
import org.thenakliman.chupe.models.User;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackSessionToFeedbackSessionDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() throws Exception {
    modelMapper.addConverter(new FeedbackSessionToFeedbackSessionDtoMapping().converter());
    now = new Date();
  }

  @Test
  public void shouldMapFeedbackSessionToFeedbackSessionDTO() {
    String description = "description";
    long feedbackSessionId = 10L;
    String username = "user-name";
    FeedbackSession feedbackSession = FeedbackSession
        .builder()
        .description(description)
        .id(feedbackSessionId)
        .createdBy(User.builder().userName(username).build())
        .build();

    FeedbackSessionDTO feedbackSessionDTO = modelMapper.map(feedbackSession, FeedbackSessionDTO.class);

    assertThat(feedbackSessionDTO.getId(), is(feedbackSessionId));
    assertThat(feedbackSessionDTO.getDescription(), is(description));
    assertThat(feedbackSessionDTO.getCreatedBy(), is(username));
  }
}