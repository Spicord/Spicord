/*
 * Copyright (C) 2020  OopsieWoopsie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package eu.mcdb.spicord.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.mcdb.spicord.LibraryLoader.Library;

// Run this class to convert the .json lib data into a .libinfo file
// or to do the opposite
class JsonToLibinfo {

    private final File baseDir = new File("src/main/resources");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        new JsonToLibinfo();
    }

    public JsonToLibinfo() throws IOException {
        // compile
        compile("libraries.json", "libraries.libinfo");

        // or decompile
        decompile("libraries.libinfo", "libraries-dec.json");
    }

    /**
     * @param in the input json file
     * @param out the out libinfo file
     * @throws IOException if bad stuff happens
     */
    private void compile(final String in, final String out) throws IOException {
        final FileInputStream fis = new FileInputStream(new File(baseDir, in));
        final String json = new String(ByteStreams.toByteArray(fis));
        final Libraries libs = gson.fromJson(json, Libraries.class);
        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(baseDir, out)));

        oos.writeObject(libs.libraries);
        oos.flush();
        oos.close();

        System.out.println(String.format("%s -> %s", in, out));
    }

    /**
     * @param in the input libinfo file
     * @param out the output json file
     * @throws IOException pls no
     */
    private void decompile(final String in, final String out) throws IOException {
        final String json = gson.toJson(decompile(in));

        final FileOutputStream fos = new FileOutputStream(new File(baseDir, out));
        fos.write(json.getBytes());
        fos.flush();
        fos.close();

        System.out.println(String.format("%s -> %s", in, out));
    }

    /**
     * @param in the input libinfo file
     * @return the output instance
     * @throws IOException :angryasf:
     */
    private Libraries decompile(final String in) throws IOException {
        final FileInputStream fis = new FileInputStream(new File(baseDir, in));
        final Object obj;

        try (final ObjectInputStream ois = new ObjectInputStream(fis)) {
            obj = ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (obj instanceof Library[]) {
            final Libraries libs = new Libraries();
            libs.libraries = (Library[]) obj;
            return libs;
        } else {
            throw new RuntimeException("incompatible input file");
        }
    }

    private class Libraries {
        Library[] libraries;
    }
}
