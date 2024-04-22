package com.digitallending.loanservice.model.mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.digitallending.loanservice.model.dto.LoanTypeResponseDto;
import com.digitallending.loanservice.model.entity.LoanType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {

    List<LoanTypeResponseDto> loanTypeListToloanTypeResponseDtoList(List<LoanType> loanTypeList);
}
