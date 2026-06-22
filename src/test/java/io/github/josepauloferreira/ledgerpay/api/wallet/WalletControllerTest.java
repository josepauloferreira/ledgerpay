package io.github.josepauloferreira.ledgerpay.api.wallet;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void shouldCreateWallet() throws Exception {
    mockMvc
        .perform(post("/wallets"))
        .andExpect(status().isCreated())
        .andExpect(header().exists(LOCATION))
        .andExpect(header().string(LOCATION, containsString("/wallets/")))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.balance").value("0.00"));
  }

  @Test
  void shouldFindCreatedWalletById() throws Exception {
    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

    mockMvc
        .perform(get("/wallets/{id}", walletId))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(walletId))
        .andExpect(jsonPath("$.balance").value("0.00"));
  }

  @Test
  void shouldReturnNotFoundWhenWalletDoesNotExist() throws Exception {
    mockMvc.perform(get("/wallets/{id}", "unknown-wallet-id")).andExpect(status().isNotFound());
  }

  @Test
  void shouldFundWallet() throws Exception {
    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

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
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(walletId))
        .andExpect(jsonPath("$.balance").value("100.00"));
  }

  @Test
  void shouldReturnNotFoundWhenFundingWalletDoesNotExist() throws Exception {
    String body =
        """
      {
        "amount": "100.00"
      }
      """;

    mockMvc
        .perform(
            post("/wallets/{id}/funding", "unknown-wallet-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnBadRequestWhenFundingAmountIsZero() throws Exception {

    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

    String body =
        """
      {
        "amount": "0.00"
      }
      """;

    mockMvc
        .perform(
            post("/wallets/{id}/funding", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenFundingAmountIsNegative() throws Exception {

    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

    String body =
        """
    {
      "amount": "-1.00"
    }
    """;

    mockMvc
        .perform(
            post("/wallets/{id}/funding", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenFundingAmountIsMissing() throws Exception {

    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

    String body = "{}";

    mockMvc
        .perform(
            post("/wallets/{id}/funding", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturnBadRequestWhenFundingAmountIsNotNumeric() throws Exception {

    MvcResult createResult =
        mockMvc.perform(post("/wallets")).andExpect(status().isCreated()).andReturn();

    String walletId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

    String body =
        """
      {
        "amount": "abc"
      }
      """;

    mockMvc
        .perform(
            post("/wallets/{id}/funding", walletId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldTransferMoneyBetweenWallets() throws Exception {
    String sourceId = createWallet();
    String targetId = createWallet();

    fundWallet(sourceId, "100.00", "100.00");

    mockMvc
        .perform(
            post("/wallets/{id}/transfers", sourceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(transferBody(targetId, "40.00")))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.source.id").value(sourceId))
        .andExpect(jsonPath("$.source.balance").value("60.00"))
        .andExpect(jsonPath("$.target.id").value(targetId))
        .andExpect(jsonPath("$.target.balance").value("40.00"));
  }

  @Test
  void shouldReturnNotFoundWhenSourceWalletDoesNotExist() throws Exception {
    String targetId = createWallet();

    mockMvc
        .perform(
            post("/wallets/{id}/transfers", "unknown-wallet-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transferBody(targetId, "40.00")))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnNotFoundWhenTargetWalletDoesNotExist() throws Exception {
    String sourceId = createWallet();

    mockMvc
        .perform(
            post("/wallets/{id}/transfers", sourceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(transferBody("unknown-wallet-id", "40.00")))
        .andExpect(status().isNotFound());
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
