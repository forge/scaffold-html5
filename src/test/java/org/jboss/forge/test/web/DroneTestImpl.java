/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.test.web;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Profile;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.maven.plugins.ConfigurationBuilder;
import org.jboss.forge.maven.plugins.ConfigurationElementBuilder;
import org.jboss.forge.maven.profiles.ProfileBuilder;
import org.jboss.forge.parser.JavaParser;
import org.jboss.forge.parser.java.Annotation;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaInterface;
import org.jboss.forge.parser.java.Method;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.dependencies.DependencyBuilder;
import org.jboss.forge.project.dependencies.ScopeType;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.project.packaging.PackagingType;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class DroneTestImpl implements DroneTest
{
   @Override
   public void setup(final Project project)
   {
      ResourceFacet resources = project.getFacet(ResourceFacet.class);
      FileResource<?> arquillian = resources.getTestResource("arquillian.xml");
      if (!arquillian.exists())
      {
         arquillian.createNewFile();
         arquillian.setContents(this.getClass().getResourceAsStream("/web/arquillian.xml"));
      }

      MavenCoreFacet mvn = project.getFacet(MavenCoreFacet.class);

      DependencyFacet deps = project.getFacet(DependencyFacet.class);
      deps.addDirectManagedDependency(
               DependencyBuilder.create("org.jboss.arquillian:arquillian-bom:1.0.1.Final")
                        .setPackagingType(PackagingType.BASIC).setScopeType(ScopeType.IMPORT));
      deps.addDirectManagedDependency(
              DependencyBuilder.create("org.jboss.arquillian.extension:arquillian-drone-bom:1.2.0.Alpha1")
                       .setPackagingType(PackagingType.BASIC).setScopeType(ScopeType.IMPORT));
      deps.addDirectManagedDependency(DependencyBuilder.create("org.seleniumhq.selenium:selenium-java:2.31.0"));
      deps.addDirectManagedDependency(DependencyBuilder.create("org.seleniumhq.selenium:selenium-firefox-driver:2.31.0"));
      deps.addDirectManagedDependency(DependencyBuilder.create("org.seleniumhq.selenium:selenium-api:2.31.0"));
      deps.addDirectManagedDependency(DependencyBuilder.create("org.seleniumhq.selenium:selenium-support:2.31.0"));
      deps.addDirectManagedDependency(DependencyBuilder.create("org.seleniumhq.selenium:selenium-remote-driver:2.31.0"));

      ProfileBuilder profileBuilder = ProfileBuilder
               .create()
               .setId("JBOSS_AS_MANAGED_7_1")
               .setActiveByDefault(true)
               .addDependency(
                        DependencyBuilder.create("org.jboss.arquillian.junit:arquillian-junit-container").setScopeType(ScopeType.TEST))
               .addDependency(
                        DependencyBuilder.create("org.jboss.arquillian.protocol:arquillian-protocol-servlet").setScopeType(ScopeType.TEST))
               .addDependency(DependencyBuilder.create("junit:junit:4.11").setScopeType(ScopeType.TEST))
               .addDependency(DependencyBuilder.create("org.jboss.shrinkwrap.descriptors:shrinkwrap-descriptors-impl:1.1.0-beta-1").setScopeType(ScopeType.TEST))
               .addDependency(DependencyBuilder.create("org.jboss.as:jboss-as-arquillian-container-managed:7.1.1.Final").setScopeType(ScopeType.TEST))
               .addDependency(DependencyBuilder.create("org.jboss.arquillian.extension:arquillian-drone-webdriver-depchain:1.1.0.Final")
                                .setPackagingType(PackagingType.BASIC).setScopeType(ScopeType.TEST));

      Profile profile = profileBuilder.getAsMavenProfile();

      Build build = new Build();

      Plugin plugin = new Plugin();
      plugin.setArtifactId("maven-dependency-plugin");
      plugin.setExtensions(false);

      PluginExecution execution = new PluginExecution();
      execution.setId("unpack");
      execution.setPhase("process-test-classes");
      execution.addGoal("unpack");

      ConfigurationBuilder configBuilder = ConfigurationBuilder.create();
      ConfigurationElementBuilder artifactItem = configBuilder
               .createConfigurationElement("artifactItems").addChild("artifactItem");
      artifactItem.addChild("groupId").setText("org.jboss.as");
      artifactItem.addChild("artifactId").setText("jboss-as-dist");
      artifactItem.addChild("version").setText("7.1.1.Final");
      artifactItem.addChild("type").setText("zip");
      artifactItem.addChild("outputDirectory").setText("target/");
      try {
         new Xpp3DomBuilder();
         execution.setConfiguration(
                  Xpp3DomBuilder.build(new ByteArrayInputStream(configBuilder.toString().getBytes()), "UTF-8"));
      }
      catch (XmlPullParserException e) {
         throw new RuntimeException(e);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }

      plugin.addExecution(execution);

      build.addPlugin(plugin);
      profile.setBuild(build);
      Model pom = mvn.getPOM();
      pom.addProfile(profile);
      mvn.setPOM(pom);
   }

   @Override
   public JavaClass from(final Project project, final Class<?> clazz)
   {
      try {
         return (JavaClass) project.getFacet(JavaSourceFacet.class).getTestJavaResource(clazz.getName())
                  .getJavaSource();
      }
      catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void addAsTestClass(final Project project, final JavaClass clazz)
   {
      try {
         JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
         clazz.setName(clazz.getName() + "Test");

         if (!clazz.hasAnnotation(RunWith.class))
         {
            Annotation<JavaClass> runWith = clazz.addAnnotation(RunWith.class);
            runWith.setLiteralValue("Arquillian.class");
         }

         if (clazz.hasAnnotation(Ignore.class))
         {
            clazz.removeAnnotation(clazz.getAnnotation(Ignore.class));
         }

         clazz.addImport(Arquillian.class);
         java.saveTestJavaSource(clazz);
      }
      catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public Method<JavaClass> buildDefaultDeploymentMethod(final Project project, final JavaClass clazz,
            final Collection<String> deploymentItems)
   {
      try {
         JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);

         JavaResource root = java.getTestJavaResource(java.getBasePackage() + ".Root");
         if (!root.exists())
         {
            java.saveTestJavaSource(JavaParser.create(JavaInterface.class).setName("Root")
                     .setPackage(java.getBasePackage()));
         }
         clazz.addImport(root.getJavaSource());

         clazz.addImport(WebArchive.class);
         clazz.addImport(Deployment.class);
         clazz.addImport(ShrinkWrap.class);

         Method<JavaClass> method = clazz.getMethod("getDeployment");

         if (method == null)
            method = clazz.addMethod("public static WebArchive getDeployment() {}");

         if (!method.hasAnnotation(Deployment.class)) {
            Annotation<JavaClass> deployment = method.addAnnotation(Deployment.class);
            deployment.setLiteralValue("testable", "false");
        }

         clazz.addImport(ExplodedImporter.class);
         clazz.addImport(JavaArchive.class);
         clazz.addImport(Filters.class);
         String body = "return ShrinkWrap.create(WebArchive.class)"
                  + ".addPackages(true, " + "Root.class.getPackage()" + ")";

         for (String item : deploymentItems)
         {
            body = body + item;
         }

         body = body + ".merge(ShrinkWrap.create(ExplodedImporter.class, \"temp.jar\")" +
                  ".importDirectory(\"src/main/webapp\") " +
                  ".as(JavaArchive.class),\"/\", Filters.includeAll());";

         method.setBody(body);

         return method;
      }
      catch (FileNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   @Override
   public void addHelpers(Project project, JavaClass[] classes) {
       for(JavaClass clazz: classes) {
           try {
               JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
               java.saveTestJavaSource(clazz);
            }
            catch (FileNotFoundException e) {
               throw new RuntimeException(e);
            }
       }
   }
}
