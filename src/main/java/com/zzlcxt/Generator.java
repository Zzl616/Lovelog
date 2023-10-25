package com.zzlcxt;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Scanner;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description: mybatisplus代码自动生成
 * @date: 2023-05-24 9:41
 */

public class Generator {

    private static String driverName ="com.mysql.cj.jdbc.Driver";


    private static String mysqlUrl ="jdbc:mysql://localhost:3306/eating?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8";

    private static String Username ="root";

    private static String Password ="root";
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入").append(tip).append("：");
        System.out.println(help);
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        AutoGenerator autoGenerator = new AutoGenerator();
        //数据库配置
        DataSourceConfig dataSource = new DataSourceConfig();
        dataSource.setDriverName(driverName);
        dataSource.setUrl(mysqlUrl);
        dataSource.setUsername(Username);
        dataSource.setPassword(Password);
        System.out.println(driverName);
        System.out.println(Username);
        // 1. 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setOutputDir(System.getProperty("user.dir") + "/src/main/java")
                .setFileOverride(true)
                // 是否覆盖已有文件
//                .setSwagger2(true)
                // 实体属性 Swagger2 注解
//                .setBaseResultMap(true)
                // XML ResultMap
//                .setBaseColumnList(true)
                // XML columList
                .setOpen(false)
                // 是否打开生成目录
                .setAuthor("张子龙")
                // 设置作者名称
                .setServiceName("%sService")
                // Service 命名方式
                .setServiceImplName("%sServiceImpl")
                // ServiceImpl 命名方式
                .setMapperName("%sMapper")
                // Mapper 命名方式
                .setXmlName("%sMapper")
                // Xml 命名方式
                .setEntityName("%s")
                // Entity 命名方式
                .setControllerName("%sController")
                // Controller 命名方式
                .setIdType(IdType.ASSIGN_ID);
                // 主键类型
        //包名配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.zzlcxt")
                // 设置父包名
                .setEntity("modle")
                // 设置实体类包名
                .setMapper("dao")
                // 设置Mapper接口包名
                .setService("service")
                // 设置Service接口包名
                .setServiceImpl("service.impl")
                // 设置Service实现类包名
                .setController("controller");
                // 设置Controller类包名
        // 4. 策略配置
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true)
                // 驼峰命名转换为大写字母（例如：user_info -> UserInfo）
                .setNaming(NamingStrategy.underline_to_camel)
                // 数据库表名和字段名下划线转驼峰（例如：user_name -> userName）
                .setInclude(scanner("表名，多张表用逗号分隔").split(","))
                // 设置要生成的表名
                .setTablePrefix(pkConfig.getModuleName() + "_")
                // 设置数据表前缀
                .setEntityLombokModel(true)
                // 实体类使用lombok注解
                .setRestControllerStyle(true)
                // REST风格控制器
                .setControllerMappingHyphenStyle(true)
                // URL中驼峰转连字符（例如：http://localhost:8080/user-info -> /user-info）
                .setLogicDeleteFieldName("is_deleted")
                // 逻辑删除字段名
                .setEntityBooleanColumnRemoveIsPrefix(true)
                // Boolean类型字段是否移除is前缀（例如：is_deleted -> deleted）
                .setVersionFieldName("version");
                // 乐观锁字段名
        // 5. 整合配置
        autoGenerator.setGlobalConfig(config)
                // 设置全局配置
                .setDataSource(dataSource)
                // 设置数据源配置
                .setPackageInfo(pkConfig)
                // 设置包名策略配置
                .setStrategy(stConfig)
                // 设置策略配置
                .setTemplateEngine(new VelocityTemplateEngine());
                // 设置模板引擎
        autoGenerator.execute();
    }
}
