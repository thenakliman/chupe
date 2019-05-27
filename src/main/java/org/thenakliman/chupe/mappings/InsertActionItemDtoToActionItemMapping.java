package org.thenakliman.chupe.mappings;

import static java.util.Objects.isNull;

import com.github.jmnarloch.spring.boot.modelmapper.ConverterConfigurerSupport;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.thenakliman.chupe.common.utils.DateUtil;
import org.thenakliman.chupe.dto.InsertActionItemDTO;
import org.thenakliman.chupe.models.ActionItem;
import org.thenakliman.chupe.models.ActionItemStatus;
import org.thenakliman.chupe.models.Retro;
import org.thenakliman.chupe.models.RetroPoint;
import org.thenakliman.chupe.models.User;

@Component
public class InsertActionItemDtoToActionItemMapping
    extends ConverterConfigurerSupport<InsertActionItemDTO, ActionItem> {

  private final DateUtil dateUtil;

  InsertActionItemDtoToActionItemMapping(DateUtil dateUtil) {
    this.dateUtil = dateUtil;
  }

  @Override
  public Converter<InsertActionItemDTO, ActionItem> converter() {

    return new AbstractConverter<InsertActionItemDTO, ActionItem>() {
      @Override
      protected ActionItem convert(InsertActionItemDTO source) {
        RetroPoint retroPoint = null;
        if (!isNull(source.getRetroPointId())) {
          retroPoint = RetroPoint.builder().id(source.getRetroPointId()).build();
        }

        return ActionItem
            .builder()
            .assignedTo(User.builder().userName(source.getAssignedTo()).build())
            .deadlineToAct(source.getDeadlineToAct())
            .description(source.getDescription())
            .retro(Retro.builder().id(source.getRetroId()).build())
            .retroPoint(retroPoint)
            .status(ActionItemStatus.CREATED)
            .createdAt(dateUtil.getTime())
            .updatedAt(dateUtil.getTime())
            .build();
      }
    };
  }
}
