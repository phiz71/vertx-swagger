package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.Handler;
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

    public HttpServerRequest request() {
        return this.inner.request();
    }

    public HttpServerResponse response() {
        return this.inner.response();
    }

    public void fail(int statusCode) {
        if(this.failedCallback != null) {
            this.failedCallback.run();
        } else {
            this.inner.fail(statusCode);
        }
    }

    public void fail(Throwable throwable) {
        if(this.failedCallback != null) {
            this.failedCallback.run();
        } else {
            this.inner.fail(throwable);
        }
    }

    public RoutingContext put(String key, Object obj) {
        this.inner.put(key, obj);
        return this;
    }

    public <T> T get(String key) {
        return this.inner.get(key);
    }

    public <T> T remove(String key) {
        return this.inner.remove(key);
    }

    public Map<String, Object> data() {
        return this.inner.data();
    }

    public Vertx vertx() {
        return this.inner.vertx();
    }

    public int addHeadersEndHandler(Handler<Void> handler) {
        return this.inner.addHeadersEndHandler(handler);
    }

    public boolean removeHeadersEndHandler(int handlerID) {
        return this.inner.removeHeadersEndHandler(handlerID);
    }

    public int addBodyEndHandler(Handler<Void> handler) {
        return this.inner.addBodyEndHandler(handler);
    }

    public boolean removeBodyEndHandler(int handlerID) {
        return this.inner.removeBodyEndHandler(handlerID);
    }

    public void setSession(Session session) {
        this.inner.setSession(session);
    }

    public Session session() {
        return this.inner.session();
    }

    public void setUser(User user) {
        this.inner.setUser(user);
    }

    public void clearUser() {
        this.inner.clearUser();
    }

    public User user() {
        return this.inner.user();
    }

    public void next() {
        if(this.nextCallback != null) {
            this.nextCallback.run();
        } else {
            this.inner.next();
        }

    }

    public boolean failed() {
        return this.inner.failed();
    }

    public Throwable failure() {
        return this.inner.failure();
    }

    public int statusCode() {
        return this.inner.statusCode();
    }

    public String mountPoint() {
        return this.inner.mountPoint();
    }

    public Route currentRoute() {
        return this.inner.currentRoute();
    }

    public String normalisedPath() {
        return this.inner.normalisedPath();
    }

    public Cookie getCookie(String name) {
        return this.inner.getCookie(name);
    }

    public RoutingContext addCookie(Cookie cookie) {
        this.inner.addCookie(cookie);
        return this;
    }

    public Cookie removeCookie(String name) {
        return this.inner.removeCookie(name);
    }

    public int cookieCount() {
        return this.inner.cookieCount();
    }

    public Set<Cookie> cookies() {
        return this.inner.cookies();
    }

    public String getBodyAsString() {
        return this.inner.getBodyAsString();
    }

    public String getBodyAsString(String encoding) {
        return this.inner.getBodyAsString(encoding);
    }

    public JsonObject getBodyAsJson() {
        return this.inner.getBodyAsJson();
    }

    public JsonArray getBodyAsJsonArray() {
        return this.inner.getBodyAsJsonArray();
    }

    public Buffer getBody() {
        return this.inner.getBody();
    }

    public void setBody(Buffer body) {
        this.inner.setBody(body);
    }

    public Set<FileUpload> fileUploads() {
        return this.inner.fileUploads();
    }

    public String getAcceptableContentType() {
        return this.inner.getAcceptableContentType();
    }

    public ParsedHeaderValues parsedHeaders() {
        return this.inner.parsedHeaders();
    }

    public void setAcceptableContentType(String contentType) {
        this.inner.setAcceptableContentType(contentType);
    }

    public void reroute(String path) {
        this.inner.reroute(path);
    }

    public void reroute(HttpMethod method, String path) {
        this.inner.reroute(method, path);
    }

    public List<Locale> acceptableLocales() {
        return this.inner.acceptableLocales();
    }

    public List<LanguageHeader> acceptableLanguages() {
        return this.inner.acceptableLanguages();
    }

    public Locale preferredLocale() {
        return this.inner.preferredLocale();
    }

    public LanguageHeader preferredLanguage() {
        return this.inner.preferredLanguage();
    }

    public Map<String, String> pathParams() {
        return this.inner.pathParams();
    }

    public String pathParam(String name) {
        return this.inner.pathParam(name);
    }
}
