package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.QuestionDTO;
import org.thenakliman.chupe.models.Question;

@Component
public class QuestionToQuestionDtoMapping extends PropertyMapConfigurerSupport<Question, QuestionDTO> {

  @Override
  public PropertyMap<Question, QuestionDTO> mapping() {

    return new PropertyMap<Question, QuestionDTO>() {
      @Override
      protected void configure() {
        map().setAssignedTo(source.getAssignedTo().getUserName());
        map().setOwner(source.getOwner().getUserName());
      }
    };
  }
}
