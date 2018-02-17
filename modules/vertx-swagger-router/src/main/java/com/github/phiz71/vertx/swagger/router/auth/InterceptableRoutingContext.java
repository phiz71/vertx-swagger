package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InterceptableRoutingContext implements RoutingContext {

    protected final RoutingContext inner;

    private Runnable failedCallback;
    private Runnable nextCallback;

    public InterceptableRoutingContext(RoutingContext inner) {
        this.inner = inner;
    }

    public void setFailedCallback(Runnable failedCallback) {
        this.failedCallback = failedCallback;
    }

    public void setNextCallback(Runnable nextCallback) {
        this.nextCallback = nextCallback;
    }

    @Override
    public HttpServerRequest request() {
        return this.inner.request();
    }

    @Override
    public HttpServerResponse response() {
        return this.inner.response();
    }

    @Override
    public void fail(int statusCode) {
        if (this.failedCallback != null) {
            this.failedCallback.run();
        } else {
            this.inner.fail(statusCode);
        }
    }

    @Override
    public void fail(Throwable throwable) {
        if (this.failedCallback != null) {
            this.failedCallback.run();
        } else {
            this.inner.fail(throwable);
        }
    }

    @Override
    public RoutingContext put(String key, Object obj) {
        this.inner.put(key, obj);
        return this;
    }

    @Override
    public <T> T get(String key) {
        return this.inner.get(key);
    }

    @Override
    public <T> T remove(String key) {
        return this.inner.remove(key);
    }

    @Override
    public Map<String, Object> data() {
        return this.inner.data();
    }

    @Override
    public Vertx vertx() {
        return this.inner.vertx();
    }

    @Override
    public int addHeadersEndHandler(Handler<Void> handler) {
        return this.inner.addHeadersEndHandler(handler);
    }

    @Override
    public boolean removeHeadersEndHandler(int handlerID) {
        return this.inner.removeHeadersEndHandler(handlerID);
    }

    @Override
    public int addBodyEndHandler(Handler<Void> handler) {
        return this.inner.addBodyEndHandler(handler);
    }

    @Override
    public boolean removeBodyEndHandler(int handlerID) {
        return this.inner.removeBodyEndHandler(handlerID);
    }

    @Override
    public void setSession(Session session) {
        this.inner.setSession(session);
    }

    @Override
    public Session session() {
        return this.inner.session();
    }

    @Override
    public void setUser(User user) {
        this.inner.setUser(user);
    }

    @Override
    public void clearUser() {
        this.inner.clearUser();
    }

    @Override
    public User user() {
        return this.inner.user();
    }

    @Override
    public void next() {
        if (this.nextCallback != null) {
            this.nextCallback.run();
        } else {
            this.inner.next();
        }

    }

    @Override
    public boolean failed() {
        return this.inner.failed();
    }

    @Override
    public Throwable failure() {
        return this.inner.failure();
    }

    @Override
    public int statusCode() {
        return this.inner.statusCode();
    }

    @Override
    public String mountPoint() {
        return this.inner.mountPoint();
    }

    @Override
    public Route currentRoute() {
        return this.inner.currentRoute();
    }

    @Override
    public String normalisedPath() {
        return this.inner.normalisedPath();
    }

    @Override
    public Cookie getCookie(String name) {
        return this.inner.getCookie(name);
    }

    @Override
    public RoutingContext addCookie(Cookie cookie) {
        this.inner.addCookie(cookie);
        return this;
    }

    @Override
    public Cookie removeCookie(String name) {
        return this.inner.removeCookie(name);
    }

    @Override
    public int cookieCount() {
        return this.inner.cookieCount();
    }

    @Override
    public Set<Cookie> cookies() {
        return this.inner.cookies();
    }

    @Override
    public String getBodyAsString() {
        return this.inner.getBodyAsString();
    }

    @Override
    public String getBodyAsString(String encoding) {
        return this.inner.getBodyAsString(encoding);
    }

    @Override
    public JsonObject getBodyAsJson() {
        return this.inner.getBodyAsJson();
    }

    @Override
    public JsonArray getBodyAsJsonArray() {
        return this.inner.getBodyAsJsonArray();
    }

    @Override
    public Buffer getBody() {
        return this.inner.getBody();
    }

    @Override
    public void setBody(Buffer body) {
        this.inner.setBody(body);
    }

    @Override
    public Set<FileUpload> fileUploads() {
        return this.inner.fileUploads();
    }

    @Override
    public String getAcceptableContentType() {
        return this.inner.getAcceptableContentType();
    }

    @Override
    public ParsedHeaderValues parsedHeaders() {
        return this.inner.parsedHeaders();
    }

    @Override
    public void setAcceptableContentType(String contentType) {
        this.inner.setAcceptableContentType(contentType);
    }

    @Override
    public void reroute(String path) {
        this.inner.reroute(path);
    }

    @Override
    public void reroute(HttpMethod method, String path) {
        this.inner.reroute(method, path);
    }

    /**
     * @deprecated (depends on Eclipse/Vert.X)
     */
    @Override
    @Deprecated
    public List<Locale> acceptableLocales() {
        return this.inner.acceptableLocales();
    }

    @Override
    public List<LanguageHeader> acceptableLanguages() {
        return this.inner.acceptableLanguages();
    }

    /**
     * @deprecated (depends on Eclipse/Vert.X)
     */
    @Override
    @Deprecated
    public Locale preferredLocale() {
        return this.inner.preferredLocale();
    }

    @Override
    public LanguageHeader preferredLanguage() {
        return this.inner.preferredLanguage();
    }

    @Override
    public Map<String, String> pathParams() {
        return this.inner.pathParams();
    }

    @Override
    public String pathParam(String name) {
        return this.inner.pathParam(name);
    }

    @Override
    public MultiMap queryParams() {
        return this.inner.queryParams();
    }

    @Override
    public List<String> queryParam(String s) {
        return this.inner.queryParam(s);
    }
}
