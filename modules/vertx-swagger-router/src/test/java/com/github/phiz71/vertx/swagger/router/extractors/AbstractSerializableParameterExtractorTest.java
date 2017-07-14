package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.SerializableParameter;
import io.vertx.core.MultiMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class AbstractSerializableParameterExtractorTest {
    private AbstractSerializableParameterExtractor abstractSerializableParameterExtractor = new AbstractSerializableParameterExtractor() {
    };

    @Test(expected = IllegalArgumentException.class)
    public void testMissingRequiredParam() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setRequired(true);

        abstractSerializableParameterExtractor.extract(name, parameter,
                MultiMap.caseInsensitiveMultiMap());
    }

    @Test()
    public void testMissingNonRequiredParam() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setRequired(false);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter,
                MultiMap.caseInsensitiveMultiMap());
        Assert.assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotAllowEmptyValueWithEmptyParameter() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setAllowEmptyValue(false);
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, "");

        abstractSerializableParameterExtractor.extract(name, parameter, params);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAllowEmptyValueWithEmptyParameter() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, "");

        abstractSerializableParameterExtractor.extract(name, parameter, params);
    }

    @Test()
    public void testArrayMulti() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat("multi");
        List<String> paramsList = new ArrayList<>();
        paramsList.add("toto");
        paramsList.add("tutu");
        paramsList.add("titi");
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, paramsList);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(paramsList.toArray(), ((List) result).toArray());
    }

    @Test()
    public void testArray() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat(null);
        String param = "toto,tutu,titi";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(new String[] { "toto", "tutu", "titi" },
                ((List) result).toArray());
    }

    @Test()
    public void testArraySsv() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat("ssv");
        String param = "toto tutu titi";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(new String[] { "toto", "tutu", "titi" },
                ((List) result).toArray());
    }

    @Test()
    public void testArrayCsv() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat("csv");
        String param = "toto,tutu,titi";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(new String[] { "toto", "tutu", "titi" },
                ((List) result).toArray());
    }

    @Test()
    public void testArrayTsv() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat("tsv");
        String param = "toto\ttutu\ttiti";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(new String[] { "toto", "tutu", "titi" },
                ((List) result).toArray());
    }

    @Test()
    public void testArrayPipes() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat("pipes");
        String param = "toto|tutu|titi";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(new String[] { "toto", "tutu", "titi" },
                ((List) result).toArray());
    }

    @Test()
    public void testArrayOther() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("array");
        parameter.setCollectionFormat("anyOtherValue");
        String param = "toto,tutu,titi";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertArrayEquals(new String[] { "toto", "tutu", "titi" },
                ((List) result).toArray());
    }

    @Test()
    public void testDefault() {
        String name = "myName";
        SerializableParameter parameter = new QueryParameter();
        parameter.setName("myName");
        parameter.setType("string");
        String param = "toto";
        MultiMap params = MultiMap.caseInsensitiveMultiMap();
        params.add(name, param);

        Object result = abstractSerializableParameterExtractor.extract(name, parameter, params);
        Assert.assertEquals("toto", result);
    }
}
