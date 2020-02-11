package com.example.mathengerapi.controllers;

import com.example.mathengerapi.services.MathService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@CrossOrigin
@RequestMapping("/math")
@AllArgsConstructor
public class MathController {
    private MathService mathService;

    @PostMapping(value = "/transform", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> transformToPng(@RequestBody String formula) throws IOException, InterruptedException {
        var file = mathService.texToPng(formula);
        byte[] img = Files.readAllBytes(file.toPath());
        FileSystemUtils.deleteRecursively(file.getParentFile());
        return new ResponseEntity<>(img, HttpStatus.OK);
    }
}
