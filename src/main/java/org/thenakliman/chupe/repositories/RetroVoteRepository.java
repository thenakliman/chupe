package org.thenakliman.chupe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thenakliman.chupe.models.RetroVote;


@Repository
public interface RetroVoteRepository extends JpaRepository<RetroVote, Long> {
  RetroVote findByRetroPointIdAndVotedByUserName(Long retroPointId, String votedBy);

  Long countByVotedByUserNameAndRetroPointRetroId(String username, Long retroId);

  Long countByRetroPointId(Long retroPointId);
}
