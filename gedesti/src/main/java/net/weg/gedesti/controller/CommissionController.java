//package net.weg.gedesti.controller;
//
//import lombok.AllArgsConstructor;
//import net.weg.gedesti.model.entity.Commission;
//import net.weg.gedesti.model.service.CommissionService;
//import org.springframework.beans.BeanUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//@AllArgsConstructor
//@RequestMapping("/api/commission")
//public class CommissionController {
//
//    private CommissionService commissionService;
//
//    @GetMapping
//    public ResponseEntity<List<Commission>> findAll() {
//        return ResponseEntity.ok(commissionService.findAll());
//    }
//
//    @PostMapping
//    public ResponseEntity<Object> save(@RequestBody @Valid CommissionDTO comiCommissionDTO) {
//        Commission commission = new Commission();
//        BeanUtils.copyProperties(comiCommissionDTO, commission);
//        return ResponseEntity.ok(commissionService.save(commission));
//    }
//
//    @GetMapping("/{comissionCode}")
//    public ResponseEntity<Object> findById(Integer comissionCode) {
//        Optional<Commission> optionalCommission = commissionService.findById(comissionCode);
//        if(optionalCommission.isPresent()) {
//            return ResponseEntity.ok(optionalCommission);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error! No real benefit with code:  " + comissionCode);
//    }
//}
