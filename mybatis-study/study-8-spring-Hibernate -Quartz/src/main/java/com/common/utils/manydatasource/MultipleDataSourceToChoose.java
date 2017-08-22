package com.common.utils.manydatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * descrption: 多数据源的选择
 * authohr: wangji
 * date: 2017-08-21 10:32
 */
public class MultipleDataSourceToChoose extends AbstractRoutingDataSource {

    /**
     * @desction: 根据Key获取数据源的信息，上层抽象函数的钩子
     * @author: wangji
     * @date: 2017/8/21
     * @param:
     * @return:
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return HandlerDataSource.getDataSource();
    }
}
