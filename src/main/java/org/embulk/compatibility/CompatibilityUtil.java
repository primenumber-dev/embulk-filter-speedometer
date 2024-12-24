package org.embulk.compatibility;

import org.embulk.spi.BufferAllocator;
import org.embulk.spi.Column;
import org.embulk.spi.Exec;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.PageOutput;
import org.embulk.spi.PageReader;
import org.embulk.spi.Schema;

public class CompatibilityUtil {
    @SuppressWarnings("deprecation")
    public static PageBuilder getPageBuilder(final BufferAllocator bufferAllocator, final Schema schema, final PageOutput output) {
        if (HAS_EXEC_GET_PAGE_BUILDER) {
            return Exec.getPageBuilder(bufferAllocator, schema, output);
        } else {
            return new PageBuilder(bufferAllocator, schema, output);
        }
    }

    @SuppressWarnings("deprecation")
    public static PageReader getPageReader(final Schema schema) {
        if (HAS_EXEC_GET_PAGE_READER) {
            return Exec.getPageReader(schema);
        } else {
            return new PageReader(schema);
        }
    }

    private static boolean hasExecGetPageReader() {
        try {
            Exec.class.getMethod("getPageReader", Schema.class);
        } catch (final NoSuchMethodException ex) {
            return false;
        }
        return true;
    }

    private static boolean hasExecGetPageBuilder() {
        try {
            Exec.class.getMethod("getPageBuilder", BufferAllocator.class, Schema.class, PageOutput.class);
        } catch (final NoSuchMethodException ex) {
            return false;
        }
        return true;
    }

    private static boolean hasPageBuilderTimestampInstant() {
        try {
            PageReader.class.getMethod("getTimestampInstant", Column.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    public static final boolean HAS_EXEC_GET_PAGE_READER = hasExecGetPageReader();
    public static final boolean HAS_EXEC_GET_PAGE_BUILDER = hasExecGetPageBuilder();
    public static final boolean HAS_PAGE_BUILDER_TIMESTAMP_INSTANT = hasPageBuilderTimestampInstant();

}
