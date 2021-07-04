# AzResourceGraphSelectQuery

Azure Resource Graph查询结果导出为Excel文件

## 用法

1. az login登录Azure账户

2. mvn clean package

3. 编写查询文件

   ```shell
   name:xxx
   kqlQuery:xxx
   ---
   name:xxx
   kqlQuery:xxx
   ---
   name:xxx
   kqlQuery:xxx
   ```

   各个查询语句用---分隔，name为该查询结果导出的sheet名称，kqlQuery为查询语句

4. 执行查询

   ```shell
   java -Dfile-name.prefix=/xxx/AzResourceExport -Dkql-config.file-name=/xxx/kqlConfig -Dazure.subscription-id=xxxx-xxxx,xxxx-xxxx -jar target/AzResourceGraphSelectQuery-1.0-SNAPSHOT.jar
   ```

   - file-name.prefix: 为导出的文件前缀，导出结果文件命名格式：文件前缀+yyyyMMddHHmmss.xlsx
   - kql-config.file-name: 为上一步查询文件的绝对路径
   - azure.subscription-id: 为Azure订阅的订阅ID, 多个ID用','分隔

   