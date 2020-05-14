package com.example.mathengerapi.controllers;

import com.example.mathengerapi.services.mathCompiler.MathCompilerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/math")
@AllArgsConstructor
public class MathController {
    private MathCompilerService mathCompilerService;

    @PostMapping(value = "/transform/latex")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> transformToLatex(@RequestBody String formula) {
        return new ResponseEntity<>(mathCompilerService.toLatex(formula), HttpStatus.OK);
    }
}
