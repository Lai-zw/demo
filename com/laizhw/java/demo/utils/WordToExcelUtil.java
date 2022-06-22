package demo.utils;

import demo.entity.ExcelEntity;
import lombok.Data;
import org.apache.commons.text.CaseUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Data
public class WordToExcelUtil {
    /**
     * 读取doc文件
     */
    public static Map<String, Object> importWord(String path) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        XWPFDocument document = new XWPFDocument(new FileInputStream(path));
        if (!document.getTables().isEmpty() || !document.getParagraphs().isEmpty()) {
            // 获取全部段落
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                // String[] split = paragraph.getText().split(":|：|/");
                String[] split = paragraph.getText().split(":|：");
                for (String s : split) {
                    s = s.replaceAll("\\s*", "");
                    s = s.replaceAll("^[0-9]*", "");
                    list.add(s);
                }
            }
            // 获取文档中的所有表格
            List<XWPFTable> tables = document.getTables();
            System.out.println("tables:" + tables);
            System.out.println("tablesSize:" + tables.size());
            List<XWPFTableRow> rows;
            List<XWPFTableCell> cells;
            for (XWPFTable table : tables) {
                // 获取表格对应行
                rows = table.getRows();

                for (XWPFTableRow row : rows) {
                    // 获取对应的单元格
                    int size = row.getTableCells().size();
                    // System.out.println(size);
                    for (int i = 0; i < size; i++) {
                        // System.out.println(row.getTableCells().get(i).getText());
                    }
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // String[] split = cell.getText().split(":|：|/");
                        String[] split = cell.getText().split("[:：]");
                        for (String s : split) {
                            s = s.replaceAll("\\s*", "");
                            s = s.replaceAll("^[0-9]*", "");
                            list.add(s);
                        }
                    }
                }
            }
            list.removeIf(String::isEmpty);
            map.put("title", list.get(0));
            list.remove(0);
            map.put("data", list);
        }
        return map;
    }

    public static List<ExcelEntity> data(List<String> list, String language) {

        Map<String, String> propertyNameMap = new HashMap<>();
        Map<String, String> databaseFieldNameMap = new HashMap<>();
        if (language.equals("en")) {
            for (String s : list) {
                // String s1 = GoogleTranslateUtil.getInstance().translateText(s.trim(), "zh_cn", "en");
                String s1 = AliyunTranslateUtil.aliyunTranslate(s);
                String camelCase = CaseUtils.toCamelCase(s1, false, ' ');
                String lowerUnderscore = s1.toLowerCase().replace(' ', '_');
                propertyNameMap.put(s, camelCase);
                databaseFieldNameMap.put(s, lowerUnderscore);
            }
        } else {
            for (String s : list) {
                propertyNameMap.put(s, PinYinUtil.getFirstPinyin(s));
                databaseFieldNameMap.put(s, PinYinUtil.getFirstPinyin(s));
            }
        }

        // LinkedList<String> linkedList = list.stream().collect(Collectors.toCollection(LinkedList::new));
        List<ExcelEntity> entityList = new LinkedList<>();
        for (String s : list) {
            if (s.contains("日期")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "日期型", "", "", "日期控件", "yyyy-MM-dd", "", "", ""));
            } else if (s.contains("人") || s.contains("复核") || s.contains("试验") || s.contains("批准") || s.contains("审核")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "id", propertyNameMap.get(s) + "id", databaseFieldNameMap.get(s) + "id", "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "签名", propertyNameMap.get(s) + "签名", databaseFieldNameMap.get(s) + "签名", "", "大文本", "", "", "附件上传", "", "", "", ""));
            } else if (s.contains("结论") || s.contains("备注")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "大文本", "", "", "单行文本", "", "", "", ""));
            } else if (s.contains("图")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "大文本", "", "", "附件上传", "", "", "", ""));
            } else if (s.contains("序号")) {
                entityList.add(new ExcelEntity("主表id", PinYinUtil.getFirstPinyin("主表id"), PinYinUtil.getFirstPinyin("主表id"), "", "字符串", "50", "", "单行文本", "", "", "", ""));
            } else {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "字符串", "50", "", "单行文本", "", "", "", ""));
            }
        }
        entityList.add(new ExcelEntity("创建人", "creater", "creater", "", "字符串", "50", "${currentUserName}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建人id", "createrId", "creater_id", "", "字符串", "50", "${currentUserId}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建人部门", "createrDep", "creater_dep", "", "字符串", "50", "${currentOrgName}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建人部门id", "createrDepId", "creater_dep_id", "", "字符串", "50", "${currentOrgId}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建日期", "createTime", "create_time", "", "日期型", "", "${currentDateTime}", "日期控件", "yyyy-MM-dd", "", "", ""));
        entityList.add(new ExcelEntity("检测示意图", "jcsyt", "jcsyt", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("报告标题", "name", "name", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("工程id", "projectId", "project_id", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("焊口id", "hkid", "hkid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("委托单id", "wtdid", "wtdid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("任务单id", "rwdid", "rwdid ", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("指导书id", "zdsid", "zdsid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("指导书明细id", "zdsmxid", "zdsmxid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("指导书复制id", "zdsfzid", "zdsfzid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("审批状态", "spzt", "spzt", "", "字符串", "50", "未审批", "单行文本", "", "", "", ""));
        return entityList;
    }

    public static List<ExcelEntity> data2(List<String> list, String language) {

        Map<String, String> propertyNameMap = new HashMap<>();
        Map<String, String> databaseFieldNameMap = new HashMap<>();
        if (language.equals("en")) {
            for (String s : list) {
                // String s1 = GoogleTranslateUtil.getInstance().translateText(s.trim(), "zh_cn", "en");
                String s1 = AliyunTranslateUtil.aliyunTranslate(s);
                String camelCase = CaseUtils.toCamelCase(s1, false, ' ');
                String lowerUnderscore = s1.toLowerCase().replace(' ', '_');
                propertyNameMap.put(s, camelCase);
                databaseFieldNameMap.put(s, lowerUnderscore);
            }
        } else {
            for (String s : list) {
                propertyNameMap.put(s, PinYinUtil.getFirstPinyin(s));
                databaseFieldNameMap.put(s, PinYinUtil.getFirstPinyin(s));
            }
        }
        // LinkedList<String> linkedList = list.stream().collect(Collectors.toCollection(LinkedList::new));
        List<ExcelEntity> entityList = new LinkedList<>();
        entityList.add(new ExcelEntity("主表id", "zbid", "zbid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        for (String s : list) {
            if (s.contains("日期")) {
                entityList.add(new ExcelEntity(s, PinYinUtil.getFirstPinyin(s), PinYinUtil.getFirstPinyin(s), "", "日期型", "", "", "日期控件", "yyyy年MM月dd日", "", "年   月   日", ""));
            } else if ((s.contains("人") && !s.contains("证书") && !s.contains("工"))
                    || s.contains("批准") || s.contains("审核") || s.contains("复核")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "id", propertyNameMap.get(s) + "id", databaseFieldNameMap.get(s) + "id", "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "签名", propertyNameMap.get(s + "签名"), databaseFieldNameMap.get(s + "签名"), "", "大文本", "", "", "附件上传", "", "", "", ""));
            } else if (s.contains("试验") && !s.contains("结")
                    && !s.contains("类型") && !s.contains("编号")
                    && !s.contains("前") && !s.contains("后")
                    && !s.contains("修正") && !s.contains("标")
                    && !s.contains("环境") && !s.contains("方法")
                    && !s.contains("证书") && !s.contains("质量")
                    && !s.contains("抗冻性")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "id", propertyNameMap.get(s) + "id", databaseFieldNameMap.get(s) + "id", "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "签名", propertyNameMap.get(s + "签名"), databaseFieldNameMap.get(s + "签名"), "", "大文本", "", "", "附件上传", "", "", "", ""));
            } else if (s.contains("检验") && !s.contains("依据")
                    && !s.contains("前") && !s.contains("后")
                    && !s.contains("标") && !s.contains("环境")
                    && !s.contains("方法") && !s.contains("证书")
                    && !s.contains("荷载") && !s.contains("地")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "id", propertyNameMap.get(s) + "id", databaseFieldNameMap.get(s) + "id", "", "字符串", "50", "", "单行文本", "", "", "", ""));
                entityList.add(new ExcelEntity(s + "签名", propertyNameMap.get(s + "签名"), databaseFieldNameMap.get(s + "签名"), "", "大文本", "", "", "附件上传", "", "", "", ""));
            } else if (s.contains("结论") || s.contains("备注")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "大文本", "", "", "多行文本", "", "", "", ""));
            } else if (s.contains("图")) {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "大文本", "", "", "附件上传", "", "", "", ""));
            } else if (s.contains("序号")) {
                entityList.add(new ExcelEntity("主表id", "zbid", "zbid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
            } else {
                entityList.add(new ExcelEntity(s, propertyNameMap.get(s), databaseFieldNameMap.get(s), "", "字符串", "50", "", "单行文本", "", "", "", ""));
            }
        }
        entityList.add(new ExcelEntity("创建人", "creater", "creater", "", "字符串", "50", "${currentUserName}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建人id", "createrId", "creater_id", "", "字符串", "50", "${currentUserId}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建人部门", "createrDep", "creater_dep", "", "字符串", "50", "${currentOrgName}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建人部门id", "createrDepId", "creater_dep_id", "", "字符串", "50", "${currentOrgId}", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("创建日期", "createTime", "create_time", "", "日期型", "", "${currentDateTime}", "日期控件", "yyyy-MM-dd", "", "", ""));
        entityList.add(new ExcelEntity("工程id", "projectId", "project_id", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("审批状态", "spzt", "spzt", "", "字符串", "50", "未审批", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("委托单id", "wtdid", "wtdid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        entityList.add(new ExcelEntity("记录id", "jlid", "jlid", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        // entityList.add(new ExcelEntity("vuekey", "", "", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        // entityList.add(new ExcelEntity("recordkey", "", "", "", "字符串", "50", "", "单行文本", "", "", "", ""));
        return entityList;
    }

}
