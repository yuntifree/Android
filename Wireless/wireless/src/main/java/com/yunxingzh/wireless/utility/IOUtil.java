package com.yunxingzh.wireless.utility;

import android.database.Cursor;
import android.os.MemoryFile;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hzg on 16/11/18.
 */

public class IOUtil {
    /** Closes the given stream inside a try/catch. Does nothing if stream is null. */
    public static void safeClose(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Closes the given stream inside a try/catch. Does nothing if stream is null. */
    public static void safeClose(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Closes the given stream inside a try/catch. Does nothing if stream is null. */
    public static void safeClose(Reader in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Closes the given stream inside a try/catch. Does nothing if stream is null. */
    public static void safeClose(Writer out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void safeClose(MemoryFile mf) {
        if (mf != null) {
            mf.close();
        }
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void safeClose(Socket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void safeClose(ServerSocket closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void safeClose(Cursor cursor) {
        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }
        } catch (Exception e) {
        }
    }

}
