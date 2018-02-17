package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpFrame;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerFileUpload;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.impl.RouterImpl;
import io.vertx.ext.web.impl.RoutingContextImpl;
import io.vertx.ext.web.impl.Utils;
import io.vertx.ext.web.sstore.impl.SessionImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;

public class InterceptableRoutingContextImplTest {

    private static RoutingContext rc;
    private static RouterImpl router;
    private static InterceptableRoutingContext contextToTest;
    private static String mountPoint;
    private static Vertx myVertx;

    @BeforeClass
    public static void init() {
        mountPoint = "/dummy";
        myVertx = Vertx.vertx();
        router = (RouterImpl) Router.router(myVertx);
        rc = new RoutingContextImpl(mountPoint, router, new TestHttpServerRequest(mountPoint, "fr"),
                new HashSet<>());
        contextToTest = new InterceptableRoutingContext(rc);
    }

    @Test
    public void testPathParams() {
        rc.pathParams().put("MY_KEY", "MY_VALUE");
        rc.pathParams().put("MY_KEY_2", "MY_VALUE_2");

        Assert.assertEquals("MY_VALUE", contextToTest.pathParam("MY_KEY"));
        Assert.assertEquals(rc.pathParams(), contextToTest.pathParams());
    }

    @Test
    public void testLanguageAndLocale() {
        Assert.assertEquals(rc.parsedHeaders().acceptLanguage().get(0),
                contextToTest.preferredLanguage());
        Assert.assertEquals(rc.parsedHeaders().acceptLanguage().get(0),
                contextToTest.preferredLocale());
        Assert.assertEquals(rc.parsedHeaders().acceptLanguage(),
                contextToTest.acceptableLanguages());
        Assert.assertEquals(rc.parsedHeaders().acceptLanguage(), contextToTest.acceptableLocales());

    }

    @Test
    public void testHeaders() {
        Assert.assertEquals(rc.parsedHeaders(), contextToTest.parsedHeaders());
    }

    @Test
    public void testAcceptableContentType() {
        contextToTest.setAcceptableContentType("foo");
        Assert.assertEquals("foo", rc.getAcceptableContentType());

        rc.setAcceptableContentType("bar");
        Assert.assertEquals("bar", contextToTest.getAcceptableContentType());
    }

    @Test
    public void testBody() {

        contextToTest.setBody(Buffer.buffer("foo"));
        Assert.assertEquals("foo", rc.getBody().toString());
        Assert.assertEquals("foo", rc.getBodyAsString());

        rc.setBody(Buffer.buffer("bar"));
        Assert.assertEquals("bar", contextToTest.getBody().toString());
        Assert.assertEquals("bar", contextToTest.getBodyAsString());

        JsonObject body = new JsonObject().put("foo", new JsonObject().put("bar", true));
        contextToTest.setBody(Buffer.buffer(body.encode()));
        Assert.assertEquals(body, contextToTest.getBodyAsJson());

        JsonArray jsonArray = new JsonArray()
                .add(new JsonObject().put("foo", new JsonObject().put("bar", true)));
        contextToTest.setBody(Buffer.buffer(jsonArray.encode()));
        Assert.assertEquals(jsonArray, contextToTest.getBodyAsJsonArray());

    }

    @Test
    public void testCookies() {
        Cookie myCookie = Cookie.cookie("MY_COOKIE", "MY_COOKIE_VALUE");
        contextToTest.addCookie(myCookie);
        Assert.assertEquals(myCookie, contextToTest.getCookie("MY_COOKIE"));
        Assert.assertEquals(1, contextToTest.cookieCount());
        Assert.assertEquals(rc.cookies(), contextToTest.cookies());

        contextToTest.removeCookie("MY_COOKIE");
        Assert.assertEquals(null, contextToTest.getCookie("MY_COOKIE"));
        Assert.assertEquals(0, contextToTest.cookieCount());
        Assert.assertEquals(new HashSet<>(), contextToTest.cookies());
    }

    @Test
    public void testData() {
        contextToTest.put("MY_KEY", "MY_VALUE");

        Assert.assertEquals("MY_VALUE", contextToTest.get("MY_KEY"));
        Assert.assertEquals("MY_VALUE", contextToTest.data().get("MY_KEY"));

        int size = contextToTest.data().size();
        contextToTest.remove("MY_KEY");
        Assert.assertEquals((Object) null, (Object) (contextToTest.get("MY_KEY")));
        Assert.assertEquals(size - 1, contextToTest.data().size());

    }

    @Test
    public void testOthers() {
        Assert.assertEquals(mountPoint, contextToTest.mountPoint());
        Assert.assertEquals(Utils.normalizePath(mountPoint), contextToTest.normalisedPath());
        Assert.assertEquals(null, contextToTest.currentRoute());
        Assert.assertEquals(-1, contextToTest.statusCode());
        Assert.assertEquals(null, contextToTest.failure());
        Assert.assertEquals(myVertx, contextToTest.vertx());
        Assert.assertEquals(false, contextToTest.failed());

        Session sess = new SessionImpl();
        contextToTest.setSession(sess);
        Assert.assertEquals(sess, contextToTest.session());
    }
}

class TestHttpServerRequest implements HttpServerRequest {

    MultiMap headers = MultiMap.caseInsensitiveMultiMap();
    String mountPoint;

    public TestHttpServerRequest(String mountPoint, String acceptLanguage) {
        this.mountPoint = mountPoint;
        headers.add("Accept-Language", acceptLanguage);
    }

    @Override
    public HttpServerRequest exceptionHandler(Handler<Throwable> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerRequest handler(Handler<Buffer> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerRequest pause() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerRequest resume() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerRequest endHandler(Handler<Void> endHandler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpVersion version() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpMethod method() {
        // TODO Auto-generated method stub
        return HttpMethod.POST;
    }

    @Override
    public String rawMethod() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isSSL() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String scheme() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String uri() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String path() {
        // TODO Auto-generated method stub
        return mountPoint;
    }

    @Override
    public String query() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String host() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerResponse response() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultiMap headers() {
        return headers;
    }

    @Override
    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    @Override
    public String getHeader(CharSequence headerName) {
        return headers.get(headerName);
    }

    @Override
    public MultiMap params() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getParam(String paramName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SocketAddress remoteAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SocketAddress localAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SSLSession sslSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String absoluteURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerRequest bodyHandler(Handler<Buffer> bodyHandler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NetSocket netSocket() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpServerRequest setExpectMultipart(boolean expect) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isExpectMultipart() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public HttpServerRequest uploadHandler(Handler<HttpServerFileUpload> uploadHandler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultiMap formAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFormAttribute(String attributeName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServerWebSocket upgrade() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEnded() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public HttpServerRequest customFrameHandler(Handler<HttpFrame> handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpConnection connection() {
        // TODO Auto-generated method stub
        return null;
    }

}
