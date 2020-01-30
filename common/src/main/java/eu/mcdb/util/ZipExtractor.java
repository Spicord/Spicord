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

package eu.mcdb.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

public class ZipExtractor implements AutoCloseable {

    private final JarFile zipFile;
    private final List<ZipEntry> entries;

    /**
     * 
     * 
     * @param file the zip file
     * @throws IOException if an I/O error has occurred
     */
    public ZipExtractor(final File file) throws IOException {
        this.zipFile = new JarFile(file);
        this.entries = new ArrayList<ZipEntry>();

        final Enumeration<JarEntry> e = zipFile.entries();

        while (e.hasMoreElements()) {
            entries.add(e.nextElement());
        }
    }

    /**
     * Filters all the zip entries by using a pattern.
     * <br>
     * All the entries that matches with the pattern will be available for
     * extraction and the ones who didn't matched will be ignored.
     * 
     * @see {@link #extract(File)}
     * @param regex the regex pattern
     */
    public void filter(final String regex) {
        final Pattern pattern = Pattern.compile(regex);
        entries.removeIf(entry -> !pattern.matcher(entry.getName()).find());
    }

    /**
     * Extracts all the zip files into the {@code folder}.
     * <br>
     * If a filter was applied, only the filtered files will be extracted.
     * 
     * @see {@link #filter(String)}
     * @param out the output folder to extract the files
     * @throws IOException if an I/O error has occurred
     */
    public void extract(final File out) throws IOException {
        for (final ZipEntry entry : entries) {
            String name = entry.getName();
            name = name.substring(name.lastIndexOf('/') + 1);

            final File file = new File(out, name);

            Files.copy(zipFile.getInputStream(entry), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public boolean hasEntry(String path) {
        return entries.stream().map(ZipEntry::getName).count() > 0;
    }

    public Optional<Reader> readEntry(String path) throws IOException {
        if (hasEntry(path)) {
            final Predicate<ZipEntry> filter = e -> e.getName().equals(path);
            final Optional<ZipEntry> entry = entries.stream().filter(filter).findFirst();

            if (entry.isPresent()) {
                final InputStream is = zipFile.getInputStream(entry.get());
                return Optional.of(new InputStreamReader(is));
            }
        }

        return Optional.empty();
    }

    @Override
    public void close() throws IOException {
        entries.clear();
        zipFile.close();
    }
}
