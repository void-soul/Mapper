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
public class ControllerPlugin extends PluginAdapter {
    private String target_service;
    private String target_app;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.target_app = this.properties.getProperty("target_app") + this.properties.getProperty("create");
        this.target_service = this.properties.getProperty("target_service");
    }


    public boolean validate(List<String> warnings) {
        return true;
    }


    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List files = new ArrayList();
        FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        TopLevelClass controller = new TopLevelClass(new FullyQualifiedJavaType("com.mk.controller." + target_service + "." + entityType.getShortName() + "Controller"));

        controller.addImportedType(new FullyQualifiedJavaType("com.alibaba.dubbo.config.annotation.Reference"));
        controller.addImportedType(new FullyQualifiedJavaType("com.alibaba.fastjson.JSON"));

        controller.addImportedType(new FullyQualifiedJavaType("javax.servlet.http.HttpServletRequest"));
        controller.addImportedType(new FullyQualifiedJavaType("javax.servlet.http.HttpServletResponse"));

        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.stereotype.Controller"));
        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.ui.Model"));
        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMapping"));
        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestParam"));
        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.PathVariable"));
        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.RequestMethod"));
        controller.addImportedType(new FullyQualifiedJavaType("org.springframework.web.bind.annotation.ResponseBody"));

        controller.addImportedType(new FullyQualifiedJavaType("com.mk.util.common.StringUtils"));
        controller.addImportedType(new FullyQualifiedJavaType("com.mk.annotation.Log"));
        controller.addImportedType(new FullyQualifiedJavaType("com.mk.util.base.GlobalValues"));
        controller.addImportedType(new FullyQualifiedJavaType("com.mk.util.base.AjaxResponse"));
        controller.addImportedType(new FullyQualifiedJavaType("com.mk.domain." + entityType.getShortName()));
        controller.addImportedType(new FullyQualifiedJavaType("com.mk.service." + target_service + "." + entityType.getShortName() + "Service"));

        controller.addAnnotation("@Controller");
        controller.addAnnotation("@RequestMapping(path = \"/" + toLowerCase(entityType.getShortName()) + "\")");
        controller.setVisibility(JavaVisibility.PUBLIC);

        Field field = new Field();
        field.setName(toLowerCase(entityType.getShortName() + "Service"));
        field.setType(new FullyQualifiedJavaType("com.mk.service." + target_service + "." + entityType.getShortName() + "Service"));
        field.setVisibility(JavaVisibility.PRIVATE);
        field.addAnnotation("@Reference");
        controller.addField(field);


        Method method = new Method();
        method.setName("index");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@RequestMapping(path = \"/a/index.html\")");
        method.addAnnotation("@Log(\"显示" + entityType.getShortName() + "首页\")");
        method.addBodyLine("return \"/" + toLowerCase(entityType.getShortName()) + "/index\";");
        controller.addMethod(method);


        method = new Method();
        method.setName("add");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@RequestMapping(path = \"/a/add.html\" , method = RequestMethod.GET)");
        method.addAnnotation("@Log(\"显示" + entityType.getShortName() + "新增页面\")");
        Parameter parameter = new Parameter(new FullyQualifiedJavaType("org.springframework.ui.Model"), "model");
        method.addParameter(parameter);
        method.addBodyLine(entityType.getShortName() + " " + toLowerCase(entityType.getShortName()) + " = new " + entityType.getShortName() + "();");
        method.addBodyLine("model.addAttribute(\"" + toLowerCase(entityType.getShortName()) + "\"," + toLowerCase(entityType.getShortName()) + ");");
        method.addBodyLine("model.addAttribute(\"isAdd\",true);");
        method.addBodyLine("return \"/" + toLowerCase(entityType.getShortName()) + "/form\";");
        controller.addMethod(method);


        method = new Method();
        method.setName("update");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@RequestMapping(path = \"/a/update/{id}.html\" , method = RequestMethod.GET)");
        method.addAnnotation("@Log(\"显示" + entityType.getShortName() + "修改页面\")");
        parameter = new Parameter(new FullyQualifiedJavaType("java.lang.String"), "id");
        parameter.addAnnotation("@PathVariable(\"id\")");
        method.addParameter(parameter);
        parameter = new Parameter(new FullyQualifiedJavaType("org.springframework.ui.Model"), "model");
        method.addParameter(parameter);
        method.addBodyLine(entityType.getShortName() + " " + toLowerCase(entityType.getShortName()) + " = " + toLowerCase(entityType.getShortName() + "Service") + ".selectByPrimaryKey(id);");
        method.addBodyLine("model.addAttribute(\"" + toLowerCase(entityType.getShortName()) + "\"," + toLowerCase(entityType.getShortName()) + ");");
        method.addBodyLine("model.addAttribute(\"isAdd\",false);");
        method.addBodyLine("return \"/" + toLowerCase(entityType.getShortName()) + "/form\";");
        controller.addMethod(method);


        method = new Method();
        method.setName("save");
        method.setReturnType(new FullyQualifiedJavaType("java.lang.String"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@RequestMapping(path = \"/a/save.html\" , method = RequestMethod.POST)");
        method.addAnnotation("@Log(\"保存" + entityType.getShortName() + "\")");
        parameter = new Parameter(new FullyQualifiedJavaType("com.mk.domain." + entityType.getShortName()), toLowerCase(entityType.getShortName()));
        method.addParameter(parameter);
        parameter = new Parameter(new FullyQualifiedJavaType("boolean"), "isAdd");
        method.addParameter(parameter);
        method.addBodyLine("if(isAdd){");
        method.addBodyLine(toLowerCase(entityType.getShortName() + "Service.insertSelective(" + toLowerCase(entityType.getShortName()) + ");"));
        method.addBodyLine("}else{");
        method.addBodyLine(toLowerCase(entityType.getShortName() + "Service.updateByPrimaryKeySelective(" + toLowerCase(entityType.getShortName()) + ");"));
        method.addBodyLine("}");
        method.addBodyLine("return GlobalValues.SUCCESS.value();");
        controller.addMethod(method);


        method = new Method();
        method.setName("del");
        method.setReturnType(new FullyQualifiedJavaType("com.mk.util.base.AjaxResponse"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@RequestMapping(path = \"/a/del/{id}.json\" , method = RequestMethod.POST)");
        method.addAnnotation("@ResponseBody");
        method.addAnnotation("@Log(value = \"删除ID为{id}的记录" + entityType.getShortName() + "\",explanation = true)");
        parameter = new Parameter(new FullyQualifiedJavaType("java.lang.String"), "id");
        parameter.addAnnotation("@PathVariable(\"id\")");
        method.addParameter(parameter);
        method.addBodyLine("return AjaxResponse.OK("+ toLowerCase(entityType.getShortName() + "Service") + ".deleteByPrimaryKey(id));");
        controller.addMethod(method);


        GeneratedJavaFile file = new GeneratedJavaFile(controller,
            "../../webapp/" + target_app + "/src/main/java",
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
