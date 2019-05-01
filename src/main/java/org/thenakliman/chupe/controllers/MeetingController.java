package org.thenakliman.chupe.controllers;

import java.util.List;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.thenakliman.chupe.dto.CreateMeetingDiscussionItemDTO;
import org.thenakliman.chupe.dto.MeetingDTO;
import org.thenakliman.chupe.dto.MeetingDiscussionItemDTO;
import org.thenakliman.chupe.services.MeetingService;

@Controller
public class MeetingController extends BaseController {
  @Autowired
  private MeetingService meetingService;

  @GetMapping("/meetings")
  public ResponseEntity<List<MeetingDTO>> getMeetings() {
    List<MeetingDTO> meetings = meetingService.getMeetings(getRequestUsername());
    return new ResponseEntity<>(meetings, HttpStatus.OK);
  }

  @PostMapping("/meetings")
  public ResponseEntity<MeetingDTO> createMeeting(@RequestBody String subject) {
    MeetingDTO createdMeeting = meetingService.createMeeting(subject, getRequestUsername());
    return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
  }

  @PutMapping("/meetings/{meetingId}")
  public ResponseEntity<MeetingDTO> updateMeeting(@PathVariable Long meetingId,
                                                  @RequestBody String subject) throws NotFoundException {

    MeetingDTO updatedMeeting = meetingService.updateMeeting(meetingId, subject, getRequestUsername());
    return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
  }

  @GetMapping("/meeting-discussion-items")
  public ResponseEntity<List<MeetingDiscussionItemDTO>> getMeetingDiscussionItems(@RequestParam Long meetingId) {
    List<MeetingDiscussionItemDTO> meetings = meetingService.getMeetingDiscussionItems(meetingId);
    return new ResponseEntity<>(meetings, HttpStatus.OK);
  }

  @PostMapping("/meeting-discussion-items")
  public ResponseEntity<MeetingDiscussionItemDTO> createMeetingDiscussionItems(
      @RequestBody CreateMeetingDiscussionItemDTO createMeetingDiscussionItemDTO) {

    MeetingDiscussionItemDTO createdMeeting = meetingService.createMeetingDiscussionItem(
        getRequestUsername(),
        createMeetingDiscussionItemDTO);

    return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
  }

  @PutMapping("/meeting-discussion-items/{meetingDiscussionItemId}")
  public ResponseEntity<MeetingDiscussionItemDTO> updateMeetingDiscussionItem(
      @PathVariable Long meetingDiscussionItemId,
      @RequestBody CreateMeetingDiscussionItemDTO createMeetingDiscussionItemDTO) throws NotFoundException {

    MeetingDiscussionItemDTO updatedMeeting = meetingService.updateMeetingDiscussionItem(
        meetingDiscussionItemId,
        createMeetingDiscussionItemDTO);
    return new ResponseEntity<>(updatedMeeting, HttpStatus.OK);
  }
}
