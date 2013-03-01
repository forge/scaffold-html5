package org.jboss.forge.scaffold.html5.scenario.dronetests;

import static org.junit.Assert.*;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

@Ignore
@RunWith(Arquillian.class)
@RunAsClient
public class CustomerViewClient {

    private static final String WEBAPP_SRC = "src/main/webapp";
    
    @Drone
    WebDriver driver;
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Deployment
    public static WebArchive getDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true,"com.example.scaffoldtester")
                .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                        .importDirectory(WEBAPP_SRC).as(GenericArchive.class),
                        "/", Filters.includeAll())
                .addAsWebInfResource("META-INF/persistence.xml", "classes/META-INF/persistence.xml");
    }
    
    @Test
    public void testSaveNewCustomer(@ArquillianResource URL baseUrl) throws Exception {
        driver.get(baseUrl.toString() + "#/");
        driver.findElement(By.linkText("Customers")).click();
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        wait.until(new HasLandedOnSearchCustomerView());
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewCustomerView());
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("John Doe");
        driver.findElement(By.id("dateOfBirth")).clear();
        driver.findElement(By.id("dateOfBirth")).sendKeys("2013-01-10");
        driver.findElement(By.id("saveCustomer")).click();
        wait.until(new HasLandedOnEditCustomerView());
        assertEquals(baseUrl.toString() + "#/Customers/edit/1", driver.getCurrentUrl());
        assertEquals("John Doe", driver.findElement(By.id("firstName")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("dateOfBirth")).getAttribute("value"));
    }
    
}
