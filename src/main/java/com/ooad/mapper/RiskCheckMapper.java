package com.ooad.mapper;

import com.ooad.entity.RiskCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Qizixi on 2017/6/2.
 */
public interface RiskCheckMapper {

    RiskCheck getRiskCheckById(@Param("id") int id);

    List<RiskCheck> getRiskChecks();

    void createRiskCheck(RiskCheck riskCheck);

    void updateRiskCheck(RiskCheck riskCheck);

    List<RiskCheck> getRiskCheckByCompanyId(@Param("id_company")String id_company);

}
