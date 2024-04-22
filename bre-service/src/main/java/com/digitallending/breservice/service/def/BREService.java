package com.digitallending.breservice.service.def;


import com.digitallending.breservice.model.dto.BREParamTypeResponseDTO;
import com.digitallending.breservice.model.dto.BREParamAndSubParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREParamWeightRequestDTO;
import com.digitallending.breservice.model.dto.BREParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREResultResponseDTO;
import com.digitallending.breservice.model.dto.BREScoreMatrixRequestDTO;
import com.digitallending.breservice.model.dto.BREScoreMatrixResponseDTO;
import com.digitallending.breservice.model.dto.BRESubParamTypeForSanctionConditionResponseDTO;
import com.digitallending.breservice.model.dto.BRESubParamWeightRequestDTO;
import com.digitallending.breservice.model.dto.externalservice.loanservice.LoanApplicationRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeResponseDTO;
import com.digitallending.breservice.model.dto.SanctionConditionValueRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionValueResponseDTO;
import com.digitallending.breservice.model.dto.externalservice.loanservice.UserRequestDTO;
import com.digitallending.breservice.model.entity.BREParamType;

import java.util.List;
import java.util.UUID;

public interface BREService {
    List<BREParamTypeResponseDTO> getParamTypesByLoanTypeId(UUID loanTypeId);
    List<BREParamWeightResponseDTO> saveBREParamWeights(List<BREParamWeightRequestDTO> breParamWeightRequestDTOList,UUID loanProductId);
    List<BREParamType> getParamsAndSubParamsTypeByLoanProductId(UUID loanProductId);
    BREParamAndSubParamWeightResponseDTO saveBRESubParamWeights(List<BRESubParamWeightRequestDTO> breSubParamWeightRequestDTOList, UUID paramWeightId);
    BRESubParamTypeForSanctionConditionResponseDTO getSubParamTypeForSanctionCondition(UUID loanTypeId);
    List<SanctionConditionRangeResponseDTO> saveSanctionConditionRange(List<SanctionConditionRangeRequestDTO> sanctionConditionRangeRequestDTOList, UUID loanProductId);
    List<SanctionConditionValueResponseDTO> saveSanctionConditionValue(List<SanctionConditionValueRequestDTO> sanctionConditionValueRequestDTOList, UUID loanProductId);
    List<BREScoreMatrixResponseDTO> saveScoreMatrix(List<BREScoreMatrixRequestDTO> breScoreMatrixRequestDTOList, UUID loanProductId);
    List<BREResultResponseDTO> runBRE(UserRequestDTO userRequestDTO, LoanApplicationRequestDTO loanApplicationRequestDTO, List<UUID> loanProductIdList);

}
