package cn.liuxining.azresourcegraphquery.service.impl;

import cn.liuxining.azresourcegraphquery.bean.KqlBean;
import cn.liuxining.azresourcegraphquery.config.KqlQueryConfig;
import cn.liuxining.azresourcegraphquery.service.QueryService;
import cn.liuxining.azresourcegraphquery.util.PatternUtil;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.resourcegraph.ResourceGraphManager;
import com.azure.resourcemanager.resourcegraph.models.QueryRequest;
import com.azure.resourcemanager.resourcegraph.models.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>Title: QueryServiceImpl </p >
 * <p>Description: QueryServiceImpl </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 00:06
 * @version: v1.0
 */
@Service
public class QueryServiceImpl implements QueryService {

    private static final Logger log = LoggerFactory.getLogger(QueryServiceImpl.class);

    @Autowired
    private KqlQueryConfig kqlQueryConfig;

    @Override
    public Map<String, List<Map<String, String>>> getData(List<KqlBean> kqlBeanList) {
        Map<String, List<Map<String, String>>> result = new HashMap<>(kqlBeanList.size());

        log.info("subscriptionIdList: {}.", kqlQueryConfig.getSubscriptionId());


        AzureProfile azureProfile = new AzureProfile(AzureEnvironment.AZURE_CHINA);
        DefaultAzureCredential azureCredential = new DefaultAzureCredentialBuilder().build();
        ResourceGraphManager manager = ResourceGraphManager.authenticate(azureCredential, azureProfile);


        for (KqlBean kqlBean:kqlBeanList) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("未知异常", e);
            }

            String strQuery = kqlBean.getKqlQuery();

            QueryRequest queryRequest = new QueryRequest()
                    .withSubscriptions(kqlQueryConfig.getSubscriptionId())
                    .withQuery(strQuery);

            QueryResponse response = manager.resourceProviders().resources(queryRequest);

            System.out.println("Records: " + response.totalRecords());
            System.out.println("Data:\n" + response.data());

//            List<String> headList = headMap.get(kqlBean.getName());

            List<Map<String, String>> itemResult = parseResponse(response);

            result.put(kqlBean.getName(), itemResult);

        }

        return result;
    }

    private List<Map<String, String>> parseResponse(QueryResponse response) {
        List<Map<String, String>> result = new ArrayList<>((int) response.totalRecords());

        List<String> responseDataList = getResponseDataList(response);

        if (responseDataList == null || responseDataList.size() == 0) {
            return result;
        }

        String data = responseDataList.get(0);
        String[] dataSplit = data.split("=");
        int dataPropertiesCount = dataSplit.length - 1;

        String dataRegexItem = "\\s*(.*?=.*?)\\s*,";
        StringBuilder dataRegexBuild = new StringBuilder();
        dataRegexBuild.append("^\\{");
        for (int i = 0;i < dataPropertiesCount;i++) {
            dataRegexBuild.append(dataRegexItem);
        }
        if (dataRegexBuild.charAt(dataRegexBuild.length() - 1) == ',') {
            dataRegexBuild.deleteCharAt(dataRegexBuild.length() - 1);
        }
        dataRegexBuild.append("\\}$");

        Map<String, String> dataColumnNameMap = getDataColumnName(responseDataList.get(0), dataRegexBuild.toString(), dataPropertiesCount);
        result.add(dataColumnNameMap);
        for (String respData:responseDataList) {
            Map<String, String> respDataMap = dataStr2Map(respData, dataRegexBuild.toString(), dataPropertiesCount);
            result.add(respDataMap);
        }

        return result;
    }

    private List<String> getResponseDataList(QueryResponse response) {
        String listRegexItem = "\\s*(\\{.*?\\})\\s*,";
        StringBuilder listRegexBuild = new StringBuilder();
        listRegexBuild.append("^\\[");
        for (int i = 0;i < response.totalRecords();i++) {
            listRegexBuild.append(listRegexItem);
        }
        if (listRegexBuild.charAt(listRegexBuild.length() - 1) == ',') {
            listRegexBuild.deleteCharAt(listRegexBuild.length() - 1);
        }
        listRegexBuild.append("\\]$");

        List<String> responseDataList = PatternUtil.getStrList(String.valueOf(response.data()), listRegexBuild.toString(), (int) response.totalRecords());

        return responseDataList;
    }

    private Map<String, String> dataStr2Map(String data, String dataRegex, Integer count) {
        Map<String, String> dataMap = new HashMap<>(count);
        List<String> respDataItem = PatternUtil.getStrList(data, dataRegex, count);
        for (String dataItem:respDataItem) {
            String[] split = dataItem.split("=");
            dataMap.put(split[0], split.length > 1 ? split[1] : "");
        }
        return dataMap;
    }

    private Map<String, String> getDataColumnName(String data, String dataRegex, Integer count) {
        Map<String, String> dataMap = new HashMap<>(count);
        List<String> respDataItem = PatternUtil.getStrList(data, dataRegex, count);
        for (int i = 0;i < respDataItem.size();i++) {
            String dataItem = respDataItem.get(i);
            String[] split = dataItem.split("=");
            dataMap.put(String.valueOf(i), split[0]);
        }
        return dataMap;
    }
}
