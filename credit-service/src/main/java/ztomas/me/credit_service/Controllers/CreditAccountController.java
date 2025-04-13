package ztomas.me.credit_service.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ztomas.me.credit_service.Entities.ReadModels.CreditAccount;
import ztomas.me.credit_service.Services.CreditAccountService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CreditAccountController {

    private final CreditAccountService creditAccountService;

    @GetMapping("/employee/{employeeId}")
    public CreditAccount getAccountByEmployeeId(@PathVariable Integer employeeId)
    {
        return creditAccountService.getAccount(employeeId);
    }

    @GetMapping("/employee/create/{employeeId}")
    public String createAccountByEmployeeId(@PathVariable Integer employeeId)
    {
        creditAccountService.createAccount(employeeId);
        return "success";
    }

    @GetMapping("/employee/add/{employeeId}")
    public String testAdd(@PathVariable Integer employeeId)
    {
        creditAccountService.addCreditsToAccount(employeeId, 500);
        return "success";
    }

    @GetMapping("/employee/remove/{employeeId}")
    public String testRemove(@PathVariable Integer employeeId)
    {
        creditAccountService.removeCreditsFromAccount(employeeId, 500);
        return "success";
    }
}
