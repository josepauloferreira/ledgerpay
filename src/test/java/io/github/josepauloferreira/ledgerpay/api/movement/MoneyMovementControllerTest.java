package io.github.josepauloferreira.ledgerpay.api.movement;

import static org.hamcrest.Matchers.hasSize;
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

    String walletId = createWallet();

    fundWallet(walletId, "100.00", "100.00");

    mockMvc
        .perform(get("/movements"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].type").value("TREASURY_FUNDING"))
        .andExpect(jsonPath("$[0].source.type").value("SYSTEM_TREASURY"))
        .andExpect(jsonPath("$[0].destination.type").value("WALLET"))
        .andExpect(jsonPath("$[0].destination.walletId").value(walletId))
        .andExpect(jsonPath("$[0].amount").value("100.00"))
        .andExpect(jsonPath("$[0].occurredAt").isNotEmpty());
  }

  @Test
  void shouldListPeerTransferMovement() throws Exception {
    String sourceWalletId = createWallet();
    String targetWalletId = createWallet();

    fundWallet(sourceWalletId, "100.00", "100.00");

    mockMvc
        .perform(
            post("/wallets/{id}/transfers", sourceWalletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(transferBody(targetWalletId, "40.00")))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/movements"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[1].type").value("PEER_TRANSFER"))
        .andExpect(jsonPath("$[1].source.type").value("WALLET"))
        .andExpect(jsonPath("$[1].source.walletId").value(sourceWalletId))
        .andExpect(jsonPath("$[1].destination.type").value("WALLET"))
        .andExpect(jsonPath("$[1].destination.walletId").value(targetWalletId))
        .andExpect(jsonPath("$[1].amount").value("40.00"))
        .andExpect(jsonPath("$[1].occurredAt").isNotEmpty());
  }

  private String transferBody(String targetId, String amount) {
    return """
      {
        "targetWalletId": "%s",
        "amount": "%s"
      }
      """
        .formatted(targetId, amount);
  }

  private void fundWallet(String id, String amount, String expectedBalance) throws Exception {

    String body =
        """
    {
      "amount": "%s"
    }
    """
            .formatted(amount);

    mockMvc
        .perform(
            post("/wallets/{id}/funding", id).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.balance").value(expectedBalance));
  }

  private String createWallet() throws Exception {
    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    return JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");
  }
}
