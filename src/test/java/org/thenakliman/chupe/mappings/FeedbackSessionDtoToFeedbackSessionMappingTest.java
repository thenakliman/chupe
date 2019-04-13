package org.thenakliman.chupe.mappings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.FeedbackSessionDTO;
import org.thenakliman.chupe.models.FeedbackSession;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackSessionDtoToFeedbackSessionMappingTest {

  @Mock
  private DateUtil dateUtil;

  @InjectMocks
  private ModelMapper modelMapper;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    modelMapper.addConverter(new FeedbackSessionDtoToFeedbackSessionMapping(dateUtil).converter());
  }

  @Test
  public void shouldMapFeedbackSessionDtoToFeedbackSession() {
    String description = "description";
    long id = 10111L;
    FeedbackSessionDTO feedbackSessionDTO = FeedbackSessionDTO
        .builder()
        .id(id)
        .description(description)
        .build();

    FeedbackSession feedbackSession = modelMapper.map(feedbackSessionDTO, FeedbackSession.class);

    assertThat(feedbackSession.getDescription(), is(description));
    assertNull(feedbackSession.getId());
    assertThat(feedbackSession.getCreatedAt(), is(now));
    assertThat(feedbackSession.getUpdatedAt(), is(now));
  }
}