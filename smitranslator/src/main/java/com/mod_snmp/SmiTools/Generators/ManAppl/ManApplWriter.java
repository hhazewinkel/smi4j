/**
 * ManApplWriter
 * Extends GenWriter with additional functions.
 */

package com.mod_snmp.SmiTools.Generators.ManAppl;

import com.mod_snmp.SmiParser.ErrorHandler.ErrorException;
import com.mod_snmp.SmiParser.SyntaxTree.Identifier;
import com.mod_snmp.SmiParser.SyntaxTree.ModuleIdentifier;
import com.mod_snmp.SmiTools.Generators.Common.GenWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class ManApplWriter extends GenWriter {
    public static ManApplWriter create(String pkgName,
                               ModuleIdentifier modName,
                               Identifier id) throws ErrorException {
        return create(pkgName, toPackage(modName), toClass(id));
    }
    public static ManApplWriter create(String pkgName,
                               String modName,
                               Identifier id) throws ErrorException {
        return create(pkgName, modName, toClass(id));
    }
    public static ManApplWriter create(String pkgName, String modName, String name)
                                                throws ErrorException {
        try {
System.out.println(pkgName + "/" + modName + "/" + name + ".java");
            File output_file = new File(pkgName + "/" + modName + "/" + name + ".java");
            return new ManApplWriter(new FileOutputStream(output_file));
        } catch (FileNotFoundException e) {
            throw new ErrorException("ManApplWriter:" + e.getMessage());
        } catch (NullPointerException e) {
            throw new ErrorException("ManApplWriter: file name = null");
        } catch (IOException e) {
            throw new ErrorException("ManApplWriter: I/O error " + e.getMessage());
        }
    }
    public ManApplWriter(OutputStream o) {
        super(o);
    }
    public ManApplWriter(Writer w) {
        super(w);
    }

    public String stripDots(String str) {
        int i = 0;
        while ((i < str.length()) && ((str.charAt(i) == '.') || (str.charAt(i) == '/'))) {
            i++;
        }
        return str.substring(i);
    }

    public static String toClass(Identifier id) {
        String str = id.toString();
        str = str.replace('-', '_');
        str = str.replace('.', '_');
        if (str.length() > 0) {
             char[] tmp = str.toCharArray();
             tmp[0] = Character.toUpperCase(tmp[0]);
              str = new String(tmp);
        }
        return str;
    }
    public static String toPackage(ModuleIdentifier id) {
        String str = id.toString();
        if (str.length() > 0) {
             char[] tmp = str.toCharArray();
             tmp[0] = Character.toUpperCase(tmp[0]);
             int i = 1;
             int j = 1;
             while (i < tmp.length) {
                  if (Character.isUpperCase(tmp[i])) {
                      tmp[j++] = Character.toLowerCase(tmp[i++]);
                  } else if (Character.isLowerCase(tmp[i])) {
                      tmp[j++] = Character.toUpperCase(tmp[i++]);
                  } else if ((tmp[i] == '-') || (tmp[i] == '.')) {
                      i++;
                      if (i < tmp.length) {
                          tmp[j++] = Character.toUpperCase(tmp[i++]);
                      }
                  } else {
                      tmp[j++] = tmp[i++];
                  }
             }
             str = new String(tmp, 0, j);
        }
        return str;
    }

}
