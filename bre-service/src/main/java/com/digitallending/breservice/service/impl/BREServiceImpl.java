package com.digitallending.breservice.service.impl;

import com.digitallending.breservice.exception.AlreadyExistException;
import com.digitallending.breservice.exception.EmptyPayloadException;
import com.digitallending.breservice.exception.ExternalServiceException;
import com.digitallending.breservice.exception.InvalidParameterException;
import com.digitallending.breservice.exception.InvalidRangeException;
import com.digitallending.breservice.exception.InvalidValueException;
import com.digitallending.breservice.exception.InvalidWeightException;
import com.digitallending.breservice.exception.NotAllowedException;
import com.digitallending.breservice.exception.NotFoundException;
import com.digitallending.breservice.model.dto.BREParamTypeResponseDTO;
import com.digitallending.breservice.model.dto.BREParamAndSubParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREParamWeightRequestDTO;
import com.digitallending.breservice.model.dto.BREParamWeightResponseDTO;
import com.digitallending.breservice.model.dto.BREResultResponseDTO;
import com.digitallending.breservice.model.dto.BREScoreMatrixRequestDTO;
import com.digitallending.breservice.model.dto.BREScoreMatrixResponseDTO;
import com.digitallending.breservice.model.dto.BRESubParamRangeRequestDTO;
import com.digitallending.breservice.model.dto.BRESubParamTypeForSanctionConditionResponseDTO;
import com.digitallending.breservice.model.dto.BRESubParamValueRequestDTO;
import com.digitallending.breservice.model.dto.BRESubParamWeightRequestDTO;
import com.digitallending.breservice.model.dto.externalservice.loanservice.LoanApplicationRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionRangeResponseDTO;
import com.digitallending.breservice.model.dto.SanctionConditionValueRequestDTO;
import com.digitallending.breservice.model.dto.SanctionConditionValueResponseDTO;
import com.digitallending.breservice.model.dto.externalservice.loanservice.UserRequestDTO;
import com.digitallending.breservice.model.dto.VerifyRangeConstraintDTO;
import com.digitallending.breservice.model.dto.apiresponse.APIResponse;
import com.digitallending.breservice.model.entity.BREParamType;
import com.digitallending.breservice.model.entity.BREParamWeight;
import com.digitallending.breservice.model.entity.BREScoreMatrix;
import com.digitallending.breservice.model.entity.BRESubParamRange;
import com.digitallending.breservice.model.entity.BRESubParamType;
import com.digitallending.breservice.model.entity.BRESubParamTypeValue;
import com.digitallending.breservice.model.entity.BRESubParamValue;
import com.digitallending.breservice.model.entity.BRESubParamWeight;
import com.digitallending.breservice.model.entity.LoanBREParamType;
import com.digitallending.breservice.model.entity.SanctionConditionRange;
import com.digitallending.breservice.model.entity.SanctionConditionValue;
import com.digitallending.breservice.model.mapper.BREParamTypeMapper;
import com.digitallending.breservice.model.mapper.BREParamWeightMapper;
import com.digitallending.breservice.model.mapper.BREScoreMatrixMapper;
import com.digitallending.breservice.model.mapper.BRESubParamRangeMapper;
import com.digitallending.breservice.model.mapper.SanctionConditionRangeMapper;
import com.digitallending.breservice.model.mapper.SanctionConditionValueMapper;
import com.digitallending.breservice.repository.BREParamTypeRepository;
import com.digitallending.breservice.repository.BREParamWeightRepository;
import com.digitallending.breservice.repository.BREScoreMatrixRepository;
import com.digitallending.breservice.repository.BRESubParamTypeRepository;
import com.digitallending.breservice.repository.LoanBREParamTypeRepository;
import com.digitallending.breservice.repository.SanctionConditionRangeRepository;
import com.digitallending.breservice.repository.SanctionConditionValueRepository;
import com.digitallending.breservice.retrofit.generator.RetrofitServiceGenerator;
import com.digitallending.breservice.retrofit.service.LoanService;
import com.digitallending.breservice.service.def.BREService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class BREServiceImpl implements BREService {

    @Autowired
    private LoanBREParamTypeRepository loanBREParamTypeRepository;
    @Autowired
    private BREParamTypeRepository breParamTypeRepository;
    @Autowired
    private BREParamWeightRepository breParamWeightRepository;
    @Autowired
    private BREScoreMatrixRepository breScoreMatrixRepository;
    @Autowired
    private BRESubParamTypeRepository breSubParamTypeRepository;
    @Autowired
    private SanctionConditionRangeRepository sanctionConditionRangeRepository;
    @Autowired
    private SanctionConditionValueRepository sanctionConditionValueRepository;


    @Autowired
    private RetrofitServiceGenerator retrofitServiceGenerator;
    @Autowired
    private BREParamTypeMapper breParamTypeMapper;
    @Autowired
    private BREScoreMatrixMapper breScoreMatrixMapper;
    @Autowired
    private BREParamWeightMapper breParamWeightMapper;
    @Autowired
    private SanctionConditionRangeMapper sanctionConditionRangeMapper;
    @Autowired
    private SanctionConditionValueMapper sanctionConditionValueMapper;
    @Autowired
    private BRESubParamRangeMapper breSubParamRangeMapper;

    private List<BREParamType> getParamTypeListByLoanTypeId(UUID loanTypeId)
    {
        List<LoanBREParamType> loanBREParamTypeList = loanBREParamTypeRepository.findByLoanTypeId(loanTypeId);
        if(loanBREParamTypeList.isEmpty())
        {
            throw new NotFoundException("Loan Type Id Not Found");
        }
        List<BREParamType> breParamTypeList = new ArrayList<>();

        for(LoanBREParamType loanBREParamType : loanBREParamTypeList)
        {
            BREParamType breParamType = loanBREParamType.getBreParamType();
            breParamTypeList.add(breParamType);
        }
        return breParamTypeList;
    }

    public List<BREParamTypeResponseDTO> getParamTypesByLoanTypeId(UUID loanTypeId)
    {
        List<BREParamType> breParamTypeList = getParamTypeListByLoanTypeId(loanTypeId);
        List<BREParamTypeResponseDTO> breParamTypeResponseDTOList = new ArrayList<>();
        for(BREParamType breParamType : breParamTypeList)
        {
            BREParamTypeResponseDTO breParamTypeResponseDTO = breParamTypeMapper.toDto(breParamType);
            breParamTypeResponseDTOList.add(breParamTypeResponseDTO);
        }
        return breParamTypeResponseDTOList;
    }


    private UUID getLoanTypeIdByLoanProductId(UUID loanProductId)
    {
        LoanService loanService = retrofitServiceGenerator.createService(LoanService.class,"LOAN-SERVICE");
        Call<APIResponse<UUID>> callSync = loanService.getLoanTypeIdByLoanProductId(loanProductId);

        try{
            Response<APIResponse<UUID>> response = callSync.execute();

            if(response.code()==404)
            {
                throw new NotFoundException("Loan Product Id Not found");
            }
            if(response.code()==200)
            {
                APIResponse<UUID> apiResponse = response.body();
                if(apiResponse.getPayload()==null)
                {
                    throw new EmptyPayloadException("Data in Payload is Not Present");
                }
                return apiResponse.getPayload();
            }
        }
        catch (IOException ex) {
            throw new ExternalServiceException("Loan Service is not working");
        }
        return null;
    }


    public List<BREParamWeightResponseDTO> saveBREParamWeights(List<BREParamWeightRequestDTO> breParamWeightRequestDTOList,UUID loanProductId)
    {

        Integer totalWeight = 0;
        for(BREParamWeightRequestDTO breParamWeightRequestDTO : breParamWeightRequestDTOList)
        {
            totalWeight+=breParamWeightRequestDTO.getWeight();
        }

        if(totalWeight!=100)
        {
            throw new InvalidWeightException("Sum of Weights of all Param types should be 100");
        }

        UUID loanTypeId = getLoanTypeIdByLoanProductId(loanProductId);
        if(loanTypeId==null)
        {
            throw new NotFoundException("Loan Type Id Not Found For Loan Product Id " + loanProductId);
        }

        List<BREParamType> breParamTypeList = getParamTypeListByLoanTypeId(loanTypeId);
        Set<UUID> paramTypeIdHashSet= new HashSet<>();

        for(BREParamType breParamType : breParamTypeList)
        {
            paramTypeIdHashSet.add(breParamType.getParamTypeId());
        }

        for(BREParamWeightRequestDTO breParamWeightRequestDTO : breParamWeightRequestDTOList)
        {
            if(!paramTypeIdHashSet.remove(breParamWeightRequestDTO.getParamTypeId()))
            {
                 throw new InvalidParameterException("Param Type ID " + breParamWeightRequestDTO.getParamTypeId() + " is not Valid Param For Your Loan Type");
            }
        }

        if(!paramTypeIdHashSet.isEmpty())
        {
            throw new InvalidParameterException("Insufficient Parameters Provided for Your Loan Type");
        }

        List<BREParamWeightResponseDTO> breParamWeightResponseDTOList = new ArrayList<>();

        for(BREParamWeightRequestDTO breParamWeightRequestDTO : breParamWeightRequestDTOList)
        {
            BREParamType breParamType = breParamTypeRepository.findById(breParamWeightRequestDTO.getParamTypeId()).orElseThrow(()-> new NotFoundException("Param Type with Id " + (breParamWeightRequestDTO.getParamTypeId()) + " Not Found"));
            BREParamWeight breParamWeight = BREParamWeight.builder()
                    .loanProductId(loanProductId)
                            .paramType(breParamType)
                                    .weight(breParamWeightRequestDTO.getWeight())
                                            .breSubParamWeightsList(new ArrayList<>()).build();

            BREParamWeight savedBREParamWeight = breParamWeightRepository.save(breParamWeight);
            breParamWeightResponseDTOList.add(breParamWeightMapper.toDto(savedBREParamWeight));
        }
        return breParamWeightResponseDTOList;
    }

    public List<BREParamType> getParamsAndSubParamsTypeByLoanProductId(UUID loanProductId)
    {
        List<BREParamWeight> breParamWeightList = breParamWeightRepository.findByLoanProductIdAndWeightNot(loanProductId,0);
        List<BREParamType> breParamTypeList = new ArrayList<>();
        if(breParamWeightList.isEmpty())
        {
            throw new NotFoundException("Requested Data Not Found");
        }

        for(BREParamWeight breParamWeight : breParamWeightList)
        {
            BREParamType breParamType = breParamWeight.getParamType();
            breParamTypeList.add(breParamType);
        }
        return breParamTypeList;
    }


    private void checkRangeConstraints(List<VerifyRangeConstraintDTO> verifyRangeConstraintDTOList, String subParamName)
    {
        verifyRangeConstraintDTOList.sort(Comparator.comparing(VerifyRangeConstraintDTO::getMin));

        int verifyRangeConstraintDTOListSize = verifyRangeConstraintDTOList.size();

        for(int i=0;i<verifyRangeConstraintDTOListSize-1;i++)
        {
            if(verifyRangeConstraintDTOList.get(i).getMin().compareTo(verifyRangeConstraintDTOList.get(i).getMax())>0)
            {
                throw new InvalidRangeException("Invalid Range Given For " + subParamName);
            }
            if(verifyRangeConstraintDTOList.get(i).getMax().compareTo(verifyRangeConstraintDTOList.get(i+1).getMin())>=0)
            {
                throw new InvalidRangeException("Invalid Range Given For " + subParamName);
            }
        }
        if(verifyRangeConstraintDTOList.get(verifyRangeConstraintDTOListSize-1).getMax().compareTo(verifyRangeConstraintDTOList.get(verifyRangeConstraintDTOListSize-1).getMin())<0)
        {
            throw new InvalidRangeException("Invalid Range Given For " + subParamName);
        }


        switch(subParamName)
        {
            case "age" :
                    if(verifyRangeConstraintDTOList.get(0).getMin().compareTo(BigDecimal.valueOf(216))<=0)
                    {
                        throw new InvalidRangeException("Age should be greater than 18 years (216 months)");
                    }
                    break;

            case "businessExperience","ownershipVintage","businessVintage" :
                    if(verifyRangeConstraintDTOList.get(0).getMin().compareTo(BigDecimal.valueOf(0))<0)
                    {
                        throw new InvalidRangeException(subParamName + " should be atleast 0 months");
                    }
                    break;

            case "monthlyIncome", "propertyValuation":
                    if(verifyRangeConstraintDTOList.get(0).getMin().compareTo(BigDecimal.valueOf(20000))<0)
                    {
                        throw new InvalidRangeException(subParamName + " should be atleast 20,000");
                    }
                    break;

            case "cibilScore" :
                    if(verifyRangeConstraintDTOList.get(0).getMin().compareTo(BigDecimal.valueOf(300))<0)
                    {
                        throw new InvalidRangeException("Cibil Score should be atleast 300");
                    }
                    else if(verifyRangeConstraintDTOList.get(verifyRangeConstraintDTOListSize-1).getMax().compareTo(BigDecimal.valueOf(900))>0)
                    {
                        throw new InvalidRangeException("Cibil Score should be atmost 900");
                    }
                    break;
            case "cibilRank" :
                    if(verifyRangeConstraintDTOList.get(0).getMin().compareTo(BigDecimal.valueOf(1))<0)
                    {
                        throw new InvalidRangeException("Cibil Rank should be atleast 1");
                    }
                    else if(verifyRangeConstraintDTOList.get(verifyRangeConstraintDTOListSize-1).getMax().compareTo(BigDecimal.valueOf(10))>0)
                    {
                        throw new InvalidRangeException("Cibil Rank should be atmost 10");
                    }
        }

    }



    public BREParamAndSubParamWeightResponseDTO saveBRESubParamWeights(List<BRESubParamWeightRequestDTO> breSubParamWeightRequestDTOList, UUID paramWeightId)
    {

        BREParamWeight breParamWeight = breParamWeightRepository.findById(paramWeightId).orElseThrow(()-> new NotFoundException("BRE Param Weight with Id " + paramWeightId + " Not Found"));

        if(!breParamWeight.getBreSubParamWeightsList().isEmpty())
        {
            throw new AlreadyExistException("BRE Sub Param Weights Already Exists for Param Weight Id "+ paramWeightId);
        }

        if(breParamWeight.getWeight()==0)
        {
            throw new NotAllowedException("Not allowed to specify sub param weights for param weight id " + paramWeightId + " as it's weight is Zero");
        }

        Integer weight = 0;
        for(BRESubParamWeightRequestDTO breSubParamWeightRequestDTO : breSubParamWeightRequestDTOList)
        {
            weight += breSubParamWeightRequestDTO.getWeight();
        }
        if(weight != 100)
        {
            throw new InvalidWeightException("Sum of Weights of Sub Params will be 100");
        }

        BREParamType breParamType = breParamWeight.getParamType();
        HashMap<UUID,BRESubParamType> subParamHashMap = new HashMap<>();
        for(BRESubParamType breSubParamType : breParamType.getBreSubParamTypeList())
        {
            subParamHashMap.put(breSubParamType.getSubParamTypeId(),breSubParamType);
        }

        List<BRESubParamWeight> breSubParamWeightList = new ArrayList<>();

        for(BRESubParamWeightRequestDTO breSubParamWeightRequestDTO : breSubParamWeightRequestDTOList)
        {
            BRESubParamWeight breSubParamWeight = new BRESubParamWeight();

            BRESubParamType breSubParamType = subParamHashMap.get(breSubParamWeightRequestDTO.getBreSubParamTypeId());
            if(breSubParamType == null)
            {
                throw new NotFoundException("Invalid Sub Param Id given for " + breParamWeight.getParamType().getParamName() + " Param with Id "+ breSubParamWeightRequestDTO.getBreSubParamTypeId());
            }
            subParamHashMap.remove(breSubParamWeightRequestDTO.getBreSubParamTypeId());

            if(breSubParamType.getIsRange())
            {
                if(breSubParamWeightRequestDTO.getBreSubParamRangeList()==null ||breSubParamWeightRequestDTO.getBreSubParamRangeList().isEmpty())
                {
                    throw new NotFoundException("BRE Sub Param Range List must not be Empty for Sub Parma Type " + breSubParamType.getSubParamName());
                }
                else
                {
                    List<BRESubParamRange> breSubParamRangeList = new ArrayList<>();
                    List<BRESubParamRangeRequestDTO> breSubParamRangeRequestDTOList = breSubParamWeightRequestDTO.getBreSubParamRangeList();

                    List<VerifyRangeConstraintDTO> verifyRangeConstraintDTOList = new ArrayList<>();

                    for(BRESubParamRangeRequestDTO breSubParamRangeRequestDTO : breSubParamRangeRequestDTOList)
                    {
                        verifyRangeConstraintDTOList.add(breSubParamRangeMapper.toDTO(breSubParamRangeRequestDTO));
                    }

                    checkRangeConstraints(verifyRangeConstraintDTOList,breSubParamType.getSubParamName());

                    for(BRESubParamRangeRequestDTO breSubParamRangeRequestDTO : breSubParamRangeRequestDTOList)
                    {
                        BRESubParamRange breSubParamRange = BRESubParamRange.builder()
                                .score(breSubParamRangeRequestDTO.getScore())
                                .min(breSubParamRangeRequestDTO.getMin())
                                .max(breSubParamRangeRequestDTO.getMax())
                                .build();

                        breSubParamRangeList.add(breSubParamRange);
                    }
                    breSubParamWeight.setBreSubParamRangeList(breSubParamRangeList);
                }
            }
            else
            {
                if(breSubParamWeightRequestDTO.getBreSubParamValueList()==null || breSubParamWeightRequestDTO.getBreSubParamValueList().isEmpty())
                {
                    throw new NotFoundException("BRE Sub Param Value List must not be Empty for Sub Parma Type " + breSubParamType.getSubParamName());
                }
                else
                {
                    List<BRESubParamTypeValue> breSubParamTypeValueList = breSubParamType.getBreSubParamTypeValueList();

                    HashMap<UUID,BRESubParamTypeValue> subParamTypeValueHashMap = new HashMap<>();

                    for(BRESubParamTypeValue breSubParamTypeValue : breSubParamTypeValueList)
                    {
                        subParamTypeValueHashMap.put(breSubParamTypeValue.getBreSubParamTypeValueId(),breSubParamTypeValue);
                    }

                    List<BRESubParamValueRequestDTO> breSubParamValueRequestDTOList = breSubParamWeightRequestDTO.getBreSubParamValueList();

                    List<BRESubParamValue> breSubParamValueList = new ArrayList<>();

                    for(BRESubParamValueRequestDTO breSubParamValueRequestDTO : breSubParamValueRequestDTOList)
                    {
                        BRESubParamTypeValue breSubParamTypeValue = subParamTypeValueHashMap.get(breSubParamValueRequestDTO.getBreSubParamTypeValueId());
                        if(breSubParamTypeValue == null)
                        {
                            throw new InvalidValueException("Invalid Value for Sub Param " + breSubParamType.getSubParamName());
                        }
                        subParamTypeValueHashMap.remove(breSubParamValueRequestDTO.getBreSubParamTypeValueId());

                        BRESubParamValue breSubParamValue = BRESubParamValue.builder()
                                .breSubParamTypeValue(breSubParamTypeValue)
                                .score(breSubParamValueRequestDTO.getScore())
                                .build();

                        breSubParamValueList.add(breSubParamValue);
                    }
                    breSubParamWeight.setBreSubParamValueList(breSubParamValueList);
                }
            }
            breSubParamWeight.setBreSubParamType(breSubParamType);
            breSubParamWeight.setWeight(breSubParamWeightRequestDTO.getWeight());

            breSubParamWeightList.add(breSubParamWeight);
        }
        breParamWeight.setBreSubParamWeightsList(breSubParamWeightList);
        BREParamWeight savedBreParamWeight = breParamWeightRepository.save(breParamWeight);

        return breParamWeightMapper.toDtoAfterSave(savedBreParamWeight);
    }


    public BRESubParamTypeForSanctionConditionResponseDTO getSubParamTypeForSanctionCondition(UUID loanTypeId)
    {

        List<BREParamType> breParamTypeList = getParamTypeListByLoanTypeId(loanTypeId);

        List<BRESubParamType> breSubParamTypeValueList = new ArrayList<>();
        List<BRESubParamType> breSubParamTypeRangeList = new ArrayList<>();
        for(BREParamType breParamType : breParamTypeList)
        {
            List<BRESubParamType> breSubParamTypeList = breParamType.getBreSubParamTypeList();
            for(BRESubParamType breSubParamType : breSubParamTypeList)
            {
                if(!breSubParamType.getIsRange())
                {
                    breSubParamTypeValueList.add(breSubParamType);
                }
                else
                {
                    breSubParamTypeRangeList.add(breSubParamType);
                }
            }
        }

        BRESubParamTypeForSanctionConditionResponseDTO breSubParamTypeForSanctionConditionResponseDTO
                = BRESubParamTypeForSanctionConditionResponseDTO.builder().breSubParamTypeValueList(breSubParamTypeValueList)
                .breSubParamTypeRangeList(breSubParamTypeRangeList).build();
        return breSubParamTypeForSanctionConditionResponseDTO;
    }



    public List<SanctionConditionRangeResponseDTO> saveSanctionConditionRange(List<SanctionConditionRangeRequestDTO> sanctionConditionRangeRequestDTOList,UUID loanProductId)
    {
        if(sanctionConditionRangeRepository.existsByLoanProductId(loanProductId))
        {
            throw new AlreadyExistException("Sanction Conditions Range with Loan Product Id "+ loanProductId +" Already Exists");
        }

        UUID loanTypeId = getLoanTypeIdByLoanProductId(loanProductId);
        if(loanTypeId==null)
        {
            throw new NotFoundException("Loan Type Id Not Found For Loan Product Id " + loanProductId);
        }
        List<BREParamType> breParamTypeList = getParamTypeListByLoanTypeId(loanTypeId);
        HashMap<UUID,BRESubParamType> subParamTypeIdAndSubParamTypeHashMap = new HashMap<>();

        for(BREParamType breParamType : breParamTypeList)
        {
            for(BRESubParamType breSubParamType : breParamType.getBreSubParamTypeList())
            {
                if(breSubParamType.getIsRange())
                {
                    subParamTypeIdAndSubParamTypeHashMap.put(breSubParamType.getSubParamTypeId(),breSubParamType);
                }
            }
        }

        HashMap<UUID,List<VerifyRangeConstraintDTO>> subParamIdAndRangeListHashMap = new HashMap<>();

        for(SanctionConditionRangeRequestDTO sanctionConditionRangeRequestDTO : sanctionConditionRangeRequestDTOList) {
            if (!subParamTypeIdAndSubParamTypeHashMap.containsKey(sanctionConditionRangeRequestDTO.getBreSubParamTypeId())) {
                throw new InvalidParameterException("Invalid Parameter for this Loan Type");
            }
            subParamIdAndRangeListHashMap.computeIfAbsent(sanctionConditionRangeRequestDTO.getBreSubParamTypeId(), subParamTypeId -> new ArrayList<>());
            subParamIdAndRangeListHashMap.get(sanctionConditionRangeRequestDTO.getBreSubParamTypeId()).add(sanctionConditionRangeMapper.toVerifyRangeConstraintDTO(sanctionConditionRangeRequestDTO));
        }

        subParamIdAndRangeListHashMap.forEach((key,value)->{
            BRESubParamType breSubParamType = subParamTypeIdAndSubParamTypeHashMap.get(key);
            checkRangeConstraints(value,breSubParamType.getSubParamName());
        });

        List<SanctionConditionRangeResponseDTO> sanctionConditionRangeResponseDTOList = new ArrayList<>();

        for(SanctionConditionRangeRequestDTO sanctionConditionRangeRequestDTO : sanctionConditionRangeRequestDTOList)
        {
            BRESubParamType breSubParamType = subParamTypeIdAndSubParamTypeHashMap.get(sanctionConditionRangeRequestDTO.getBreSubParamTypeId());
            SanctionConditionRange sanctionConditionRange = SanctionConditionRange.builder()
                    .loanProductId(loanProductId)
                    .min(sanctionConditionRangeRequestDTO.getMin())
                    .max(sanctionConditionRangeRequestDTO.getMax())
                    .breSubParamType(breSubParamType)
                    .build();

            SanctionConditionRange savedSanctionConditionRange = sanctionConditionRangeRepository.save(sanctionConditionRange);
            sanctionConditionRangeResponseDTOList.add(sanctionConditionRangeMapper.toDto(savedSanctionConditionRange));
        }
        return sanctionConditionRangeResponseDTOList;
    }


    public List<SanctionConditionValueResponseDTO> saveSanctionConditionValue(List<SanctionConditionValueRequestDTO> sanctionConditionValueRequestDTOList,UUID loanProductId)
    {

        if(sanctionConditionValueRepository.existsByLoanProductId(loanProductId))
        {
            throw new AlreadyExistException("Sanction Conditions Value with Loan Product Id "+ loanProductId +" Already Exists");
        }

        UUID loanTypeId = getLoanTypeIdByLoanProductId(loanProductId);
        if(loanTypeId==null)
        {
            throw new NotFoundException("Loan Type Id Not Found For Loan Product Id " + loanProductId);
        }
        List<BREParamType> breParamTypeList = getParamTypeListByLoanTypeId(loanTypeId);
        HashMap<UUID,BRESubParamType> subParamTypeIdAndSubParamTypeHashMap = new HashMap<>();

        for(BREParamType breParamType : breParamTypeList)
        {
            for(BRESubParamType breSubParamType : breParamType.getBreSubParamTypeList())
            {
                if(!breSubParamType.getIsRange())
                {
                    subParamTypeIdAndSubParamTypeHashMap.put(breSubParamType.getSubParamTypeId(),breSubParamType);
                }
            }
        }

        List<SanctionConditionValue> sanctionConditionValueList = new ArrayList<>();
        HashSet<UUID> subParamTypeValueIdHashSet = new HashSet();

        for(SanctionConditionValueRequestDTO sanctionConditionValueRequestDTO : sanctionConditionValueRequestDTOList)
        {
            if (!subParamTypeIdAndSubParamTypeHashMap.containsKey(sanctionConditionValueRequestDTO.getSubParamTypeId())) {
                throw new InvalidParameterException("Invalid Parameter for this Loan Type");
            }

            BRESubParamType breSubParamType = subParamTypeIdAndSubParamTypeHashMap.get(sanctionConditionValueRequestDTO.getSubParamTypeId());


            List<BRESubParamTypeValue> breSubParamTypeValueList = breSubParamType.getBreSubParamTypeValueList();

            BRESubParamTypeValue breSubParamTypeValue = null;
            for(BRESubParamTypeValue subParamTypeValue : breSubParamTypeValueList)
            {
                if(subParamTypeValue.getBreSubParamTypeValueId().equals(sanctionConditionValueRequestDTO.getBreSubParamTypeValueId()))
                {
                    breSubParamTypeValue = subParamTypeValue;
                }
            }
            if(breSubParamTypeValue == null)
            {
                throw new InvalidValueException("Invalid Value for Sub Param " + breSubParamType.getSubParamName());
            }

            if(subParamTypeValueIdHashSet.contains(sanctionConditionValueRequestDTO.getBreSubParamTypeValueId()))
            {
                throw new AlreadyExistException("Multiple same entry provided for same sub param type " + breSubParamType.getSubParamName());
            }
            subParamTypeValueIdHashSet.add(sanctionConditionValueRequestDTO.getBreSubParamTypeValueId());

            SanctionConditionValue sanctionConditionValue = SanctionConditionValue.builder()
                    .loanProductId(loanProductId)
                    .breSubParamType(breSubParamType)
                    .breSubParamTypeValue(breSubParamTypeValue)
                    .build();

            sanctionConditionValueList.add(sanctionConditionValue);
        }

        List<SanctionConditionValueResponseDTO> sanctionConditionValueResponseDTOList = new ArrayList<>();
        for(SanctionConditionValue sanctionConditionValue : sanctionConditionValueList)
        {
            SanctionConditionValue savedSanctionConditionValue = sanctionConditionValueRepository.save(sanctionConditionValue);
            sanctionConditionValueResponseDTOList.add(sanctionConditionValueMapper.toDto(savedSanctionConditionValue));
        }

        return sanctionConditionValueResponseDTOList;
    }



    public List<BREScoreMatrixResponseDTO> saveScoreMatrix(List<BREScoreMatrixRequestDTO> breScoreMatrixRequestDTOList,UUID loanProductId)
    {

        UUID loanTypeId = getLoanTypeIdByLoanProductId(loanProductId);
        if(loanTypeId==null)
        {
            throw new NotFoundException("Loan Type Id Not Found For Loan Product Id " + loanProductId);
        }

        if(breScoreMatrixRepository.existsByLoanProductId(loanProductId))
        {
            throw new AlreadyExistException("Score Matrix for loan Product Id  " + loanProductId + " already Exist");
        }

        breScoreMatrixRequestDTOList.sort((obj1,obj2) -> {
            return obj1.getScoreFrom()-obj2.getScoreFrom();
        });

        int breScoreMatrixRequestDTOListSize = breScoreMatrixRequestDTOList.size();
        if(breScoreMatrixRequestDTOList.get(0).getScoreFrom()!=-10 || breScoreMatrixRequestDTOList.get(breScoreMatrixRequestDTOListSize-1).getScoreTo()!=10)
        {
            throw new InvalidRangeException("Invalid Range Given For Score Matrix");
        }
        for(int i=0;i<breScoreMatrixRequestDTOListSize-1;i++)
        {
            if(breScoreMatrixRequestDTOList.get(i).getScoreFrom()>breScoreMatrixRequestDTOList.get(i).getScoreTo())
            {
                throw new InvalidRangeException("Invalid Range Given For Score Matrix");
            }
            if(breScoreMatrixRequestDTOList.get(i).getScoreTo()+1!=breScoreMatrixRequestDTOList.get(i+1).getScoreFrom())
            {
                throw new InvalidRangeException("Invalid Range Given For Score Matrix");
            }
        }
        if(breScoreMatrixRequestDTOList.get(breScoreMatrixRequestDTOListSize-1).getScoreFrom()>breScoreMatrixRequestDTOList.get(breScoreMatrixRequestDTOListSize-1).getScoreTo())
        {
            throw new InvalidRangeException("Invalid Range Given For Score Matrix");
        }


        List<BREScoreMatrixResponseDTO> breScoreMatrixResponseDTOList = new ArrayList<>();
        for(BREScoreMatrixRequestDTO breScoreMatrixRequestDTO : breScoreMatrixRequestDTOList)
        {
            BREScoreMatrix breScoreMatrix = BREScoreMatrix.builder()
                    .loanProductId(loanProductId)
                    .scoreFrom(breScoreMatrixRequestDTO.getScoreFrom())
                    .scoreTo(breScoreMatrixRequestDTO.getScoreTo())
                    .maxLoanAmount(breScoreMatrixRequestDTO.getMaxLoanAmount())
                    .maxTenureInMonths(breScoreMatrixRequestDTO.getMaxTenureInMonths())
                    .minInterestRate(breScoreMatrixRequestDTO.getMinInterestRate())
                    .build();
            BREScoreMatrix savedBreScoreMatrix = breScoreMatrixRepository.save(breScoreMatrix);
            breScoreMatrixResponseDTOList.add(breScoreMatrixMapper.toDto(savedBreScoreMatrix));
        }

        return breScoreMatrixResponseDTOList;
    }

    private final HashMap<String, BigDecimal> rangeHashMap = new HashMap<>();
    private final HashMap<String, String> valueHashMap = new HashMap<>();


    private void convertUserAndLoanApplicationToRangeAndValueHashMap(UserRequestDTO userRequestDTO, LoanApplicationRequestDTO loanApplicationRequestDTO)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,String> userHashMap = objectMapper.convertValue(userRequestDTO,  new TypeReference<HashMap<String, String>>() {});
        HashMap<String,String> loanApplicationHashMap = objectMapper.convertValue(loanApplicationRequestDTO, new TypeReference<HashMap<String, String>>() {});

        List<BRESubParamType> breSubParamTypeList = breSubParamTypeRepository.findAll();

        for (BRESubParamType breSubParamType : breSubParamTypeList)
        {
            if (userHashMap.containsKey(breSubParamType.getSubParamName()) && userHashMap.get(breSubParamType.getSubParamName())!=null)
            {
                if (breSubParamType.getIsRange())
                {
                    rangeHashMap.put(breSubParamType.getSubParamName(), BigDecimal.valueOf(Double.parseDouble(userHashMap.get(breSubParamType.getSubParamName()))));
                }
                else
                {
                    valueHashMap.put(breSubParamType.getSubParamName(), userHashMap.get(breSubParamType.getSubParamName()));
                }
            }
            else if (loanApplicationHashMap.containsKey(breSubParamType.getSubParamName()) && loanApplicationHashMap.get(breSubParamType.getSubParamName())!=null)
            {
                if (breSubParamType.getIsRange())
                {
                    rangeHashMap.put(breSubParamType.getSubParamName(), BigDecimal.valueOf(Double.parseDouble(loanApplicationHashMap.get(breSubParamType.getSubParamName()))));
                }
                else
                {
                    valueHashMap.put(breSubParamType.getSubParamName(), loanApplicationHashMap.get(breSubParamType.getSubParamName()));
                }
            }
        }
    }


    private List<UUID> applySanctionCondition(UserRequestDTO userRequestDTO, LoanApplicationRequestDTO loanApplicationRequestDTO,List<UUID> loanProductIdList)
    {
        convertUserAndLoanApplicationToRangeAndValueHashMap(userRequestDTO,loanApplicationRequestDTO);
        List<UUID> applicableLoanProductList = new ArrayList<>(loanProductIdList);

        for (UUID loanProductId : loanProductIdList)
        {
            boolean isApplicableLoanProduct = true;

            List<SanctionConditionRange> sanctionConditionRangeList = sanctionConditionRangeRepository.findByLoanProductId(loanProductId);
            if (sanctionConditionRangeList.isEmpty())
            {
                throw new NotFoundException("Loan Product Id " + loanProductId + " not found");
            }

            for (SanctionConditionRange sanctionConditionRange : sanctionConditionRangeList)
            {
                if (sanctionConditionRange.getMin().compareTo(rangeHashMap.get(sanctionConditionRange.getBreSubParamType().getSubParamName())) <= 0
                        && sanctionConditionRange.getMax().compareTo(rangeHashMap.get(sanctionConditionRange.getBreSubParamType().getSubParamName())) >= 0)
                {
                    applicableLoanProductList.remove(sanctionConditionRange.getLoanProductId());
                    isApplicableLoanProduct = false;
                    break;
                }
            }
            if(!isApplicableLoanProduct)
            {
                continue;
            }

            List<SanctionConditionValue> sanctionConditionValueList = sanctionConditionValueRepository.findByLoanProductId(loanProductId);
            if (sanctionConditionValueList.isEmpty())
            {
                throw new NotFoundException("Loan Product Id " + loanProductId + " not found");
            }

            for (SanctionConditionValue sanctionConditionValue : sanctionConditionValueList)
            {
                if (sanctionConditionValue.getBreSubParamTypeValue().getValue().equals(valueHashMap.get(sanctionConditionValue.getBreSubParamType().getSubParamName())))
                {
                    applicableLoanProductList.remove(sanctionConditionValue.getLoanProductId());
                    break;
                }
            }
        }
        return applicableLoanProductList;
    }

    private  List<BREResultResponseDTO> run(List<UUID> loanProductIdList,LoanApplicationRequestDTO loanApplicationRequestDTO)
    {
        List<BREResultResponseDTO> breResultResponseDTOList = new ArrayList<>();
        for(UUID loanProduct : loanProductIdList)
        {
            List<BREParamWeight> breParamWeightList = breParamWeightRepository.findByLoanProductIdAndWeightNot(loanProduct,0);

            float paramScore = 0;
            for(BREParamWeight breParamWeight : breParamWeightList)
            {
                List<BRESubParamWeight> breSubParamWeightList = breParamWeight.getBreSubParamWeightsList();

                float subParamScore = 0;
                for(BRESubParamWeight breSubParamWeight : breSubParamWeightList)
                {

                    if(breSubParamWeight.getBreSubParamType().getIsRange())
                    {
                        List<BRESubParamRange> breSubParamRangeList = breSubParamWeight.getBreSubParamRangeList();

                        for(BRESubParamRange breSubParamRange : breSubParamRangeList)
                        {
                            if(breSubParamRange.getMin().compareTo(rangeHashMap.get(breSubParamWeight.getBreSubParamType().getSubParamName())) <=0
                            && breSubParamRange.getMax().compareTo(rangeHashMap.get(breSubParamWeight.getBreSubParamType().getSubParamName())) >=0)
                            {
                                subParamScore += ((float) (breSubParamWeight.getWeight() * breSubParamRange.getScore()) / 100);
                                break;
                            }
                        }
                    }
                    else
                    {
                        List<BRESubParamValue> breSubParamValueList = breSubParamWeight.getBreSubParamValueList();

                        for(BRESubParamValue breSubParamValue : breSubParamValueList)
                        {
                            if(breSubParamValue.getBreSubParamTypeValue().getValue().equals(valueHashMap.get(breSubParamWeight.getBreSubParamType().getSubParamName())))
                            {
                                subParamScore += ((float) (breSubParamWeight.getWeight() * breSubParamValue.getScore()) / 100);
                                break;
                            }
                        }
                    }

                }
                paramScore += (breParamWeight.getWeight() * subParamScore / 100);
            }


            BREScoreMatrix breScoreMatrix = breScoreMatrixRepository.findByLoanProductIdAndScoreFromLessThanEqualAndScoreToGreaterThanEqual(loanProduct,Math.round(paramScore),Math.round(paramScore));
            if(breScoreMatrix==null)
            {
                throw new NotFoundException("BREScore Matrix Requested Data not found");
            }

            if(breScoreMatrix.getMaxLoanAmount().compareTo(loanApplicationRequestDTO.getAmount()) >=0 && breScoreMatrix.getMaxTenureInMonths() >= loanApplicationRequestDTO.getTenure())
            {
                BREResultResponseDTO breResultResponseDTO = BREResultResponseDTO.builder().loanProductId(loanProduct).minInterestRate(breScoreMatrix.getMinInterestRate()).score(Math.round(paramScore)).build();
                breResultResponseDTOList.add(breResultResponseDTO);
            }

        }
        return breResultResponseDTOList;
    }

    public  List<BREResultResponseDTO> runBRE(UserRequestDTO userRequestDTO, LoanApplicationRequestDTO loanApplicationRequestDTO, List<UUID> loanProductIdList)
    {
        List<UUID> applicableLoanProductsListAfterApplyingSanctionConditions = applySanctionCondition(userRequestDTO,loanApplicationRequestDTO,loanProductIdList);
        return run(applicableLoanProductsListAfterApplyingSanctionConditions,loanApplicationRequestDTO);
    }

}
