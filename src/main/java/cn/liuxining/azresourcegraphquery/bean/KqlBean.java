package cn.liuxining.azresourcegraphquery.bean;

import java.io.Serializable;

/**
 * <p>Title: KqlBean </p >
 * <p>Description: KqlBean </p >
 *
 * @author: liuxining
 * @date: 2021/7/4 00:06
 * @version: v1.0
 */
public class KqlBean implements Serializable {

    private String name;
    private String kqlQuery;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKqlQuery() {
        return kqlQuery;
    }

    public void setKqlQuery(String kqlQuery) {
        this.kqlQuery = kqlQuery;
    }

    @Override
    public String toString() {
        return "KqlBean{" +
                "name='" + name + '\'' +
                ", kqlQuery='" + kqlQuery + '\'' +
                '}';
    }
}
