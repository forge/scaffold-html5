package org.jboss.forge.scaffold.html5.freemarker;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.jboss.forge.scaffold.html5.FreemarkerClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;
import org.metawidget.util.simple.StringUtils;

public class FreemarkerClientPartialsNToManyPropertyTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient();
    }
    
    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        Map<String, String> ordersProperties = new HashMap<String, String>();
        String oneToManyProperty = "orders";
        ordersProperties.put("name", oneToManyProperty);
        ordersProperties.put("type", "java.lang.String");
        ordersProperties.put("n-to-many", "true");
        ordersProperties.put("parameterized-type", "com.example.scaffoldtester.model.StoreOrder");
        ordersProperties.put("type", "java.util.Set");
        ordersProperties.put("simpleType", "StoreOrder");
        ordersProperties.put("optionLabel", "id");
        
        Map<String, Object> root = new HashMap<String, Object>();
        String entityName = "SampleEntity";
        root.put("entityName", entityName);
        root.put("property", ordersProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.control-group > div.controls");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements repeaterElement = nToManyWidgetElement.select(" > div");
        assertThat(repeaterElement, notNullValue());
        assertThat(repeaterElement.attr("ng-repeat"), equalTo(oneToManyProperty + "Element in " + StringUtils.decapitalize(entityName) + "." + oneToManyProperty));
        
        Elements widgetCollectionAddElement = nToManyWidgetElement.select(" > button");
        assertThat(widgetCollectionAddElement, notNullValue());
        assertThat(widgetCollectionAddElement.attr("ng-click"), equalTo("add" + oneToManyProperty + "()"));
        
        Elements selectElement = repeaterElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(oneToManyProperty +"{{$index}}"));
        assertThat(selectElement.attr("ng-model"), equalTo(StringUtils.decapitalize(entityName)+"."+oneToManyProperty+"[$index]"));
        String collectionElement = oneToManyProperty.substring(0, 1);
        String optionsExpression = collectionElement +" as " + collectionElement +".id for "+ collectionElement +" in " + oneToManyProperty + "List";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }
    
    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        Map<String, String> usersProperties = new HashMap<String, String>();
        String manyToManyProperty = "users";
        usersProperties.put("name", manyToManyProperty);
        usersProperties.put("type", "java.lang.String");
        usersProperties.put("n-to-many", "true");
        usersProperties.put("parameterized-type", "com.example.scaffoldtester.model.UserIdentity");
        usersProperties.put("type", "java.util.Set");
        usersProperties.put("simpleType", "UserIdentity");
        usersProperties.put("optionLabel", "id");
        
        Map<String, Object> root = new HashMap<String, Object>();
        String entityName = "SampleEntity";
        root.put("entityName", entityName);
        root.put("property", usersProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.control-group > div.controls");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements repeaterElement = nToManyWidgetElement.select(" > div");
        assertThat(repeaterElement, notNullValue());
        assertThat(repeaterElement.attr("ng-repeat"), equalTo(manyToManyProperty + "Element in " + StringUtils.decapitalize(entityName) + "." + manyToManyProperty));
        
        Elements widgetCollectionAddElement = nToManyWidgetElement.select(" > button");
        assertThat(widgetCollectionAddElement, notNullValue());
        assertThat(widgetCollectionAddElement.attr("ng-click"), equalTo("add" + manyToManyProperty + "()"));
        
        Elements selectElement = repeaterElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(manyToManyProperty +"{{$index}}"));
        assertThat(selectElement.attr("ng-model"), equalTo(StringUtils.decapitalize(entityName)+"."+manyToManyProperty+"[$index]"));
        String collectionElement = manyToManyProperty.substring(0, 1);
        String optionsExpression = collectionElement +" as " + collectionElement +".id for "+ collectionElement +" in " + manyToManyProperty + "List";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }

}
