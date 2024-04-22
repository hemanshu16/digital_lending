package com.digitallending.breservice.controller;

import com.digitallending.breservice.model.dto.BREParamTypeResponseDTO;
import com.digitallending.breservice.model.dto.BREParamAndSubParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREParamWeightRequestDTO;
import com.digitallending.breservice.model.dto.BREParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREResultResponseDTO;
import com.digitallending.breservice.model.dto.BREScoreMatrixRequestDTO;
import com.digitallending.breservice.model.dto.BREScoreMatrixResponseDTO;
import com.digitallending.breservice.model.dto.BRESubParamTypeForSanctionConditionResponseDTO;
import com.digitallending.breservice.model.dto.BRESubParamWeightRequestDTO;
import com.digitallending.breservice.model.dto.BREConfigRequestDTO;
import com.digitallending.breservice.model.dto.externalservice.loanservice.RunBRERequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeResponseDTO;
import com.digitallending.breservice.model.dto.SanctionConditionValueRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionValueResponseDTO;
import com.digitallending.breservice.model.dto.apiresponse.APIResponse;
import com.digitallending.breservice.model.entity.BREParamType;
import com.digitallending.breservice.service.impl.BREServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/bre")
public class BREController {

    @Autowired
    private BREServiceImpl configureBREService;

    @GetMapping("get-param-types/{loanTypeId}")
    public ResponseEntity<APIResponse<List<BREParamTypeResponseDTO>>> getParamTypesByLoanTypeId(@PathVariable UUID loanTypeId)
    {
        return new ResponseEntity<>(APIResponse.<List<BREParamTypeResponseDTO>>builder().payload(configureBREService.getParamTypesByLoanTypeId(loanTypeId)).build(), HttpStatus.OK);
    }

    @PostMapping("save-param-weights")
    public ResponseEntity<APIResponse<List<BREParamWeightResponseDTO>>> saveParamWeights(
            @RequestBody
            @Valid
            BREConfigRequestDTO<BREParamWeightRequestDTO> breConfigRequestDTO)
    {
        return new ResponseEntity<>(APIResponse.<List<BREParamWeightResponseDTO>>builder().payload(configureBREService.saveBREParamWeights(breConfigRequestDTO.getData(),breConfigRequestDTO.getId())).build(), HttpStatus.CREATED);
    }

    @GetMapping("get-sub-params/{loanProductId}")
    public ResponseEntity<APIResponse<List<BREParamType>>> getParamsAndSubParamsTypeByLoanProductId(@PathVariable UUID loanProductId)
    {
        return new ResponseEntity<>(APIResponse.<List<BREParamType>>builder().payload(configureBREService.getParamsAndSubParamsTypeByLoanProductId(loanProductId)).build(), HttpStatus.OK);
    }

    @PostMapping("save-sub-param-weights")
    public ResponseEntity<APIResponse<BREParamAndSubParamWeightResponseDTO>> saveSubParamWeights(
                @RequestBody
                @Valid
                BREConfigRequestDTO<BRESubParamWeightRequestDTO> breConfigRequestDTO)
    {
        return new ResponseEntity<>(APIResponse.<BREParamAndSubParamWeightResponseDTO>builder().payload(configureBREService.saveBRESubParamWeights(breConfigRequestDTO.getData(),breConfigRequestDTO.getId())).build(), HttpStatus.CREATED);
    }

    @GetMapping("get-sub-params-for-sanction-condition/{loanTypeId}")
    public ResponseEntity<APIResponse<BRESubParamTypeForSanctionConditionResponseDTO>> getSubParamTypeValueForSanctionCondition(@PathVariable UUID loanTypeId)
    {
        return new ResponseEntity<>(APIResponse.<BRESubParamTypeForSanctionConditionResponseDTO>builder().payload(configureBREService.getSubParamTypeForSanctionCondition(loanTypeId)).build(), HttpStatus.OK);
    }

    @PostMapping("save-sanction-condition-range")
    public ResponseEntity<APIResponse<List<SanctionConditionRangeResponseDTO>>> saveSanctionConditionRange(
            @RequestBody
            @Valid
            BREConfigRequestDTO<SanctionConditionRangeRequestDTO> breConfigRequestDTO)
    {
        return new ResponseEntity<>(APIResponse.<List<SanctionConditionRangeResponseDTO>>builder().payload(configureBREService.saveSanctionConditionRange(breConfigRequestDTO.getData(),breConfigRequestDTO.getId())).build(), HttpStatus.CREATED);
    }

    @PostMapping("save-sanction-condition-value")
    public ResponseEntity<APIResponse<List<SanctionConditionValueResponseDTO>>> saveSanctionConditionValue(
            @RequestBody
            @Valid
            BREConfigRequestDTO<SanctionConditionValueRequestDTO> breConfigRequestDTO)
    {
        return new ResponseEntity<>(APIResponse.<List<SanctionConditionValueResponseDTO>>builder().payload(configureBREService.saveSanctionConditionValue(breConfigRequestDTO.getData(),breConfigRequestDTO.getId())).build(), HttpStatus.CREATED);
    }

    @PostMapping("save-score-matrix")
    public ResponseEntity<APIResponse<List<BREScoreMatrixResponseDTO>>> saveScoreMatrix(
            @RequestBody
            @Valid
            BREConfigRequestDTO<BREScoreMatrixRequestDTO> breConfigRequestDTO)
    {
        return new ResponseEntity<>(APIResponse.<List<BREScoreMatrixResponseDTO>>builder().payload(configureBREService.saveScoreMatrix(breConfigRequestDTO.getData(),breConfigRequestDTO.getId())).build(), HttpStatus.CREATED);
    }

    @PostMapping("run")
    public ResponseEntity<APIResponse<List<BREResultResponseDTO>>> runBRE(@RequestBody @Valid RunBRERequestDTO runBRERequestDTO)
    {
        return new ResponseEntity<>(APIResponse.<List<BREResultResponseDTO>>builder().payload(configureBREService.runBRE(runBRERequestDTO.getUserRequestDTO(),runBRERequestDTO.getLoanApplicationRequestDTO(),runBRERequestDTO.getLoanProductIdList())).build(), HttpStatus.OK);
    }

}
