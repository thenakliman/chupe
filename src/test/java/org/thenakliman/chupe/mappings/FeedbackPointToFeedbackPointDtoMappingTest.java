package org.thenakliman.chupe.mappings;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.dto.FeedbackPointDTO;
import org.thenakliman.chupe.models.FeedbackPoint;
import org.thenakliman.chupe.models.FeedbackSession;
import org.thenakliman.chupe.models.User;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackPointToFeedbackPointDtoMappingTest {

  @InjectMocks
  private ModelMapper modelMapper;

  @Before
  public void setUp() throws Exception {
    modelMapper.addConverter(new FeedbackPointToFeedbackPointDtoMapping().converter());
  }

  @Test
  public void shouldMapFeedbackPointToFeedbackPointDTO() {
    String description = "description";
    long sessionId = 10L;
    long feedbackPointId = 101L;
    String givenBy = "given - by";
    String givenTo = "given - to";
    FeedbackPoint feedbackPoint = FeedbackPoint
        .builder()
        .givenTo(User.builder().userName(givenTo).build())
        .description(description)
        .givenBy(User.builder().userName(givenBy).build())
        .id(feedbackPointId)
        .feedbackSession(FeedbackSession.builder().id(sessionId).build())
        .build();

    FeedbackPointDTO feedbackPointDTO = modelMapper.map(feedbackPoint, FeedbackPointDTO.class);

    assertThat(feedbackPointDTO.getId(), is(feedbackPointId));
    assertThat(feedbackPointDTO.getDescription(), is(description));
    assertThat(feedbackPointDTO.getGivenBy(), is(givenBy));
    assertThat(feedbackPointDTO.getGivenTo(), is(givenTo));
    assertThat(feedbackPointDTO.getSessionId(), is(sessionId));
  }
}