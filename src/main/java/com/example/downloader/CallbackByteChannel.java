package com.example.downloader;

import com.example.downloader.protocols.Connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class CallbackByteChannel implements ReadableByteChannel {
    private static long currentFullRead;
    private int id;
    private long currentRead;
    private long start;
    private long end;
    private ProgressCallBack delegate;
    private ReadableByteChannel rbc;

    public CallbackByteChannel(Connection connection, ProgressCallBack delegate, int id, long start, long end) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.delegate = delegate;
        this.rbc = Channels.newChannel(connection.getInputStream(start, end));
    }

    @Override
    public void close() throws IOException {
        rbc.close();
    }

    public long getFullReadSoFar() {
        return currentFullRead;
    }

    @Override
    public boolean isOpen() {
        return rbc.isOpen();
    }

    @Override
    public int read(ByteBuffer bb) throws IOException {
        int n;
        if ((n = rbc.read(bb)) > 0) {
            currentRead += n;
            currentFullRead += n;
        }
        delegate.callback(this);
        return n;
    }

    public long getCurrentRead() {
        return currentRead;
    }

    public int getId() {
        return id;
    }

    public boolean isPartialDownloadFailed() {
        return (currentRead + start < end);
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }
}
