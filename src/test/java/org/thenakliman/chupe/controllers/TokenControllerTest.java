package org.thenakliman.chupe.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thenakliman.chupe.services.TokenService;

import static org.junit.Assert.assertEquals;


@WebMvcTest(controllers = TokenController.class)
@RunWith(SpringRunner.class)
public class TokenControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private Jackson2ObjectMapperBuilder jacksonBuilder;

    private ObjectMapper objectMapper;

    /**
     * Setup web application context.
     */
    @Before()
    public void testSetup() {
        objectMapper = jacksonBuilder.build();
        /* NOTE(thenakliman) this approach has been used to bypass authentication
         * layer from the test. It creates a standalone application with single
         * controller(QuestionController).
         * */
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnToken() throws Exception {
        String token = "access_token";
        BDDMockito.given(tokenService.createToken("testUser")).willReturn(token);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/token?username=testUser")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertEquals(token, mvcResult.getResponse().getHeader("Authorization"));
    }
}