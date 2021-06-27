# 介绍一款好用的数据库文档生成工具

- screw - 简洁好用的数据库表结构文档生成工具 [https://gitee.com/leshalv/screw](https://gitee.com/leshalv/screw)
- 首先我们来看一下实现的效果
[EasyBoot数据库设计文档](https://gitee.com/zmzhou-star/easyboot/blob/master/docs/EasyBoot%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3.md ':include :type=markdown')
- 支持生成WORD、HTML和Markdown3种文档类型
- 支持MySQL 、Oracle、 SqlServer、PostgreSQL、MariaDB等主流数据库

## 使用方式

### 引入依赖

```xml
<dependency>
    <groupId>cn.smallbun.screw</groupId>
    <artifactId>screw-core</artifactId>
    <version>1.0.5</version>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <artifactId>logback-classic</artifactId>
            <groupId>ch.qos.logback</groupId>
        </exclusion>
    </exclusions>
</dependency>
```

### 编写如下代码

```java
import java.util.ArrayList;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
/**
 * 数据库文档生成工具
 *
 * @author zmzhou
 * @since 2020/08/18 16:06
 */
public final class DBGenerationTool {
    /**
     * 生成文件路径
     */
    private static final String FILE_OUTPUT_DIR = "docs/";
    /**
     * 文件名
     */
    private static final String FILE_NAME = "EasyBoot数据库设计文档";
    /**
     * 构造器
     *
     * @author zmzhou
     * @date 2020/08/18 16:10
     */
    private DBGenerationTool() {
    }
    /**
     * 生成数据库文档
     *
     * @param args 参数
     * @author zmzhou
     * @date 2020/08/18 16:14
     */
    public static void main(String[] args) {
        documentGeneration(EngineFileType.HTML);
        documentGeneration(EngineFileType.WORD);
        documentGeneration(EngineFileType.MD);
    }
    /**
     * 生成数据库文档
     *
     * @param fileType 文档类型
     * @author zmzhou
     * @date 2020/08/18 16:45
     */
    private static void documentGeneration(EngineFileType fileType) {
        //数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/easyboot?useUnicode=true&characterEncoding=utf8" +
            "&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&useInformationSchema=true");
        hikariConfig.setUsername("easyboot");
        hikariConfig.setPassword("Zmzhou.V587");
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        //生成配置
        EngineConfig engineConfig = EngineConfig.builder()
            //生成文件路径
            .fileOutputDir(FILE_OUTPUT_DIR)
            //打开目录
            .openOutputDir(true)
            //文件类型
            .fileType(fileType)
            //生成模板实现
            .produceType(EngineTemplateType.freemarker)
            //自定义文件名称
            .fileName(FILE_NAME).build();
        //忽略表
        ArrayList<String> ignoreTableName = new ArrayList<>();
        ignoreTableName.add("test_user");
        //忽略表前缀
        ArrayList<String> ignorePrefix = new ArrayList<>();
        ignorePrefix.add("test_");
        //忽略表后缀
        ArrayList<String> ignoreSuffix = new ArrayList<>();
        ignoreSuffix.add("_test");
        ProcessConfig processConfig = ProcessConfig.builder()
            //指定生成逻辑、当存在指定表、指定表前缀、指定表后缀时，将生成指定表，其余表不生成、并跳过忽略表配置
            //根据名称指定表生成
            .designatedTableName(new ArrayList<>())
            //根据表前缀生成
            .designatedTablePrefix(new ArrayList<>())
            //根据表后缀生成
            .designatedTableSuffix(new ArrayList<>())
            //忽略表名
            .ignoreTableName(ignoreTableName)
            //忽略表前缀
            .ignoreTablePrefix(ignorePrefix)
            //忽略表后缀
            .ignoreTableSuffix(ignoreSuffix).build();
        //配置
        Configuration config = Configuration.builder()
            //版本
            .version("1.0.0")
            //描述
            .description(FILE_NAME)
            .title(FILE_NAME)
            //数据源
            .dataSource(dataSource)
            //生成配置
            .engineConfig(engineConfig)
            //生成配置
            .produceConfig(processConfig)
            .build();
        //执行生成
        new DocumentationExecute(config).execute();
    }
}
```

### 执行代码，即可生成数据库设计文档

[http://120.78.87.244/easyboot-database.html](http://120.78.87.244/easyboot-database.html)
