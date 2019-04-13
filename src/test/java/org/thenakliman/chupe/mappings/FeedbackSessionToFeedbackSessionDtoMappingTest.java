package org.thenakliman.chupe.mappings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;

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
    FeedbackSessionDTO feedbackSessionDTO = FeedbackSessionDTO
        .builder()
        .description(description)
        .id(feedbackSessionId)
        .build();

    FeedbackSession feedbackSession = modelMapper.map(feedbackSessionDTO, FeedbackSession.class);

    assertThat(feedbackSession.getId(), is(feedbackSessionId));
    assertThat(feedbackSession.getDescription(), is(description));
  }
}