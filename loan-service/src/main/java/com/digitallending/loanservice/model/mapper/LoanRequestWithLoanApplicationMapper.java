package com.digitallending.loanservice.model.mapper;

import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseDto;
import com.digitallending.loanservice.model.dto.loanrequest.LoanRequestResponseWithLoanApplicationDto;
import com.digitallending.loanservice.model.enums.LoanApplicationProcessStage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = LoanRequestMapper.class)
public interface LoanRequestWithLoanApplicationMapper {

    LoanRequestResponseWithLoanApplicationDto toLoanRequestResponseWithLoanApplicationDto
            (
                    LoanRequestResponseDto loanRequestResponseDto,
                    LoanApplicationProcessStage loanApplicationProcessStage,
                    String loanApplicationName,
                    String MSMEUserName,
                    Long amount,
                    Long tenure
            );
}
