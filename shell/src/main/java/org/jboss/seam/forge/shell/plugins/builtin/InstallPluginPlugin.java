/*
 * JBoss, by Red Hat.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.seam.forge.shell.plugins.builtin;

import org.jboss.seam.forge.shell.Shell;
import org.jboss.seam.forge.shell.ShellImpl;
import org.jboss.seam.forge.shell.plugins.DefaultCommand;
import org.jboss.seam.forge.shell.plugins.Option;
import org.jboss.seam.forge.shell.plugins.PipeOut;
import org.jboss.seam.forge.shell.plugins.Plugin;
import org.jboss.seam.forge.shell.util.PluginRef;
import org.jboss.seam.forge.shell.util.PluginRepoUtil;
import org.jboss.seam.forge.shell.util.ShellColor;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Mike Brock .
 */
@Named("install-plugin")
public class InstallPluginPlugin implements Plugin
{
   private Shell shell;

   @Inject
   public InstallPluginPlugin(Shell shell)
   {
      this.shell = shell;
   }

   @DefaultCommand
   public void install(@Option(description = "plugin-name") String pluginName, final PipeOut out) throws Exception
   {

      String defaultRepo = (String) shell.getProperty(ShellImpl.PROP_DEFAULT_PLUGIN_REPO);

      if (defaultRepo == null)
      {
         out.println("no default repository set: (to set, type: set "
               + ShellImpl.PROP_DEFAULT_PLUGIN_REPO + " <repository>)");
         return;
      }

      List<PluginRef> plugins = PluginRepoUtil.findPlugin(defaultRepo, pluginName, out);

      if (plugins.isEmpty())
      {
         out.println("no plugin found: " + pluginName);
      }
      else if (plugins.size() > 1)
      {
         out.println("ambiguous plugin query: multiple matches.");
      }
      else
      {
         PluginRef ref = plugins.get(0);
         out.println(ShellColor.BOLD, "*** Preparing to install plugin: " + ref.getName());
         PluginRepoUtil.downloadPlugin(ref, out);
      }
   }

}
