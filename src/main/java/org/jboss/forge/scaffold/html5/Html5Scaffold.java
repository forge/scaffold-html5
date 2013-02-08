package org.jboss.forge.scaffold.html5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.parser.java.Field;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffold.AccessStrategy;
import org.jboss.forge.scaffold.ScaffoldProvider;
import org.jboss.forge.scaffold.TemplateStrategy;
import org.jboss.forge.scaffold.html5.metawidget.inspector.ForgeInspector;
import org.jboss.forge.scaffold.html5.metawidget.inspector.propertystyle.ForgePropertyStyle;
import org.jboss.forge.scaffold.html5.metawidget.inspector.propertystyle.ForgePropertyStyleConfig;
import org.jboss.forge.scaffold.util.ScaffoldUtil;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.spec.javaee.CDIFacet;
import org.jboss.forge.spec.javaee.EJBFacet;
import org.jboss.forge.spec.javaee.PersistenceFacet;
import org.metawidget.inspector.beanvalidation.BeanValidationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jpa.JpaInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 */
@Alias("html5")
@Help("HTML5 scaffolding")
@RequiresFacet({ WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class, EJBFacet.class, CDIFacet.class })
public class Html5Scaffold extends BaseFacet implements ScaffoldProvider {

    protected ShellPrompt prompt;

    @Inject
    public Html5Scaffold(final ShellPrompt prompt) {
        this.prompt = prompt;
    }

    @Override
    public boolean install() {
        // TODO Add Maven artifacts to the project here. Required facet installation is already handled by the class-level
        // @RequiresFacet annotation.
        return true;
    }

    @Override
    public boolean isInstalled() {
        // TODO Looks unnecessary for this scaffold. See comments on install(). We could extract install() and installed() out.
        return true;
    }

    @Override
    public List<Resource<?>> setup(String targetDir, Resource<?> template, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);

        // Setup static resources.

        // TODO: Make this dynamic and dependent on the HTML5 framework. Choose AngularJS libraries (or Backbone.js, Aerogear or
        // any other) based on configuration.
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/styles/bootstrap.css"), getClass()
                .getResourceAsStream("/scaffold/angularjs/styles/bootstrap.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/styles/main.css"), getClass()
                .getResourceAsStream("/scaffold/angularjs/styles/main.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/styles/bootstrap-responsive.css"),
                getClass().getResourceAsStream("/scaffold/angularjs/styles/bootstrap-responsive.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/scripts/vendor/angular.js"), getClass()
                .getResourceAsStream("/scaffold/angularjs/scripts/vendor/angular.js"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/scripts/vendor/angular-resource.js"),
                getClass().getResourceAsStream("/scaffold/angularjs/scripts/vendor/angular-resource.js"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/images/forge-logo.png"), getClass()
                .getResourceAsStream("/scaffold/angularjs/images/forge-logo.png"), overwrite));
        return result;
    }

    @Override
    public List<Resource<?>> generateTemplates(String targetDir, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        return result;
    }

    @Override
    public List<Resource<?>> generateIndex(String targetDir, Resource<?> template, boolean overwrite) {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(getClass(), "/scaffold/angularjs");
        config.setObjectWrapper(new DefaultObjectWrapper());

        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        List<String> entityNames = new ArrayList<String>();
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
        FileResource<?> partialsDirectory = web.getWebResource("partials");
        for (Resource<?> resource : partialsDirectory.listResources()) {
            entityNames.add(resource.getName());
        }
        Map root = new HashMap();
        root.put("entityNames", entityNames);
        MetadataFacet metadata = this.project.getFacet(MetadataFacet.class);
        root.put("project", metadata);

        try {
            Template indexTemplate = config.getTemplate("index.html.ftl");
            Writer contents = new StringWriter();
            indexTemplate.process(root, contents);
            contents.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource("index.html"), contents.toString(), overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

        try {
            Template appJsTemplate = config.getTemplate("scripts/app.js.ftl");
            Writer contents = new StringWriter();
            appJsTemplate.process(root, contents);
            contents.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource("scripts/app.js"), contents.toString(),
                    overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

        try {
            Template controllerTemplate = config.getTemplate("scripts/filters.js.ftl");
            Writer contents = new StringWriter();
            controllerTemplate.process(root, contents);
            contents.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource("scripts/filters.js"), contents.toString(),
                    overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Resource<?>> generateFromEntity(String targetDir, Resource<?> template, JavaClass entity, boolean overwrite) {
        System.out.println("Generating artifacts from Entity:" + entity.getQualifiedName());
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(getClass(), "/scaffold/angularjs");
        config.setObjectWrapper(new DefaultObjectWrapper());

        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
        
        Map root = new HashMap();
        // TODO: Provide a 'utility' class for allowing transliteration across language naming schemes
        // We need this to use contextual naming schemes instead of performing toLowerCase etc. in FTLs.
        root.put("entityName", entity.getName());
        
        ForgePropertyStyleConfig forgePropertyStyleConfig = new ForgePropertyStyleConfig();
        forgePropertyStyleConfig.setProject(project);
        BaseObjectInspectorConfig baseObjectInspectorConfig = new BaseObjectInspectorConfig();
        baseObjectInspectorConfig.setPropertyStyle(new ForgePropertyStyle(forgePropertyStyleConfig));
        
        PropertyTypeInspector propertyTypeInspector = new PropertyTypeInspector(baseObjectInspectorConfig);
        
        ForgeInspector forgeInspector = new ForgeInspector(baseObjectInspectorConfig);
        
        JpaInspectorConfig jpaInspectorConfig = new JpaInspectorConfig();
        jpaInspectorConfig.setHideIds(true);
        jpaInspectorConfig.setHideVersions(true);
        jpaInspectorConfig.setHideTransients(true);
        jpaInspectorConfig.setPropertyStyle(new ForgePropertyStyle(forgePropertyStyleConfig));
        JpaInspector jpaInspector = new JpaInspector(jpaInspectorConfig);
        
        BeanValidationInspector beanValidationInspector = new BeanValidationInspector(baseObjectInspectorConfig);
        
        CompositeInspectorConfig compositeInspectorConfig = new CompositeInspectorConfig();
        compositeInspectorConfig.setInspectors(propertyTypeInspector,forgeInspector,jpaInspector,beanValidationInspector);
        CompositeInspector compositeInspector = new CompositeInspector(compositeInspectorConfig);
        
        Element inspectionResult = compositeInspector.inspectAsDom(null, entity.getQualifiedName(), null);
        Element inspectedEntity = XmlUtils.getFirstChildElement( inspectionResult );
        System.out.println(XmlUtils.nodeToString(inspectedEntity, true));
        
        Element inspectedProperty = XmlUtils.getFirstChildElement(inspectedEntity);
        List<Map<String,String>> viewPropertyAttributes = new ArrayList<Map<String,String>>();  
        while (inspectedProperty != null) {
            System.out.println(XmlUtils.nodeToString(inspectedProperty, true));
            Map<String, String> propertyAttributes = XmlUtils.getAttributesAsMap(inspectedProperty);
            
            // Canonicalize all numerical types in Java to "number" for HTML5 form input type support
            String propertyType = propertyAttributes.get("type");
            if (propertyType.equals(short.class.getName()) || propertyType.equals(int.class.getName())
                    || propertyType.equals(long.class.getName()) || propertyType.equals(float.class.getName())
                    || propertyType.equals(double.class.getName()) || propertyType.equals(Short.class.getName())
                    || propertyType.equals(Integer.class.getName()) || propertyType.equals(Long.class.getName())
                    || propertyType.equals(Float.class.getName()) || propertyType.equals(Double.class.getName())) {
                propertyAttributes.put("type", "number");
            }

            // Extract simple type name of the relationship types
            String manyToOneRel = propertyAttributes.get("many-to-one");
            if("true".equals(manyToOneRel)){
                String manyToOneType = propertyAttributes.get("type");
                propertyAttributes.put("simpleType", getSimpleName(manyToOneType));
            }
            String oneToOneRel = propertyAttributes.get("one-to-one");
            if("true".equals(oneToOneRel)){
                String oneToOneType = propertyAttributes.get("type");
                propertyAttributes.put("simpleType", getSimpleName(oneToOneType));
            }
            String oneToManyRel = propertyAttributes.get("n-to-many");
            if("true".equals(oneToManyRel)){
                String oneToManyType = propertyAttributes.get("parameterized-type");
                propertyAttributes.put("simpleType", getSimpleName(oneToManyType));
            }
            
            // Add the property attributes into a list, made accessible as a sequence to the FTL
            viewPropertyAttributes.add(propertyAttributes);
            inspectedProperty = XmlUtils.getNextSiblingElement(inspectedProperty);
        }
        root.put("properties", viewPropertyAttributes);
        System.out.println("Root:" + root);

        // TODO: The list of template files to be processed per-entity (like detail.html.ftl and search.html.ftl) needs to
        // be obtained dynamically. Another list to be processed for all entities (like index.html.ftl) also needs to be
        // maintained. In short, a template should be associated with a processing directive like PER_ENTITY, ALL_ENTITIES etc.
        try {
            Template controllerTemplate = config.getTemplate("scripts/controllers.js.ftl");
            Writer contents = new StringWriter();
            controllerTemplate.process(root, contents);
            contents.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(entity.getName() + "Controllers.js"),
                    contents.toString(), overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        
        try {
            Template indexTemplate = config.getTemplate("partials/detail.html.ftl");
            Writer out = new StringWriter();
            indexTemplate.process(root, out);
            out.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt,
                    web.getWebResource("/partials/" + entity.getName() + "/detail.html"), out.toString(), overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

        try {
            Template indexTemplate = config.getTemplate("partials/search.html.ftl");
            Writer out = new StringWriter();
            indexTemplate.process(root, out);
            out.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt,
                    web.getWebResource("/partials/" + entity.getName() + "/search.html"), out.toString(), overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }

        try {
            Template indexTemplate = config.getTemplate("scripts/entityModule.js.ftl");
            Writer out = new StringWriter();
            indexTemplate.process(root, out);
            out.flush();
            result.add(ScaffoldUtil.createOrOverwrite(prompt,
                    web.getWebResource("/scripts/" + entity.getName() + "/" + entity.getName() + ".js"), out.toString(),
                    overwrite));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        generateIndex(targetDir, template, overwrite);
        return result;
    }

    @Override
    public List<Resource<?>> getGeneratedResources(String targetDir) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AccessStrategy getAccessStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemplateStrategy getTemplateStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

    private String getSimpleName(String manyToOneType) {
        JavaSourceFacet java = this.project.getFacet(JavaSourceFacet.class);
        try {
            JavaResource relatedResource = java.getJavaResource(manyToOneType);
            return relatedResource.getJavaSource().getName();
        } catch (FileNotFoundException fileEx) {
            // This is not supposed to happen, since the JPA entity class/file is supposed to be present by now.
            throw new RuntimeException(fileEx);
        }
    }

}
