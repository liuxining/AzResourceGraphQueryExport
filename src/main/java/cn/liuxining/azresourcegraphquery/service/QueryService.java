package cn.liuxining.azresourcegraphquery.service;

import cn.liuxining.azresourcegraphquery.bean.KqlBean;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: QueryService </p >
 * <p>Description: QueryService </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 00:06
 * @version: v1.0
 */
public interface QueryService {

    Map<String, List<Map<String, String>>> getData(List<KqlBean> kqlBeanList);

}
