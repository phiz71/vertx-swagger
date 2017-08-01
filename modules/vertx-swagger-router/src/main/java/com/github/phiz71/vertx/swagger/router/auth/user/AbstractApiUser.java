package com.github.phiz71.vertx.swagger.router.auth.user;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.auth.AbstractUser;

import java.nio.charset.StandardCharsets;

public abstract class AbstractApiUser extends AbstractUser implements ApiUser {

    private String authProviderName;

    public AbstractApiUser() {
    }

    public AbstractApiUser(String authProviderName) {
        this.authProviderName = authProviderName;
    }

    @Override
    public String getAuthProviderName() {
        return this.authProviderName;
    }

    public void setAuthProviderName(String authProviderName) {
        this.authProviderName = authProviderName;
    }

    @Override
    public void writeToBuffer(Buffer buffer) {
        super.writeToBuffer(buffer);
        if (authProviderName != null) {
            byte[] bytes = authProviderName.getBytes(StandardCharsets.UTF_8);
            buffer.appendByte((byte) 1);
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);
        } else {
            buffer.appendByte((byte) 0);
        }
    }

    @Override
    public int readFromBuffer(int pos, Buffer buffer) {
        pos = super.readFromBuffer(pos, buffer);
        byte b = buffer.getByte(pos++);
        if (b == (byte) 1) {
            int len = buffer.getInt(pos);
            pos += 4;
            byte[] bytes = buffer.getBytes(pos, pos + len);
            pos += len;
            this.authProviderName = new String(bytes, StandardCharsets.UTF_8);
        } else {
            this.authProviderName = null;
        }
        return pos;
    }
}
