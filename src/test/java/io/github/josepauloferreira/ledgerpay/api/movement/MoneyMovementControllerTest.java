package io.github.josepauloferreira.ledgerpay.api.movement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MoneyMovementControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldListEmptyMoneyMovementHistory() throws Exception {
    mockMvc
        .perform(get("/movements"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(content().json("[]"));
  }

  @Test
  void shouldListTreasuryFundingMovement() throws Exception {
    MvcResult walletResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(walletResult.getResponse().getContentAsString(), "$.id");

    String body =
        """
    {
      "amount": "100.00"
    }
    """;

    mockMvc
        .perform(
            post("/wallets/{id}/funding", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/movements"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].type").value("TREASURY_FUNDING"))
        .andExpect(jsonPath("$[0].source.type").value("SYSTEM_TREASURY"))
        .andExpect(jsonPath("$[0].destination.type").value("WALLET"))
        .andExpect(jsonPath("$[0].destination.walletId").value(walletId))
        .andExpect(jsonPath("$[0].amount").value("100.00"))
        .andExpect(jsonPath("$[0].occurredAt").isNotEmpty());
  }
}
