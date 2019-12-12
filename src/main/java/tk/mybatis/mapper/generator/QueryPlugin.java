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

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.*;

/**
 * 通用Mapper生成器插件
 *
 * @author zcc
 */
public class QueryPlugin extends PluginAdapter {
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


    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List files = new ArrayList();



        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        Calendar calendar = Calendar.getInstance();
        String time = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH)) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));


        Document document = new Document();
        XmlElement root = new XmlElement("beans");
        document.setRootElement(root);
        root.addAttribute(new Attribute("xmlns","http://www.springframework.org/schema/beans"));
        root.addAttribute(new Attribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"));
        root.addAttribute(new Attribute("xmlns:eme","http://code.mk.com/schema/eme"));
        root.addAttribute(new Attribute("xsi:schemaLocation","http://www.springframework.org/schema/beans\nhttp://www.springframework.org/schema/beans/spring-beans-3.0.xsd\nhttp://code.mk.com/schema/eme\nhttp://code.mk.com/schema/eme/eme.xsd"));

        XmlElement query = new XmlElement("eme:query");
        query.addAttribute(new Attribute("remark","查询" + tableName + "列表"));
        query.addAttribute(new Attribute("id",target + "_select_" + tableName));
        query.addAttribute(new Attribute("version","1.0.0"));
        query.addAttribute(new Attribute("author","generator"));
        query.addAttribute(new Attribute("date",time));

        XmlElement select = new XmlElement("eme:select");
        StringBuilder sb = new StringBuilder();
        introspectedTable.getAllColumns().forEach(item -> sb.append(item.getActualColumnName() + ","));
        select.addElement(new TextElement("SELECT\n" + sb.substring(0,sb.length() - 1) + "\nFROM\n" + tableName));
        query.addElement(select);

        XmlElement count = new XmlElement("eme:count");
        count.addElement(new TextElement("SELECT\nCOUNT(1)\nFROM\n" + tableName));
        query.addElement(count);
        root.addElement(query);



        GeneratedXmlFile gxf = new GeneratedXmlFile(document,
            toLowerCase(entityType.getShortName()) + "Query.xml",
            "query/" + target,
            "../../api/src/main/resources" + project, false, this.context.getXmlFormatter());
        files.add(gxf);

        return files;
    }


    protected String toLowerCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }


}
