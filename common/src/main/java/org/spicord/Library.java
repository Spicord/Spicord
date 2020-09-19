package org.spicord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Library implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String sha1;
    private final String url;
    private String dontloadifclassfound;

    public String getFileName() {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    public byte[] download() throws IOException {
        final InputStream in = connect(url).getInputStream();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int bytesRead;
        byte[] buffer = new byte[4096];

        while ((bytesRead = in.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        try {
            return baos.toByteArray();
        } finally {
            in.close();
        }
    }

    private URLConnection connect(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.connect();
        return conn;
    }
}
