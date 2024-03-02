package com.sap.assignment.emissioncalculator.workflow;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class TestCoordResolverFactory {

    @InjectMocks
    private CoordResolverFactory factory;

    @Test(expected = RuntimeException.class)
    public void testResolverWithEmpty() {
        factory.getCoordResolver("");
    }

    @Test(expected = RuntimeException.class)
    public void testResolverWithNull() {
        factory.getCoordResolver(null);
    }

    @Test(expected = RuntimeException.class)
    public void testFirstCoordResolver() {
        Assert.assertNotNull(factory.getCoordResolver("fetch_first_coord"));
    }
}
