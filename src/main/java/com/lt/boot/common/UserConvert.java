package com.lt.boot.common;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.lt.boot.model.enums.user.UserGenderEnum;
import com.lt.boot.model.enums.user.UserStatusEnum;

/**
 * @description: 用户 Excel 类型转换器
 * @author: ~Teng~
 * @date: 2024/2/20 15:10
 */
public class UserConvert {
    public static class UserGenderConvert implements Converter<UserGenderEnum> {
        @Override
        public WriteCellData<?> convertToExcelData(UserGenderEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            return new WriteCellData<>(value.getDesc());
        }
    }

    public static class UserStatusConvert implements Converter<UserStatusEnum> {
        @Override
        public WriteCellData<?> convertToExcelData(UserStatusEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
            return new WriteCellData<>(value.getDesc());
        }
    }
}
