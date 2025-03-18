package com.example.api_.transfers;


import com.example.api_.transfers.dto.entry.Account;
import com.example.api_.transfers.dto.entry.Activity;
import com.example.api_.transfers.dto.entry.Card;
import com.example.api_.transfers.dto.exit.TransferRequestOutDTO;
import com.example.api_.transfers.dto.exit.TransferenceOutDTO;
import com.example.api_.transfers.entities.Transference;
import com.example.api_.transfers.feign.AccountFeignClient;
import com.example.api_.transfers.feign.ActivityFeignClient;
import com.example.api_.transfers.feign.CardFeignClient;
import com.example.api_.transfers.repository.TransferenceRepository;
import com.example.api_.transfers.service.impl.TransferenceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.eq;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class TransferenceServiceTest {
    @Mock
    private AccountFeignClient accountFeignClient;

    @Mock
    private CardFeignClient cardFeignClient;

    @Mock
    private TransferenceRepository transferenceRepository;

    @Mock
    private ActivityFeignClient activityFeignClient;
    @InjectMocks
    private TransferenceServiceImpl transferenceServiceImpl;

    private Account account;
    private Card card;
    private TransferenceOutDTO transferenceOutDto;
    private Account senderAccount;
    private Account recipientAccount;
    private TransferRequestOutDTO transferRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setUserId(1L);
        account.setBalance(new BigDecimal("1000.00"));
        account.setCvu("1234567890");

        card = new Card();
        card.setId(1L);
        card.setNumber("1111222233334444");

        transferenceOutDto = new TransferenceOutDTO();
        transferenceOutDto.setCardId(1L);


        //test para transferencia en Efectivo
        // Cuenta remitente
        senderAccount = new Account();
        senderAccount.setUserId(1L);
        senderAccount.setBalance(new BigDecimal("1000.00"));
        senderAccount.setCvu("1234567890");

        // Cuenta destinatario
        recipientAccount = new Account();
        recipientAccount.setUserId(2L);
        recipientAccount.setBalance(new BigDecimal("500.00"));
        recipientAccount.setCvu("0987654321");

        // Datos de la transferencia
        transferRequest = new TransferRequestOutDTO();
        transferRequest.setAmount(new BigDecimal("200.00"));
        transferRequest.setRecipient("0987654321");
    }

    @Test
    public void deberiaDeHacerUnaTransferenciaMedianteTarjeta() throws Exception {
        String jwtToken = "mockJwtToken";
        Long accountId = 1L;
        Long cardId = 1L;

        // Asignar un monto a la transferencia para evitar NullPointerException
        transferenceOutDto.setAmount(new BigDecimal("500.00"));

        // Arrange
        when(accountFeignClient.getAccountById(accountId, "Bearer " + jwtToken)).thenReturn(account);
        when(cardFeignClient.getCardtById(accountId, cardId, "Bearer " + jwtToken)).thenReturn(Optional.of(card));

        // Act
        transferenceServiceImpl.registerTransferenceFromCards(accountId, transferenceOutDto, "Bearer " + jwtToken);

        // Assert
        verify(transferenceRepository, times(1)).save(any(Transference.class));
        verify(accountFeignClient, times(1)).saveAccount(any(Account.class), eq(accountId), eq("Bearer " + jwtToken));
        verify(activityFeignClient, times(1)).save(any(Activity.class), eq(accountId), eq("Bearer " + jwtToken));

        // Verificar el nuevo balance de la cuenta después de la transferencia
        Assertions.assertEquals(new BigDecimal("1500.00"), account.getBalance());
    }

    @Test
    public void deberiaDeHacerUnaTransferenciaMedianteDineroEnEfectivo() throws Exception {
        String jwtToken = "mockJwtToken";
        Long accountId = 1L;

        // Arrange: Simulamos la cuenta del remitente
        when(accountFeignClient.getAccountById(eq(1L), eq(jwtToken))).thenReturn(senderAccount);
        when(accountFeignClient.getAccountById(eq(2L), eq(jwtToken))).thenReturn(recipientAccount);

        // Simulamos la búsqueda de la cuenta del destinatario
        when(accountFeignClient.findByAlias(eq(transferRequest.getRecipient()), eq(jwtToken))).thenReturn(null); // No encontrado por alias
        when(accountFeignClient.findByCvu(eq(transferRequest.getRecipient()), eq(jwtToken))).thenReturn(recipientAccount); // Encontrado por CVU

        // Simulamos el comportamiento del repositorio y la creación de los objetos
        when(accountFeignClient.saveAccount(any(Account.class), eq(accountId), eq(jwtToken)))
                .thenReturn(senderAccount)
                .thenReturn(recipientAccount);

        when(transferenceRepository.save(any(Transference.class))).thenReturn(new Transference());
        when(activityFeignClient.save(any(Activity.class), eq(accountId), eq(jwtToken))).thenReturn(new Activity());

        // Act: Ejecutamos la transferencia
        transferenceServiceImpl.makeTransferFromCash(1L, transferRequest, jwtToken);

        // Assert: Verificamos que las cuentas fueron actualizadas correctamente
        verify(accountFeignClient, times(2)).saveAccount(any(Account.class), eq(accountId), eq(jwtToken)); // Ambas cuentas deben ser guardadas
        verify(transferenceRepository, times(1)).save(any(Transference.class)); // La transferencia debe guardarse
        verify(activityFeignClient, times(2)).save(any(Activity.class), eq(accountId), eq(jwtToken)); // Ambas actividades deben registrarse
        Assertions.assertEquals(new BigDecimal("800.00"), senderAccount.getBalance()); // Verifica el balance actualizado del remitente
        Assertions.assertEquals(new BigDecimal("700.00"), recipientAccount.getBalance()); // Verifica el balance actualizado del destinatario
    }

    @Test
    public void DeberiaDeObtenerLasUltimasTransferencias() {
        String jwtToken = "mockJwtToken";
        // Arrange: Crear algunas transferencias simuladas
        Transference transfer1 = new Transference();
        transfer1.setAccountId(1L);
        transfer1.setAmount(new BigDecimal("100.00"));
        transfer1.setDate(LocalDateTime.now().minusDays(1));

        Transference transfer2 = new Transference();
        transfer2.setAccountId(1L);
        transfer2.setAmount(new BigDecimal("200.00"));
        transfer2.setDate(LocalDateTime.now().minusDays(2));

        // Lista con las transferencias simuladas
        List<Transference> transferList = Arrays.asList(transfer1, transfer2);

        // Simular que el repositorio devuelve las transferencias para la cuenta 1L
        when(transferenceRepository.findTop5ByAccountIdOrderByDateDesc(1L)).thenReturn(transferList);

        // Act: Llamar al método del servicio
        List<Transference> result = transferenceServiceImpl.getLastTransferredAccounts(1L);

        // Assert: Verificar que el servicio devolvió la lista correcta
        assertNotNull(result); // Verifica que la lista no sea nula
        assertEquals(2, result.size()); // Verifica que la lista contiene 2 transferencias
        Assertions.assertEquals(new BigDecimal("100.00"), result.get(0).getAmount()); // Verifica el monto de la primera transferencia
        Assertions.assertEquals(new BigDecimal("200.00"), result.get(1).getAmount()); // Verifica el monto de la segunda transferencia

        // Verificar que el repositorio fue llamado correctamente
        verify(transferenceRepository, times(1)).findTop5ByAccountIdOrderByDateDesc(1L);
    }

}