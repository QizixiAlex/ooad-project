package com.ooad.mapper;

import com.ooad.entity.Company;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Alex on 2017/6/1.
 */
@Mapper
@Component
public interface CompanyMapper {

    @Select("SELECT * FROM company WHERE id = #{id}")
    @Results({
            @Result(property = "industryType",column = "industry_type"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactTel",column = "contact_tel")
    })
    Company getCompanyById(@Param("id") String id);

    @Insert("INSERT INTO company(id,name,status,code,industry_type,industry,trade,contact_name,contact_tel) VALUES " +
            "(#{id},#{name},#{status},#{code},#{industryType},#{industry},#{trade},#{contactName},#{contactTel})")
    @Results({
            @Result(property = "industryType",column = "industry_type"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactTel",column = "contact_tel")
    })
    void createCompany(Company company);

    @Update("UPDATE company SET name = #{name},status = #{status},code = #{code}," +
            "industry_type = #{industryType},industry = #{industry},trade = #{trade}, contact_name = #{contactName},contact_tel = #{contactTel} " +
            "WHERE id = #{id}")
    @Results({
            @Result(property = "industryType",column = "industry_type"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactTel",column = "contact_tel")
    })
    void updateCompany(Company company);

    @Select("SELECT * FROM company")
    @Results({
            @Result(property = "industryType",column = "industry_type"),
            @Result(property = "contactName",column = "contact_name"),
            @Result(property = "contactTel",column = "contact_tel")
    })
    List<Company> getCompanies();

}
