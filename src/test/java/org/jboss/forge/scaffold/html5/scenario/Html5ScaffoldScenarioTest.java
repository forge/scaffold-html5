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
    
    @Test
    public void testScaffoldForManyToOneRelation() throws Exception {
        generateCustomerEntity();
        
        generateStoreOrderEntity();
        
        generateManyStoreOrderOneCustomerRelation();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "many-to-one");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "many-to-one");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "many-to-one");
        assertWebResourceContents(web, "/views/Customer/detail.html", "many-to-one");
        assertWebResourceContents(web, "/views/StoreOrder/search.html", "many-to-one");
        assertWebResourceContents(web, "/views/StoreOrder/detail.html", "many-to-one");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/newStoreOrderController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editStoreOrderController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchStoreOrderController.js", "many-to-one");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/services/StoreOrderFactory.js", "many-to-one");
    }
    
    @Test
    public void testScaffoldForOneToOneRelation() throws Exception {
        generateCustomerEntity();

        generateAddressEntity();

        generateOneCustomerOneAddressRelation();
        
        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "one-to-one");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "one-to-one");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "one-to-one");
        assertWebResourceContents(web, "/views/Customer/detail.html", "one-to-one");
        assertWebResourceContents(web, "/views/Address/search.html", "one-to-one");
        assertWebResourceContents(web, "/views/Address/detail.html", "one-to-one");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/newAddressController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editAddressController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchAddressController.js", "one-to-one");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/services/AddressFactory.js", "one-to-one");
    }
    
    @Test
    public void testScaffoldForOneToManyRelation() throws Exception {
        generateCustomerEntity();
        
        generateStoreOrderEntity();
        
        generateOneCustomerManyStoreOrderRelation();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "one-to-many");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "one-to-many");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "one-to-many");
        assertWebResourceContents(web, "/views/Customer/detail.html", "one-to-many");
        assertWebResourceContents(web, "/views/StoreOrder/search.html", "one-to-many");
        assertWebResourceContents(web, "/views/StoreOrder/detail.html", "one-to-many");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/newStoreOrderController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editStoreOrderController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchStoreOrderController.js", "one-to-many");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/services/StoreOrderFactory.js", "one-to-many");
    }
    
    @Test
    public void testScaffoldForManyToManyRelation() throws Exception {
        generateUserIdentityEntity();

        generateGroupIdentityEntity();

        generateManyGroupManyUserRelation();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "many-to-many");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "many-to-many");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/GroupIdentity/search.html", "many-to-many");
        assertWebResourceContents(web, "/views/GroupIdentity/detail.html", "many-to-many");
        assertWebResourceContents(web, "/views/UserIdentity/search.html", "many-to-many");
        assertWebResourceContents(web, "/views/UserIdentity/detail.html", "many-to-many");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newGroupIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editGroupIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchGroupIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/newUserIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editUserIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchUserIdentityController.js", "many-to-many");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/GroupIdentityFactory.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/services/UserIdentityFactory.js", "many-to-many");
    }

    private void generateCustomerEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named Customer");
        getShell().execute("field string --named firstName");
        getShell().execute("field temporal --type DATE --named dateOfBirth");
        getShell().execute("constraint NotNull --onProperty firstName");
    }
    
    private void generateStoreOrderEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named StoreOrder");
        getShell().execute("field string --named firstName");
        getShell().execute("field temporal --type DATE --named dateOfBirth");
        getShell().execute("constraint NotNull --onProperty firstName");
    }

    private void generateAddressEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named Address");
        getShell().execute("field string --named street");
        getShell().execute("field string --named city");
        getShell().execute("field string --named state");
        getShell().execute("field string --named country");
        getShell().execute("field string --named postalcode");
    }

    private void generateUserIdentityEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named UserIdentity");
        getShell().execute("field string --named userName");
    }
    
    private void generateGroupIdentityEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named GroupIdentity");
        getShell().execute("field string --named groupName");
    }
    
    private void generateOneCustomerManyStoreOrderRelation() throws Exception {
        getShell().execute("cd ../Customer.java");
        getShell().execute("field oneToMany --named orders --fieldType com.test.model.StoreOrder.java");        
    }

    private void generateOneCustomerOneAddressRelation() throws Exception {
        getShell().execute("cd ../Customer.java");
        getShell().execute("field oneToOne --named shippingAddress --fieldType com.test.model.Address.java");
    }
    
    private void generateManyStoreOrderOneCustomerRelation() throws Exception {
        getShell().execute("cd ../StoreOrder.java");
        getShell().execute("field manyToOne --named customer --fieldType com.test.model.Customer.java");
    }
    
    private void generateManyGroupManyUserRelation() throws Exception {
        getShell().execute("cd ../GroupIdentity.java");
        getShell().execute("field manyToMany --named users --fieldType com.test.model.UserIdentity.java");
    }
    
}
