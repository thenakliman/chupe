package org.thenakliman.chupe.services;

import java.util.List;
import java.util.Optional;

import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.CreateMeetingDiscussionItemDTO;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.models.Meeting;
import org.thenakliman.chupe.models.MeetingDiscussionItem;
import org.thenakliman.chupe.models.User;
import org.thenakliman.chupe.repositories.MeetingDiscussionItemRepository;
import org.thenakliman.chupe.repositories.MeetingRepository;

@Service
public class MeetingService {
  private MeetingRepository meetingRepository;
  private MeetingDiscussionItemRepository meetingDiscussionItemRepository;
  private DateUtil dateUtil;
  private Converter converter;

  @Autowired
  public MeetingService(MeetingRepository meetingRepository,
                        MeetingDiscussionItemRepository meetingDiscussionItemRepository,
                        ModelMapper modelMapper,
                        DateUtil dateUtil,
                        Converter converter) {

    this.meetingRepository = meetingRepository;
    this.meetingDiscussionItemRepository = meetingDiscussionItemRepository;
    this.dateUtil = dateUtil;
    this.converter = converter;
  }

  public MeetingDTO createMeeting(String subject, String createdBy) {
    User createdByUser = getUser(createdBy);
    Meeting meeting = Meeting
        .builder()
        .subject(subject)
        .createdBy(createdByUser)
        .createdAt(dateUtil.getTime())
        .updatedAt(dateUtil.getTime())
        .build();

    Meeting createdMeeting = meetingRepository.save(meeting);
    return converter.convertToObject(createdMeeting, MeetingDTO.class);
  }

  public List<MeetingDTO> getMeetings() {
    List<Meeting> meetings = meetingRepository.findAll();
    return converter.convertToListOfObjects(meetings, MeetingDTO.class);
  }

  public MeetingDTO updateMeeting(Long meetingId, String subject, String createdBy) throws NotFoundException {
    Optional<Meeting> meetingOptional = meetingRepository.findByIdAndCreatedByUserName(meetingId, createdBy);
    Meeting meeting = meetingOptional.orElseThrow(() -> new NotFoundException(
        String.format("Meeting with id %s does not exist", meetingId)));

    meeting.setSubject(subject);
    meeting.setUpdatedAt(dateUtil.getTime());
    Meeting updatedMeeting = meetingRepository.save(meeting);
    return converter.convertToObject(updatedMeeting, MeetingDTO.class);
  }

  public List<MeetingDiscussionItemDTO> getMeetingDiscussionItems(Long meetingId) {
    List<MeetingDiscussionItem> meetingDiscussionItems = meetingDiscussionItemRepository.findByMeetingId(meetingId);
    return converter.convertToListOfObjects(meetingDiscussionItems, MeetingDiscussionItemDTO.class);
  }

  public MeetingDiscussionItemDTO createMeetingDiscussionItem(
      String createdBy,
      CreateMeetingDiscussionItemDTO createMeetingDiscussionItemDTO) {

    MeetingDiscussionItem meetingDiscussionItem = converter.convertToObject(
        createMeetingDiscussionItemDTO,
        MeetingDiscussionItem.class);

    meetingDiscussionItem.setCreatedBy(User.builder().userName(createdBy).build());
    MeetingDiscussionItem savedDiscussionItem = meetingDiscussionItemRepository.save(meetingDiscussionItem);
    return converter.convertToObject(savedDiscussionItem, MeetingDiscussionItemDTO.class);
  }

  public MeetingDiscussionItemDTO updateMeetingDiscussionItem(
      Long discussionItemId,
      CreateMeetingDiscussionItemDTO createMeetingDiscussionItemDTO) throws NotFoundException {

    Optional<MeetingDiscussionItem> discussionItemOptional = meetingDiscussionItemRepository.findByIdAndMeetingId(
        discussionItemId,
        createMeetingDiscussionItemDTO.getMeetingId());

    MeetingDiscussionItem discussionItem = discussionItemOptional.orElseThrow(() -> new NotFoundException(
        String.format("Meeting discussion item with id %s does not exist", discussionItemId)));

    discussionItem.setDiscussionItem(createMeetingDiscussionItemDTO.getDiscussionItem());
    discussionItem.setDiscussionItemType(createMeetingDiscussionItemDTO.getDiscussionItemType());
    User assignedTo = getUser(createMeetingDiscussionItemDTO.getAssignedTo());
    discussionItem.setAssignedTo(assignedTo);
    discussionItem.setUpdatedAt(dateUtil.getTime());
    MeetingDiscussionItem savedDiscussionItem = meetingDiscussionItemRepository.save(discussionItem);
    return converter.convertToObject(savedDiscussionItem, MeetingDiscussionItemDTO.class);
  }

  private User getUser(String username) {
    return User
        .builder()
        .userName(username)
        .build();
  }
}
