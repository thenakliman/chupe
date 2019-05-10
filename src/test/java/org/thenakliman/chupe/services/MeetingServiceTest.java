package org.thenakliman.chupe.services;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.CreateMeetingDiscussionItemDTO;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.DiscussionItemType;
import org.thenakliman.chupe.models.Meeting;
import org.thenakliman.chupe.models.MeetingDiscussionItem;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.MeetingDiscussionItemRepository;
import org.thenakliman.chupe.repositories.MeetingRepository;

@RunWith(MockitoJUnitRunner.class)
public class MeetingServiceTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();
  @Mock
  private MeetingRepository meetingRepository;
  @Mock
  private MeetingDiscussionItemRepository meetingDiscussionItemRepository;
  @Mock
  private DateUtil dateUtil;
  @Mock
  private Converter converter;
  @InjectMocks
  private MeetingService meetingService;

  private Date now;

  @Before
  public void setUp() {
    now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
  }

  @Test
  public void shouldCreateMeeting() {
    Meeting savedMeeting = new Meeting();
    Date now = new Date();
    when(dateUtil.getTime()).thenReturn(now);
    String subject = "meeting";
    String userName = "created-by";
    Meeting meeting = Meeting
        .builder()
        .updatedAt(now)
        .createdAt(now)
        .subject(subject)
        .createdBy(User.builder().userName(userName).build())
        .build();

    when(meetingRepository.save(meeting)).thenReturn(savedMeeting);
    MeetingDTO savedMeetingDto = new MeetingDTO();
    when(converter.convertToObject(savedMeeting, MeetingDTO.class)).thenReturn(savedMeetingDto);
    MeetingDTO meetingDto = meetingService.createMeeting(subject, userName);

    assertThat(savedMeetingDto, is(meetingDto));
  }

  @Test
  public void shouldReturnMeetings() {
    String createdBy = "created-by";
    List<Meeting> meetings = Arrays.asList(new Meeting(), new Meeting());
    when(meetingRepository.findAll()).thenReturn(meetings);
    MeetingDTO meetingDTO1 = new MeetingDTO();
    meetingDTO1.setId(10L);
    MeetingDTO meetingDTO2 = new MeetingDTO();
    when(converter.convertToListOfObjects(meetings, MeetingDTO.class))
        .thenReturn(Arrays.asList(meetingDTO1, meetingDTO2));

    List<MeetingDTO> meetingsDtos = meetingService.getMeetings();
    assertThat(meetingsDtos, hasSize(2));
    assertThat(meetingsDtos, hasItems(meetingDTO1, meetingDTO2));
  }

  @Test
  public void shouldUpdateMeeting() throws NotFoundException {
    String createdBy = "created-by";
    Meeting meeting = getMeeting("subject", createdBy);
    long meetingId = 10L;
    when(meetingRepository.findByIdAndCreatedByUserName(meetingId, createdBy)).thenReturn(Optional.of(meeting));
    String newSubject = "new-subject";
    Meeting updatedMeeting = getMeeting(newSubject, createdBy);
    updatedMeeting.setUpdatedAt(now);
    when(meetingRepository.save(updatedMeeting)).thenReturn(updatedMeeting);
    MeetingDTO updatedMeetingDto = new MeetingDTO();
    when(converter.convertToObject(updatedMeeting, MeetingDTO.class)).thenReturn(updatedMeetingDto);

    MeetingDTO meetingDto = meetingService.updateMeeting(meetingId, newSubject, createdBy);

    assertThat(updatedMeetingDto, is(meetingDto));
    verify(meetingRepository).save(updatedMeeting);
  }

  @Test
  public void shouldThrowNotFoundExceptionWhenMeetingIdDoesNotExist() throws NotFoundException {
    long meetingId = 10L;
    String createdBy = "created-by";
    when(meetingRepository.findByIdAndCreatedByUserName(meetingId, createdBy)).thenReturn(Optional.empty());

    exception.expect(NotFoundException.class);
    meetingService.updateMeeting(meetingId, "subject", createdBy);

  }

  @Test
  public void getMeetingDiscussionItems() {
    Long meetingId = 10L;
    List<MeetingDiscussionItem> meetingDiscussionItems = Arrays.asList(
        mock(MeetingDiscussionItem.class),
        mock(MeetingDiscussionItem.class));
    when(meetingDiscussionItemRepository.findByMeetingId(meetingId)).thenReturn(meetingDiscussionItems);
    MeetingDiscussionItemDTO meetingDiscussionItemDTO1 = mock(MeetingDiscussionItemDTO.class);
    MeetingDiscussionItemDTO meetingDiscussionItemDTO2 = mock(MeetingDiscussionItemDTO.class);
    List<MeetingDiscussionItemDTO> meetingDiscussionItemDTOS = Arrays.asList(
        meetingDiscussionItemDTO1,
        meetingDiscussionItemDTO2
    );

    when(converter.convertToListOfObjects(meetingDiscussionItems, MeetingDiscussionItemDTO.class))
        .thenReturn(meetingDiscussionItemDTOS);

    List<MeetingDiscussionItemDTO> receivedMeetingDiscussionItem = meetingService.getMeetingDiscussionItems(meetingId);

    assertThat(receivedMeetingDiscussionItem, hasItems(meetingDiscussionItemDTO1, meetingDiscussionItemDTO2));
  }

  @Test
  public void shouldCreateDiscussionItem() {
    MeetingDiscussionItem discussionItem = new MeetingDiscussionItem();
    CreateMeetingDiscussionItemDTO createDiscussionItem = new CreateMeetingDiscussionItemDTO();
    when(converter.convertToObject(createDiscussionItem, MeetingDiscussionItem.class)).thenReturn(discussionItem);
    MeetingDiscussionItem discussionItemToBeSaved = new MeetingDiscussionItem();
    discussionItemToBeSaved.setCreatedBy(User.builder().userName("created-by").build());
    MeetingDiscussionItem savedDiscussionItem = mock(MeetingDiscussionItem.class);
    when(meetingDiscussionItemRepository.save(discussionItemToBeSaved)).thenReturn(savedDiscussionItem);
    MeetingDiscussionItemDTO savedDiscussionItemDto = mock(MeetingDiscussionItemDTO.class);
    when(converter.convertToObject(savedDiscussionItem, MeetingDiscussionItemDTO.class)).thenReturn(savedDiscussionItemDto);

    MeetingDiscussionItemDTO discussionItemDTO = meetingService.createMeetingDiscussionItem("created-by", createDiscussionItem);

    verify(meetingDiscussionItemRepository).save(discussionItem);
    assertThat(discussionItemDTO, is(savedDiscussionItemDto));
  }

  @Test
  public void shouldThrowNotFoundExceptionIfDiscussionItemDoesNotExist() throws NotFoundException {
    exception.expect(NotFoundException.class);
    meetingService.updateMeetingDiscussionItem(20L, new CreateMeetingDiscussionItemDTO());
  }

  @Test
  public void shouldUpdateDiscussionItem() throws NotFoundException {
    CreateMeetingDiscussionItemDTO createMeetingDiscussionItemDTO = new CreateMeetingDiscussionItemDTO();
    createMeetingDiscussionItemDTO.setAssignedTo("lal");
    createMeetingDiscussionItemDTO.setMeetingId(101L);
    String discussionItem = "Discussion item";
    createMeetingDiscussionItemDTO.setDiscussionItem(discussionItem);
    createMeetingDiscussionItemDTO.setDiscussionItemType(DiscussionItemType.INFORMATION);
    when(meetingDiscussionItemRepository.findByIdAndMeetingId(10L, 101L)).thenReturn(Optional.of(new MeetingDiscussionItem()));

    MeetingDiscussionItem meetingDiscussionItem = new MeetingDiscussionItem();
    meetingDiscussionItem.setDiscussionItem(discussionItem);
    meetingDiscussionItem.setAssignedTo(User.builder().userName("lal").build());
    meetingDiscussionItem.setUpdatedAt(now);
    meetingDiscussionItem.setDiscussionItemType(DiscussionItemType.INFORMATION);
    MeetingDiscussionItem savedDiscussionItem = mock(MeetingDiscussionItem.class);
    when(meetingDiscussionItemRepository.save(meetingDiscussionItem)).thenReturn(savedDiscussionItem);
    MeetingDiscussionItemDTO meetingDiscussionItemDTO = mock(MeetingDiscussionItemDTO.class);
    when(converter.convertToObject(savedDiscussionItem, MeetingDiscussionItemDTO.class)).thenReturn(meetingDiscussionItemDTO);

    MeetingDiscussionItemDTO discussionItemDTO = meetingService.updateMeetingDiscussionItem(10L, createMeetingDiscussionItemDTO);

    assertThat(discussionItemDTO, is(meetingDiscussionItemDTO));
    verify(meetingDiscussionItemRepository).save(meetingDiscussionItem);
  }

  private Meeting getMeeting(String subject, String createdBy) {
    return Meeting
        .builder()
        .subject(subject)
        .createdBy(User.builder().userName(createdBy).build())
        .build();
  }
}
