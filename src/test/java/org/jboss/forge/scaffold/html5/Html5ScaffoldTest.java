package org.jboss.forge.scaffold.html5;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;

// TODO: Fix this to implement end-to-end tests that generate the entire scaffold
@Ignore
public class Html5ScaffoldTest extends AbstractShellTest
{
   @Deployment
   public static JavaArchive getDeployment()
   {
      return AbstractShellTest.getDeployment().addPackages(true, Html5Scaffold.class.getPackage());
   }

   @Test
   public void testDefaultCommand() throws Exception
   {
      getShell().execute("htm5scaffod");
   }

   @Test
   public void testCommand() throws Exception
   {
      getShell().execute("htm5scaffod command");
   }

   @Test
   public void testPrompt() throws Exception
   {
      queueInputLines("y");
      getShell().execute("htm5scaffod prompt foo bar");
   }
}
