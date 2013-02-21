package org.jboss.forge.scaffold.html5;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffold.html5.metawidget.inspector.ForgeInspector;
import org.jboss.forge.scaffold.html5.metawidget.inspector.propertystyle.ForgePropertyStyle;
import org.jboss.forge.scaffold.html5.metawidget.inspector.propertystyle.ForgePropertyStyleConfig;
import org.jboss.forge.shell.ShellPrompt;
import org.metawidget.inspector.beanvalidation.BeanValidationInspector;
import org.metawidget.inspector.composite.CompositeInspector;
import org.metawidget.inspector.composite.CompositeInspectorConfig;
import org.metawidget.inspector.impl.BaseObjectInspectorConfig;
import org.metawidget.inspector.jpa.JpaInspector;
import org.metawidget.inspector.jpa.JpaInspectorConfig;
import org.metawidget.inspector.propertytype.PropertyTypeInspector;
import org.metawidget.util.XmlUtils;
import org.w3c.dom.Element;

public class IntrospectorClient {

    @Inject
    private Project project;
    
    @Inject
    private ShellPrompt prompt;

    @Inject
    RelatedTypeHolder relatedClassHolder;

    public Map<String, Object> inspect(JavaClass entity) {
        Map<String, Object> root = new HashMap<String, Object>();
        // TODO: Provide a 'utility' class for allowing transliteration across language naming schemes
        // We need this to use contextual naming schemes instead of performing toLowerCase etc. in FTLs.
        root.put("entityName", entity.getName());

        ForgePropertyStyleConfig forgePropertyStyleConfig = new ForgePropertyStyleConfig();
        forgePropertyStyleConfig.setProject(this.project);
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
        compositeInspectorConfig.setInspectors(propertyTypeInspector, forgeInspector, jpaInspector, beanValidationInspector);
        CompositeInspector compositeInspector = new CompositeInspector(compositeInspectorConfig);

        Element inspectionResult = compositeInspector.inspectAsDom(null, entity.getQualifiedName(), (String[]) null);
        Element inspectedEntity = XmlUtils.getFirstChildElement(inspectionResult);
        System.out.println(XmlUtils.nodeToString(inspectedEntity, true));

        Element inspectedProperty = XmlUtils.getFirstChildElement(inspectedEntity);
        List<Map<String, String>> viewPropertyAttributes = new ArrayList<Map<String, String>>();
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
            if ("true".equals(manyToOneRel)) {
                String manyToOneType = propertyAttributes.get("type");
                relatedClassHolder.setRelatedType(manyToOneType);
                String simpleName = getSimpleName(manyToOneType);
                propertyAttributes.put("simpleType", simpleName);
                propertyAttributes.put("optionLabel", prompt.promptCompleter("Which property of " + simpleName
                        + " do you want to display in the dropdown ?", RelatedPropertyCompleter.class));
            }
            String oneToOneRel = propertyAttributes.get("one-to-one");
            if ("true".equals(oneToOneRel)) {
                String oneToOneType = propertyAttributes.get("type");
                relatedClassHolder.setRelatedType(oneToOneType);
                String simpleName = getSimpleName(oneToOneType);
                propertyAttributes.put("simpleType", simpleName);
                propertyAttributes.put("optionLabel", prompt.promptCompleter("Which property of " + simpleName
                        + " do you want to display in the dropdown ?", RelatedPropertyCompleter.class));
            }
            String oneToManyRel = propertyAttributes.get("n-to-many");
            if ("true".equals(oneToManyRel)) {
                String oneToManyType = propertyAttributes.get("parameterized-type");
                relatedClassHolder.setRelatedType(oneToManyType);
                String simpleName = getSimpleName(oneToManyType);
                propertyAttributes.put("simpleType", simpleName);
                propertyAttributes.put("optionLabel", prompt.promptCompleter("Which property of " + simpleName
                        + " do you want to display in the dropdown ?", RelatedPropertyCompleter.class));
            }

            // Add the property attributes into a list, made accessible as a sequence to the FTL
            viewPropertyAttributes.add(propertyAttributes);
            inspectedProperty = XmlUtils.getNextSiblingElement(inspectedProperty);
        }
        root.put("properties", viewPropertyAttributes);
        System.out.println("Root:" + root);
        return root;
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
