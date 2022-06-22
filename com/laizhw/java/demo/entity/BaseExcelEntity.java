package demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseExcelEntity {

    @ExcelProperty(value="必填",order= 4)
    private String required;
    @ExcelProperty(value="默认值",order= 7)
    private String defaultValue;
    @ExcelProperty(value="字段控件",order= 8)
    private String fieldControl;
    @ExcelProperty(value="控件配置",order= 9)
    private String controlConfiguration;
    @ExcelProperty(value="控件配置名称",order= 10)
    private String controlConfigurationName;
    @ExcelProperty(value="显示占位符",order= 11)
    private String placeholder;
    @ExcelProperty(value="字段校验",order= 12)
    private String fieldCalibration;

}
