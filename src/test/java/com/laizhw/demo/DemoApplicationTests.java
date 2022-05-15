package com.laizhw.demo;

import com.alibaba.excel.EasyExcel;
import com.google.common.base.CaseFormat;
import com.laizhw.demo.entity.ExcelEntity;
import com.laizhw.demo.utils.AliyunTranslateUtil;
import com.laizhw.demo.utils.PinYinUtil;
import com.laizhw.demo.utils.WordToExcelUtil;
import org.apache.commons.text.CaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {
    long start = 0;


    @Test
    void contextLoads() {
    }


    @BeforeEach
    public void test00() {
        start = System.currentTimeMillis();
    }

    @AfterEach
    public void test() {
        System.out.println("用时：" + (System.currentTimeMillis() - start) + "ms");
    }


    @Test
    public void testTranslate() {
        String s = AliyunTranslateUtil.aliyunTranslate("可溶物含量测定");
        System.out.println("s = " + s);
        String s1 = CaseUtils.toCamelCase(s, false, ' ');
        System.out.println("s1 = " + s1);
        String s2 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s1);
        System.out.println("s2 = " + s2);
    }

    @Test
    public void testTranslate1() {
        String s = PinYinUtil.getFirstPinyin("水泥基灌浆材料检测依据");
        System.out.println("s = " + s);
    }

    /**
     * 金属
     */
    @Test
    public void test01() throws Exception {

        String path = "C:\\Users\\Administrator\\Desktop\\新建 DOCX 文档.docx";
        // String path = "C:\\Users\\MSI\\Desktop\\新建 DOC 文档.docx";
        Map<String, Object> map = WordToExcelUtil.importWord(path);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) map.get("data");
        String fileName = map.get("title") != null ? map.get("title") + ".xlsx" : "模板.xlsx";
        EasyExcel.write(fileName, ExcelEntity.class).sheet("模板").doWrite(WordToExcelUtil.data(list, "en"));

    }

    /**
     * 土建
     */
    @Test
    public void test02() throws Exception {

        // String[] paths = {"C:\\Users\\Administrator\\Desktop\\记录模板.docx"};
        // String[] paths = {"C:\\Users\\Administrator\\Desktop\\报告模板.docx"};
        // String[] paths = {"C:\\Users\\Administrator\\Desktop\\委托单模板.docx"};
        String[] paths = {"C:\\Users\\Administrator\\Desktop\\委托单模板.docx", "C:\\Users\\Administrator\\Desktop\\报告模板.docx", "C:\\Users\\Administrator\\Desktop\\记录模板.docx"};
        for (String path : paths) {
            Map<String, Object> map = WordToExcelUtil.importWord(path);
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) map.get("data");
            String fileName = map.get("title") + ".xlsx";
            EasyExcel.write(fileName, ExcelEntity.class).sheet("模板").doWrite(WordToExcelUtil.data2(list, ""));
        }
    }

}
