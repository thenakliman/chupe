package org.thenakliman.chupe.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.config.TokenAuthenticationService;
import org.thenakliman.chupe.dto.CreateMeetingDiscussionItemDTO;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.dto.User;
import org.thenakliman.chupe.models.DiscussionItemType;
import org.thenakliman.chupe.services.MeetingService;
import org.thenakliman.chupe.services.TokenService;

@WebMvcTest(controllers = MeetingController.class, secure = false)
@RunWith(SpringRunner.class)
public class MeetingControllerTest {
  final private String username = "user-name";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext webApplicationContext;
  @MockBean
  private MeetingService meetingService;
  @Autowired
  private Jackson2ObjectMapperBuilder jacksonBuilder;
  @MockBean
  private TokenService tokenService;
  @MockBean
  private TokenAuthenticationService tokenAuthenticationService;
  private Authentication authToken;
  private ObjectMapper objectMapper;

  /**
   * Setup web application context.
   */
  @Before()
  public void testSetup() {
    objectMapper = jacksonBuilder.build();
    /* NOTE(thenakliman) this approach has been used to bypass authentication
     * layer from the test. It creates a standalone application with single
     * controller(QuestionController).
     * */
    authToken = new UsernamePasswordAuthenticationToken(
        User.builder().username(username).build(),
        null,
        null);

    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void shouldCreateMeeting() throws Exception {
    MeetingDTO meetingDTO = new MeetingDTO();
    meetingDTO.setId(101L);
    meetingDTO.setCreatedBy(username);
    meetingDTO.setSubject("new - subject");
    BDDMockito.given(meetingService.createMeeting("subject", username)).willReturn(meetingDTO);

    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/meetings")
        .contentType(MediaType.APPLICATION_JSON)
        .content("subject"))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    MeetingDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), MeetingDTO.class);

    assertThat(meetingDTO, samePropertyValuesAs(result));
  }

  @Test
  public void shouldReturnMeetings() throws Exception {
    MeetingDTO meetingDTO1 = new MeetingDTO();
    meetingDTO1.setId(101L);
    MeetingDTO meetingDTO2 = new MeetingDTO();
    meetingDTO1.setId(102L);
    List<MeetingDTO> meetingDTOs = Arrays.asList(meetingDTO1, meetingDTO2);
    BDDMockito.given(meetingService.getMeetings(username)).willReturn(meetingDTOs);
    SecurityContextHolder.getContext().setAuthentication(authToken);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/meetings"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<MeetingDTO> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new ArrayList<MeetingDTO>().getClass());

    assertThat(result, hasSize(2));
  }

  @Test
  public void shouldUpdateMeeting() throws Exception {
    Long id = 10L;
    when(meetingService.updateMeeting(id, "subject", username)).thenReturn(new MeetingDTO());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/meetings/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content("new subject")
    ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }

  @Test
  public void shouldCreateMeetingDiscussionItem() throws Exception {
    CreateMeetingDiscussionItemDTO createDiscussionItemDTO = new CreateMeetingDiscussionItemDTO();
    createDiscussionItemDTO.setDiscussionItem("discussion-item");
    createDiscussionItemDTO.setAssignedTo("lal");
    createDiscussionItemDTO.setMeetingId(102L);
    createDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.INFORMATION);

    MeetingDiscussionItemDTO discussionItemDTO = new MeetingDiscussionItemDTO();
    discussionItemDTO.setId(192L);
    BDDMockito.given(meetingService.createMeetingDiscussionItem(username, createDiscussionItemDTO))
        .willReturn(discussionItemDTO);

    SecurityContextHolder.getContext().setAuthentication(authToken);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/meeting-discussion-items")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(createDiscussionItemDTO)))
        .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

    MeetingDiscussionItemDTO result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), MeetingDiscussionItemDTO.class);

    assertThat(result.getId(), is(192L));
  }

  @Test
  public void shouldReturnMeetingDiscussionItems() throws Exception {
    MeetingDiscussionItemDTO meetingDTO1 = new MeetingDiscussionItemDTO();
    meetingDTO1.setId(101L);
    MeetingDiscussionItemDTO meetingDTO2 = new MeetingDiscussionItemDTO();
    meetingDTO1.setId(102L);
    List<MeetingDiscussionItemDTO> discussionItemDTOS = Arrays.asList(meetingDTO1, meetingDTO2);
    BDDMockito.given(meetingService.getMeetingDiscussionItems(101L)).willReturn(discussionItemDTOS);
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
        .get("/api/v1/meeting-discussion-items?meetingId=101"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    List<MeetingDiscussionItemDTO> result = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new ArrayList<MeetingDiscussionItemDTO>().getClass());

    assertThat(result, hasSize(2));
  }

  @Test
  public void shouldUpdateMeetingDiscussionItem() throws Exception {
    Long id = 10L;
    CreateMeetingDiscussionItemDTO createDiscussionItemDTO = new CreateMeetingDiscussionItemDTO();
    createDiscussionItemDTO.setDiscussionItem("discussion-item");
    createDiscussionItemDTO.setAssignedTo("lal");
    createDiscussionItemDTO.setMeetingId(102L);
    createDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.INFORMATION);
    when(meetingService.updateMeetingDiscussionItem(id, createDiscussionItemDTO)).thenReturn(new MeetingDiscussionItemDTO());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    mockMvc.perform(MockMvcRequestBuilders
        .put("/api/v1/meeting-discussion-items/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(createDiscussionItemDTO))
    ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
  }
}