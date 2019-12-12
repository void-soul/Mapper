/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.generator;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 通用Mapper生成器插件
 *
 * @author zcc
 */
public class ServicePlugin extends PluginAdapter {
    private String target;
    private String project;


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        target = this.properties.getProperty("target");
        project = this.properties.getProperty("project");
    }


    public boolean validate(List<String> warnings) {
        return true;
    }


    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List files = new ArrayList();

        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        //接口
        Interface interfacex = new Interface(new FullyQualifiedJavaType("com.mk.service." + target + "." + entityType.getShortName() + "Service"));
        interfacex.addImportedType(new FullyQualifiedJavaType("com.mk.domain." + entityType.getShortName()));
        interfacex.addImportedType(new FullyQualifiedJavaType("com.mk.util.base.BaseService"));
        interfacex.setVisibility(JavaVisibility.PUBLIC);
        interfacex.addSuperInterface(new FullyQualifiedJavaType("com.mk.util.base.BaseService<" + entityType.getShortName() + ">"));
        GeneratedJavaFile file = new GeneratedJavaFile(interfacex,
            "../../api/src/main/java" + project,
            this.context.getProperty("javaFileEncoding"),
            this.context.getJavaFormatter());
        files.add(file);


        //实现
        TopLevelClass topLevelClassImpl = new TopLevelClass(new FullyQualifiedJavaType("com.mk.service." + target + "." + entityType.getShortName() + "ServiceImpl"));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("com.alibaba.dubbo.config.annotation.Service"));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("com.mk.dao." + target + "." + entityType.getShortName() + "Mapper"));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("com.mk.domain." + entityType.getShortName()));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("com.mk.util.base.BaseMapper"));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("com.mk.util.base.BaseServiceImpl"));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("org.springframework.transaction.annotation.EnableTransactionManagement"));
        topLevelClassImpl.addImportedType(new FullyQualifiedJavaType("org.springframework.beans.factory.annotation.Autowired"));
        topLevelClassImpl.addSuperInterface(new FullyQualifiedJavaType("com.mk.service." + target + "." + entityType.getShortName() + "Service"));
        topLevelClassImpl.setSuperClass(new FullyQualifiedJavaType("com.mk.util.base.BaseServiceImpl<" + entityType.getShortName() + ">"));
        topLevelClassImpl.addAnnotation("@EnableTransactionManagement");
        topLevelClassImpl.addAnnotation("@Service");
        topLevelClassImpl.addAnnotation("@org.springframework.stereotype.Service");
        Field field = new Field();
        field.setName(toLowerCase(entityType.getShortName() + "Mapper"));
        field.setType(new FullyQualifiedJavaType("com.mk.dao." + target + "." + entityType.getShortName() + "Mapper"));
        field.setVisibility(JavaVisibility.PRIVATE);
        field.addAnnotation("@Autowired");
        topLevelClassImpl.addField(field);

        Method method = new Method();
        method.setName("getBaseMapper");
        method.setReturnType(new FullyQualifiedJavaType("com.mk.util.base.BaseMapper"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        method.addBodyLine("return " + field.getName() + ";");
        topLevelClassImpl.addMethod(method);


        file = new GeneratedJavaFile(topLevelClassImpl,
            "../../service/service-" + target + "/src/main/java" + project,
            this.context.getProperty("javaFileEncoding"),
            this.context.getJavaFormatter());
        files.add(file);

        return files;
    }


    protected String toLowerCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }
}
