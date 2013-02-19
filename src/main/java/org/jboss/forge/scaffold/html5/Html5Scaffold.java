package org.jboss.forge.scaffold.html5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.scaffold.AccessStrategy;
import org.jboss.forge.scaffold.ScaffoldProvider;
import org.jboss.forge.scaffold.TemplateStrategy;
import org.jboss.forge.scaffold.util.ScaffoldUtil;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.spec.javaee.CDIFacet;
import org.jboss.forge.spec.javaee.EJBFacet;
import org.jboss.forge.spec.javaee.PersistenceFacet;
import org.metawidget.util.simple.StringUtils;

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
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        List<String> entityNames = new ArrayList<String>();
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
        FileResource<?> partialsDirectory = web.getWebResource("views");
        for (Resource<?> resource : partialsDirectory.listResources()) {
            entityNames.add(resource.getName());
        }
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        MetadataFacet metadata = this.project.getFacet(MetadataFacet.class);
        String projectIdentifier = StringUtils.camelCase(metadata.getProjectName());
        String projectTitle = StringUtils.uncamelCase(metadata.getProjectName());
        root.put("projectId", projectIdentifier);
        root.put("projectTitle", projectTitle);

        Map<String, String> projectGlobalTemplates = new HashMap<String, String>();
        projectGlobalTemplates.put("index.html.ftl", "index.html");
        projectGlobalTemplates.put("scripts/app.js.ftl", "scripts/app.js");
        projectGlobalTemplates.put("scripts/filters.js.ftl", "scripts/filters.js");
        
        FreemarkerClient freemarkerClient = new FreemarkerClient();
        for (String projectGlobalTemplate : projectGlobalTemplates.keySet()) {
            String output = freemarkerClient.processFTL(root, projectGlobalTemplate);
            String outputPath = projectGlobalTemplates.get(projectGlobalTemplate);
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(outputPath), output, overwrite));
        }
        return result;
    }

    @Override
    public List<Resource<?>> generateFromEntity(String targetDir, Resource<?> template, JavaClass entity, boolean overwrite) {
        System.out.println("Generating artifacts from Entity:" + entity.getQualifiedName());
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
        
        Map<String, Object> root = new IntrospectorClient(project).inspect(entity);

        // TODO: The list of template files to be processed per-entity (like detail.html.ftl and search.html.ftl) needs to
        // be obtained dynamically. Another list to be processed for all entities (like index.html.ftl) also needs to be
        // maintained. In short, a template should be associated with a processing directive like PER_ENTITY, PER_PROJECT etc.
        Map<String, String> perEntityTemplates = new HashMap<String, String>();
        perEntityTemplates.put("views/detail.html.ftl", "/views/" + entity.getName() + "/detail.html");
        perEntityTemplates.put("views/search.html.ftl", "/views/" + entity.getName() + "/search.html");
        perEntityTemplates.put("scripts/entityModule.js.ftl", "/scripts/" + entity.getName() + "/" + entity.getName() + ".js");
        
        FreemarkerClient freemarkerClient = new FreemarkerClient();
        for (String entityTemplate : perEntityTemplates.keySet()) {
            String output = freemarkerClient.processFTL(root, entityTemplate);
            String outputPath = perEntityTemplates.get(entityTemplate);
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(outputPath), output, overwrite));
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

}
