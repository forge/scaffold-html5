package org.jboss.forge.scaffold.html5.scenario;

import static org.jboss.forge.scaffold.html5.scenario.TestHelpers.assertWebResourceContents;
import static org.jboss.forge.scaffold.html5.scenario.TestHelpers.assertStaticFilesAreGenerated;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.scaffold.html5.AbstractHtml5ScaffoldTest;
import org.junit.Test;

public class Html5ScaffoldScenarioTest extends AbstractHtml5ScaffoldTest {

    @Test
    public void testScaffoldForSingleEntity() throws Exception {
        generateCustomerEntity();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "single-entity");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "single-entity");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "single-entity");
        assertWebResourceContents(web, "/views/Customer/detail.html", "single-entity");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "single-entity");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "single-entity");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "single-entity");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "single-entity");
    }

    private void generateCustomerEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named Customer");
        getShell().execute("field string --named firstName");
        getShell().execute("field temporal --type DATE --named dateOfBirth");
        getShell().execute("constraint NotNull --onProperty firstName");
    }
    
}
