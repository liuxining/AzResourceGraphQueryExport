package cn.liuxining.azresourcegraphquery.runner;

import cn.liuxining.azresourcegraphquery.bean.KqlBean;
import cn.liuxining.azresourcegraphquery.service.QueryService;
import cn.liuxining.azresourcegraphquery.util.ExcelUtil;
import cn.liuxining.azresourcegraphquery.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * <p>Title: KqlExecRunner </p >
 * <p>Description: KqlExecRunner </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 00:06
 * @version: v1.0
 */
@Component
public class KqlExecRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(KqlExecRunner.class);

    @Autowired
    private QueryService queryService;

    @Value("${file-name.prefix}")
    private String fileNamePrefix;

    @Value("${file-name.date-format}")
    private String fileNameDateFormat;

    @Value("${kql-config.file-name}")
    private String kqlConfigFileName;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("kqlConfigFileName: {}.", kqlConfigFileName);

        String kqlConfigContent = FileUtil.readFile(kqlConfigFileName);
        log.info("kqlConfigContent: {}.", kqlConfigContent);

        List<KqlBean> kqlBeanList = getKqlBeanList(kqlConfigContent);

        Map<String, List<Map<String, String>>> data = queryService.getData(kqlBeanList);
        System.out.println(data);

        Calendar instance = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(fileNameDateFormat);
        String dateStr = dateFormat.format(instance.getTime());
        ExcelUtil.writeExcelCustomHead(fileNamePrefix + dateStr + ".xlsx", data);

    }

    private List<KqlBean> getKqlBeanList(String kqlConfig) {
        String[] split = kqlConfig.split("---");
        List<KqlBean> result = new ArrayList<>(split.length);
        for (String item:split) {
            KqlBean kqlBean = new KqlBean();
            int kqlQueryIndex = item.indexOf("kqlQuery");
            String nameConfig = item.substring(0, kqlQueryIndex);
            String kqlQueryConfig = item.substring(kqlQueryIndex);
            String[] nameSplit = nameConfig.split(":");
            kqlBean.setName(nameSplit[1]);
            String[] kqlQuerySplit = kqlQueryConfig.split(":");
            kqlBean.setKqlQuery(kqlQuerySplit[1]);
            result.add(kqlBean);
        }
        return result;
    }

    class StrToHeadNameFunction implements Function<String, String> {
        @Override
        public String apply(String s) {
            int equalIndex = s.indexOf("=");
            if (equalIndex > 0) {
                return s.substring(0, equalIndex);
            }
            return s.replaceAll("\\.", "_").trim();
        }
    }

}
