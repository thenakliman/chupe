package org.thenakliman.chupe.services;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thenakliman.chupe.common.utils.Converter;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.ActionItem;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.dto.UpsertMeetingDiscussionItemDTO;
import org.thenakliman.chupe.exceptions.NotFoundException;
import org.thenakliman.chupe.models.ActionItemStatus;
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

  public MeetingDTO updateMeeting(Long meetingId, String subject, String createdBy) {
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
      UpsertMeetingDiscussionItemDTO upsertMeetingDiscussionItemDTO) {

    MeetingDiscussionItem meetingDiscussionItem = converter.convertToObject(
        upsertMeetingDiscussionItemDTO,
        MeetingDiscussionItem.class);

    meetingDiscussionItem.setCreatedBy(User.builder().userName(createdBy).build());
    MeetingDiscussionItem savedDiscussionItem = meetingDiscussionItemRepository.save(meetingDiscussionItem);
    return converter.convertToObject(savedDiscussionItem, MeetingDiscussionItemDTO.class);
  }

  public MeetingDiscussionItemDTO updateMeetingDiscussionItem(
      Long discussionItemId,
      UpsertMeetingDiscussionItemDTO upsertMeetingDiscussionItemDTO) {

    Optional<MeetingDiscussionItem> discussionItemOptional = meetingDiscussionItemRepository.findByIdAndMeetingId(
        discussionItemId,
        upsertMeetingDiscussionItemDTO.getMeetingId());

    MeetingDiscussionItem discussionItem = discussionItemOptional.orElseThrow(() -> new NotFoundException(
        String.format("Meeting discussion item with id %s does not exist", discussionItemId)));

    discussionItem.setDiscussionItem(upsertMeetingDiscussionItemDTO.getDiscussionItem());
    discussionItem.setDiscussionItemType(upsertMeetingDiscussionItemDTO.getDiscussionItemType());
    User assignedTo = getUser(upsertMeetingDiscussionItemDTO.getAssignedTo());
    discussionItem.setAssignedTo(assignedTo);
    discussionItem.setUpdatedAt(dateUtil.getTime());
    MeetingDiscussionItem savedDiscussionItem = meetingDiscussionItemRepository.save(discussionItem);
    return converter.convertToObject(savedDiscussionItem, MeetingDiscussionItemDTO.class);
  }

  List<ActionItem> getMeetingActionItem(String username) {
    List<MeetingDiscussionItem> meetingDiscussionItems = meetingDiscussionItemRepository
        .findByAssignedToUserNameAndStatusIn(username, asList(ActionItemStatus.IN_PROGRESS, ActionItemStatus.CREATED));

    return converter.convertToListOfObjects(meetingDiscussionItems, ActionItem.class);
  }

  private User getUser(String username) {
    return User
        .builder()
        .userName(username)
        .build();
  }
}
