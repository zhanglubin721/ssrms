package com.yanghui.ssrms.dao;

import com.github.pagehelper.Page;
import com.yanghui.ssrms.entity.QueryPageBean;
import com.yanghui.ssrms.pojo.Ssr;
import org.apache.ibatis.annotations.Param;

public interface SsrDao {
    /**
     * 分页查询
     * @param queryPageBean
     * @return Page<Student>
     */
    Page<Ssr> selectByConditon(QueryPageBean queryPageBean);

    /**
     * 删除自习室
     * @param ssrid
     */
    void deleteBySsrid(@Param("ssrid") Long ssrid);

    /**
     * 添加自习室
     * @param ssr
     */
    void insertSsr(Ssr ssr);


    /**
     * 根据自习室id查找自习室
     * @param ssrid
     */
    Ssr selectBySsrid(@Param("ssrid") Long ssrid);

    /**
     * 编辑自习室信息
     * @param ssr
     */
    void updateSsr(Ssr ssr);
}
