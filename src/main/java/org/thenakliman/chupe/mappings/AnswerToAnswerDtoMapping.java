package org.thenakliman.chupe.mappings;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.dto.AnswerDTO;
import org.thenakliman.chupe.models.Answer;

@Component
public class AnswerToAnswerDtoMapping extends PropertyMapConfigurerSupport<Answer, AnswerDTO> {

  @Override
  public PropertyMap<Answer, AnswerDTO> mapping() {

    return new PropertyMap<Answer, AnswerDTO>() {
      @Override
      protected void configure() {
        map().setAnsweredBy(source.getAnsweredBy().getUserName());
      }
    };
  }
}
