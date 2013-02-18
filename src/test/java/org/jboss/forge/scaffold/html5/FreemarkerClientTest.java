package org.jboss.forge.scaffold.html5;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.core.IsNull;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreemarkerClientTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient();
    }
    
    @Test
    public void testGenerateEntityModule() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "scripts/entityModule.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateDetailPartial() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "partials/detail.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchPartial() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "partials/search.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateIndex() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        List<String> entityNames = new ArrayList<String>();
        entityNames.add("SampleEntity");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        root.put("projectId", "scaffold");
        root.put("projectTitle", "scaffold");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "index.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }

    @Test
    public void testGenerateAngularApplication() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        List<String> entityNames = new ArrayList<String>();
        entityNames.add("SampleEntity");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        root.put("properties", entityAttributeProperties);
        root.put("projectId", "scaffold");
        root.put("projectTitle", "scaffold");
        String output = freemarkerClient.processFTL(root, "scripts/app.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateFilter() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        List<String> entityNames = new ArrayList<String>();
        entityNames.add("SampleEntity");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        root.put("projectId", "scaffold");
        root.put("projectTitle", "scaffold");
        String output = freemarkerClient.processFTL(root, "scripts/filters.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }

}
