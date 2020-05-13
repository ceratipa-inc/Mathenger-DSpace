package com.example.mathengerapi.controllers;

import com.example.mathengerapi.services.MathService;
import com.example.mathengerapi.services.mathCompiler.MathCompilerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@CrossOrigin
@RequestMapping("/math")
@AllArgsConstructor
public class MathController {
    private MathService mathService;
    private MathCompilerService mathCompilerService;

    @PostMapping(value = "/latex/transform/png", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ResponseEntity<byte[]> transformToPng(@RequestBody String latex) throws IOException, InterruptedException {
        var file = mathService.texToPng(latex);
        byte[] img = Files.readAllBytes(file.toPath());
        FileSystemUtils.deleteRecursively(file.getParentFile());
        return new ResponseEntity<>(img, HttpStatus.OK);
    }

    @PostMapping(value = "/transform/latex")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> transformToLatex(@RequestBody String formula) {
        return new ResponseEntity<>(mathCompilerService.toLatex(formula), HttpStatus.OK);
    }
}
