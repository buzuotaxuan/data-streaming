package io.metersphere.streaming.base.mapper.ext;

import io.metersphere.streaming.base.domain.LoadTestReportDetail;
import io.metersphere.streaming.base.domain.LoadTestReportResultPart;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtLoadTestReportMapper {

    @Update({"UPDATE load_test_report ",
            "SET status = #{nextStatus} ",
            "WHERE id = #{id} AND status = #{prevStatus}"})
    int updateStatus(@Param("id") String id, @Param("nextStatus") String nextStatus, @Param("prevStatus") String prevStatus);

    @Select(value = {
            "SELECT report_id AS reportId, content, part ",
            "FROM load_test_report_detail ",
            "WHERE report_id = #{reportId} AND part > 1 ",
            "ORDER BY part "
    })
    @Options(fetchSize = Integer.MIN_VALUE, resultSetType = ResultSetType.FORWARD_ONLY)
    List<LoadTestReportDetail> fetchTestReportDetails(@Param("reportId") String reportId);

    @Insert({"INSERT INTO load_test_report_detail(report_id, content) " +
            "VALUES( ",
            "#{report.reportId}, ",
            "#{report.content}) "})
    void insert(@Param("report") LoadTestReportDetail record);

    @Select(value = {
            "SELECT report_id AS reportId, report_key AS reportKey, resource_index AS resourceIndex, report_value AS reportValue ",
            "FROM load_test_report_result_part ",
            "WHERE report_id = #{reportId} AND report_key = #{reportKey} "
    })
    @Options(fetchSize = Integer.MIN_VALUE, resultSetType = ResultSetType.FORWARD_ONLY)
    List<LoadTestReportResultPart> fetchTestReportParts(@Param("reportId") String reportId, @Param("reportKey") String reportKey);
}
