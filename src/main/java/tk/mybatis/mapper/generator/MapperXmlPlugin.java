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
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 通用Mapper生成器插件
 *
 * @author zcc
 */
public class MapperXmlPlugin extends PluginAdapter {
    private String target_api;
    private String target_service;


    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.target_api = this.properties.getProperty("target_api");
        this.target_service = this.properties.getProperty("target_service");
    }


    public boolean validate(List<String> warnings) {
        return true;
    }


    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List files = new ArrayList();



        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());


        Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        XmlElement root = new XmlElement("mapper");
        document.setRootElement(root);
        root.addAttribute(new Attribute("namespace","com.mk.dao." + target_service + "." + entityType.getShortName() + "Mapper"));


        GeneratedXmlFile gxf = new GeneratedXmlFile(document,
            toLowerCase(entityType.getShortName()) + "Mapper.xml",
            "mapper/" + target_service,
            target_api, false, this.context.getXmlFormatter());
        files.add(gxf);

        return files;
    }


    protected String toLowerCase(String tableName) {
        StringBuilder sb = new StringBuilder(tableName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }


}
