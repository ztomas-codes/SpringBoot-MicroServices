package ztomas.me.credit_service.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ztomas.me.credit_service.Entities.ReadModels.CreditAccount;
import ztomas.me.credit_service.Models.CreditAccountManipulationCreditsRequest;
import ztomas.me.credit_service.Models.CreditBankCreationRequest;
import ztomas.me.credit_service.Models.SuccessResponse;
import ztomas.me.credit_service.Services.CreditAccountService;

@RestController
@RequestMapping("/api/credit")
@AllArgsConstructor
public class CreditAccountController {

    private final CreditAccountService creditAccountService;

    @GetMapping("/{employeeId}")
    public CreditAccount getAccountByEmployeeId(@PathVariable Integer employeeId)
    {
        return creditAccountService.getAccount(employeeId);
    }

    @PostMapping("/create")
    public SuccessResponse createAccountByEmployeeId(@RequestBody @Valid CreditBankCreationRequest req)
    {
        creditAccountService.createAccount(req.getEmployeeId());
        return SuccessResponse
            .builder()
            .message("successfully created")
            .success(true)
            .build();
    }

    @PostMapping("/add")
    public SuccessResponse addCredits(@RequestBody @Valid CreditAccountManipulationCreditsRequest req)
    {
        creditAccountService.addCreditsToAccount(req.getEmployeeId(), req.getAmount());
        return SuccessResponse
            .builder()
            .message("successfully added credits")
            .success(true)
            .build();
    }

    @PostMapping("/remove")
    public SuccessResponse removeCredits(@RequestBody @Valid CreditAccountManipulationCreditsRequest req)
    {
        creditAccountService.removeCreditsFromAccount(req.getEmployeeId(), req.getAmount());
        return SuccessResponse
            .builder()
            .message("successfully removed credits")
            .success(true)
            .build();
    }
}
