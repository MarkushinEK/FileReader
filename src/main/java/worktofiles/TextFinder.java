package worktofiles;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class TextFinder {

    private List<String> result = new ArrayList<>();

    public void fileFinde(File file, String extension, String text) throws IOException {
        File[] files = file.listFiles();
        for (File entry : files) {
            if (entry.isDirectory()) {
                fileFinde(entry, extension, text);
                continue;
            }
            if (extension.equals(getFileExtension(entry.getName()))) {
                if (checkText(entry, text))
                    result.add(entry.getAbsolutePath());
            }
        }
    }

    private static boolean checkText(File entry, String text) throws IOException {
        long mapsize = 4 * 1024 * 1024;
        final byte[] tosearch = text.getBytes("Cp1251");
        try (FileChannel channel = FileChannel.open(entry.toPath(),StandardOpenOption.READ)) {
            final long length = channel.size();
            long pos = 0;
            while (pos < length) {
                long remaining = length - pos;

                long trymap = mapsize + tosearch.length;
                long tomap = Math.min(trymap, remaining);

                long limit = trymap == tomap ? mapsize : (tomap - tosearch.length);
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, pos, tomap);
                pos += (trymap == tomap) ? mapsize : tomap;
                for (int i = 0; i < limit; i++) {
                    if (wordMatch(buffer, i, tosearch)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static boolean wordMatch(MappedByteBuffer buffer, int pos, byte[] tosearch) throws IOException {
        byte end = 13;
        int error = 0;
        for (int i = 0; i < tosearch.length; ++i) {
            if (buffer.get(pos + i + error) == end) {
                ++i;
                ++error;
            }
            if (i == tosearch.length) break;
            if (tosearch[i] != buffer.get(pos + i + error)) {
                return false;
            }
        }
        return true;
    }

    private static String getFileExtension(String file) {
        if (file.lastIndexOf(".") != -1 && file.lastIndexOf(".") != 0)
            return file.substring(file.lastIndexOf(".") + 1);
        return "";
    }

    public List<String> getResult() {
        return result;
    }
}
