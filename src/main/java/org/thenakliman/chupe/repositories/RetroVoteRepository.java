package org.thenakliman.chupe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.RetroVote;
import org.thenakliman.chupe.models.User;


@Repository
public interface RetroVoteRepository extends JpaRepository<RetroVote, Long> {
  RetroVote findByRetroPointAndVotedBy(RetroPoint retroPoint, User votedBy);

  List<RetroVote> findByVotedBy(User votedBy);
}
